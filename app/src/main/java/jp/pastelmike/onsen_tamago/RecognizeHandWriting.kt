package jp.pastelmike.onsen_tamago

import androidx.compose.ui.geometry.Offset
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
    val modelIdentifier = DigitalInkRecognitionModelIdentifier.EN
    val model = DigitalInkRecognitionModel.builder(modelIdentifier).build()

    // 3. デジタルインク認識を実行（日本語モデル `ja` の例）
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