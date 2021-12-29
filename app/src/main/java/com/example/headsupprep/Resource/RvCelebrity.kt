package com.example.headsupprep.Resource

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.headsupprep.Model.CelebItem
import com.example.headsupprep.databinding.ItemRowBinding


class rvCelebrity(var arrayCeleb: ArrayList<CelebItem>) :
    RecyclerView.Adapter<rvCelebrity.ViewHolder>() {
    class ViewHolder(var binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newU = arrayCeleb[position]

        holder.binding.apply {
            tvCleb.text = "${newU.name}\n ${newU.taboo1}\n ${newU.taboo2}\n ${newU.taboo3}"
        }
    }

    override fun getItemCount(): Int = arrayCeleb.size
}