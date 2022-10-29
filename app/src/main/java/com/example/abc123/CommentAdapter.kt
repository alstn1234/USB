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
import com.google.common.primitives.UnsignedBytes.toInt
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class CommentAdapter(private val CommentList: ArrayList<Commentmodel>,var context: Context,val text1: String,val text2: String) :
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
            fireDatabase.child("board").child(text1).child(text2).child("comment_count").get().addOnSuccessListener {
                var num = Integer.parseInt(it.value.toString())
                num = num - 1
                fireDatabase.child("board").child(text1).child(text2).child("comment").child(num.toString()).child("favorite").setValue(1)
                holder.commentfavorite.setImageResource(R.drawable.ic_favorite)
            }
        }

        val select_user = arrayOf("삭제하기","수정하기")
        val select_other = arrayOf("채팅하기","신고하기")
        val builder = AlertDialog.Builder(context)

        if(user == uid){
            holder.commentmenu.setOnClickListener{
                builder.setItems(select_user) { DialogInterface, which ->
                    when(which){
                        0 -> {
                            fireDatabase.child("board").child(text1).child(text2).child("comment").child(position.toString()).removeValue()
                            fireDatabase.child("board").child(text1).child(text2).child("comment_count").get().addOnSuccessListener {
                                var num = Integer.parseInt(it.value.toString())
                                num = num - 1
                                fireDatabase.child("board").child(text1).child(text2).child("comment_count").setValue(num)
                            }
                            notifyDataSetChanged()
                        }
                        1 -> Log.d("tag","2")
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

}