package com.glebkrep.yandexcup.yoga.ui.pages.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.glebkrep.yandexcup.yoga.breatheDetector.BreathingDetector
import com.glebkrep.yandexcup.yoga.ui.Screen
import com.glebkrep.yandexcup.yoga.utils.getActivity

@Composable
fun HomePage(outterNavController: NavController) {
    val context = LocalContext.current

    Column(Modifier.fillMaxSize()) {
        Button(onClick = {
            if (BreathingDetector.requestPermission(
                    context.getActivity() ?: throw Exception("Activity can't be null")
                )
            ) {
                outterNavController.navigate(Screen.BreathingPage.route)
            }
        }) {
            Text(text = "Start exercise")
        }
    }
}