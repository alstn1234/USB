package com.example.abc123

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MypostAdapter(private val MypostList: ArrayList<Mypostmodel>) :
    RecyclerView.Adapter<MypostAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val Mypostitem = MypostList[position]

        holder.postboard.text = Mypostitem.postboard
        holder.posttitletext.text = Mypostitem.posttitletext
        holder.dates.text = Mypostitem.dates
    }

    override fun getItemCount(): Int {
        return MypostList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postboard: TextView = itemView.findViewById(R.id.postboard)
        val posttitletext: TextView = itemView.findViewById(R.id.posttitletext)
        val dates: TextView = itemView.findViewById((R.id.dates))
    }

}