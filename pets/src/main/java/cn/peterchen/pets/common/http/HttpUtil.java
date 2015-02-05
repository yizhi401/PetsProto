package cn.peterchen.pets.common.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


import cn.peterchen.pets.global.PetApplication;

/**
 * Created by peter on 14-8-6.
 */
public class HttpUtil {

    private static final String TAG = "http";
    private static final boolean SHOULD_CACHE = false;

    public static void jsonRequest(final Context context, String url, RequestParams params, VolleyRequestListener listener) {
        Log.i(TAG, "command = " + url);
        System.out.println("command = " + url);
        VolleyRequest request = new VolleyRequest(context, url, params, listener);
        request.setShouldCache(SHOULD_CACHE);
//        PetApplication.getInstance().getmRequestQueue().add(request);
    }

}
