package com.example.headsupprep.Activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.headsupprep.*
import com.example.headsupprep.Model.Celeb
import com.example.headsupprep.Model.CelebItem
import com.example.headsupprep.Resource.APIClinet
import com.example.headsupprep.Resource.APIInterface
import com.example.headsupprep.Resource.rvCelebrity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    lateinit var rvCeleb: RecyclerView
    lateinit var addCeleBtn: Button
    lateinit var editCelebrity: EditText
    lateinit var btnSub: Button

    lateinit var arrayCeleb: ArrayList<CelebItem>

    var searcher = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadRV()
        connectView()
        apiInterFace()

        addCeleBtn.setOnClickListener {
            val intent = Intent(this, New_Celebrity::class.java)
            startActivity(intent)
        }

        btnSub.setOnClickListener {
            for (i in arrayCeleb) {
                if (i.name == editCelebrity.text.toString()) {
                    searcher = i.pk!!.toInt()
                }
            }
            val intent = Intent(this, Update_Celebrity::class.java)
            intent.putExtra("id", searcher)
            startActivity(intent)
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


    private fun loadRV() {
        rvCeleb = findViewById(R.id.rvCeleb)
        arrayCeleb = arrayListOf()
        rvCeleb.adapter = rvCelebrity(arrayCeleb)
        rvCeleb.layoutManager = LinearLayoutManager(this)
    }


    private fun connectView() {
        editCelebrity = findViewById(R.id.edtCele)
        addCeleBtn = findViewById(R.id.addBtn)
        btnSub = findViewById(R.id.subBtn)
    }


    private fun apiInterFace() {
        val apiInterface = APIClinet().getClinet()?.create(APIInterface::class.java)
        val call: Call<Celeb?>? = apiInterface!!.getCelebs()

        call?.enqueue(object : Callback<Celeb?> {
            override fun onResponse(call: Call<Celeb?>, response: Response<Celeb?>) {
                val resource: List<CelebItem?>? =
                    response.body()// response.body() {information in API }
                if (resource != null) {
                    for (i in resource) {
                        arrayCeleb.add(CelebItem(i!!.pk, i.name, i.taboo1, i.taboo2, i.taboo3))
                        rvCeleb.adapter?.notifyDataSetChanged()
                        rvCeleb.scrollToPosition(arrayCeleb.size - 1)
                    }
                }
            }

            override fun onFailure(call: Call<Celeb?>, t: Throwable) {
                call.cancel()
                Log.d("MainActivity", "${t.message}")
            }
        })
    }


    private fun startGame(activity: Activity) {
        val intent = Intent(this, activity::class.java)
        startActivity(intent)
    }


}