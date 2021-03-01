package com.lindenlabs.scorebook.androidApp

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.lindenlabs.scorebook.androidApp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var  appBarConfiguration : AppBarConfiguration
    private val binding: ActivityMainBinding by lazy { viewBinding() }

    fun viewBinding(): ActivityMainBinding {
        val view: View = findViewById(R.id.drawer_layout)
        return ActivityMainBinding.bind(view)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding.toolbar.title = "Score Book"

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.container) as NavHostFragment? ?: return

        // Set up Action Bar
        val navController = host.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBar(navController, appBarConfiguration)
        setupNavigationMenu(navController)
        setupBottomNavMenu(navController)
    }

    private fun setupBottomNavMenu(navController: NavController) =
        binding.bottomNavView.setupWithNavController(navController)

    private fun setupNavigationMenu(navController: NavController) {
//        // In split screen mode, you can drag this view out from the left
        // This does NOT modify the actionbar
        binding.navView.setupWithNavController(navController)
        // TODO END STEP 9.4
    }

    private fun setupActionBar(navController: NavController, appBarConfig : AppBarConfiguration) {
//        // This allows NavigationUI to decide what label to show in the action bar
//        // By using appBarConfig, it will also determine whether to
//        // show the up arrow or drawer menu icon
        setupActionBarWithNavController(navController, appBarConfig)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val navigationView = binding.navView
        // The NavigationView already has these same navigation items, so we only add
        // navigation items to the menu here if there isn't a NavigationView
        if (navigationView == null) {
            menuInflater.inflate(R.menu.overflow_menu, menu)
            return true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return super.onOptionsItemSelected(item)
//        // Have the NavigationUI look for an action or destination matching the menu
//        // item id and navigate there if found.
//        // Otherwise, bubble up to the parent.
        return item.onNavDestinationSelected(findNavController(R.id.my_nav_host_fragment))
                || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        // Allows NavigationUI to support proper up navigation or the drawer layout
        // drawer menu, depending on the situation
        return findNavController(R.id.my_nav_host_fragment).navigateUp()
    }
}