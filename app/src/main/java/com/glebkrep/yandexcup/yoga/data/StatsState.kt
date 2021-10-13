package com.glebkrep.yandexcup.yoga.data

sealed class StatsState {
    object Loading:StatsState()
    object NoStats:StatsState()
    data class Success(val breathingItems:List<BreathingItem>):StatsState()
}
