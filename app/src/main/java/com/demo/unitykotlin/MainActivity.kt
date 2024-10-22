package com.demo.unitykotlin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.demo.unitykotlin.ui.theme.UnityKotlinDemoTheme
import com.unity3d.player.IUnityMessageListener
import com.unity3d.player.UnityPlayer
import com.unity3d.player.UnityPlayerActivity


// IUnityMessageListener interface used to share messages between UnityPlayerActivity to MainActivity
class MainActivity : ComponentActivity(), IUnityMessageListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UnityPlayerActivity.setiUnityMessageListener(this)
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
                            onClick = { callUnityPlayerActivity() }
                        ) {
                            Text(text = "Start UnityPlayer")
                        }
                    }
                }
            }
        }

    }
    //star Unity Player
    private fun callUnityPlayerActivity(){
        val intent = Intent(this, UnityPlayerActivity::class.java)
        startActivity(intent);
    }
    // Message received from unity
    override fun onUnityMessageReceived(route: String?) {
        Log.e("abc:", "MainActivity onUnityMessageReceived: $route")
        sendMessageToUnity(route)
    }
}
// send message to Unity
fun sendMessageToUnity(route: String?) {
    Log.e("abc:", "MainActivity sendMessageToUnity: ", )
    if(route == AndroidRouts.AUTH_TOKEN){
        UnityPlayer.UnitySendMessage("AndroidNativeBridge", "ReceiveMessageFromAndroid", "Paste auth token here...")
    }
}
class AndroidRouts {
    companion object {
        const val AUTH_TOKEN = "AUTH_TOKEN"
        const val HOME_PAGE = "HOME_PAGE"
        const val UNITY_INITIALIZED = "UNITY_INITIALIZED"
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

