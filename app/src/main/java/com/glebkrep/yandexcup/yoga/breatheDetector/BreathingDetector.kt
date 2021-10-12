package com.glebkrep.yandexcup.yoga.breatheDetector

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioRecord
import androidx.core.content.ContextCompat
import com.glebkrep.yandexcup.yoga.data.BreathingState
import com.glebkrep.yandexcup.yoga.utils.Debug
import org.tensorflow.lite.support.audio.TensorAudio
import org.tensorflow.lite.task.audio.classifier.AudioClassifier
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

class BreathingDetector(context: Context) {
    private val classifier: AudioClassifier = AudioClassifier.createFromFile(context, MODEL_PATH)
    private val tensorAudio: TensorAudio = classifier.createInputTensorAudio()
    private val record: AudioRecord = classifier.createAudioRecord()

    private var timerTask: TimerTask? = null

    fun startRecording(intervalMillis: Long = 500, detectedSound: (BreathingState) -> (Unit)) {
        record.startRecording()

        var lastState: BreathingState = BreathingState.NotStarted
        var lastBreathe: BreathingState = BreathingState.BreatheOut(0L)
        timerTask = Timer().scheduleAtFixedRate(1, intervalMillis) {
            tensorAudio.load(record)
            val output = classifier.classify(tensorAudio)

            val filteredModelOutput = output[0].categories.filter {
                (it.score > PROBABILITY_THRESHOLD) &&
                        it.label in listOf(BREATHING)
            }
            Debug.log("BreathingDetector: output: ${filteredModelOutput.map { it.label }}")
            val outputClassifications = filteredModelOutput.maxByOrNull { it.score }
            val startTime: Long = System.currentTimeMillis()

            if (outputClassifications?.label?:"" == lastState.label) {
                //this is continuation of previous state
                Debug.log("BreathingDetector: prev state")
                detectedSound.invoke(BreathingState.PrevState)
                return@scheduleAtFixedRate
            }
            when (outputClassifications?.label) {
                null -> {
                    //this is silence
                    Debug.log("BreathingDetector: silence")
                    lastState = BreathingState.Silence(startTime)
                    detectedSound.invoke(lastState)
                }
                BREATHING -> {
                    when (lastBreathe) {
                        is BreathingState.BreatheIn -> {
                            //this is breathe out
                            Debug.log("BreathingDetector: breathe out")
                            lastBreathe = BreathingState.BreatheOut(startTime)
                            lastState = lastBreathe
                            detectedSound.invoke(lastBreathe)
                        }
                        else -> {
                            //this is breathe in
                            Debug.log("BreathingDetector: breathe in")
                            lastBreathe = BreathingState.BreatheIn(startTime)
                            lastState = lastBreathe
                            detectedSound.invoke(lastBreathe)
                        }
                    }
                }
            }

        }
    }

    private fun pauseRecording() {
        record.stop()
        timerTask?.cancel()
        timerTask = null
    }

    fun stop() {
        pauseRecording()
        classifier.close()
    }

    companion object {
        const val SILENCE = "Silence"
        const val BREATHING = "Breathing"

        private const val MODEL_PATH = "lite-model_yamnet_classification_tflite_1.tflite"
        private const val PROBABILITY_THRESHOLD: Float = 0.1f
        const val REQUEST_RECORD_AUDIO = 1337

        fun requestPermission(activity: Activity): Boolean {
            val check =
                ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO)
            if (check != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    REQUEST_RECORD_AUDIO
                )
                return false
            }
            return true
        }
    }
}