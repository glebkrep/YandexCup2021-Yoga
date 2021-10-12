package com.glebkrep.yandexcup.yoga.data.repository

import com.glebkrep.yandexcup.yoga.data.BreathingItem

class BreathingItemRepository(private val breathingItemDao: BreathingItemDao) {
    suspend fun getAllNotifications() = breathingItemDao.getAllItems()


    suspend fun deleteById(id:Int){
        breathingItemDao.deleteById(id)
    }

    suspend fun insert(breathingItem:BreathingItem){
        breathingItemDao.insert(breathingItem)
    }

    suspend fun clearAll(){
        breathingItemDao.clearAll()
    }
}