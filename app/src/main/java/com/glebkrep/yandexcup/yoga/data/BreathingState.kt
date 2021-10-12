package com.glebkrep.yandexcup.yoga.data

sealed class BreathingState {
    object NotStarted:BreathingState()
    object PrevBreath:BreathingState()
    data class BreatheIn(val startTimeStamp:Long):BreathingState()
    data class BreatheOut(val startTimeStamp:Long):BreathingState()
    data class Silence(val startTimeStamp:Long):BreathingState()
    data class Error(val e:Throwable):BreathingState()
}
