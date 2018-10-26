package com.example.larrieu_martinez.iot_lab_1

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.StringRequest

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ////////////
        //TODO : beacons modify room var. If nothing -> room = 0
        var room = 1
        ////////////


        server_request_devices(room)

    }

    fun server_request_devices(room : Int){

        val url = "http://"+ resources.getString(R.string.server_ip) +":5000/devices/" + room.toString()

        val textview = findViewById<TextView>(R.id.room_title)

        when(room){ // TODO : IN beacons func -> text = server inecc.. here
            0 -> textview.text = "Vous n'êtes pas à proximité d'un Beacons"
            else -> textview.text = "Vous êtes dans la pièce " + room.toString()
        }

        val cache = DiskBasedCache(cacheDir, 1024 * 1024)
        val network = BasicNetwork(HurlStack())
        val mRequestQueue = RequestQueue(cache, network)
        mRequestQueue.start()

        var mStringRequest = StringRequest(Request.Method.GET, url, object : Response.Listener<String> {
            override fun onResponse(response: String) {

                var list_devices = response.split(",")
                var listview = findViewById<ListView>(R.id.list_devices)

                var sAdapter = ArrayAdapter<String>(this@MainActivity, android.R.layout.simple_list_item_1, list_devices)
                listview.adapter = sAdapter

                listview.setOnItemClickListener { parent, view, position, id ->
                    //Toast.makeText(this, "Position Clicked:"+" "+position, Toast.LENGTH_LONG).show()
                    var intent = Intent(this@MainActivity,ActionsActivity::class.java)
                    intent.putExtra("EXTRA_POSITION",list_devices[position])
                    startActivity(intent)
                    finish()
                }
            }
        }, object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError) {
                Toast.makeText(this@MainActivity, error.toString(), Toast.LENGTH_LONG).show()
                Thread.sleep(500)
                server_request_devices(room)
            }
        })

        mRequestQueue.add(mStringRequest)

    }
}
