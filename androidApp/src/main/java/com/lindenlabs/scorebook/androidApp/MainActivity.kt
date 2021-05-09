package com.lindenlabs.scorebook.androidApp

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.lindenlabs.scorebook.androidApp.base.BaseFragment
import com.lindenlabs.scorebook.androidApp.base.utils.postEvent
import com.lindenlabs.scorebook.androidApp.databinding.ActivityMainBinding
import com.lindenlabs.scorebook.androidApp.navigation.Destination
import com.lindenlabs.scorebook.androidApp.screens.editgame.EditGameFragment
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation.GameDetailFragment
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.HomeFragment
import com.lindenlabs.scorebook.androidApp.screens.playerentry.presentation.AddPlayersFragment
import com.lindenlabs.scorebook.androidApp.screens.updatepoints.presentation.UpdatePointsDialogFragment
import com.lindenlabs.scorebook.androidApp.screens.victory.presentation.VictoryFragment
import com.lindenlabs.scorebook.shared.common.Event

class SharedNavigationViewModel : ViewModel() {
    val destinationLiveData = MutableLiveData<Event<Destination>>()

    fun navigateTo(destination: Destination) =
        destinationLiveData.postEvent(destination)
}

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private val binding: ActivityMainBinding by lazy { viewBinding() }
    private val sharedNavigationViewModel: SharedNavigationViewModel by viewModels()

    private fun viewBinding(): ActivityMainBinding {
        val view: View = findViewById(R.id.drawer_layout)
        return ActivityMainBinding.bind(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedNavigationViewModel.destinationLiveData.observe(this, this::navigate)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setNavigationIcon(R.drawable.ic_menu) {
            onBackPressed()
        }

        binding.toolbar.title = "Score Book"
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController)
    }

    private fun navigate(event: Event<Destination>) {
        when (val destination = event.getContentIfNotHandled()) {
            Destination.Home -> navController.navigate(R.id.navHomeFragment)
            is Destination.AddPlayers -> navController.navigate(
                R.id.navAddPlayers,
                bundleOf("gameArg" to destination.game.id)
            )
            is Destination.EditGame -> navController.navigate(
                R.id.navEditGame,
                bundleOf("gameArg" to destination.game.id)
            )
            is Destination.GameDetail -> navController.navigate(
                R.id.navActiveGame,
                bundleOf("gameArg" to destination.game.id)
            )
            is Destination.UpdatePoints ->
                with(destination) {
                    UpdatePointsDialogFragment.newInstance(game, player) { onDismissAction() }
                        .show(supportFragmentManager, "UpdatePoints")
                }
            is Destination.VictoryScreen ->
                navController.navigate(R.id.navVictoryFragment, bundleOf("gameArg" to destination.game.id)
            )
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val navHost = supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment)
        navHost?.let { navFragment ->
            navFragment.childFragmentManager.primaryNavigationFragment?.let { fragment ->
                when (fragment) {
                    is GameDetailFragment,
                    is VictoryFragment -> findNavController(R.id.my_nav_host_fragment).navigate(R.id.navHomeFragment)
                    is HomeFragment -> super.onBackPressed()
                    is EditGameFragment -> {
                        navigateFirstTabWithClearStack()
                        fragment.handleBackPress()
                    }
                    else -> navigateFirstTabWithClearStack()
                }
            }
        }
    }

    fun navigateFirstTabWithClearStack() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.score_book_nav)
        graph.startDestination = R.id.navHomeFragment
        navController.graph = graph
    }

    fun setNavigationIcon(id: Int, navAction: () -> Unit) {
        binding.toolbar.setNavigationIcon(id)
        binding.toolbar.setNavigationOnClickListener { navAction() }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
