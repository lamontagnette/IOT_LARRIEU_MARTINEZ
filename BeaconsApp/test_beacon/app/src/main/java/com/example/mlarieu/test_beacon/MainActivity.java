package com.example.mlarieu.test_beacon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.estimote.coresdk.common.config.EstimoteSDK;
import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.recognition.packets.ConfigurableDevice;
import com.estimote.coresdk.service.BeaconManager;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private BeaconManager beaconManager = new BeaconManager(getApplicationContext());;


    beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
        @Override
        public void onServiceReady() {
            beaconManager.startMonitoring(new BeaconRegion(
                    "monitored region",
                    UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
                    22504, 48827));
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beaconManager = new BeaconManager(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }





}
