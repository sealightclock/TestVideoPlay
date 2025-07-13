package com.example.jonathan.testvideoplay

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WebViewWithUrlInput()
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewWithUrlInput() {
    var currentUrl by remember { mutableStateOf("https://www.youtube.com") }

    Column(modifier = Modifier.fillMaxSize()) {
        // Button row with proper modifier placement
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (currentUrl == "https://www.youtube.com") {
                Button(
                    onClick = { currentUrl = "https://www.youtube.com" },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("YouTube")
                }
            } else {
                OutlinedButton(
                    onClick = { currentUrl = "https://www.youtube.com" },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("YouTube")
                }
            }

            if (currentUrl == "https://chat.openai.com") {
                Button(
                    onClick = { currentUrl = "https://chat.openai.com" },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("ChatGPT")
                }
            } else {
                OutlinedButton(
                    onClick = { currentUrl = "https://chat.openai.com" },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("ChatGPT")
                }
            }
        }

        // WebView rendering
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        useWideViewPort = true
                        loadWithOverviewMode = true
                        builtInZoomControls = true
                        displayZoomControls = false
                        userAgentString = "Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 " +
                                "(KHTML, like Gecko) Chrome/119.0.0.0 Mobile Safari/537.36"
                    }

                    layoutParams = android.widget.FrameLayout.LayoutParams(
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    setInitialScale(100)
                    webChromeClient = WebChromeClient()
                    webViewClient = WebViewClient()

                    loadUrl(currentUrl)
                }
            },
            update = { webView ->
                webView.loadUrl(currentUrl)
            }
        )
    }
}
