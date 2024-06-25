package com.example.dimmer

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.Slider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var slider: Slider
    private lateinit var myText: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        slider = findViewById(R.id.sl_slider)
        myText = findViewById(R.id.tv_kecerahan)


        // Write a message to the database
        val database = Firebase.database
        val myRef = database.getReference("nilai_int")

        slider.addOnChangeListener(Slider.OnChangeListener { slider, value, fromUser ->
            myRef.setValue(value.toInt())
            myText.text = value.toInt().toString()

            val brightness = value.toInt()
            sendBrightnessToNodeMCU(brightness)
        })
    }

    private fun sendBrightnessToNodeMCU(brightness: Int) {
        val client = OkHttpClient()
        val url = "https://192.168.1.100/set_brightness" // Ubah IP dengan IP NodeMCU Anda
        val json = "{\"brightness\":$brightness}"

        val body = RequestBody.create("application/json".toMediaTypeOrNull(), json)
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
//                response.use {
                    if (!response.isSuccessful) {
                        throw IOException("Unexpected code $response")
                    }
//                }
            }
        })
    }
}