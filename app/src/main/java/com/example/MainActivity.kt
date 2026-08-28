package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.ui.MainAppContainer
import com.example.ui.theme.MinimalBackground
import com.example.ui.theme.SmartChiclayoTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      SmartChiclayoTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MinimalBackground
        ) {
          MainAppContainer()
        }
      }
    }
  }
}

