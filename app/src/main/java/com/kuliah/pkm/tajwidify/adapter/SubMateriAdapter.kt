package com.kuliah.pkm.tajwidify.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kuliah.pkm.tajwidify.data.SubModul
import com.kuliah.pkm.tajwidify.databinding.ItemSubmateriBinding
import com.kuliah.pkm.tajwidify.ui.materi.submateri.SubMateriFragmentDirections

class SubMateriAdapter(private val context: Context) :
    RecyclerView.Adapter<SubMateriAdapter.SubMateriViewHolder>() {
    inner class SubMateriViewHolder(private val binding: ItemSubmateriBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(subModul: SubModul) {
                binding.apply {
                    tvTitle.text = subModul.title

                    Glide.with(itemView)
                        .load(subModul.imgUrl)
                        .centerCrop()
                        .into(ivSubmateri)
                }
            }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<SubModul>() {
        override fun areItemsTheSame(oldItem: SubModul, newItem: SubModul): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SubModul, newItem: SubModul): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubMateriViewHolder {
        return SubMateriViewHolder(
            ItemSubmateriBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: SubMateriViewHolder, position: Int) {
        val subMateri = differ.currentList[position]
        holder.bind(subMateri)

        holder.itemView.setOnClickListener {
            val action = SubMateriFragmentDirections.actionSubMateriFragmentToModulFragment(subMateri)
            holder.itemView.findNavController().navigate(action)
        }
    }
}