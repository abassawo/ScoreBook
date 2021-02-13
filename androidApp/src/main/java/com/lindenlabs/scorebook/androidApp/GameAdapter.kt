package com.lindenlabs.scorebook.androidApp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.lindenlabs.scorebook.androidApp.GameViewHolder.Companion.layoutId
import com.lindenlabs.scorebook.androidApp.databinding.GameItemRowBinding
import com.lindenlabs.scorebook.androidApp.screens.home.data.model.Game

class GameAdapter(private val openGames: List<Game>) :
    Adapter<GameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(layoutId, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int)= holder.bind(openGames[position])

    override fun getItemCount(): Int = openGames.size
}


open class GameViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val binding: GameItemRowBinding by lazy { viewBinding() }

    fun viewBinding(): GameItemRowBinding = GameItemRowBinding.bind(itemView)

    fun bind(game: Game) {
        binding.text1.text = game.name
    }

    companion object {
        const val layoutId = R.layout.game_item_row
    }
}
