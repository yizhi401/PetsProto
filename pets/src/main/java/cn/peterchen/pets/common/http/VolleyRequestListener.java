package cn.peterchen.pets.common.http;

import com.android.volley.VolleyError;

/**
 * Created by peter on 14-8-6.
 */
public interface VolleyRequestListener<T> {
    /**
     * 请求成功
     *
     * @param response
     */
    public void onSuccess(T response);

    /**
     * 网络错误，得到VolleyError
     *
     * @param error
     */
    public void onNetError(VolleyError error);

    /**
     * 非网络错误，返回错误信息
     * @param msg
     */
    public void onResponseError(String msg);

}
