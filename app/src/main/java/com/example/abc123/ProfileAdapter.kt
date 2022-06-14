package com.example.abc123

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.test28.DataModel


class ProfileAdapter(private val profileList: ArrayList<Profile>) :
    RecyclerView.Adapter<ProfileAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_profile, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val profileitem = profileList[position]
        Glide.with(holder.itemView).load(profileitem.profileImageUrl)
            .into(holder.imageView)
        holder.nickname.text = profileitem.nickname
        holder.school.text = profileitem.school
        holder.major.text = "학과 :" + profileitem.major
    }

    override fun getItemCount(): Int {
        return profileList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.Profileimage)
        val nickname: TextView = itemView.findViewById(R.id.nick)
        val school: TextView = itemView.findViewById((R.id.school))
        val major: TextView = itemView.findViewById(R.id.major)

    }

}
