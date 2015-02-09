package cn.peterchen.pets.ui.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import cn.peterchen.pets.R;
import cn.peterchen.pets.common.http.HttpUtil;
import cn.peterchen.pets.common.http.RequestParams;
import cn.peterchen.pets.common.http.URLConfig;
import cn.peterchen.pets.common.http.VolleyRequestListenerImp;
import cn.peterchen.pets.entity.Career;
import cn.peterchen.pets.entity.Course;
import cn.peterchen.pets.entity.Pet;
import cn.peterchen.pets.entity.Result;
import cn.peterchen.pets.entity.ShopItem;
import cn.peterchen.pets.entity.User;
import cn.peterchen.pets.global.UserManager;
import cn.peterchen.pets.ui.game.PatrickAnim;

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
        LoadingAsnycTask task = new LoadingAsnycTask();
        task.execute();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private class LoadingAsnycTask extends AsyncTask<Void, Integer, Boolean> {

        private static final int TOTAL_STEPS = 5;
        private int steps = 0;
        private boolean hasFailed = false;
        private Long uid;

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

            //Loading the animation bitmaps into memory
            PatrickAnim.getInstance(LoadingActivity.this);
            steps++;
            publishProgress(steps / TOTAL_STEPS * 100);
            getUserId();
            while (steps < TOTAL_STEPS && !hasFailed) {
                //check the progress every 20ms, avoiding asking all the time
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            return hasFailed;
        }

        private void getCourses() {
            RequestParams params = new RequestParams();
            params.put("uid", uid);
            HttpUtil.jsonRequest(LoadingActivity.this, URLConfig.GET_COURSE_LIST, params, new TypeToken<Result<List<Course>>>() {
            }, new VolleyRequestListenerImp<List<Course>>() {
                @Override
                public void onSuccess(List<Course> response) {
                    steps++;
                    publishProgress(steps / TOTAL_STEPS * 100);

                }

                @Override
                public void onNetError(VolleyError error) {
                    hasFailed = true;
                    Log.i("mInfo", "Failed to get Courses");
                }

                @Override
                public void onResponseError(String msg) {
                    hasFailed = true;
                    Log.i("mInfo", "Failed to get Courses" + "The Error is :" + msg);
                }
            });
        }

        private void getCareer() {
            RequestParams params = new RequestParams();
            params.put("uid", uid);

            HttpUtil.jsonRequest(LoadingActivity.this, URLConfig.GET_CAREER_LIST, params, new TypeToken<Result<List<Career>>>() {
            }, new VolleyRequestListenerImp<List<Career>>() {
                @Override
                public void onSuccess(List<Career> response) {
                    steps++;
                    publishProgress(steps / TOTAL_STEPS * 100);
                }

                @Override
                public void onNetError(VolleyError error) {
                    hasFailed = true;
                    Log.i("mInfo", "Failed to get Career");

                }

                @Override
                public void onResponseError(String msg) {
                    hasFailed = true;
                    Log.i("mInfo", "Failed to get Career" + "The Error is :" + msg);

                }
            });
        }


        private void getShopItem() {
            RequestParams params = new RequestParams();
            params.put("uid", uid);

            HttpUtil.jsonRequest(LoadingActivity.this, URLConfig.GET_SHOP_LIST, params, new TypeToken<Result<List<ShopItem>>>() {
            }, new VolleyRequestListenerImp<List<ShopItem>>() {
                @Override
                public void onSuccess(List<ShopItem> response) {
                    steps++;
                    publishProgress(steps / TOTAL_STEPS * 100);

                }

                @Override
                public void onNetError(VolleyError error) {
                    hasFailed = true;
                    Log.i("mInfo", "Failed to get ShopItem");

                }

                @Override
                public void onResponseError(String msg) {
                    hasFailed = true;
                    Log.i("mInfo", "Failed to get ShopItem" + "The Error is :" + msg);

                }
            });
        }

        private void getUserId() {
            if (UserManager.getInstance().getUser() != null) {
                uid = UserManager.getInstance().getUser().getId();
                steps++;
                publishProgress(steps / TOTAL_STEPS * 100);
                getShopItem();
                getCareer();
                getCourses();
                return;
            } else {
                HttpUtil.jsonRequest(LoadingActivity.this, URLConfig.CREATE_USER, null, new TypeToken<Result<User>>() {
                }, new VolleyRequestListenerImp<User>() {
                    @Override
                    public void onSuccess(User response) {
                        //A new user has come, and create a new master and a pet for him/her
                        steps++;
                        publishProgress(steps / TOTAL_STEPS * 100);
                        uid = response.getId();
                        UserManager.getInstance().setUser(response);
                        createNewMasterAndPet();
                        getShopItem();
                        getCareer();
                        getCourses();
                    }

                    @Override
                    public void onNetError(VolleyError error) {
                        Log.i("mInfo", "Failed to get User");
                        hasFailed = true;
                    }

                    @Override
                    public void onResponseError(String msg) {
                        Log.i("mInfo", "Failed to get User" + "The Error is :" + msg);
                        hasFailed = true;
                    }
                });
            }
        }

        private void createNewMasterAndPet() {
            final User user = UserManager.getInstance().getUser();
            //This method will need a callback
            RequestParams params = new RequestParams();
            params.put("uid", user.getId());
            HttpUtil.jsonRequest(LoadingActivity.this, URLConfig.GET_NEW_PET, params, new TypeToken<Result<Pet>>() {
            }, new VolleyRequestListenerImp<Pet>() {
                @Override
                public void onSuccess(Pet response) {
                    steps++;
                    publishProgress(steps / TOTAL_STEPS * 100);
                    user.setPet(response);
                }

                @Override
                public void onNetError(VolleyError error) {
                    super.onNetError(error);
                    hasFailed = true;
                }

                @Override
                public void onResponseError(String msg) {
                    super.onResponseError(msg);
                    hasFailed = true;
                }
            });
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
                startActivity(new Intent(LoadingActivity.this, MainActivity.class));
                LoadingActivity.this.finish();
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
