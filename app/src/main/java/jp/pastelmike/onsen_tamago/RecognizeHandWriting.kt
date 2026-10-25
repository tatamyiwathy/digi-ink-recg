package jp.pastelmike.onsen_tamago

import androidx.compose.ui.geometry.Offset
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.vision.digitalink.DigitalInkRecognition
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModel
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier
import com.google.mlkit.vision.digitalink.DigitalInkRecognizerOptions
import com.google.mlkit.vision.digitalink.Ink

data class PointerData(
    val offset: Offset,
    val timestamp: Long = System.currentTimeMillis()
)

fun recognizeHandwriting(
    inkStrokes: List<List<PointerData>>,
    onSuccess: (String) -> Unit,
    onFailure: (Exception) -> Unit
) {

    val inkBuilder = Ink.builder()

    for (strokePoints in inkStrokes) {
        val strokeBuilder = Ink.Stroke.builder()
        strokePoints.forEach {
            strokeBuilder.addPoint(
                Ink.Point.create(it.offset.x, it.offset.y, it.timestamp)
            )
        }
        inkBuilder.addStroke(strokeBuilder.build())
    }

    val inkObject = inkBuilder.build()
    val modelIdentifier = DigitalInkRecognitionModelIdentifier.JA
    val model = DigitalInkRecognitionModel.builder(modelIdentifier).build()
    val remoteModelManager = RemoteModelManager.getInstance()

    // 認識
    fun recognize() {
        val options = DigitalInkRecognizerOptions.builder(
            model
        ).build()

        val recognizer = DigitalInkRecognition.getClient(options)

        recognizer.recognize(inkObject)
            .addOnSuccessListener { result ->
                // 最も信頼度の高い候補テキストを取得
                val candidateText = result.candidates.firstOrNull()?.text ?: ""
                onSuccess(candidateText)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    remoteModelManager.download(model, DownloadConditions.Builder().build())
        .addOnSuccessListener {
            recognize()
        }
        .addOnFailureListener { e ->
            onFailure(e)
        }


}