package com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation.showplayers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lindenlabs.scorebook.androidApp.databinding.PlayerRowItemBinding
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.ScoreBookEntity
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.entities.ScoreBookInteraction

class PlayerAdapter : RecyclerView.Adapter<PlayerViewHolder>() {
    private val scoreBooks: MutableList<ScoreBookEntity> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder =
        PlayerViewHolder(binding = PlayerRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) =
        holder.bind(scoreBooks[position])

    override fun getItemCount(): Int = scoreBooks.size

    fun setData(scoreBooks: List<ScoreBookEntity>) {
        this.scoreBooks.clear()
        this.scoreBooks.addAll(scoreBooks)
        notifyDataSetChanged()
    }
}

class PlayerViewHolder(val binding: PlayerRowItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(scoreBookEntity: ScoreBookEntity) = binding.run {
        val player = scoreBookEntity.player
        playerName.text = player.name
        playerScoreView.text = player.scoreTotal.toString()

//        if(playerEntity.isPlayersTurn) {
//            itemView.background = itemView.context.getDrawable(android.R.color.holo_green_light)
//            itemView.setOnClickListener { playerEntity.clickAction(PlayerInteraction.PlayerClicked(player)) }
//        } else {
//            itemView.setOnClickListener(null)
//        }

        itemView.setOnClickListener { scoreBookEntity.clickAction(ScoreBookInteraction.PlayerClicked(player)) }
    }
}
