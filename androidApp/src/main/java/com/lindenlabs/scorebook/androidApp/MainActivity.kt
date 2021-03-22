package com.lindenlabs.scorebook.androidApp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.lindenlabs.scorebook.androidApp.base.BaseFragment
import com.lindenlabs.scorebook.androidApp.databinding.ActivityMainBinding
import com.lindenlabs.scorebook.androidApp.screens.editgame.EditGameFragment
import com.lindenlabs.scorebook.androidApp.screens.gamedetail.presentation.GameDetailFragment
import com.lindenlabs.scorebook.androidApp.screens.home.presentation.HomeFragment
import com.lindenlabs.scorebook.androidApp.screens.playerentry.presentation.AddPlayersFragment
import com.lindenlabs.scorebook.androidApp.screens.victory.presentation.VictoryFragment

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private val binding: ActivityMainBinding by lazy { viewBinding() }

    private fun viewBinding(): ActivityMainBinding {
        val view: View = findViewById(R.id.drawer_layout)
        return ActivityMainBinding.bind(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.toolbar.title = "Score Book"
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val navHost = supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment)
        navHost?.let { navFragment ->
            navFragment.childFragmentManager.primaryNavigationFragment?.let { fragment ->
                when (fragment) {
                    is AddPlayersFragment,
                    is GameDetailFragment,
                    is VictoryFragment -> findNavController(R.id.my_nav_host_fragment).navigate(R.id.navHomeFragment)
                    is HomeFragment -> super.onBackPressed()
                    is EditGameFragment -> {
                        navigateFirstTabWithClearStack()
                        fragment.handleBackPress()
                    }
                    else ->  navigateFirstTabWithClearStack()
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
