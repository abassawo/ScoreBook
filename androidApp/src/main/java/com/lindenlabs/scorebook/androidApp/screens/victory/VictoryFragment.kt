package com.lindenlabs.scorebook.androidApp.screens.victory

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.ViewModelFactory
import com.lindenlabs.scorebook.androidApp.appComponent
import com.lindenlabs.scorebook.androidApp.base.Environment
import com.lindenlabs.scorebook.androidApp.databinding.FragmentVictoryBinding
import com.lindenlabs.scorebook.androidApp.di.VictoryModule
import nl.dionsegijn.konfetti.emitters.StreamEmitter
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import javax.inject.Inject

class VictoryFragment : Fragment(R.layout.fragment_victory) {
    private val binding: FragmentVictoryBinding by lazy { viewBinding() }
    private val viewModel: VictoryViewModel by lazy { viewModelFactory.makeViewModel(this, VictoryViewModel::class.java) }
    private val args: VictoryFragmentArgs by navArgs()

    @Inject
    lateinit var environment: Environment

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private fun viewBinding(): FragmentVictoryBinding {
        val view: View = requireView().findViewById(R.id.victoryRoot)
        return FragmentVictoryBinding.bind(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent().value
            .victoryFragmentComponentBuilder()
            .plus(VictoryModule(args))
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.updateUI()
        viewModel.viewState.observe(viewLifecycleOwner, ::showVictory)
        viewModel.viewEvent.observe(viewLifecycleOwner, { goHome() })
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
            .setPosition(
                minX = viewKonfetti.width / 2f,
                maxX = viewKonfetti.width + 0f,
                minY = -50f,
                maxY = viewKonfetti.width + 50f
            )
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
        binding.victoryTextView.text = victoryState.victoryText
        binding.bottomNavView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navHomeFragment -> goHome()
                else -> false
            }
        }
        val runnable =
            Runnable { findNavController().navigate(VictoryFragmentDirections.navigateHome()) }
        Handler().postDelayed(runnable, 10000)
    }
}