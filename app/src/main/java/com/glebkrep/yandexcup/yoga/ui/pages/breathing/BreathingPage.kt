package com.glebkrep.yandexcup.yoga.ui.pages.breathing

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.glebkrep.yandexcup.yoga.R
import com.glebkrep.yandexcup.yoga.data.BreathingState
import com.glebkrep.yandexcup.yoga.ui.theme.UiConsts

@Composable
fun BreathingPage(navController: NavController, viewModel: BreathingPageVM = viewModel()) {
    val pageState by viewModel.breathingState.observeAsState(BreathingState.NotStarted)
    Text(text = stringResource(R.string.training), Modifier.padding(UiConsts.padding))

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (pageState) {
            is BreathingState.BreatheIn, is BreathingState.BreatheOut -> {
                BreathingBlock(state = pageState)
            }
            is BreathingState.NotStarted -> {
                NotStartedBlock {
                    viewModel.startRecording()
                }
            }
            is BreathingState.Silence -> {
                SilenceBlock()
            }
            else -> {

            }
        }
        Button(onClick = {
            viewModel.stopRecording()
            navController.popBackStack()
        }, Modifier.padding(UiConsts.padding)) {
            Text(text = stringResource(R.string.stop_session))
        }
    }
}

@Composable
fun SilenceBlock() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = ".....", Modifier.padding(UiConsts.padding))
    }
}

@Composable
fun NotStartedBlock(onStartClick: () -> (Unit)) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.start_with_breathing_in),
            Modifier.padding(UiConsts.padding)
        )
        Button(onClick = { onStartClick.invoke() }, Modifier.padding(UiConsts.padding)) {
            Text(text = stringResource(R.string.start))
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
        Text(
            text = if (isBreatheIn) stringResource(R.string.inhale) else stringResource(R.string.exhale),
            Modifier.padding(UiConsts.padding)
        )
    }
}