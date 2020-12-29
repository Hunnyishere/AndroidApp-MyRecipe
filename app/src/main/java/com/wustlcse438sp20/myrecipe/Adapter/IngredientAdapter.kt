package com.wustlcse438sp20.myrecipe.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.wustlcse438sp20.myrecipe.R
import com.wustlcse438sp20.myrecipe.data.ExtendedIngredients
import kotlinx.android.synthetic.main.item_ingredient.view.*

class IngredientAdapter(private var context: Context?, private var IngredientList: ArrayList<ExtendedIngredients>)
    : RecyclerView.Adapter<IngredientAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun OnItemClick(view: View, position: Int)
    }
    var onItemClickListener: OnItemClickListener?=null
    fun setOnItemClick(onItemClickListener: OnItemClickListener){
        this.onItemClickListener=onItemClickListener
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_ingredient, parent, false)
        val holder = ViewHolder(itemView)
        return holder
    }

    override fun getItemCount(): Int {
        return IngredientList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.ingredient_unit.text = IngredientList[position].original
        if (!IngredientList[position].image.equals(""))
            Picasso.get().load("https://spoonacular.com/cdn/ingredients_100x100/"+IngredientList[position].image).into(holder.itemView.image_ingredient)
        if (onItemClickListener != null){
            holder.itemView.setOnClickListener{
                onItemClickListener!!.OnItemClick(holder.itemView, position)
            }
        }

    }

}