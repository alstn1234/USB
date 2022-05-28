package com.example.abc123

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val boardList: ArrayList<Board_Model>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item2, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = boardList[position]
        holder.title.text = currentitem.title
        holder.name.text = currentitem.nickname
        holder.time.text = currentitem.time + " " + currentitem.time2
        holder.num.text = (position + 1).toString()
    }

    override fun getItemCount(): Int {
        return boardList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.findViewById(R.id.board_title2)
        val name: TextView = itemView.findViewById(R.id.board_name2)
        val time: TextView = itemView.findViewById(R.id.board_time2)
        val num: TextView = itemView.findViewById(R.id.board_num2)


    }

}