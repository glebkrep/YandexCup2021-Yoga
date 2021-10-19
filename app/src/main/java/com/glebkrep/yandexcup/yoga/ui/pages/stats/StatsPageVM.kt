package com.glebkrep.yandexcup.yoga.ui.pages.stats

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.glebkrep.yandexcup.yoga.data.StatsState
import com.glebkrep.yandexcup.yoga.data.repository.BreathingItemRepository
import com.glebkrep.yandexcup.yoga.data.repository.BreathingRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class StatsPageVM(application: Application):AndroidViewModel(application) {
    private var breathingItemRepository: BreathingItemRepository? = null

    private var _statsState:MutableLiveData<StatsState> = MutableLiveData(StatsState.Loading)
    var statsState:LiveData<StatsState> = _statsState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            breathingItemRepository = BreathingItemRepository(
                BreathingRoomDatabase.getDatabase(getApplication()).breathingItemDao()
            )
            breathingItemRepository?.let { it ->
                it.getAllBreathingItems()
                    .collect {
                        if (it.isEmpty()){
                            _statsState.postValue(StatsState.NoStats)
                        }
                        _statsState.postValue(StatsState.Success(it))
                    }
            }

        }
    }

}