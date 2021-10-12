package com.glebkrep.yandexcup.yoga.utils

import android.util.Log
import com.glebkrep.yandexcup.yoga.BuildConfig

object Debug {
    fun log(any:Any?){
        if (BuildConfig.DEBUG){
            Log.e("Yoga.Debug:",any.toString())
        }
    }
}