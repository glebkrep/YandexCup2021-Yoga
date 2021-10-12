package com.glebkrep.yandexcup.yoga.breatheDetector

import android.Manifest
import android.app.Activity
import android.content.Context
import android.media.AudioRecord
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

    private var timerTask : TimerTask? = null

    fun startRecording(intervalMillis:Long = 500, detectedSound:(BreathingState)->(Unit)) {
        record.startRecording()

        var lastBreathe:BreathingState = BreathingState.BreatheOut(0L)
        var hadSilence = true
        timerTask = Timer().scheduleAtFixedRate(1, intervalMillis) {
            tensorAudio.load(record)
            val output = classifier.classify(tensorAudio)

            val filteredModelOutput = output[0].categories.filter {
                (it.score > PROBABILITY_THRESHOLD) &&
                        it.label in listOf(BREATHING, SILENCE)
            }

            val outputClassifications = filteredModelOutput.maxByOrNull { it.score }
            val startTime:Long = System.currentTimeMillis()
            when (outputClassifications?.label){
                null, SILENCE -> {
                    //this is silence
                    Debug.log("BreathingDetector: silence")
                    hadSilence = true
                    detectedSound.invoke(BreathingState.Silence(startTime))
                }
                BREATHING -> {
                    if (hadSilence){
                        hadSilence = false
                        when(lastBreathe){
                            is BreathingState.BreatheIn->{
                                //this is breathe out
                                Debug.log("BreathingDetector: breathe out")
                                lastBreathe = BreathingState.BreatheOut(startTime)
                                detectedSound.invoke(BreathingState.BreatheOut(startTime))
                            }
                            else ->{
                                //this is breathe in
                                Debug.log("BreathingDetector: breathe in")
                                lastBreathe = BreathingState.BreatheIn(startTime)
                                detectedSound.invoke(BreathingState.BreatheIn(startTime))
                            }
                        }
                    }
                    else{
                        //this is continuation of previous breathe
                        Debug.log("BreathingDetector: prev breath")
                        detectedSound.invoke(BreathingState.PrevBreath)
                    }
                }
            }

        }
    }

    fun pauseRecording() {
        record.stop()
        timerTask?.cancel()
    }

    fun stop() {
        pauseRecording()
        classifier.close()
    }

    companion object {
        private const val SILENCE = "Silence"
        private const val BREATHING = "Breathing"

        private const val MODEL_PATH = "lite-model_yamnet_classification_tflite_1.tflite"
        private const val PROBABILITY_THRESHOLD: Float = 0.1f
        const val REQUEST_RECORD_AUDIO = 1337

        fun requestPermission(activity: Activity) {
            activity.requestPermissions(
                arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_RECORD_AUDIO
            )
        }
    }
}