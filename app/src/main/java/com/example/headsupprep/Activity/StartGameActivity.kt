package com.example.headsupprep.Activity

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Surface
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.core.view.isVisible
import com.example.headsupprep.Model.Celeb
import com.example.headsupprep.Model.CelebrityGame
import com.example.headsupprep.R
import com.example.headsupprep.Resource.APIClinet
import com.example.headsupprep.Resource.APIInterface
import io.paperdb.Paper
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL

class StartGameActivity : AppCompatActivity() {

    private lateinit var llTop: LinearLayout
    private lateinit var llMain: LinearLayout
    private lateinit var llCelebrity: LinearLayout
    private lateinit var tvTime: TextView
    private lateinit var tvName: TextView
    private lateinit var tvTaboo1: TextView
    private lateinit var tvTaboo2: TextView
    private lateinit var tvTaboo3: TextView
    private lateinit var tvMain: TextView
    private lateinit var btStart: Button

    lateinit var arrayCeleb: Celeb
    private var gameActive = false

    private var celeb = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_game)

        connectView()

        btStart.setOnClickListener {
            /* Paper is a library alternative to shared preferences
         but works as a fast NoSQL storage. Helped me in storing
         data of non-primitive type, in this case is Celeb which
         is an array list of CelebrityGame.
         I chose to store data so I don't bother calling the API
         each time running the app.
       */
            Paper.init(this) // initialize paper object
            arrayCeleb = Paper.book().read(
                "celebs",
                Celeb()
            ) // get stored Celeb object. If there's none, the default value is an empty object
            celeb = Paper.book().read("celebsCount", 0)

            if (arrayCeleb.isEmpty()) { // first time running the app, or data's wiped
                setCelebs() // get data from the API
                Log.d("celebCount", "inside if celebs.isEmpty()")
            } else { // not the first time running the app but at the beginning of the game
                newCelebrity()
                Log.d("celebCount", "inside else, the count is $celeb")
            }

            startTimer()
        }
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            newCelebrity() // the counter increased when phone was in portrait mode so we just have to update the views
            updateStatus(false)
        }
        else {
            Paper.book().write("celebsCount", ++celeb) // to get new celebrity when phone get back to landscape mode
            updateStatus(true)
        }
    }


    private fun connectView() {
        llTop = findViewById(R.id.llTop)
        llMain = findViewById(R.id.llMain)
        llCelebrity = findViewById(R.id.llCelebrity)
        tvTime = findViewById(R.id.tvTime)
        tvName = findViewById(R.id.tvName)
        tvTaboo1 = findViewById(R.id.tvTaboo1)
        tvTaboo2 = findViewById(R.id.tvTaboo2)
        tvTaboo3 = findViewById(R.id.tvTaboo3)
        tvMain = findViewById(R.id.tvMain)
        btStart = findViewById(R.id.btStart)
    }


    private fun setCelebs() {
        val apiInterface = APIClinet().getClinet()?.create(APIInterface::class.java)
        val call: Call<List<CelebrityGame?>> = apiInterface!!.showInfo()

        call?.enqueue(object: Callback<List<CelebrityGame?>> {
            override fun onResponse(call: Call<List<CelebrityGame?>>, response: Response<List<CelebrityGame?>>) {
                val resource: List<CelebrityGame?>? = response.body() // set the response to the object to be iterated through
                newCelebrity() // to be displayed first time
                Paper.book().write("celebs", arrayCeleb) // store it in paper
            }
            override fun onFailure(call: Call<List<CelebrityGame?>>, t: Throwable) {
                Toast.makeText(this@StartGameActivity, t.message, Toast.LENGTH_SHORT).show()
                call.cancel()
            }
        })
    }


    private fun newCelebrity() {
        val currentCeleb = arrayCeleb[celeb]
        tvName.text = currentCeleb.name
        tvTaboo1.text = currentCeleb.taboo1
        tvTaboo2.text = currentCeleb.taboo2
        tvTaboo3.text = currentCeleb.taboo3
    }


    private fun updateStatus(showCelebrity: Boolean) {
        if (showCelebrity) {
            llCelebrity.isVisible = true
            llMain.isVisible = false
        } else {
            llCelebrity.isVisible = false
            llMain.isVisible = true
        }
    }


    private fun startTimer() {
        if (!gameActive) {
            gameActive = true
            tvMain.text = "Please Rotate Device"
            btStart.isVisible = false
            val rotation = windowManager.defaultDisplay.rotation
            if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
                updateStatus(false)
            } else {
                updateStatus(true)
            }

            object : CountDownTimer(60000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    tvTime.text = "Time: ${millisUntilFinished / 1000}"
                }

                override fun onFinish() {
                    gameActive = false
                    tvTime.text = "Time: --"
                    tvMain.text = "Heads Up!"
                    btStart.isVisible = true
                    updateStatus(false)
                }
            }.start()
        }
    }
}