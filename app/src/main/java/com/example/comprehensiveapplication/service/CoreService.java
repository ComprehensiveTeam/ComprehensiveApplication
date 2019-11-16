package com.example.comprehensiveapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

@Deprecated
public class CoreService extends Service {
    public CoreService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
