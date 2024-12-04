package com.example.newssearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.features.newslist.NewsMainScreen
import com.example.uikit.NewsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsTheme {
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color =  MaterialTheme.colorScheme.background
                ) {
                    NewsMainScreen()
                }
            }
        }
    }
}
