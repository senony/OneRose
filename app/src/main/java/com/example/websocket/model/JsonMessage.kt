package com.example.websocket.model

import org.json.JSONObject

class JsonMessage {
    private fun createJsonMessage(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("type", "control")
        jsonObject.put("direction", "N")
        jsonObject.put("speed" , "0.8")
        jsonObject.put("servo", "90")
        //jsonObject.put("action", "move_forward")
        //jsonObject.put("pose" , "standing")

        return jsonObject
    }
}