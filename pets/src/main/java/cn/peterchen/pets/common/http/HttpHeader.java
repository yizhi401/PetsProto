package cn.peterchen.pets.common.http;

import android.content.Context;
import android.util.Log;


import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.HashMap;
import java.util.Map;

/**
 * 名字超级清楚的啦！
 * 提供两种header方式：
 * 1. Header[]
 * 2. Map<String,String>
 *
 * @author peter
 */
public class HttpHeader {

    private static HttpHeader instance;

    private Header[] headers;

    private Map<String, String> headerMap;

    private Context context;

    private HttpHeader(Context context) {
        this.context = context;
        initHeaders();
    }

    public static HttpHeader getInstance(Context context) {
        if (instance == null) {
            synchronized (HttpHeader.class) {
                if (instance == null) {
                    instance = new HttpHeader(context);
                }
            }
        }
        return instance;
    }

    /**
     */
    private void initHeaders() {
        headers = new Header[6];
        headers[1] = new BasicHeader("phoneModel", "google");
        headerMap = new HashMap<String, String>();
        for (int i = 0; i < headers.length; i++) {
            headerMap.put(headers[i].getName(), headers[i].getValue());
        }
        printHeaders();
    }

    private void refreshHeaders() {
        // 有一些可能会变化的东西，每次调用Header就重新获取一下吧
    }

    public Header[] getHeaders() {
        // 有一些可能会变化的东西，每次调用Header就重新获取一下吧
        refreshHeaders();
        return headers;
    }

    public Map<String, String> getHeaderMap() {
        refreshHeaders();
        return headerMap;
    }

    private void printHeaders() {
        for (int i = 0; i < headers.length; i++) {
            Log.i("http", headers[i].getName() + " " + headers[i].getValue());
        }
    }
}
