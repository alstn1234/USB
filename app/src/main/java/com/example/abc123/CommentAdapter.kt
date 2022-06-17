package com.example.abc123

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class CommentAdapter(private val CommentList: ArrayList<Commentmodel>,var context: Context) :
    RecyclerView.Adapter<CommentAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val Commentitem = CommentList[position]
        var uid = Commentitem.uid
        val fireDatabase = FirebaseDatabase.getInstance().reference
        val user = Firebase.auth.currentUser?.uid.toString()

        Glide.with(holder.itemView).load(Commentitem.profileImageUrl)
            .into(holder.profileImageUrl)
        holder.nickname.text = Commentitem.nickname
        holder.contents.text = Commentitem.contents
        holder.dates.text = Commentitem.dates
        holder.favorite.text = "좋아요 : " + Commentitem.favorite.toString()

        holder.commentfavorite.setOnClickListener{
            holder.commentfavorite.setImageResource(R.drawable.ic_favorite)
        }

        val select_user = arrayOf("삭제하기","수정하기")
        val select_other = arrayOf("신고하기","취소")
        val builder = AlertDialog.Builder(context)

        if(user == uid){
            holder.commentmenu.setOnClickListener{
                builder.setItems(select_user) { DialogInterface, which ->
                    when(which){

                    }
                }
                builder.show()
            }
        }else{
            holder.commentmenu.setOnClickListener{
                builder.setItems(select_other) { DialogInterface, which ->
                    when(which){

                    }
                }
                builder.show()
            }
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

    fun Comment_delete(){

    }
}