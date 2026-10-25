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
    logD("inkStrokes: ${inkStrokes.size}")
    inkStrokes[0].forEach {
        logD("inkStrokes: ${it.offset}, ${it.timestamp}")
    }

    // 1. Ink.Builder を作成
    val inkBuilder = Ink.builder()

    // 2. 記録したストロークデータを Ink に変換
    for (strokePoints in inkStrokes) {
        val strokeBuilder = Ink.Stroke.builder()
        for (point in strokePoints) {
            strokeBuilder.addPoint(
                Ink.Point.create(point.offset.x, point.offset.y, point.timestamp)
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
                logD("result: $candidateText")
                onSuccess(candidateText)
            }
            .addOnFailureListener { e ->
                logD("failure: $e")
                onFailure(e)
            }
    }

    remoteModelManager.download(model, DownloadConditions.Builder().build())
        .addOnSuccessListener {
            recognize()
        }
        .addOnFailureListener { e ->
            logD("failure: $e")
            onFailure(e)
        }


}