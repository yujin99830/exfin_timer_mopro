package kr.ac.dankook.mobile.week091

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Button
import android.widget.TextView

var total = 0
var started = false
lateinit var Record : String
lateinit var result : String

class BkgThread : Thread() {
    override fun run() {
        while(true) {
            if(started) {
                total += 1
                Log.d("BkgThread", "In background thread : $total")
            }else {
                total = 0
            }
        }
    }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val timerText = findViewById<TextView>(R.id. timerText)
        val buttonStart = findViewById<Button>(R.id. buttonStart)
        val buttonStop = findViewById<Button>(R.id. buttonStop)
        val buttonRecord = findViewById<Button>(R.id. buttonRecord)
        var RecordText = findViewById<TextView>(R.id. RecordText)

        val myHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                Log.d("BkgThread", "Main thread")
                if(msg.what == 1) {
                    val hour = String.format("%02d", total/3600)
                    val minute = String.format("%02d",total/60)
                    val second = String.format("%02d",total%60)
                    timerText.text = "$hour : $minute : $second"// Update timerText.text
                    Record = timerText.text.toString()
                }
            }
        }

        Thread {
            while(true) {
                Thread.sleep(1000)
                if(started) {
                    total += 1
                    // Use sendMessage()
                    Log.d("BkgThread", "In background thread : $total")
                    var msg = myHandler.obtainMessage()
                    msg.what = 1
                    myHandler.sendMessage(msg)

                } else {
                    total=0
                }
            }
        }.start()

        buttonStart.setOnClickListener {
            started = true
            total = 0
            timerText.text = "00 : 00 : 00"
            RecordText.text = ""
            result = ""

        }

        buttonStop.setOnClickListener {
            started = false
            result = ""
        }

        buttonRecord.setOnClickListener {
            result += Record.toString()+"\n"
            RecordText.setText(result)
        }

    }
}