package com.appcognito.calleridsample

import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.appcognito.calleridsample.ui.theme.CallerIDSampleTheme

class MainActivity : ComponentActivity() {
    private val receiver = CustomPhoneStateReceiver(onResult)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CallerIDSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }

        requestMultiplePermissions.launch(
            arrayOf(
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.READ_PHONE_NUMBERS,
                android.Manifest.permission.INTERNET
            )
        )

        registerReceiver(receiver, IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED))

    }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.e("LOG_TAG", "${it.key} = ${it.value}")
            }
        }
}

private val onResult: (String, String?, Uri?) -> Unit = { phone, name, photoUri ->
    println("onResult -> $phone, $name, $photoUri ")
//    val dataTv = findViewById<TextView>(R.id.data_tv)
//    val imageView = findViewById<ImageView>(R.id.imageView_screenshot)
    val uriString = photoUri.toString().ifBlank { "No photo" }
    val formattedName = name?.ifBlank { phone } ?: phone
    Log.e("", "calling: $phone, $formattedName, $uriString")
//    dataTv.text = "phone number: $phone\ncallerName: $formattedName\nimageUri: ${if(uriString == "null") "No photo" else uriString}"
    // requestMediaProjection()
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CallerIDSampleTheme {
        Greeting("Android")
    }
}

