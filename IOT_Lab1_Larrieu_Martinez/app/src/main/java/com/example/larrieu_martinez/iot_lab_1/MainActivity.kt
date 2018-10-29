package com.example.larrieu_martinez.iot_lab_1

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.StringRequest

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker
import com.estimote.coresdk.observation.region.beacon.BeaconRegion
import com.estimote.coresdk.recognition.packets.Beacon
import com.estimote.coresdk.service.BeaconManager
import java.util.ArrayList
import java.util.Collections
import java.util.HashMap
import java.util.UUID
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private var beaconManager: BeaconManager? = null
    private var region: BeaconRegion? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var room = 0

        beaconManager = BeaconManager(this)
        region = BeaconRegion("ranged region",
                UUID.fromString("b9407f30-f5f8-466e-aff9-25556b57fe6d"), null, null)

        beaconManager!!.setRangingListener(BeaconManager.BeaconRangingListener { region, list ->
            if (!list.isEmpty()) {
                val nearestBeacon = list[0]
                val places = placesNearBeacon(nearestBeacon)
                if (room != places!![0].toInt()){
                    room = places!![0].toInt()

                    server_request_devices(room)
                }
            }
            else {
                room = 0
                server_request_devices(room)
            }
        })
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
                Thread.sleep(250)
                server_request_devices(room)
            }
        })

        mRequestQueue.add(mStringRequest)

    }

    override fun onResume() {
        super.onResume()
        SystemRequirementsChecker.checkWithDefaultDialogs(this)

        beaconManager!!.connect { beaconManager!!.startRanging(region) }
    }

    override fun onPause() {
        beaconManager!!.stopRanging(region)
        super.onPause()

    }

    private fun placesNearBeacon(beacon: Beacon): List<String>? {
        val no_beacon_list = listOf("0")
        val beaconKey = String.format("%d:%d", beacon.major, beacon.minor)
        return if (PLACES_BY_BEACONS.containsKey(beaconKey)) {
            PLACES_BY_BEACONS[beaconKey]
        } else no_beacon_list
    }

    companion object {
        private val PLACES_BY_BEACONS: Map<String, List<String>>

        init {
            val placesByBeacons = HashMap<String, List<String>>()
            placesByBeacons["23899:517"] = object : ArrayList<String>() {
                init {
                    add("1") //beacon 3 -> room 1

                }
            }
            placesByBeacons["23899:48851"] = object : ArrayList<String>() {
                init {
                    add("2") //beacon 13 -> room 2

                }
            }
            PLACES_BY_BEACONS = Collections.unmodifiableMap(placesByBeacons)
        }
    }
}
