package com.wustlcse438sp20.myrecipe.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.QuerySnapshot
import com.squareup.picasso.Picasso
import com.wustlcse438sp20.myrecipe.R
import com.wustlcse438sp20.myrecipe.data.RecipeByIngredients
import com.wustlcse438sp20.myrecipe.data.RecipeShownFormat
import kotlinx.android.synthetic.main.item_recipe.view.*

class RecipeAdapter (private var context: Context?, private var RecipeList: ArrayList<RecipeShownFormat>, private var collection_id:String):

    RecyclerView.Adapter<RecipeAdapter.ViewHolder>(),ItemTouchHelperAdapter {

    val db : FirebaseFirestore =FirebaseFirestore.getInstance()
    val settings = FirebaseFirestoreSettings.Builder()
        .setTimestampsInSnapshotsEnabled(true)
        .build()
    interface OnItemClickListener{
        fun OnItemClick(view: View, position: Int)
    }
    var onItemClickListener: OnItemClickListener?=null
    fun setOnItemClick(onItemClickListener: OnItemClickListener){
        this.onItemClickListener=onItemClickListener
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false)
        val holder = ViewHolder(itemView)
        return holder
    }

    override fun getItemCount(): Int {
        return RecipeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.recipe_title.text= RecipeList[position].title
        if (!RecipeList[position].image.equals(""))
            Picasso.get().load(RecipeList[position].image).into(holder.itemView.recipe_image)
        if (onItemClickListener != null){
            holder.itemView.setOnClickListener{
                onItemClickListener!!.OnItemClick(holder.itemView, position)
            }
        }
    }

    override fun onItemDissmiss(position: Int) {
        db.firestoreSettings = settings
        if(collection_id!=""){
            RecipeList.removeAt(position)
            notifyItemRemoved(position)

            //do database operation: Delete
            db.collection("collections")
                .whereEqualTo(FieldPath.documentId(), collection_id)
                .get()
                .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                    if (task.isSuccessful) {
                        Log.v("Search in database", "Sucess")
                        println("search success !!!!!!!!!!!!!!!!!!")
                        for (document in task.result!!) {
                            var recipeList = document.get("recipes") as ArrayList<MutableMap<String,Any>>
                            recipeList.removeAt(position)
                            val updateMap: MutableMap<String, Any> = HashMap()
                            updateMap.put("recipes", recipeList)

                            document.reference
                                .update(updateMap)
                                .addOnSuccessListener {
                                    Log.d(
                                        "Database Update",
                                        "delete recipe in collection: DocumentSnapshot successfully updated!"
                                    )
                                }
                                .addOnFailureListener { e ->
                                    Log.w(
                                        "Database Update",
                                        "delete recipe in collection: Error updating document",
                                        e
                                    )
                                }
                        }
                    }
                    else {
                        Log.v("Search in database", "Fail")
                        println("failed to get collection")
                    }
                })
        }
    }
}