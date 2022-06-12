package com.example.abc123

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage

class MyAdapter2(
    val boardList: ArrayList<Board_Model2>,
    val str: String,
    val requestManager: RequestManager
) :
    RecyclerView.Adapter<MyAdapter2.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = boardList[position]
        val aa = currentitem.img1
        val storageReference = FirebaseStorage.getInstance().getReference("images/$aa")
        holder.apply {
            if (currentitem.image_count != 0) {
                imagesize_change(holder.img)
                storageReference.downloadUrl.addOnSuccessListener {
                    val photoUri: Uri = it
                    requestManager.load(photoUri.toString())
                        .placeholder(R.drawable.ic_outline_image_24)
                        .error(R.drawable.ic_baseline_error_24).into(holder.img)
                }
                    .addOnFailureListener {
                        holder.img.layoutParams.width = 0
                        holder.img.layoutParams.height = 0
                    }
            }
            holder.title.text = currentitem.title
            holder.name.text = currentitem.nickname
            holder.comment_count.text = currentitem.comment_count.toString()
            holder.views_count.text = currentitem.views_count.toString()
            holder.time.text = currentitem.time
            holder.num.text = (position + 1).toString()
            holder.itemView.setOnClickListener {
                val database = FirebaseDatabase.getInstance().getReference()
                val updates : MutableMap<String, Any> = HashMap()
                val intent = Intent(holder.itemView.context, BoardViewActivity::class.java)

                if(currentitem.name != FirebaseAuth.getInstance().currentUser?.uid.toString()) {
                    updates["board/${currentitem.board_title}/${currentitem.key}/views_count"] =
                        ServerValue.increment(1)
                    database.updateChildren(updates)
                }

                intent.putExtra("data", currentitem)
                intent.putExtra("title", str)
                ContextCompat.startActivity(holder.itemView.context, intent, null)
            }
        }

        /* if (position <= boardList.size ) {
             val endPosition = if (position + 3 > boardList.size) {
                 boardList.size
             } else {
                 position + 3
             }
             boardList.subList(position, endPosition ).map { it.img1 }.forEach {
                 preload(holder.itemView.context, it)
             }
         }*/
    }

    override fun getItemCount(): Int {
        return boardList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.board_image)
        val title: TextView = itemView.findViewById(R.id.board_tilte)
        val name: TextView = itemView.findViewById(R.id.board_name)
        val comment_count: TextView = itemView.findViewById((R.id.comment_count))
        val views_count: TextView = itemView.findViewById(R.id.views_count)
        val time: TextView = itemView.findViewById(R.id.board_time)
        val num: TextView = itemView.findViewById(R.id.board_num)
    }

    fun imagesize_change(image: ImageView) {
        image.layoutParams.width = 200
        image.layoutParams.height = 200
    }

    /* fun preload(context: Context, url : String) {

         val storageReference = FirebaseStorage.getInstance().getReference("images/$url")
                 storageReference.downloadUrl.addOnSuccessListener {
                     val photoUri= it
                     Glide.with(context).load(photoUri.toString())
                         .preload(150, 150)
                 }

     }*/
}