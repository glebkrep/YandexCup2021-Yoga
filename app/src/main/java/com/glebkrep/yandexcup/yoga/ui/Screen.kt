package com.glebkrep.yandexcup.yoga.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.graphics.vector.ImageVector
import com.glebkrep.yandexcup.yoga.R

sealed class Screen(val route: String) {
    object Home : Screen("Home")
    object BreathingPage : Screen("BreathingPage")

}