package cn.peterchen.animtest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * Created by peter on 15-2-2.
 */
public class TestBoundService extends Service {

    private IBinder mBinder;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public String getHello() {
        return "Hello Bind Service!";
    }

    class MyBinder extends Binder {

        public TestBoundService getService() {
            return TestBoundService.this;
        }

    }

}
