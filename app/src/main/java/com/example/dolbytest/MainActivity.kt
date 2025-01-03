package com.example.dolbytest

import android.media.MediaCodecList
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dolbytest.ui.theme.DolbyTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Check if the device has a Dolby decoder
        val isDolbyDevice = checkDolbyDecoder()

        setContent {
            DolbyTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = if (isDolbyDevice) "Dolby Device" else "Non-Dolby Device",
                        isDolbyDevice = isDolbyDevice,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun checkDolbyDecoder(): Boolean {
        // Iterate through all media codecs available on the device
        for (i in 0 until MediaCodecList.getCodecCount()) {
            val info = MediaCodecList.getCodecInfoAt(i)
            if (info.isEncoder) {
                continue // Skip encoders, we only care about decoders
            }
            // Check supported types for Dolby codecs
            for (type in info.supportedTypes) {
                if (type == "audio/ac4" || type == "audio/eac3") {
                    // Found a Dolby AC-4 or Dolby Digital Plus (DDP) decoder
                    return true
                }
            }
        }
        return false // No Dolby decoders found
    }
}

@Composable
fun Greeting(name: String, isDolbyDevice: Boolean, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hello $name!",
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp)) // Add space between text and emoji
        Text(
            text = if (isDolbyDevice) "✅" else "❌",
            fontSize = 40.sp, // Emoji size
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DolbyTestTheme {
        Greeting("Android", isDolbyDevice = true)
    }
}
