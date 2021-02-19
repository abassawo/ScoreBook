package com.lindenlabs.scorebook.androidApp.screens.gamedetail.showplayers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lindenlabs.scorebook.androidApp.databinding.PlayerRowItemBinding
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Player

class PlayerAdapter : RecyclerView.Adapter<PlayerViewHolder>() {
    private val players: MutableList<Player> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder =
        PlayerViewHolder(binding = PlayerRowItemBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) =
        holder.bind(players[position])

    override fun getItemCount(): Int = players.size

    fun setData(players: List<Player>) {
        this.players.clear()
        this.players.addAll(players)
        notifyDataSetChanged()
    }
}

class PlayerViewHolder(val binding: PlayerRowItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(player: Player) = binding.run {
        playerName.text = player.name
        playerScoreView.text = player.scoreTotal.toString()
    }
}