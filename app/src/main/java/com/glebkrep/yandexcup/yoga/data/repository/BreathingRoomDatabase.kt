package com.glebkrep.yandexcup.yoga.data.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.glebkrep.yandexcup.yoga.data.BreathingItem

@TypeConverters(Converters::class)
@Database(entities = [BreathingItem::class], version = 1)
abstract class BreathingRoomDatabase : RoomDatabase() {

    abstract fun breathingItemDao(): BreathingItemDao


    companion object {
        @Volatile
        private var INSTANCE: BreathingRoomDatabase? = null

        fun getDatabase(context: Context): BreathingRoomDatabase {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            } else {
                synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        BreathingRoomDatabase::class.java, "local_db"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                    return instance
                }

            }
        }
    }
}