package com.example.mint.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.mint.Model.MejoresNegociosModel
import com.example.mint.databinding.ViewholderMejoresNegociosBinding

class MejoresNegociosAdapter(val items: MutableList<MejoresNegociosModel>):
    RecyclerView.Adapter<MejoresNegociosAdapter.Viewholder>() {
        private var context:Context ?= null
    class Viewholder(val binding: ViewholderMejoresNegociosBinding):
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MejoresNegociosAdapter.Viewholder {
        context = parent.context
        val binding = ViewholderMejoresNegociosBinding.inflate(LayoutInflater.from(context), parent,
            false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: MejoresNegociosAdapter.Viewholder, position: Int) {
        holder.binding.textView.text = items[position].title

        val requestOption = RequestOptions().transform(CenterCrop())
        Glide.with(holder.itemView.context)
            .load(items[position].picUrl[0])
            .apply(requestOption)
            .into(holder.binding.picMejoresNegocios)
    }

    override fun getItemCount(): Int = items.size
}