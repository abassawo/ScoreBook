package com.lindenlabs.scorebook.androidApp.screens.victory.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.di.ViewModelFactory
import com.lindenlabs.scorebook.androidApp.base.utils.appComponent
import com.lindenlabs.scorebook.androidApp.base.domain.AppRepository
import com.lindenlabs.scorebook.androidApp.databinding.FragmentVictoryBinding
import com.lindenlabs.scorebook.androidApp.di.VictoryModule
import com.lindenlabs.scorebook.androidApp.screens.playerentry.entities.AddPlayerInteraction
import com.lindenlabs.scorebook.androidApp.screens.victory.entities.VictoryInteraction
import com.lindenlabs.scorebook.androidApp.screens.victory.entities.VictoryState
import javax.inject.Inject

class VictoryFragment : Fragment(R.layout.fragment_victory) {
    private val binding: FragmentVictoryBinding by lazy { viewBinding() }
    private val viewModel: VictoryViewModel by lazy { viewModelFactory.makeViewModel(this, VictoryViewModel::class.java) }
    private val args: VictoryFragmentArgs by navArgs()

    @Inject
    lateinit var appRepository: AppRepository

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private fun viewBinding(): FragmentVictoryBinding {
        val view: View = requireView().findViewById(R.id.victoryRoot)
        return FragmentVictoryBinding.bind(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    viewModel.handleInteraction(VictoryInteraction.GoHome)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

        appComponent().value
            .victoryFragmentComponentBuilder()
            .plus(VictoryModule(args))
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewState.observe(viewLifecycleOwner, ::showVictory)
        viewModel.viewEvent.observe(viewLifecycleOwner, { goHome() })
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
    }
}