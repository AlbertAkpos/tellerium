package me.alberto.tellerium.screens.newfarmer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.alberto.tellerium.data.local.db.Farm
import me.alberto.tellerium.data.local.db.FarmerEntity
import me.alberto.tellerium.databinding.FarmItemBinding
import me.alberto.tellerium.databinding.FarmerItemBinding


class FarmAdapter(private val listener: ItemClickListener) :
    ListAdapter<Farm, RecyclerView.ViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MenuItemHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MenuItemHolder) holder.bind(getItem(position), listener)
    }

    class MenuItemHolder(private val binding: FarmItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            farmer: Farm,
            listener: ItemClickListener
        ) {
            binding.farm = farmer
            binding.listener = listener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
                val view =
                    FarmItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return MenuItemHolder(view)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Farm>() {
        override fun areItemsTheSame(oldItem: Farm, newItem: Farm): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Farm, newItem: Farm): Boolean {
            return oldItem == newItem
        }
    }

    interface ItemClickListener {
        fun onDelete(farm: Farm)
        fun onNavigate(farm: Farm)
    }


}