package com.glebkrep.yandexcup.yoga.features.reportBuilder

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import com.glebkrep.yandexcup.yoga.data.BreathingItem
import com.glebkrep.yandexcup.yoga.utils.Utils
import com.glebkrep.yandexcup.yoga.utils.durationMillis
import com.glebkrep.yandexcup.yoga.utils.millisToSeconds
import java.io.File

class ReportBuilder(val data:List<BreathingItem>) {


    fun generateMessage():String {
        var finalString = "==== Breathing Report ===="
        for (item in data){
            finalString+="\n${item.type.name}(${item.durationMillis.millisToSeconds()})"
        }
        return finalString
    }
}