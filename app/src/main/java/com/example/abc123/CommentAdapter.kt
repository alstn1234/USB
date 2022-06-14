package com.example.abc123

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CommentAdapter(private val CommentList: ArrayList<Commentmodel>) :
    RecyclerView.Adapter<CommentAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val Commentitem = CommentList[position]
        Glide.with(holder.itemView).load(Commentitem.profileImageUrl)
            .into(holder.profileImageUrl)
        holder.nickname.text = Commentitem.nickname
        holder.contents.text = Commentitem.contents
        holder.dates.text = Commentitem.dates
        holder.favorite.text = "좋아요 : " + Commentitem.favorite.toString()

        holder.commentfavorite.setOnClickListener{
            holder.commentfavorite.setImageResource(R.drawable.ic_favorite)
        }
        holder.commentmenu.setOnClickListener{

        }
    }

    override fun getItemCount(): Int {
        return CommentList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImageUrl: ImageView = itemView.findViewById(R.id.Profileimage)
        val nickname: TextView = itemView.findViewById(R.id.nickname)
        val contents: TextView = itemView.findViewById(R.id.contents)
        val dates: TextView = itemView.findViewById(R.id.dates)
        val favorite: TextView = itemView.findViewById(R.id.favorite)
        val commentfavorite: ImageButton = itemView.findViewById(R.id.commentfavorite)
        val commentmenu: ImageButton = itemView.findViewById(R.id.commentmenu)
    }

}