package com.kuliah.pkm.tajwidify.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kuliah.pkm.tajwidify.data.Doa
import com.kuliah.pkm.tajwidify.databinding.ItemDoaBinding
import com.kuliah.pkm.tajwidify.ui.doa.DoaFragmentDirections

class DoaAdapter (private val context: Context) :
    RecyclerView.Adapter<DoaAdapter.DoaViewHolder>() {

    inner class DoaViewHolder(private val binding: ItemDoaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(doa: Doa) {
            binding.apply {
                tvTitle.text = doa.judul
            }
        }

    }

    private val diffCallback = object : DiffUtil.ItemCallback<Doa>() {
        override fun areItemsTheSame(oldItem: Doa, newItem: Doa): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Doa, newItem: Doa): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoaViewHolder {
        return DoaViewHolder(
            ItemDoaBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: DoaViewHolder, position: Int) {
        val doa = differ.currentList[position]
        holder.bind(doa)

        holder.itemView.setOnClickListener {
            val action = DoaFragmentDirections.actionDoaFragmentToDetailDoaFragment(doa)
            holder.itemView.findNavController().navigate(action)
        }
    }
}