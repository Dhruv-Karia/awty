package edu.uw.ischool.dkaria.awty

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

class MainActivity : ComponentActivity() {

    private lateinit var messageEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var minutesEditText: EditText
    private lateinit var startStopButton: Button

    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        messageEditText = findViewById(R.id.messageEditText)
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText)
        minutesEditText = findViewById(R.id.minutesEditText)
        startStopButton = findViewById(R.id.startStopButton)

        startStopButton.setOnClickListener {
            if (startStopButton.text == "Start") {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.SEND_SMS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.SEND_SMS),
                        1
                    )
                } else {
                    startSendingSMS()
                }
            } else {
                startStopButton.text = "Start"
                timer?.cancel()
            }
        }
    }

    private fun startSendingSMS() {
        val message = messageEditText.text.toString()
        val phoneNumber = phoneNumberEditText.text.toString()
        val minutes = minutesEditText.text.toString().toIntOrNull()

        if (message.isEmpty() || phoneNumber.isEmpty() || minutes == null || minutes <= 0) {
            Toast.makeText(this, "Please fill out all fields with valid values.", Toast.LENGTH_SHORT).show()
        } else if (phoneNumber.any { it.isLetter() }) {
            Toast.makeText(this, "Letters are not allowed in the phone number section", Toast.LENGTH_SHORT).show()
        } else {
            startStopButton.text = "Stop"
            timer = Timer()
            timer?.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    val smsManager = android.telephony.SmsManager.getDefault()
                    smsManager.sendTextMessage(phoneNumber, null, message, null, null)
                }
            }, 0, (minutes * 60 * 1000).toLong())
        }
    }
}