package cn.peterchen.imtest.xmpp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.peterchen.imtest.xmpp.core.XmppEntityCapsCache;


public class StorageLowReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        XmppEntityCapsCache.emptyCache();
    }

}
