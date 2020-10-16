package me.alberto.tellerium.screens.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.alberto.tellerium.data.local.db.FarmerEntity
import me.alberto.tellerium.databinding.FarmerItemBinding


class FarmerAdapter(private val listener: ItemClickListener) :
    ListAdapter<FarmerEntity, RecyclerView.ViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MenuItemHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MenuItemHolder) holder.bind(getItem(position), listener)
    }

    class MenuItemHolder(private val binding: FarmerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            farmer: FarmerEntity,
            listener: ItemClickListener
        ) {
            binding.farmer = farmer
            binding.listener = listener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
                val view =
                    FarmerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return MenuItemHolder(view)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<FarmerEntity>() {
        override fun areItemsTheSame(oldItem: FarmerEntity, newItem: FarmerEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FarmerEntity, newItem: FarmerEntity): Boolean {
            return oldItem == newItem
        }
    }

    interface ItemClickListener {
        fun onDelete(farmer: FarmerEntity)
        fun onNavigate(id: Long)
        fun onEdit(farmer: FarmerEntity)
    }


}