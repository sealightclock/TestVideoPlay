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
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.TextFieldValue
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
    var textFieldValue by remember { mutableStateOf(TextFieldValue("https://www.google.com")) }

    val sites = listOf(
        SiteInfo("YouTube", "https://www.youtube.com", Icons.Default.PlayArrow),
        SiteInfo("News", "https://news.google.com", Icons.Default.Info),
        SiteInfo("ChatGPT", "https://chat.openai.com", Icons.Default.Search) // Updated icon
    )

    Column(modifier = Modifier.fillMaxSize()) {

        // Buttons in vertical list
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            sites.forEach { site ->
                val isSelected = site.url == currentUrl

                val buttonContent: @Composable RowScope.() -> Unit = {
                    Icon(site.icon, contentDescription = site.label)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        site.label,
                        fontSize = MaterialTheme.typography.labelLarge.fontSize // slightly smaller
                    )
                }

                if (isSelected) {
                    Button(
                        onClick = { /* already selected */ },
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                        content = buttonContent
                    )
                } else {
                    OutlinedButton(
                        onClick = {
                            currentUrl = site.url
                            textFieldValue = TextFieldValue(site.url)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        content = buttonContent
                    )
                }
            }

            // Text field for user-entered URL
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = textFieldValue,
                onValueChange = {
                    textFieldValue = it
                    currentUrl = it.text
                },
                label = { Text("Enter URL") },
                singleLine = true
            )
        }

        // WebView container
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp),
            factory = { context ->
                WebView(context).apply {
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        useWideViewPort = true
                        loadWithOverviewMode = true
                        builtInZoomControls = true
                        displayZoomControls = false
                        userAgentString =
                            "Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 " +
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
