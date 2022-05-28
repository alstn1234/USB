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
import com.google.firebase.storage.FirebaseStorage

class MyAdapter3(val boardList: ArrayList<Board_Model3>, val requestManager: RequestManager) :
    RecyclerView.Adapter<MyAdapter3.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item3, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = boardList[position]
        val aa = currentitem.img1
        val storageReference = FirebaseStorage.getInstance().getReference("images/$aa")
        holder.apply {
            if (currentitem.image_count != 0) {
                storageReference.downloadUrl.addOnSuccessListener {
                    val photoUri: Uri = it
                    requestManager.load(photoUri.toString())
                        .placeholder(R.drawable.ic_outline_image_24)
                        .error(R.drawable.ic_baseline_error_24).into(holder.img)
                }
            }
            if (currentitem.sell_direct) {
                holder.direct.layoutParams.height = 60
                holder.direct_img.visibility = View.VISIBLE
                holder.locaion.visibility = View.VISIBLE
            }
            if (currentitem.sell_delivery)
                delivery.layoutParams.height = 60

            holder.title.text = currentitem.title
            holder.name.text = currentitem.nickname
            holder.time.text = currentitem.time
            holder.num.text = (position + 1).toString()
            holder.price.text = currentitem.price + "원"
            holder.locaion.text = currentitem.location

            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, buysellviewActivity::class.java)
                intent.putExtra("data", currentitem)
                ContextCompat.startActivity(holder.itemView.context, intent, null)
            }

            if (position <= boardList.size) {
                val endPosition = if (position + 6 > boardList.size) {
                    boardList.size
                } else {
                    position + 6
                }
                storageReference.downloadUrl.addOnSuccessListener {
                    val photoUri = it.toString()

                    boardList.subList(position, endPosition).map { photoUri }.forEach {
                        preload(it)
                    }
                }
            }


        }
    }

    override fun getItemCount(): Int {
        return boardList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.board_image3)
        val title: TextView = itemView.findViewById(R.id.board_title3)
        val name: TextView = itemView.findViewById(R.id.board_name3)
        val time: TextView = itemView.findViewById(R.id.board_time3)
        val num: TextView = itemView.findViewById(R.id.board_num3)
        val price: TextView = itemView.findViewById(R.id.price)
        val locaion: TextView = itemView.findViewById(R.id.location)
        val delivery: TextView = itemView.findViewById(R.id.sell_delivery)
        val direct: TextView = itemView.findViewById(R.id.sell_direct)
        val direct_img: ImageView = itemView.findViewById(R.id.location_img)
    }


    fun preload(url: String) {
        requestManager.load(url)
            .preload(150, 150)
    }

}