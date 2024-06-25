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

class MainActivity : AppCompatActivity() {
    private lateinit var slider: Slider
    private lateinit var myText: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //akan menggunakan 2 slider dan 2 table di firebase

        slider = findViewById(R.id.sl_slider)
        myText = findViewById(R.id.tv_kecerahan)
        

        // Write a message to the database
        val database = Firebase.database
        val myRef = database.getReference("nilai_int")

        slider.addOnChangeListener(Slider.OnChangeListener { slider, value, fromUser ->
            myRef.setValue(value.toInt())
            myText.text = value.toInt().toString()
        })
    }

}