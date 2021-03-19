package com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation.showplayers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lindenlabs.scorebook.androidApp.databinding.PlayerRowItemBinding
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.PlayerDataEntity
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.GameDetailInteraction

class PlayerAdapter : RecyclerView.Adapter<PlayerViewHolder>() {
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
}

class PlayerViewHolder(val binding: PlayerRowItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(playerDataEntity: PlayerDataEntity) = binding.run {
        val player = playerDataEntity.player
        playerName.text = player.name
        playerScoreView.text = player.scoreTotal.toString()

//        if(playerEntity.isPlayersTurn) {
//            itemView.background = itemView.context.getDrawable(android.R.color.holo_green_light)
//            itemView.setOnClickListener { playerEntity.clickAction(PlayerInteraction.PlayerClicked(player)) }
//        } else {
//            itemView.setOnClickListener(null)
//        }

        itemView.setOnClickListener { playerDataEntity.clickAction(GameDetailInteraction.PlayerClicked(player)) }
    }
}
