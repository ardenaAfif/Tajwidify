package com.kuliah.pkm.tajwidify.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kuliah.pkm.tajwidify.data.Modul
import com.kuliah.pkm.tajwidify.databinding.ItemMateriBinding
import com.kuliah.pkm.tajwidify.ui.home.HomeFragmentDirections

class MateriAdapter (private val context: Context) :
    RecyclerView.Adapter<MateriAdapter.MateriViewHolder>() {
    inner class MateriViewHolder(private val binding: ItemMateriBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(modul: Modul) {
            binding.apply {
                tvTitle.text = modul.title

                Glide.with(itemView)
                    .load(modul.imgUrl)
                    .centerCrop()
                    .into(ivMateri)
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Modul>() {
        override fun areItemsTheSame(oldItem: Modul, newItem: Modul): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Modul, newItem: Modul): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MateriViewHolder {
        return MateriViewHolder(
            ItemMateriBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: MateriViewHolder, position: Int) {
        val materi = differ.currentList[position]
        holder.bind(materi)

        holder.itemView.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToQuizFragment(materi)
            holder.itemView.findNavController().navigate(action)
        }
    }
}