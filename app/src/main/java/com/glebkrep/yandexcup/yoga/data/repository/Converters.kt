package com.glebkrep.yandexcup.yoga.data.repository

import androidx.room.TypeConverter
import com.glebkrep.yandexcup.yoga.data.TypeBreath

class Converters {
    @TypeConverter
    fun toTypeBreath(value: String) = enumValueOf<TypeBreath>(value)

    @TypeConverter
    fun fromTypeBreath(value: TypeBreath) = value.name
}