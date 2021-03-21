package com.lindenlabs.scorebook.androidApp

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.lindenlabs.scorebook.androidApp.base.utils.hideSoftInput
import com.lindenlabs.scorebook.androidApp.databinding.ActivityMainBinding
import com.lindenlabs.scorebook.androidApp.views.MainMenuBottomSheet

class MainActivity : AppCompatActivity(){
    private val binding: ActivityMainBinding by lazy { viewBinding() }

    private fun viewBinding(): ActivityMainBinding {
        val view: View = findViewById(R.id.drawer_layout)
        return ActivityMainBinding.bind(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding.toolbar.title = "Score Book"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let { ab ->
            ab.setHomeAsUpIndicator(R.drawable.ic_menu)
            ab.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> MainMenuBottomSheet().show(
                supportFragmentManager,
                MainActivity::class.java.simpleName
            )
        }
        return super.onOptionsItemSelected(item)
        // Have the NavigationUI look for an action or destination matching the menu
        // item id and navigate there if found.
        // Otherwise, bubble up to the parent.
//        return item.onNavDestinationSelected(findNavController(R.id.my_nav_host_fragment))
//                || super.onOptionsItemSelected(item)
    }

    // Allows NavigationUI to support proper up navigation or the drawer layout
    // drawer menu, depending on the situation
    override fun onSupportNavigateUp(): Boolean = findNavController(R.id.my_nav_host_fragment).navigateUp()


    fun setNavigationIcon(id: Int, navAction: () -> Unit) {
        binding.toolbar.setNavigationIcon(id)
        binding.toolbar.setNavigationOnClickListener { navAction() }
    }
}