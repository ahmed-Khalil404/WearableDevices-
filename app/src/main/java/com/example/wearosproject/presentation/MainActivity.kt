package com.example.wearosproject.presentation
import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.example.wearosproject.R
import com.example.wearosproject.presentation.theme.WearOSProjectTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var heartRateSensor: Sensor? = null
    private var heartRate by mutableStateOf<Float?>(null)
    private val Url = "http://192.168.115.211:1880/app/heart/"  // Replace with your Node-RED URL
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            setupSensors()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.BODY_SENSORS)
        } else {
            setupSensors()
        }

        setContent {
            WearApp(heartRate) {
                finishAffinity() // This will close the app
            }
        }
    }

    private fun postToNodeRed(json: Float, timestamp: Long): Int {
        var  responseCode = 0
        CoroutineScope(Dispatchers.IO).launch {
            try {
                System.out.println("postToNodeRed started")
                val url = URL(Url)
                System.out.println("URL created: $url")
                val connection = url.openConnection() as HttpURLConnection
                System.out.println("Connection established")
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val jsonObject = JSONObject().apply {
                    put("heart", heartRate)
                }

                // Write the JSON object to the connection's output stream
                val outputStream = connection.outputStream
                outputStream.write(jsonObject.toString().toByteArray())
                outputStream.flush()
                outputStream.close()

                responseCode = connection.responseCode
                System.out.println("HTTP Response Code: $responseCode")

                // Close the connection
                connection.disconnect()
                System.out.println("postToNodeRed completed")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return responseCode
    }




    private fun setupSensors() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
        heartRateSensor?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }




    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_HEART_RATE) {
            heartRate = event.values.firstOrNull() ?: heartRate
            heartRate?.let {
                // Call the new function with heart rate and current timestamp
                postToNodeRed(it, System.currentTimeMillis())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }
}

@Composable
fun WearApp(heartRate: Float?, onExitClick: () -> Unit) {
    WearOSProjectTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally // Center children horizontally
        ) {
            if (heartRate != null) {
                HeartRateDisplay(heartRate = heartRate)
            } else {
                Greeting(greetingName = stringResource(R.string.heartRate))
            }
            ExitButton(onExitClick = onExitClick) // This button will be centered
        }
    }
}
@Composable
fun ExitButton(onExitClick: () -> Unit) {
    Button(onClick = onExitClick) {
        Text(text = stringResource(id = R.string.exit_app))
    }
}

@Composable
fun HeartRateDisplay(heartRate: Float) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = "Heart Rate: ${heartRate.toInt()} BPM"
    )
}

@Composable
fun Greeting(greetingName: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = stringResource(id = R.string.heartRate, greetingName)
    )
}


@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp(heartRate = null, onExitClick = {})
}