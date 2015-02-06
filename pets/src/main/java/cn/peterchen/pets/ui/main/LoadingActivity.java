package cn.peterchen.pets.ui.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.peterchen.pets.R;
import cn.peterchen.pets.common.http.HttpUtil;
import cn.peterchen.pets.common.http.RequestParams;
import cn.peterchen.pets.common.http.URLConfig;
import cn.peterchen.pets.common.http.VolleyRequestListener;
import cn.peterchen.pets.common.http.VolleyRequestListenerImp;
import cn.peterchen.pets.entity.Result;
import cn.peterchen.pets.entity.ShopItem;
import cn.peterchen.pets.entity.User;
import cn.peterchen.pets.global.PetApplication;
import cn.peterchen.pets.global.UserManager;

/**
 * Created by peter on 15-2-6.
 */
public class LoadingActivity extends Activity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_activity);
        progressBar = (ProgressBar) findViewById(R.id.loading_progress);
        doRequest();
//        doTestRequest();
//        LoadingAsnycTask task = new LoadingAsnycTask();
//        task.execute();
    }

    private void doTestRequest() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLConfig.COMMON_URL + URLConfig.GET_SHOP_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.i("mInfo", "response succeed \n " + s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("mInfo", "response failed\n ");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("uid", "1");
                return map;
            }
        };

        PetApplication.getInstance().getmRequestQueue().add(stringRequest);
    }

    private void doRequest() {

        RequestParams params = new RequestParams();
        params.put("uid", 1);
        HttpUtil.jsonRequest(this, URLConfig.GET_SHOP_LIST, params, new TypeToken<Result<List<ShopItem>>>() {
        }, new VolleyRequestListenerImp<List<ShopItem>>() {
            @Override
            public void onSuccess(List<ShopItem> response) {
                Log.i("mInfo", "request succeed!");
                Log.i("mInfo", response.toString());

            }

            @Override
            public void onNetError(VolleyError error) {

            }
        });
    }

    private class LoadingAsnycTask extends AsyncTask<Void, Integer, Boolean> {

        private static final int TOTAL_STEPS = 1;
        private int steps = 0;
        private boolean hasFailed = false;

        /**
         * 进入程序的网络测试：
         * 1. 如果本地没有userId，获取userId
         * 2. 获取总接口
         * 3. 获取宠物数据
         *
         * @param params
         * @return
         */
        @Override
        protected Boolean doInBackground(Void... params) {

            getUserId();
            while (steps < TOTAL_STEPS) {
                //check the progress every 20ms, avoiding asking all the time
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            return hasFailed;
        }

        private void getUserId() {
            if (UserManager.getInstance().getUser() != null) {
                return;
            } else {
                HttpUtil.jsonRequest(LoadingActivity.this, URLConfig.CREATE_USER, null, new TypeToken<Result<User>>() {
                }, new VolleyRequestListenerImp<User>() {
                    @Override
                    public void onSuccess(User response) {
                        steps++;
                        publishProgress(steps / TOTAL_STEPS * 100);
                        Log.i("mInfo", "on Response Succeed");
                    }

                    @Override
                    public void onNetError(VolleyError error) {
                        Log.i("mInfo", "on Response Failed");
                        hasFailed = true;
                    }

                    @Override
                    public void onResponseError(String msg) {
                        Log.i("mInfo", msg);
                        hasFailed = true;
                    }
                });
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progress = values[0];
            progressBar.setProgress(progress);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean failed) {
            if (!failed) {
//                startActivity(new Intent(LoadingActivity.this, MainActivity.class));
//                LoadingActivity.this.finish();
            } else {
                AlertDialog dialog = new AlertDialog.Builder(LoadingActivity.this).setMessage("网络错误，请重试！").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoadingActivity.this.recreate();
                    }
                }).create();
                dialog.show();
            }
            super.onPostExecute(failed);
        }

    }
}
