package com.glebkrep.yandexcup.yoga.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import com.glebkrep.yandexcup.yoga.data.BreathingItem
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*

fun Context.getActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

val BreathingItem.durationMillis: Long
    get() {
        return endTimestamp - startTimestamp
    }

fun Long.millisToSeconds():String{
    val seconds = this.toDouble()/1000f
    val decimal = BigDecimal(seconds).setScale(2, RoundingMode.HALF_EVEN)
    return "$decimal s"
}

fun Long.millisToHMS():String{
    val time = SimpleDateFormat("MM.dd HH:mm:ss").format(Date(this))
    return time
}