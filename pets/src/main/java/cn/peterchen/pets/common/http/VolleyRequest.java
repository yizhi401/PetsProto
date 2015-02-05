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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import cn.peterchen.pets.common.utils.GsonUtil;

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
    private String command;
    private String mRequestBody;

    /**
     * 事实上这里给出的url只是一个参数command,url统一用的是URLConfig.COMMON_URL;
     *
     * @param context
     * @param url
     * @param params
     * @param listener
     */
    public <T> VolleyRequest(Context context, String url, RequestParams params, final VolleyRequestListener listener) {
        //目前只允许使用post方式传输
        super(Method.POST, URLConfig.COMMON_URL, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                listener.onNetError(volleyError);
            }
        });

        this.context = context;
        this.parameters = params;
        this.command = url;
        generateParams();
        mListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String jsonObject) {
                //尚未测试volley返回的JSONObject是何种类型
                Log.i("mInfo", "ResponseBody = " + jsonObject.toString());
                listener.onSuccess(jsonObject);
            }
        };

    }

    private void generateParams() {

        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.putAll(HttpHeader.getInstance(context).getHeaderMap());
        paramsMap.put("command", command);
        paramsMap.put("param", parameters.getUrlParams());
        mRequestBody = GsonUtil.toJson(paramsMap);

        Log.i("http", "requestBody = " + mRequestBody);
    }


    /**
     * 经确认，他们不要header
     * 要把所有参数都直接放到参数里面
     *
     * @return
     * @throws
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return super.getHeaders();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return null;
    }


    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);

    }


    @Override
    public byte[] getBody() {
        try {
            return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                    mRequestBody, PROTOCOL_CHARSET);
            return null;
        }
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed =
                    new String(response.data, PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
        return Response.success(parsed,
                HttpHeaderParser.parseCacheHeaders(response));
    }


    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }
}

