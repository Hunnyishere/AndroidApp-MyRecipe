package com.wustlcse438sp20.myrecipe.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wustlcse438sp20.myrecipe.R
import com.wustlcse438sp20.myrecipe.data.Collection
import kotlinx.android.synthetic.main.item_collection.view.*

class CollectionAdapter (private var context: Context?, private var CollectionList: ArrayList<Collection>):

    RecyclerView.Adapter<CollectionAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun OnItemClick(view: View, position: Int)
    }
    var onItemClickListener: OnItemClickListener?=null
    fun setOnItemClick(onItemClickListener: OnItemClickListener){
        Log.v("Click on Collection","item click")
        this.onItemClickListener=onItemClickListener
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_collection, parent, false)
        val holder = ViewHolder(itemView)
        return holder
    }

    override fun getItemCount(): Int {
        return CollectionList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.item_collection_name.text= CollectionList[position].name
        holder.itemView.item_collection_description.text= CollectionList[position].description
        if (onItemClickListener != null){
            holder.itemView.setOnClickListener{
                onItemClickListener!!.OnItemClick(holder.itemView, position)
            }
        }
    }

}