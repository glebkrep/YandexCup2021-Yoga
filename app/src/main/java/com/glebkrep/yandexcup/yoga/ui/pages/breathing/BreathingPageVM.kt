package com.glebkrep.yandexcup.yoga.ui.pages.breathing

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.glebkrep.yandexcup.yoga.breatheDetector.BreathingDetector
import com.glebkrep.yandexcup.yoga.data.BreathingState
import com.glebkrep.yandexcup.yoga.utils.Debug
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BreathingPageVM(application: Application) : AndroidViewModel(application) {

    private val breathingDetector: BreathingDetector = BreathingDetector(getApplication())

    private val _breathingState: MutableLiveData<BreathingState> =
        MutableLiveData(BreathingState.NotStarted)
    val breathingState: LiveData<BreathingState> = _breathingState

    fun startRecording() {
        viewModelScope.launch(Dispatchers.Default) {
            breathingDetector.startRecording(200) {
                launch(Dispatchers.Default) {
                    breathingDetected(it)
                }
            }
        }
    }

    private suspend fun breathingDetected(detectorMessage: BreathingState) {
        if (detectorMessage !is BreathingState.PrevBreath){
            stateFinished(_breathingState.value)
        }
        when (detectorMessage) {
            is BreathingState.BreatheIn -> {
                _breathingState.postValue(detectorMessage)
            }
            is BreathingState.BreatheOut -> {
                _breathingState.postValue(detectorMessage)

            }
            is BreathingState.Silence -> {
                _breathingState.postValue(detectorMessage)

            }
            is BreathingState.PrevBreath -> {

            }
        }
    }

    private suspend fun stateFinished(breathingState: BreathingState?){
        if (breathingState==null){
            Debug.log("breathing state is null")
        }
        TODO("save to db")
    }

    override fun onCleared() {
        super.onCleared()
        breathingDetector.stop()
    }
}