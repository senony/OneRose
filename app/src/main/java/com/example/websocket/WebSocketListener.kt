package com.example.websocket

import android.util.Log
import android.util.LogPrinter
import com.example.websocket.model.MoveRobot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString.Companion.toByteString
import org.json.JSONObject


class WebSocketListener(private val direction : String): WebSocketListener() {


    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        Log.d("Socket", "connected!! Response: $response")
        val userDirection = when(direction) {
            "N" -> MoveRobot().moveForward()
            "S" -> MoveRobot().moveBackward()
            "CW" -> MoveRobot().turnRight()
            "CCW" -> MoveRobot().turnLeft()
            else -> MoveRobot().stop()
        }
        sendMessage(webSocket, userDirection,)
    }

    private fun sendMessage(webSocket: WebSocket, direction: String) {
        val jsonMessage = createJsonMessage(direction).toString().toByteArray()
        val result = webSocket.send(jsonMessage.toByteString())
        Log.d("Socket", "send result: $result")
    }



    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        Log.d("Socket", "receiving: $text")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        Log.d("Socket", "Closing: $code / $reason")
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        t.printStackTrace()
        Log.e("Socket", "Error: " + t.message)
    }

    private fun createJsonMessage(direction: String): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("type", "control")
        jsonObject.put("direction", direction)
        jsonObject.put("speed" , 0.8)
        jsonObject.put("servo", "90")
        //jsonObject.put("action", "move_forward")
        //jsonObject.put("pose" , "standing")

        return jsonObject
    }

    companion object {
        private const val NORMAL_CLOSURE_STATUS = 1000
    }
}