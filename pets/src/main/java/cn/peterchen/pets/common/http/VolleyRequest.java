package cn.peterchen.pets.common.http;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.peterchen.pets.common.utils.GsonUtil;
import cn.peterchen.pets.entity.Result;

/**
 * volley所用到的request
 * <p/>
 * 目前只支持返回JSONObject类型,并且不提供get方法
 * <p/>
 * 该类不需要使用者调用，只要HttpUtil调用
 * <p/>
 * Created by peter on 14-8-6.
 */
public class VolleyRequest extends com.android.volley.Request<String> {

    private static final String PROTOCOL_CHARSET = "utf-8";
    /**
     * Content type for request.
     */
    private static final String PROTOCOL_CONTENT_TYPE =
            String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    private final Response.Listener<String> mListener;
    private Context context;
    private RequestParams parameters;
    private String mRequestBody;

    /**
     * The consturctor for jsonObject request
     *
     * @param context
     * @param url
     * @param params
     * @param listener
     */
    public <T> VolleyRequest(Context context, String url, RequestParams params, final VolleyRequestListener<T> listener, final TypeToken<Result<T>> typeToken) {
        super(Method.POST, url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                listener.onNetError(volleyError);
            }
        });

        this.context = context;
        this.parameters = params;
        generateParams();
        mListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String jsonString) {
                Log.i("mInfo", "ResponseBody = " + jsonString.toString());
                try {
                    Result<T> jsonResult = GsonUtil.fromJson(jsonString, typeToken);
                    listener.onSuccess(jsonResult.getResult());
                } catch (Exception e) {
                    //if the request is not succeeded, the response cannot be parsed using type token
                    Result<Object> jsonResult = GsonUtil.fromJson(jsonString, new TypeToken<Result<Object>>() {
                    });
                    if (!jsonResult.isSuccess()) {
                        Log.i("mInfo", jsonResult.getMsg());
                        listener.onResponseError(jsonResult.getMsg());
                    }
                }

            }
        };
    }


    private void generateParams() {
        mRequestBody = GsonUtil.toJson(parameters.getUrlParams());
        Log.i("http", "requestBody = " + mRequestBody);
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
//        return parameters.getUrlParams();
        return null;
    }


    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);

    }


    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
        return Response.success(parsed,
                HttpHeaderParser.parseCacheHeaders(response));
    }


}

