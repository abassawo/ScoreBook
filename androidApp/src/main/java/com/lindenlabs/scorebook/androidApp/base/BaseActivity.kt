package com.lindenlabs.scorebook.androidApp.base

import androidx.appcompat.app.AppCompatActivity
import com.lindenlabs.scorebook.androidApp.ScoreBookApplication
import java.lang.IllegalStateException

abstract class BaseActivity : AppCompatActivity() {
    val appNavigator by lazy { (application as ScoreBookApplication).appNavigator }
}