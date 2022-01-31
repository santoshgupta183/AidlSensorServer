package com.santosh.sensor_service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.Timer;
import java.util.TimerTask;

public class SensorService extends Service {

    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private ISensorDataListener listener;
    private boolean mustReadSensor;

    @Override
    public void onCreate() {
        sensorManager = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                try {
                    if (listener != null && mustReadSensor) {
                        listener.onSensorDataChanged(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
                        mustReadSensor = false;
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mustReadSensor = true;
            }
        }, 0, 8);  // 1000 ms delay

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        return stub;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        sensorManager.unregisterListener(sensorEventListener, sensor);
        return super.onUnbind(intent);
    }

    ISensorAidlInterface.Stub stub = new ISensorAidlInterface.Stub() {
        @Override
        public void setListener(ISensorDataListener listener) throws RemoteException {
            SensorService.this.listener = listener;
        }
    };
}