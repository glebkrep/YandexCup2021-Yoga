package com.glebkrep.yandexcup.yoga.features.reportBuilder

import com.glebkrep.yandexcup.yoga.data.BreathingItem
import com.glebkrep.yandexcup.yoga.data.TypeBreath
import com.glebkrep.yandexcup.yoga.utils.durationMillis
import com.glebkrep.yandexcup.yoga.utils.millisToSeconds

class ReportBuilder(val data: List<BreathingItem>) {

    fun generateMessage(): String {
        var finalString = "==== Отчет о дыхании ===="
        var prevItem:BreathingItem? = null
        for (item in data) {
            var newLine = ""
            if (item.type!=TypeBreath.PAUSE){
                newLine = "\n${item.type.label} (${item.durationMillis.millisToSeconds()})"
            }
            else{
                newLine = when (prevItem?.type){
                    TypeBreath.BREATHE_IN ->{
                        "\nЗадержка дыхания (${item.durationMillis.millisToSeconds()})"
                    }
                    TypeBreath.BREATHE_OUT ->{
                        "\n---Пауза(${item.durationMillis.millisToSeconds()})---"
                    }
                    else -> {
                        "\n${item.type.label} (${item.durationMillis.millisToSeconds()})"
                    }
                }
            }
            prevItem = item
            finalString += newLine
        }
        return finalString
    }
}