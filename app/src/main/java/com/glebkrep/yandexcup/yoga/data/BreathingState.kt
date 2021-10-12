package com.glebkrep.yandexcup.yoga.data

import com.glebkrep.yandexcup.yoga.breatheDetector.BreathingDetector

sealed class BreathingState(val label:String) {
    object NotStarted:BreathingState("Not Started")
    object PrevState:BreathingState("")
    data class BreatheIn(val startTimeStamp:Long):BreathingState(BreathingDetector.BREATHING)
    data class BreatheOut(val startTimeStamp:Long):BreathingState(BreathingDetector.BREATHING)
    data class Silence(val startTimeStamp:Long):BreathingState("")
    data class Error(val e:Throwable):BreathingState("")
}
