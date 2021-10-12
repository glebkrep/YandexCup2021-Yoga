package com.glebkrep.yandexcup.yoga.ui.pages.breathing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.glebkrep.yandexcup.yoga.data.BreathingState
import com.glebkrep.yandexcup.yoga.ui.theme.UiConsts

@Composable
fun BreathingPage(viewModel: BreathingPageVM = viewModel()) {
    val pageState by viewModel.breathingState.observeAsState(BreathingState.NotStarted)

    Column(Modifier.fillMaxSize()) {
        Text(text = "Breathing page", Modifier.padding(UiConsts.padding))
        when (pageState) {
            is BreathingState.BreatheIn, is BreathingState.BreatheOut -> {
                BreathingBlock(state = pageState)
            }
            is BreathingState.NotStarted -> {
                NotStartedBlock() {
                    viewModel.startRecording()
                }
            }
            is BreathingState.Silence -> {
                SilenceBlock()
            }
        }

    }
}

@Composable
fun SilenceBlock() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "-----")
    }
}

@Composable
fun NotStartedBlock(onStartClick: () -> (Unit)) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Start with inhaling!")
        Button(onClick = { onStartClick.invoke() }) {
            Text(text = "Start")
        }
    }
}

@Composable
fun BreathingBlock(state: BreathingState) {
    val isBreatheIn = state is BreathingState.BreatheIn
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Breathing ${if (isBreatheIn) "In" else "Out"}")
    }
}