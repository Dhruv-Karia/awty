package edu.uw.ischool.dkaria.awty
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
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
                            runOnUiThread {
                                val toast = Toast.makeText(this@MainActivity, "", Toast.LENGTH_LONG)
                                toast.setText("Texting $phoneNumber: $message")
                                toast.show()
                            }
                        }
                    }, 0, (minutes * 60 * 1000).toLong())
                }
            } else {
                startStopButton.text = "Start"
                timer?.cancel()
            }
        }
    }
}
