package com.glebkrep.yandexcup.yoga.features.breatheDetector

import android.content.Context
import android.media.AudioRecord
import com.glebkrep.yandexcup.yoga.data.BreathingState
import com.glebkrep.yandexcup.yoga.utils.Debug
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.tensorflow.lite.support.audio.TensorAudio
import org.tensorflow.lite.task.audio.classifier.AudioClassifier
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

class BreathingDetector (context: Context) {
    private var job: Job? =null
    val classifier: AudioClassifier = AudioClassifier.createFromFile(context, MODEL_PATH)
    val record: AudioRecord = classifier.createAudioRecord()

    suspend fun startRecording(
        intervalMillis: Long = 500,
        detectedSound: (BreathingState) -> (Unit)
    ) {
        coroutineScope {

            job = launch {
                var lastState: BreathingState = BreathingState.NotStarted
                var lastBreathe: BreathingState = BreathingState.BreatheOut(0L)

                val tensorAudio: TensorAudio = classifier.createInputTensorAudio()
                record.startRecording()

                Timer().scheduleAtFixedRate(1, intervalMillis) {
                    tensorAudio.load(record)
                    val output = classifier.classify(tensorAudio)

                    val filteredModelOutput = output[0].categories.filter {
                        (it.score > PROBABILITY_THRESHOLD) &&
                                it.label in listOf(BREATHING)
                    }
                    Debug.log("BreathingDetector: output: ${filteredModelOutput.map { it.label }}")
                    val outputClassifications = filteredModelOutput.maxByOrNull { it.score }
                    val startTime: Long = System.currentTimeMillis()

                    if (outputClassifications?.label ?: "" == lastState.label) {
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
        }
    }

    fun stop() {
        record.stop()
        job?.cancel()
        job = null
    }

    companion object {
        const val BREATHING = "Breathing"
        private const val MODEL_PATH = "lite-model_yamnet_classification_tflite_1.tflite"
        private const val PROBABILITY_THRESHOLD: Float = 0.1f
    }
}