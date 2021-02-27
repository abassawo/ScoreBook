package com.lindenlabs.scorebook.androidApp

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lindenlabs.scorebook.androidApp.Destination.HomeScreen
import com.lindenlabs.scorebook.androidApp.base.GameEngine
import com.lindenlabs.scorebook.androidApp.data.GameRepository

fun sharedViewModel(fragment: Fragment) =
    ViewModelProvider(fragment).get(SharedViewModel::class.java)


fun sharedViewModel(activity: FragmentActivity) =
    ViewModelProvider(activity).get(SharedViewModel::class.java)

class SharedViewModel : ViewModel() {
    private val gameEngine: GameEngine = GameEngine()
    private val repository = GameRepository()

    private val navEvent: MutableLiveData<Destination> = MutableLiveData(HomeScreen())

    fun processEvent(destination: Destination) = navEvent.postValue(destination)



}

