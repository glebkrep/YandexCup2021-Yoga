package com.glebkrep.yandexcup.yoga.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "breathing_item_table")
data class BreathingItem(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val type:TypeBreath,
    @ColumnInfo(name = "start_time")
    val startTimestamp:Long,
    @ColumnInfo(name = "end_time")
    val endTimestamp:Long,
)

enum class TypeBreath(val label:String){
    PAUSE("Пауза"),
    BREATHE_IN("Вдох"),
    BREATHE_OUT("Выдох")
}