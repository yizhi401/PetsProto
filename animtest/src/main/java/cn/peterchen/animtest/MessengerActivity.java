package cn.peterchen.animtest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

/**
 * Created by peter on 15-2-2.
 */
public class MessengerActivity extends Activity {

    Messenger messengerService;
    boolean mBound;

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            messengerService = new Messenger(service);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            mBound = false;
            messengerService = null;
        }
    };


    final Messenger mMessenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                msg.replyTo.send(Message.obtain(null, MessengerService.SAY_HELLO, 0));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            super.handleMessage(msg);

        }
    });


    public void sayHello() {
        if (!mBound)
            return;
        ;
        Message msg = Message.obtain(null, MessengerService.SAY_HELLO, 0);
        try {
            messengerService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, MessengerService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mServiceConnection);
            mBound = false;
        }
    }
}
