package com.example.abc123

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.common.primitives.UnsignedBytes.toInt
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CommentAdapter(private val CommentList: ArrayList<Commentmodel>,var context: Context,val text1: String,val text2: String) :
    RecyclerView.Adapter<CommentAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CustomViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
        val formatted = current.format(formatter)

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
                        0 -> {
                                val intent = Intent(context, MessageActivity::class.java)
                                intent.putExtra("destinationUid", uid)
                                context?.startActivity(intent)
                        }
                        1 -> {
                            val builder1 = AlertDialog.Builder(context)

                            builder1.setTitle("신고하기")
                                .setMessage("신고하시겠습니까?")
                                .setPositiveButton("확인"){dialogInterface: DialogInterface, i: Int ->
                                    fireDatabase.child("User").child(uid.toString()).child("sanctions").child("1").child("bodys").setValue("부적절한 사유로 신고접수 되었습니다.")
                                    fireDatabase.child("User").child(uid.toString()).child("sanctions").child("1").child("headline").setValue("신고접수")
                                    fireDatabase.child("User").child(uid.toString()).child("sanctions").child("1").child("dates").setValue(formatted)
                                }
                                .setNegativeButton("취소"){dialogInterface: DialogInterface, i: Int ->}

                            builder1.show()
                        }
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