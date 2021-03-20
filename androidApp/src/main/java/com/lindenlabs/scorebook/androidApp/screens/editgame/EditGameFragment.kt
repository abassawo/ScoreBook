package com.lindenlabs.scorebook.androidApp.screens.editgame

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.lindenlabs.scorebook.androidApp.R
import com.lindenlabs.scorebook.androidApp.base.utils.appComponent
import com.lindenlabs.scorebook.androidApp.databinding.EditGameFragmentBinding
import com.lindenlabs.scorebook.androidApp.di.EditGameModule
import com.lindenlabs.scorebook.androidApp.di.ViewModelFactory
import com.lindenlabs.scorebook.androidApp.screens.editgame.entities.EditGameInteraction

class EditGameFragment : Fragment(R.layout.edit_game_fragment) {
    lateinit var viewModelFactory: ViewModelFactory
    private val args: EditGameFragmentArgs by navArgs()
    private val viewModel: EditGameViewModel   by lazy {
        viewModelFactory.makeViewModel(this, EditGameViewModel::class.java)
    }
    private val binding: EditGameFragmentBinding by lazy { viewBinding() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent().value
            .editGameComponentBuilder()
            .plus(EditGameModule(args))
            .build()
            .inject(this)
    }

    private fun viewBinding(): EditGameFragmentBinding {
        val view: View = requireView().findViewById(R.id.editGameRoot)
        return EditGameFragmentBinding.bind(view)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val text = binding.editGameField.editableText.toString()
        binding.editGameField.addTextChangedListener { viewModel.handleInteraction(EditGameInteraction.EditGameName(text)) }
    }
}