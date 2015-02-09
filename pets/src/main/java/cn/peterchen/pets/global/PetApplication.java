package cn.peterchen.pets.global;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by peter on 15-1-27.
 */
public class PetApplication extends Application {

    private Context context;
    private static PetApplication instance;
    private RequestQueue mRequestQueue;

    public static PetApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = getBaseContext();
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public RequestQueue getmRequestQueue() {
        return mRequestQueue;
    }
}
