package com.example.timetreeapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import com.codegama.timetreeapplication.R
import com.codegama.timetreeapplication.databinding.ActivitySplashScreenBinding
import com.example.timetreeapplication.activity.ToDoHome

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        binding.button.setOnClickListener{
            val intent= Intent(this, ToDoHome::class.java)
            startActivity(intent)
            finish()
        }

       /* Handler(Looper.getMainLooper()).postDelayed({
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        },30000)*/


    }

}