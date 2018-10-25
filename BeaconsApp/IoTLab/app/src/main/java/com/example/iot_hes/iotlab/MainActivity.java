package com.example.iot_hes.iotlab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

public class MainActivity extends AppCompatActivity {

    TextView PositionText;
    EditText Percentage;
    Button   IncrButton;
    Button   DecrButton;
    Button   LightButton;
    Button   StoreButton;
    Button   RadiatorButton;


    public static final String TAG = "MyTag";

    // Start the queue
    // In the "OnCreate" function below:
    // - TextView, EditText and Button elements are linked to their graphical parts (Done for you ;) )
    // - "OnClick" functions for Increment and Decrement Buttons are implemented (Done for you ;) )
    //
    // TODO List:
    // - Use the Estimote SDK to detect the closest Beacon and figure out the current Room
    //     --> See Estimote documentation:  https://github.com/Estimote/Android-SDK
    // - Set the PositionText with the Room name
    // - Implement the "OnClick" functions for LightButton, StoreButton and RadiatorButton


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PositionText   =  findViewById(R.id.PositionText);
        Percentage     =  findViewById(R.id.Percentage);
        IncrButton     =  findViewById(R.id.IncrButton);
        DecrButton     =  findViewById(R.id.DecrButton);
        LightButton    =  findViewById(R.id.LightButton);
        StoreButton    =  findViewById(R.id.StoreButton);
        RadiatorButton =  findViewById(R.id.RadiatorButton);

        // Only accept input values between 0 and 100
        Percentage.setFilters(new InputFilter[]{new InputFilterMinMax("0", "100")});

        /////////////////////////////////////////////////////////
        //RequestQueue queue = Volley.newRequestQueue(this);

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        final RequestQueue mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();





        IncrButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int number = Integer.parseInt(Percentage.getText().toString());
                if (number<100) {
                    number++;
                    Log.d("IoTLab-Inc", String.format("%d",number));
                    Percentage.setText(String.format("%d",number));
                }
            }
        });

        DecrButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int number = Integer.parseInt(Percentage.getText().toString());
                if (number>0) {
                    number--;
                    Log.d("IoTLab-Dec", String.format("%d",number));
                    Percentage.setText(String.format("%d",number));
                }
            }
        });


        LightButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Send HTTP Request to command light
                Log.d("IoTLab", Percentage.getText().toString());
            }
        });



        StoreButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String url = "http://192.168.1.109/127.1.0.1/4/2/blind_control/"+ Percentage.getText();

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        response.substring(0,500),
                                        Toast.LENGTH_SHORT);

                                toast.show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                error.toString(),
                                Toast.LENGTH_SHORT);

                        toast.show();
                    }
                });

                stringRequest.setTag(TAG);

                // Add the request to the RequestQueue.
                mRequestQueue.add(stringRequest);

                Toast toast = Toast.makeText(getApplicationContext(),
                        "OK",
                        Toast.LENGTH_LONG);

                toast.show();
            }
        });



        RadiatorButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                // TODO Send HTTP Request to command radiator
                Log.d("IoTLab", Percentage.getText().toString());
            }
        });


    }


    // You will be using "OnResume" and "OnPause" functions to resume and pause Beacons ranging (scanning)
    // See estimote documentation:  https://developer.estimote.com/android/tutorial/part-3-ranging-beacons/
    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onPause() {
        super.onPause();

    }

}

// This class is used to filter input, you won't be using it.

class InputFilterMinMax implements InputFilter {
    private int min, max;

    public InputFilterMinMax(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public InputFilterMinMax(String min, String max) {
        this.min = Integer.parseInt(min);
        this.max = Integer.parseInt(max);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            int input = Integer.parseInt(dest.toString() + source.toString());
            if (isInRange(min, max, input))
                return null;
        } catch (NumberFormatException nfe) { }
        return "";
    }

    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}
