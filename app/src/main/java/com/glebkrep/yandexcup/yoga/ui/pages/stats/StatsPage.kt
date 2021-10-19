package com.glebkrep.yandexcup.yoga.ui.pages.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.glebkrep.yandexcup.yoga.R
import com.glebkrep.yandexcup.yoga.data.BreathingItem
import com.glebkrep.yandexcup.yoga.data.StatsState
import com.glebkrep.yandexcup.yoga.ui.theme.UiConsts
import com.glebkrep.yandexcup.yoga.utils.durationMillis
import com.glebkrep.yandexcup.yoga.utils.millisToHMS
import com.glebkrep.yandexcup.yoga.utils.millisToSeconds

@Composable
fun StatsPage(statsVM: StatsPageVM = viewModel(), onSendEmailClick:()->(Unit)){

    val state by statsVM.statsState.observeAsState(StatsState.Loading)

    Column(Modifier.fillMaxSize().background(Color.LightGray)) {
        when (state){
            is StatsState.Loading ->{
                Text(text = stringResource(R.string.loading),Modifier.padding(UiConsts.padding))
            }
            is StatsState.NoStats ->{
                Text(text = stringResource(R.string.no_stats),Modifier.padding(UiConsts.padding))
            }
            is StatsState.Success ->{
                LazyColumn(Modifier.fillMaxSize()){
                    val successState = state as StatsState.Success
                    item {
                        Button(onClick = {
                            onSendEmailClick.invoke()
                        },Modifier.padding(UiConsts.padding)) {
                            Text(text = stringResource(R.string.send_to_coach))
                        }
                    }
                    items(successState.breathingItems){
                        StatsItem(it)
                    }
                }
            }
        }
    }
}

@Composable
fun StatsItem(breathingItem:BreathingItem){
    Row(
        Modifier
            .fillMaxWidth()
            .padding(UiConsts.padding)
            .border(1.dp, Color.LightGray),
    horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = "${breathingItem.type.label}(${breathingItem.durationMillis.millisToSeconds()})",Modifier.padding(UiConsts.padding))
        Column {
            Text(text = "start: ${breathingItem.startTimestamp.millisToHMS()}")
            Text(text = "end: ${breathingItem.endTimestamp.millisToHMS()}")
        }
    }
}