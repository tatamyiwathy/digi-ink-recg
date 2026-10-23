package jp.pastelmike.onsen_tamago

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
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

        Canvas(modifier = modifier.fillMaxSize()) {

            drawLine(
                brush = SolidColor(Color.Black),
                start = Offset(0f, 0f),
                end = Offset(size.width, size.height),
                strokeWidth = 5f
            )
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