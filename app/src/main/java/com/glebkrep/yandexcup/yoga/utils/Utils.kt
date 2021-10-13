package com.glebkrep.yandexcup.yoga.utils

import java.io.File

object Utils {
    fun createFile(file: File?){
        if (file?.parentFile?.exists() != true)
            file?.parentFile?.mkdirs();
        if (file?.exists() != true)
            file?.createNewFile();
    }

}