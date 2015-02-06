package cn.peterchen.pets.common.http;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import cn.peterchen.pets.entity.Result;
import cn.peterchen.pets.global.PetApplication;

/**
 * Created by peter on 14-8-6.
 */
public class HttpUtil {

    private static final String TAG = "http";
    private static final boolean SHOULD_CACHE = false;

    public static <T> void jsonRequest(final Context context, String url, RequestParams params, TypeToken<Result<T>> typeToken, VolleyRequestListener<T> listener) {
        Log.i(TAG, "Request Url = " + URLConfig.COMMON_URL + url);
        if (params == null) {
            params = new RequestParams();
        }
        VolleyRequest request = new VolleyRequest(context, URLConfig.COMMON_URL + url, params, listener, typeToken);
        request.setShouldCache(SHOULD_CACHE);
        PetApplication.getInstance().getmRequestQueue().add(request);
    }

}
