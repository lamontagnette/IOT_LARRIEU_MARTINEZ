package com.example.larrieu_martinez.iot_lab_1

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.widget.*
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.StringRequest

class DataActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.data_layout)


        val info_array : Array<String> = intent.getStringArrayExtra("EXTRA_DATA")

        val device_name = info_array[0]
        val action = info_array[1]

        val server_ip : String = resources.getString(R.string.server_ip)

        val edittext = findViewById<EditText>(R.id.data)
        val button = findViewById<Button>(R.id.button)

        val cache = DiskBasedCache(cacheDir, 1024 * 1024)
        val network = BasicNetwork(HurlStack())
        val mRequestQueue = RequestQueue(cache, network)
        mRequestQueue.start()

        button.setOnClickListener(){

            val url = "http://"+ server_ip +":5000/" + device_name + "/" + action + "/" + edittext.text.toString()

            var mStringRequest = StringRequest(Request.Method.GET, url, object : Response.Listener<String> {
                override fun onResponse(response: String) {
                    Toast.makeText(this@DataActivity, response, Toast.LENGTH_LONG).show()
                }
            }, object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError) {
                    Toast.makeText(this@DataActivity, error.toString(), Toast.LENGTH_LONG).show()
                }
            })
            mRequestQueue.add(mStringRequest)
        }
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