package com.santosh.sensor_service;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

public class SensorSdk {
    private static SensorSdk instance;
    private Context context;

    private SensorSdk(Context context) {
        this.context = context;
    }

    public static SensorSdk getInstance(Context context){
        if (instance == null){
            instance = new SensorSdk(context);
        }
        return instance;
    }

    public void startService() {
        Intent intent = new Intent(context, SensorService.class);
        context.startService(intent);
    }

    public void stopService() {
        Intent intent = new Intent(context, SensorService.class);
        context.stopService(intent);
    }

    public boolean isServiceRunning(){
        return isMyServiceRunning(SensorService.class);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
