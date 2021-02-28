package com.lindenlabs.scorebook.androidApp.navigation

import android.os.Bundle
import androidx.annotation.IdRes

data class FragmentTarget(@IdRes val targetId: Int, val bundle: Bundle)