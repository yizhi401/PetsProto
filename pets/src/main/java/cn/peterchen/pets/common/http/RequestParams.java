package cn.peterchen.pets.common.http;

import org.apache.http.message.BasicNameValuePair;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通用Request Parameter类
 *
 * @author peter 修改
 */
public class RequestParams {

    protected final static String LOG_TAG = "RequestParams";
    protected final ConcurrentHashMap<String, String> urlParams = new ConcurrentHashMap<String, String>();

    /**
     * Constructs a new empty {@code RequestParams} instance.
     */
    public RequestParams() {
        this((Map<String, String>) null);
    }

    /**
     * Constructs a new RequestParams instance containing the key/value string
     * params from the specified map.
     *
     * @param source the source key/value string map to add.
     */
    public RequestParams(Map<String, String> source) {
        if (source != null) {
            for (Map.Entry<String, String> entry : source.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }


    /**
     * Constructs a new RequestParams instance and populate it with a single
     * initial key/value string param.
     *
     * @param key   the key name for the intial param.
     * @param value the value string for the initial param.
     */
    public RequestParams(final String key, final String value) {
        this(new HashMap<String, String>() {
            {
                put(key, value);
            }
        });
    }


    /**
     * Adds a key/value string pair to the request.
     * <p/>
     * 这个方法会自动过滤掉一些特殊字符，如果不要过滤特殊字符，调用{@link #putWithoutFilter(String, String)}
     *
     * @param key   the key name for the new param.
     * @param value the value string for the new param.
     */
    public void put(String key, String value) {
        if (key != null && value != null) {
            // 防止注入攻击过滤掉特殊字符
            value = value.replace("<", "").replace(">", "").replace("'", "")
                    .replace("/", "").replace("%", "");
            urlParams.put(key, value);
        }
    }


    public void put(String key, Object value) {
        if (value != null)
            urlParams.put(key, value.toString());
    }

    /**
     * Adds a key/value string pair to the request.
     * <p/>
     * 不过滤特殊字符
     *
     * @param key   the key name for the new param.
     * @param value the value string for the new param.
     */
    public void putWithoutFilter(String key, String value) {
        if (key != null && value != null) {
            urlParams.put(key, value);
        }
    }


    /**
     * Adds a int value to the request.
     *
     * @param key   the key name for the new param.
     * @param value the value int for the new param.
     */
    public void put(String key, int value) {
        if (key != null) {
            urlParams.put(key, String.valueOf(value));
        }
    }

    /**
     * Adds a long value to the request.
     *
     * @param key   the key name for the new param.
     * @param value the value long for the new param.
     */
    public void put(String key, long value) {
        if (key != null) {
            urlParams.put(key, String.valueOf(value));
        }
    }

    /**
     * Add a double value to the request
     *
     * @param key
     * @param value
     */
    public void put(String key, double value) {
        if (key != null) {
            urlParams.put(key, String.valueOf(value));
        }
    }

    /**
     * Removes a parameter from the request.
     *
     * @param key the key name for the parameter to remove.
     */
    public void remove(String key) {
        urlParams.remove(key);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (ConcurrentHashMap.Entry<String, String> entry : urlParams
                .entrySet()) {
            if (result.length() > 0)
                result.append("&");

            result.append(entry.getKey());
            result.append("=");
            result.append(entry.getValue());
        }

        return result.toString();
    }


    public ConcurrentHashMap<String, String> getUrlParams() {
        return urlParams;
    }

}


