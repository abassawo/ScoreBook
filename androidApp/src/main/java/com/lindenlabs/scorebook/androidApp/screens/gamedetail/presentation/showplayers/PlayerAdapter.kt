package com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation.showplayers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lindenlabs.scorebook.androidApp.databinding.PlayerRowItemBinding
import com.lindenlabs.scorebook.shared.common.entities.gamedetail.GameDetailInteraction
import com.lindenlabs.scorebook.shared.common.entities.gamedetail.PlayerDataEntity

class PlayerAdapter : RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {
    private val playerData: MutableList<PlayerDataEntity> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder =
        PlayerViewHolder(binding = PlayerRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) =
        holder.bind(playerData[position])

    override fun getItemCount(): Int = playerData.size

    fun setData(playerData: List<PlayerDataEntity>) {
        this.playerData.clear()
        this.playerData.addAll(playerData)
        notifyDataSetChanged()
    }

    inner class PlayerViewHolder(val binding: PlayerRowItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(playerDataEntity: PlayerDataEntity) = binding.run {
            val player = playerDataEntity.player
            playerName.text = player.name
            playerScoreView.text = player.scoreTotal.toString()

            itemView.setOnClickListener { playerDataEntity.clickAction(GameDetailInteraction.PlayerClicked(player)) }
        }
    }

}

