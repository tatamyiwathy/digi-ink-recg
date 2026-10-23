package jp.pastelmike.onsen_tamago

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import jp.pastelmike.onsen_tamago.ui.theme.OnsenTamagoTheme


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
                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                    DrawingCanvas("OnsenTamago", modifier = Modifier.padding(padding))
                }
            }
        }
    }


    @Composable
    fun DrawingCanvas(name: String, modifier: Modifier = Modifier) {
        logD("DrawingCanvas")

        val points = remember { mutableStateListOf<Offset>() }
        val drawedPath = remember { mutableStateListOf<Path>() }

        Canvas(
            modifier = modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    /*detectTapGestures {
                        logD("Tap {at $it")
                    }*/

                    /*detectDragGestures { change, dragAmount ->
                        logD("Drag {at $dragAmount")
                        change.consume()
                        // 現在の指の位置を追従させる
                        points.add(change.position)
                    }*/
                    detectDragGestures (
                        onDragStart = {
                            logD("DragStart")
                            points.clear()
                        },
                        onDragEnd = {
                            logD("DragEnd")

                            drawedPath.add(
                                Path().apply {
                                    moveTo(points[0].x, points[0].y)

                                    for (i in 1 until points.size) {
                                        lineTo(points[i].x, points[i].y)
                                    }
                                }
                            )
                        },
                        onDragCancel = {
                            logD("DragCancel")
                        },
                        onDrag = { change, dragAmount ->
                            logD("Drag {at $dragAmount")
                            change.consume()
                            // 現在の指の位置を追従させる
                            points.add(change.position)
                        }
                    )                }
        ) {
            if (points.size > 1) {
                val path = Path().apply {
                    moveTo(points[0].x, points[0].y)

                    for (i in 1 until points.size) {
                        lineTo(points[i].x, points[i].y)
                    }
                }

                drawPath(path = path, color = Color.Black,
                    style = Stroke(5f, cap = StrokeCap.Round))
            }

            for (path in drawedPath) {
                drawPath(path = path, color = Color.Black,
                    style = Stroke(5f, cap = StrokeCap.Round))
            }

        }

        Text(
            text = "Hello $name!",
            modifier = modifier
        )
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
            Greeting("Android")
        }
    }
}