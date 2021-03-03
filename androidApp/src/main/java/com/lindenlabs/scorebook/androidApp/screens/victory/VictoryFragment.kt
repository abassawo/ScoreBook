package com.lindenlabs.scorebook.androidApp.screens.victory

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.databinding.FragmentVictoryBinding
import nl.dionsegijn.konfetti.emitters.StreamEmitter
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

class VictoryFragment : Fragment(R.layout.fragment_victory) {
    private val binding: FragmentVictoryBinding by lazy { viewBinding() }
    private val args: VictoryFragmentArgs by navArgs()

    private fun viewBinding(): FragmentVictoryBinding {
        val view: View = requireView().findViewById(R.id.victoryRoot)
        return FragmentVictoryBinding.bind(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.updateUI()
        val viewModel = ViewModelProvider(this).get(VictoryViewModel::class.java)
        viewModel.viewState.observe(viewLifecycleOwner, ::showVictory)
        viewModel.viewEvent.observe(viewLifecycleOwner, { goHome() })
        viewModel.init(args)
    }

    private fun FragmentVictoryBinding.updateUI() {
        this.viewKonfetti.build()
            .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.RED, Color.BLUE)
            .setDirection(degrees = 5.5)
            .setSpeed(1f, 5f)
            .setFadeOutEnabled(true)
            .setTimeToLive(2000L)
            .addShapes(Shape.Square, Shape.Circle)
            .addSizes(Size(12))
            .setPosition(minX =viewKonfetti.width / 2f, maxX = viewKonfetti.width + 0f, minY = -50f, maxY =viewKonfetti.width + 50f)
            .streamFor(particlesPerSecond = 300, emittingTime = StreamEmitter.INDEFINITE)
    }

    override fun onPause() {
        super.onPause()
        binding.viewKonfetti.stopGracefully()
    }

    private fun goHome(): Boolean {
        findNavController().navigate(VictoryFragmentDirections.navigateHome())
        return true
    }

    private fun showVictory(victoryState: VictoryState) {
        binding.victoryTextView.setText(victoryState.victoryText)
        binding.bottomNavView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.navHomeFragment -> goHome()
                else -> false
            }
        }
    }
}