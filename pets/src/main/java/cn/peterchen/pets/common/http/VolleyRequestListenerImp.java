package cn.peterchen.pets.common.http;

import com.android.volley.VolleyError;

/**
 * Created by peter on 15-2-6.
 */
public abstract class VolleyRequestListenerImp<T> implements VolleyRequestListener<T> {

    @Override
    public abstract void onSuccess(T response);

    @Override
    public void onNetError(VolleyError error) {

    }

    @Override
    public void onResponseError(String msg) {

    }
}
