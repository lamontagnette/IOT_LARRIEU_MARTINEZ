package com.example.larrieu_martinez.iot_lab_1

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.StringRequest

class ActionsActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actions_layout)

        val server_ip : String = resources.getString(R.string.server_ip)

        var device_name = intent.getStringExtra("EXTRA_POSITION")

        val textview = findViewById<TextView>(R.id.device_title)
        textview.text = device_name

        val cache = DiskBasedCache(cacheDir, 1024 * 1024)
        val network = BasicNetwork(HurlStack())
        val mRequestQueue = RequestQueue(cache, network)
        mRequestQueue.start()

        val url = "http://"+ server_ip +":5000/actions/"+device_name

        //String Request initialized
        var mStringRequest = StringRequest(Request.Method.GET, url, object : Response.Listener<String> {
            override fun onResponse(response: String) {

                var list_actions = response.split(",")
                var listview = findViewById<ListView>(R.id.list_actions)

                var sAdapter = ArrayAdapter<String>(this@ActionsActivity, android.R.layout.simple_list_item_1, list_actions)
                listview.adapter = sAdapter


                listview.setOnItemClickListener { parent, view, position, id ->
                    //Toast.makeText(this, "Position Clicked:"+" "+position, Toast.LENGTH_LONG).show()
                    if (list_actions[position].contains("control"))
                    {
                        val extra = arrayOf(device_name,list_actions[position])

                        var intent = Intent(this@ActionsActivity,DataActivity::class.java)
                        intent.putExtra("EXTRA_DATA",extra)
                        startActivity(intent)
                        finish()
                    }
                    else {
                        val url_action = "http://"+ server_ip +":5000/"+ device_name +"/" + list_actions[position]

                        //String Request initialized
                        var mStringRequest = StringRequest(Request.Method.GET, url_action, object : Response.Listener<String> {
                            override fun onResponse(response: String) {
                                Toast.makeText(this@ActionsActivity, response, Toast.LENGTH_LONG).show()
                            }
                        }, object : Response.ErrorListener {
                            override fun onErrorResponse(error: VolleyError) {
                                Toast.makeText(this@ActionsActivity, error.toString(), Toast.LENGTH_LONG).show()
                            }
                        })
                        mRequestQueue.add(mStringRequest)
                    }
                }
            }
        }, object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError) {
                Toast.makeText(this@ActionsActivity, error.toString(), Toast.LENGTH_LONG).show()
            }
        })

        mRequestQueue.add(mStringRequest)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
            true
        } else super.onKeyDown(keyCode, event)
    }
}