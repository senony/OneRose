package com.example.websocket.util

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.hasCameraPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        android.Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}