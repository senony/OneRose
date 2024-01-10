package com.example.websocket.model

class MoveRobot {
    fun moveForward(): String {
        return "N"
    }

    fun moveBackward(): String {
        return "S"
    }

    fun turnLeft(): String {
        return "CW"
    }

    fun turnRight(): String {
        return "CCW"
    }

    fun stop(): String {
        return "STOP"
    }
}