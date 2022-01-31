package com.santosh.aidlsensorserver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.santosh.sensor_service.SensorSdk;

public class MainActivity extends AppCompatActivity {

    private SensorSdk sensorSdk;
    private TextView serviceStatus;
    private Button startServiceBtn;
    private Button stopServiceBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceStatus = findViewById(R.id.serviceStatus);
        startServiceBtn = findViewById(R.id.startServiceBtn);
        stopServiceBtn = findViewById(R.id.stopServiceBtn);

        sensorSdk = SensorSdk.getInstance(this);

        startServiceBtn.setOnClickListener(view -> {
            if (!sensorSdk.isServiceRunning()) {
                sensorSdk.startService();
                serviceStatus.setText("Service Started");
            } else {
                Toast.makeText(this, "Service already running!", Toast.LENGTH_SHORT).show();
            }
        });

        stopServiceBtn.setOnClickListener(view -> {
            if (sensorSdk.isServiceRunning()) {
                sensorSdk.stopService();
                serviceStatus.setText("Service Stopped");
            } else {
                Toast.makeText(this, "Service not running!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}