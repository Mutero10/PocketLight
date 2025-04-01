package com.example.pocketlight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.pocketlight.ui.theme.PocketLightTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PocketLightTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun FlashlightApp() {
    var isFlashlightOn by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Button(
            onClick = {
                isFlashlightOn = !isFlashlightOn
                toggleFlashlight(context, isFlashlightOn)
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = if (isFlashlightOn) "Turn Off" else "Turn On")
        }
    }
}

private fun toggleFlashlight(context: Context, isFlashlightOn: Boolean) {
    val cameraManager = ContextCompat.getSystemService(context, CameraManager::class.java)
    val cameraId = cameraManager?.cameraIdList?.firstOrNull { id ->
        cameraManager.getCameraCharacteristics(id)
            .get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
    }

    if (cameraId != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        try {
            cameraManager.setTorchMode(cameraId, isFlashlightOn)
        } catch (e: Exception) {
            Toast.makeText(context, "Error toggling flashlight: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    } else {
        Toast.makeText(context, "Flashlight not available", Toast.LENGTH_SHORT).show()
    }
}