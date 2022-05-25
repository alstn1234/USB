package com.example.abc123

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.circleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.abc123.databinding.FragmentMypageBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


class ProfileAdapter(private val profileList: ArrayList<Profile>) :
    RecyclerView.Adapter<ProfileAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_profile, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val profileitem = profileList[position]
        Glide.with(holder.itemView).load(profileitem.profile)
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
