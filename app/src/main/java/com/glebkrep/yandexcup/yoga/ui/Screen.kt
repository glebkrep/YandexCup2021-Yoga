package com.glebkrep.yandexcup.yoga.ui

sealed class Screen(val route: String) {
    object Home : Screen("Home")
    object BreathingPage : Screen("BreathingPage")
    object StatsList : Screen("StatsList")
    object SendData: Screen("SendData")
}