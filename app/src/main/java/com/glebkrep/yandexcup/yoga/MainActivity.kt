package com.glebkrep.yandexcup.yoga

import android.Manifest
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.yandexcup.R
import org.tensorflow.lite.task.audio.classifier.AudioClassifier
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

//Алёна очень любит заниматься йогой, но постоянно забывает дышать правильно.
//Напишите приложение, которое будет определять с помощью встроенного микрофона, когда она делает вдох,
//когда — выдох, а когда вообще задержала дыхание, чтобы ее тренер Аркадий смог помочь ей тренировать дыхание.
//Решение должно определять вдох, выдох и время между ними.

//Приложение должно:
//  - определять момент вдоха,
//  - определять момент выдоха,
//  - считать и фиксировать время между вдохом-выдохом и выдохом-вдохом,
//  - составлять список вдохов-выдохов с зафиксированным временем между ними,
//  - отправлять список по электронной почте тренеру Аркадию.
class MainActivity : AppCompatActivity() {
    // TODO 2.1: defines the model to be used
    var modelPath = "lite-model_yamnet_classification_tflite_1.tflite"

    // TODO 2.2: defining the minimum threshold
    var probabilityThreshold: Float = 0.1f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val REQUEST_RECORD_AUDIO = 1337
        requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_RECORD_AUDIO)

        val recorderSpecsTextView = findViewById<TextView>(R.id.textViewAudioRecorderSpecs)

        // TODO 2.3: Loading the model from the assets folder
        val classifier = AudioClassifier.createFromFile(this, modelPath)

        // TODO 3.1: Creating an audio recorder
        val tensor = classifier.createInputTensorAudio()

        // TODO 3.2: showing the audio recorder specification
        val format = classifier.requiredTensorAudioFormat
        val recorderSpecs = "Number Of Channels: ${format.channels}\n" +
                "Sample Rate: ${format.sampleRate}"
        recorderSpecsTextView.text = recorderSpecs

        // TODO 3.3: Creating
        val record = classifier.createAudioRecord()
        record.startRecording()

        Timer().scheduleAtFixedRate(1, 500) {

            // TODO 4.1: Classifing audio data
            val numberOfSamples = tensor.load(record)
            val output = classifier.classify(tensor)

            // TODO 4.2: Filtering out classifications with low probability
            val filteredModelOutput = output[0].categories.filter {
                (it.score > probabilityThreshold)
            }

            // TODO 4.3: Creating a multiline string with the filtered results
            val outputStr =
                filteredModelOutput.sortedBy { -it.score }
                    .joinToString(separator = "\n") { "${it.label} -> ${it.score} " }

            // TODO 4.4: Updating the UI
            if (outputStr.isNotEmpty())
                runOnUiThread {
                    outputStr
                }
        }
    }
}