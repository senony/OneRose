package com.example.websocket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.websocket.client.WebSocketClient
import com.example.websocket.model.MoveRobot
import com.example.websocket.ui.theme.WebSocketTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WebSocketTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Socket()
                }
            }
        }
    }
}


@Composable
fun MoveButton(
    buttonText: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(buttonText)
    }
}

@Composable
fun Socket(modifier: Modifier = Modifier) {
    var direction by remember { mutableStateOf<String>(MoveRobot().stop()) }

    Text(
        text = stringResource(R.string.button_title),
        textAlign = TextAlign.Center,
        fontSize = 40.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .fillMaxWidth()
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MoveButton("Move Forward") {
            direction = MoveRobot().moveForward()
            WebSocketClient(direction)
        }
        MoveButton("Move Backward") {
            direction = MoveRobot().moveBackward()
            WebSocketClient(direction)
        }
        MoveButton("Turn Right") {
            direction = MoveRobot().turnRight()
            WebSocketClient(direction)
        }
        MoveButton("Turn Left") {
            direction = MoveRobot().turnLeft()
            WebSocketClient(direction)
        }
        MoveButton("Stop") {
            direction = MoveRobot().stop()
            WebSocketClient(direction)
        }

        Text(text = "Direction: $direction", fontSize = 24.sp)
    }
}


@Preview(showBackground = true)
@Composable
fun SocketPreview() {
    WebSocketTheme {
        Socket()
    }
}