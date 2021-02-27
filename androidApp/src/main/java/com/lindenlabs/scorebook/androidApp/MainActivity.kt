package com.lindenlabs.scorebook.androidApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lindenlabs.scorebook.androidApp.screens.home.HomeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment())
                .commitNow()
        }
    }
}