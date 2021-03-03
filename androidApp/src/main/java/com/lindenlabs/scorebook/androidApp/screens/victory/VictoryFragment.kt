package com.lindenlabs.scorebook.androidApp.screens.victory

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.databinding.FragmentVictoryBinding

class VictoryFragment : Fragment(R.layout.fragment_victory) {
    private val binding: FragmentVictoryBinding by lazy { viewBinding() }
    private val args: VictoryFragmentArgs by navArgs()

    private fun viewBinding(): FragmentVictoryBinding {
        val view: View = requireView().findViewById(R.id.victoryRoot)
        return FragmentVictoryBinding.bind(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(this).get(VictoryViewModel::class.java)
        viewModel.viewState.observe(viewLifecycleOwner, ::showVictory)
        viewModel.viewEvent.observe(viewLifecycleOwner, { goHome() })
        viewModel.init(args)
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