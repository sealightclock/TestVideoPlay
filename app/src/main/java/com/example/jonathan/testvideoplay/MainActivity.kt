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
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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

    val sites = listOf(
        SiteInfo("YouTube", "https://www.youtube.com", Icons.Default.PlayArrow),
        SiteInfo("ChatGPT", "https://chat.openai.com", Icons.Default.Info)
    )

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            sites.forEach { site ->
                val isSelected = site.url == currentUrl

                val buttonContent: @Composable RowScope.() -> Unit = {
                    Icon(site.icon, contentDescription = site.label)
                    Spacer(Modifier.width(4.dp))
                    Text(site.label)
                }

                if (isSelected) {
                    Button(
                        onClick = { /* already selected, no action */ },
                        enabled = false,
                        modifier = Modifier.weight(1f),
                        content = buttonContent
                    )
                } else {
                    OutlinedButton(
                        onClick = { currentUrl = site.url },
                        enabled = true,
                        modifier = Modifier.weight(1f),
                        content = buttonContent
                    )
                }
            }
        }

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
            update = { it.loadUrl(currentUrl) }
        )
    }
}

data class SiteInfo(
    val label: String,
    val url: String,
    val icon: ImageVector
)
