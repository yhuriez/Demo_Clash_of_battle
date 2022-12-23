package com.example.democlashofbattle.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.democlashofbattle.databinding.ViewPlayerInfoBinding
import com.example.democlashofbattle.models.Player
import com.example.democlashofbattle.utils.getColor
import com.example.democlashofbattle.utils.getNameId
import com.example.democlashofbattle.utils.getPlayerJob
import com.example.democlashofbattle.utils.loadImage

class PlayerAdapter(private val clickListener: (Long) -> Unit) : ListAdapter<Player, PlayerViewHolder>(PlayerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        return PlayerViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(clickListener, getItem(position))
    }
}

class PlayerViewHolder private constructor(private val binding: ViewPlayerInfoBinding) :
    ViewHolder(binding.root) {

    fun bind(clickListener: (Long) -> Unit, item: Player) {
        binding.root.setOnClickListener {
            clickListener(item.id)
        }
        loadImage(binding.playerImage, item.imageUrl)
        binding.playerName.text = item.name

        val job = getPlayerJob(item)
        binding.playerJob.setText(job.getNameId())
        binding.playerJob.setTextColor(job.getColor(binding.root.context))
    }

    companion object {
        fun from(parent: ViewGroup): PlayerViewHolder {
            val binding = ViewPlayerInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return PlayerViewHolder(binding)
        }
    }
}

class PlayerDiffCallback : DiffUtil.ItemCallback<Player>() {
    override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean {
        return oldItem == newItem
    }
}