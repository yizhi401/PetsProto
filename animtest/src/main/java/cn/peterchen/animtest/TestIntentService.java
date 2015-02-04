package cn.peterchen.animtest;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by peter on 15-2-2.
 */
public class TestIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public TestIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }


}
