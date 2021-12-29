package com.example.headsupprep.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.headsupprep.Resource.APIClinet
import com.example.headsupprep.Resource.APIInterface
import com.example.headsupprep.Model.AddCelebrity
import com.example.headsupprep.Model.CelebItem
import com.example.headsupprep.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class New_Celebrity : AppCompatActivity() {

    lateinit var nameN: EditText
    lateinit var taboN1: EditText
    lateinit var taboN2: EditText
    lateinit var taboN3: EditText
    lateinit var addNbt: Button
    lateinit var backNBtn: Button

    // Connect to APIInterface
    val apiInterface = APIClinet().getClinet()?.create(APIInterface::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_celebrity)

        connectView()


        addNbt.setOnClickListener {
            addCelebrity()
        }

        backNBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun connectView() {
        nameN = findViewById(R.id.nameEd)
        taboN1 = findViewById(R.id.tabo1Ed)
        taboN2 = findViewById(R.id.tabo2Ed)
        taboN3 = findViewById(R.id.tabo3Ed)
        addNbt = findViewById(R.id.addNbtn)
        backNBtn = findViewById(R.id.back1Btn)
    }


    private fun addCelebrity() {
        val newN = nameN.text.toString()
        val newT1 = taboN1.text.toString()
        val newT2 = taboN2.text.toString()
        val newT3 = taboN3.text.toString()

        apiInterface?.addCeleb(AddCelebrity(newN, newT1, newT2, newT3))?.enqueue(object :
            Callback<CelebItem?> {
            override fun onResponse(call: Call<CelebItem?>, response: Response<CelebItem?>) {
                Toast.makeText(this@New_Celebrity, "Celebrity Added", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<CelebItem?>, t: Throwable) {
                Toast.makeText(this@New_Celebrity, "No Celebrity Added!", Toast.LENGTH_LONG).show()
            }
        })

        // clear all editTexts
        nameN.text.clear()
        nameN.clearFocus()
        taboN1.text.clear()
        taboN1.clearFocus()
        taboN2.text.clear()
        taboN2.clearFocus()
        taboN3.text.clear()
        taboN3.clearFocus()
    }

}