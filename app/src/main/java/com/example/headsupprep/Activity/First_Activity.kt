package com.example.headsupprep.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.headsupprep.R

class First_Activity : AppCompatActivity() {

    lateinit var btngotogame: Button
    lateinit var btnadd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        connectView()

        btngotogame.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        btnadd.setOnClickListener {
            val intent = Intent(this, New_Celebrity::class.java)
            startActivity(intent)
        }

    }

    private fun connectView(){
        btngotogame = findViewById(R.id.btGoGame)
        btnadd = findViewById(R.id.btAddCel)
    }

}