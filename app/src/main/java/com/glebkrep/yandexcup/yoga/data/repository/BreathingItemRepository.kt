package com.glebkrep.yandexcup.yoga.data.repository

import com.glebkrep.yandexcup.yoga.data.BreathingItem

class BreathingItemRepository(private val breathingItemDao: BreathingItemDao) {
    fun getAllBreathingItems() = breathingItemDao.getAllItems()


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