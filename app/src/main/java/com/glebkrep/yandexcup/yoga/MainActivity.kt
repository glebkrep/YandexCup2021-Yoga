package com.glebkrep.yandexcup.yoga

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.glebkrep.yandexcup.yoga.ui.Screen
import com.glebkrep.yandexcup.yoga.ui.pages.breathing.BreathingPage
import com.glebkrep.yandexcup.yoga.ui.pages.home.HomePage
import com.glebkrep.yandexcup.yoga.ui.pages.stats.StatsPage
import com.glebkrep.yandexcup.yoga.ui.theme.YogaTheme

//Алёна очень любит заниматься йогой, но постоянно забывает дышать правильно.
//Напишите приложение, которое будет определять с помощью встроенного микрофона, когда она делает вдох,
//когда — выдох, а когда вообще задержала дыхание, чтобы ее тренер Аркадий смог помочь ей тренировать дыхание.
//Решение должно определять вдох, выдох и время между ними.

//Приложение должно:
//  - определять момент вдоха,
//  - определять момент выдоха,
//  - считать и фиксировать время между вдохом-выдохом и выдохом-вдохом,
//  - составлять список вдохов-выдохов с зафиксированным временем между ними,
//  - отправлять список по электронной почте тренеру Аркадию.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YogaTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val mainNavController = rememberNavController()
                    NavHost(
                        navController = mainNavController,
                        startDestination = Screen.Home.route
                    ) {
                        composable(Screen.Home.route) { HomePage(mainNavController) }
                        composable(Screen.BreathingPage.route) { BreathingPage() }
                        composable(Screen.StatsList.route) { StatsPage() }

                    }
                }
            }
        }
    }
}