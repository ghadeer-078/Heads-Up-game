package com.example.headsupprep.Activity

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.Surface
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.headsupprep.Model.Celeb
import com.example.headsupprep.Model.CelebItem
import com.example.headsupprep.R
import com.example.headsupprep.Resource.APIClinet
import com.example.headsupprep.Resource.APIInterface
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    lateinit var celebs: Celeb // the list of celebrities
    private var celebCount = 0 // to iterate celebrities

    private var gameActive = false


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
            celebs = Paper.book().read(
                "celebs",
                Celeb()
            ) // get stored Celeb object. If there's none, the default value is an empty object
            celebCount = Paper.book().read("celebCount", 0)

            if (celebs.isEmpty()) { // first time running the app, or data's wiped
                setCelebs() // get data from the API
                Log.d("celebCount", "inside if celebs.isEmpty()")
            } else { // not the first time running the app but at the beginning of the game
                updateCelebViews()
                Log.d("celebCount", "inside else, the count is $celebCount")
            }

            startTimer()
        }
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            updateCelebViews() // the counter increased when phone was in portrait mode so we just have to update the views

            updateStatus(true)
//            llMain.visibility = INVISIBLE
//            llCelebrity.visibility = VISIBLE

        } else {
            Paper.book().write(
                "celebsCount",
                ++celebCount
            ) // to get new celebrity when phone get back to landscape mode

            updateStatus(false)
//            llCelebrity.visibility = INVISIBLE
//            llMain.visibility = VISIBLE
        }
    }


    //...
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menue, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_addCelebrity -> {
                startGame(MainActivity())
                return true
            }
            R.id.mueu_gotoPlay -> {
                startGame(StartGameActivity())
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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
        val call: Call<Celeb?>? = apiInterface!!.getCelebs()
        call?.enqueue(object : Callback<Celeb?> {
            override fun onResponse(call: Call<Celeb?>, response: Response<Celeb?>) {
                celebs = response.body()!! // set the response to the object to be iterated through

                updateCelebViews() // to be displayed first time
                Paper.book().write("celebs", celebs) // store it in paper
            }

            override fun onFailure(call: Call<Celeb?>, t: Throwable) {
                Toast.makeText(this@StartGameActivity, t.message, Toast.LENGTH_SHORT).show()
                call.cancel()
            }
        })
    }


    private fun updateCelebViews() {
        val currentCeleb = celebs[celebCount]
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


    private fun startGame(activity: Activity) {
        val intent = Intent(this, activity::class.java)
        startActivity(intent)
    }

}