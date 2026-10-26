package jp.pastelmike.onsen_tamago

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.pastelmike.onsen_tamago.ui.theme.OnsenTamagoTheme
import kotlinx.coroutines.delay

// Log.d("OnsenTamago", message)
fun logD(message: String) {
    Log.d("OnsenTamago", message)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OnsenTamagoTheme {
                OnsenTamagoScreen()
            }
        }
    }

    @Composable
    fun OnsenTamagoScreen()
    {
        var inputText by remember { mutableStateOf("") }

        Scaffold(){innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                        label = { Text("入力欄") }

                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            inputText = inputText.dropLast(1)
                        }

                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Clear"
                        )
                    }

                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "決定"
                        )
                    }

                }

                DrawingCanvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    onRecognized = { text ->
                        inputText += text
                    }

                )

            }

        }

    }




    @Composable
    fun DrawingCanvas(modifier: Modifier = Modifier,
                      onRecognized: (String) -> Unit)
    {

        val paths = remember { mutableStateListOf<Path>() }
        val inkStrokes = remember { mutableStateListOf<List<PointerData>>() }
        var currentStroke = remember { mutableStateListOf<PointerData>() }
        var debounceTrigger by remember { mutableIntStateOf(0) }
        var resultText by remember { mutableStateOf("ここに結果が表示されます") }
        val textMeasurer = rememberTextMeasurer()
        val style = TextStyle(
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        LaunchedEffect(debounceTrigger) {
            if (debounceTrigger > 0) {
                delay(1000L)
                recognizeHandwriting(inkStrokes, onSuccess = { text ->
                    if (text.isNotEmpty()) {
                        onRecognized(text)
                    } else {
                        "認識できませんでした。"
                    }
                    logD("recognizeHandwrite result: $resultText")


                    paths.clear()
                    currentStroke.clear()
                    inkStrokes.clear()


                }, onFailure = { exception ->
                    resultText = "エラーが発生しました: ${exception.localizedMessage}"
                })
            }
        }

        Canvas(
            modifier = modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            currentStroke.clear()
                            currentStroke.add(PointerData(offset))
                            debounceTrigger = 0
                            logD("drag start")
                        },
                        onDragEnd = {
                            inkStrokes.add(currentStroke.toList())

                            paths.add(
                                Path().apply {
                                    moveTo(currentStroke[0].offset.x, currentStroke[0].offset.y)

                                    for (i in 1 until currentStroke.size) {
                                        lineTo(currentStroke[i].offset.x, currentStroke[i].offset.y)
                                    }
                                })

                            debounceTrigger++
                        },
                        onDragCancel = {
                        },
                        onDrag = { change, _ ->
                            val offset = change.position
                            currentStroke.add(PointerData(offset))
                            debounceTrigger = 0
                        }
                    )
                }
        ) {
            val textLayoutResult = textMeasurer.measure(
                text = "currentStroke: ${currentStroke.size}",
                style = style
            )

            drawText(
                textLayoutResult,
                topLeft = Offset(100f, size.height - textLayoutResult.size.height )
            )

            if (currentStroke.size > 1) {
                val path = Path().apply {
                    moveTo(currentStroke[0].offset.x, currentStroke[0].offset.y)

                    for (i in 1 until currentStroke.size) {
                        lineTo(currentStroke[i].offset.x, currentStroke[i].offset.y)
                    }
                }

                drawPath(
                    path = path, color = Color.Black,
                    style = Stroke(5f, cap = StrokeCap.Round)
                )
            }

            paths.forEach { path ->
                drawPath(path, color = Color.Black, style = Stroke(5f, cap = StrokeCap.Round))
            }
        }

    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        OnsenTamagoTheme {
            OnsenTamagoScreen()
        }
    }
}