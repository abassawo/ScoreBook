package com.lindenlabs.scorebook.androidApp.screens.victory.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lindenlabs.scorebook.androidApp.MainActivity
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.base.utils.appComponent
import com.lindenlabs.scorebook.androidApp.databinding.FragmentVictoryBinding
import com.lindenlabs.scorebook.androidApp.di.VictoryModule
import com.lindenlabs.scorebook.androidApp.di.ViewModelFactory
import com.lindenlabs.scorebook.shared.common.engines.victory.VictoryState
import com.lindenlabs.scorebook.shared.common.engines.victory.VictoryViewEvent
import javax.inject.Inject

class VictoryFragment : Fragment(R.layout.fragment_victory) {
    private val binding: FragmentVictoryBinding by lazy { viewBinding() }
    private val viewModel: VictoryViewModel by lazy { viewModelFactory.makeViewModel(this, VictoryViewModel::class.java) }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private fun viewBinding(): FragmentVictoryBinding {
        val view: View = requireView().findViewById(R.id.victoryRoot)
        return FragmentVictoryBinding.bind(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        appComponent().value
            .victoryFragmentComponentBuilder()
            .plus(VictoryModule(requireArguments()["gameArg"] as String))
            .build()
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gameId = requireArguments()["gameArg"] as String
        viewModel.launch(gameId)
        viewModel.viewState.observe(viewLifecycleOwner, ::showVictory)
        viewModel.viewEvent.observe(viewLifecycleOwner, ::handleViewEvent )
    }

    private fun handleViewEvent(viewEvent: VictoryViewEvent) {
        when(viewEvent) {
            VictoryViewEvent.None -> Unit
            VictoryViewEvent.GoHome -> goHome()
        }
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