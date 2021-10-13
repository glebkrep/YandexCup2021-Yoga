package com.glebkrep.yandexcup.yoga.data

import com.glebkrep.yandexcup.yoga.features.breatheDetector.BreathingDetector

sealed class BreathingState(val label:String) {
    object NotStarted:BreathingState("Not Started")
    object PrevState:BreathingState("")
    data class BreatheIn(val startTimeStamp:Long):BreathingState(BreathingDetector.BREATHING)
    data class BreatheOut(val startTimeStamp:Long):BreathingState(BreathingDetector.BREATHING)
    data class Silence(val startTimeStamp:Long):BreathingState("")
    data class Error(val e:Throwable):BreathingState("")

    fun mapToBreathingItem(endTimeStamp:Long):BreathingItem?{
        val typeAndStart = when (this){
            is BreatheIn ->{
                val breatheIn = this as BreatheIn
                Pair(TypeBreath.BREATHE_IN,breatheIn.startTimeStamp)
            }
            is BreatheOut ->{
                val breatheOut = this as BreatheOut
                Pair(TypeBreath.BREATHE_OUT,breatheOut.startTimeStamp)
            }
            is Silence ->{
                val silence = this as Silence
                Pair(TypeBreath.PAUSE,silence.startTimeStamp)
            }
            else ->{
                return null
            }
        }
        return BreathingItem(
            type = typeAndStart.first,
            startTimestamp = typeAndStart.second,
            endTimestamp = endTimeStamp
        )
    }
}