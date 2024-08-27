package com.demo.unitykotlin

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.demo.unitykotlin.ui.theme.UnityKotlinDemoTheme

import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.unity3d.player.UnityPlayerActivity


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UnityKotlinDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = { callGamePlay() }
                        ) {
                            Text(text = "Start UnityPlayer")
                        }
                    }
                }
            }
        }

    }

    private fun callGamePlay(){
        val intent = Intent(this, UnityPlayerActivity::class.java)
        startActivity(intent);
    }

}

@Composable
fun Greeting(name: String) {
    Text(
        text = "Hello $name!",
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UnityKotlinDemoTheme {
        Greeting("Android")
    }
}
