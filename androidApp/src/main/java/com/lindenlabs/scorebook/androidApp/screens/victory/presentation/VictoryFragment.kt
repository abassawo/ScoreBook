package com.lindenlabs.scorebook.androidApp.screens.victory.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.lindenlabs.scorebook.androidApp.MainActivity
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.di.ViewModelFactory
import com.lindenlabs.scorebook.androidApp.databinding.FragmentVictoryBinding
import com.lindenlabs.scorebook.androidApp.screens.victory.entities.VictoryState

class VictoryFragment : Fragment(R.layout.fragment_victory) {
    private val binding: FragmentVictoryBinding by lazy { viewBinding() }
    private val viewModel: VictoryViewModel = ViewModelFactory().makeViewModel(this, VictoryViewModel::class.java)

    private fun viewBinding(): FragmentVictoryBinding {
        val view: View = requireView().findViewById(R.id.victoryRoot)
        return FragmentVictoryBinding.bind(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
//        appComponent().value
//            .victoryFragmentComponentBuilder()
//            .plus(VictoryModule())
//            .build()
//            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewState.observe(viewLifecycleOwner, ::showVictory)
        viewModel.viewEvent.observe(viewLifecycleOwner, { goHome() })
    }

    private fun goHome(): Boolean {
        (activity as MainActivity).navigateFirstTabWithClearStack()
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