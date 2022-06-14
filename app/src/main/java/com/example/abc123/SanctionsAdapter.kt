package com.example.abc123

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SanctionsAdapter(private val SanctionsList: ArrayList<Sanctionsmodel>) :
    RecyclerView.Adapter<SanctionsAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_sanctions, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val Sanctionsitem = SanctionsList[position]

        holder.headline.text = Sanctionsitem.headline
        holder.bodys.text = Sanctionsitem.bodys
        holder.dates.text = Sanctionsitem.dates
    }

    override fun getItemCount(): Int {
        return SanctionsList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val headline: TextView = itemView.findViewById(R.id.headline)
        val bodys: TextView = itemView.findViewById(R.id.bodys)
        val dates: TextView = itemView.findViewById((R.id.dates))
    }

}