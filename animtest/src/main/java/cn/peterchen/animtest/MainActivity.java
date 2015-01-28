package cn.peterchen.animtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity implements OnClickListener {

    private static final String SP_NAME = "shared_preference_name";

    private GestureDetectorCompat mDetector;
    private GameSurface surface;

    private Button restartBtn;
    private Button shareBtn;
    private Button quitBtn;
    private Button hintBtn;

    private TextView scoreText;
    private TextView maxNumText;
    private TextView bestScoreText;
    private TextView bestMaxNumText;

    private int score;
    private int maxNum;

    private SharedPreferences sp;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    scoreText.setText("" + msg.arg1);
                    score = msg.arg1;
                    maxNumText.setText("" + msg.arg2);
                    maxNum = msg.arg2;
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_activity);
        surface = (GameSurface) findViewById(R.id.surfaceview);
        mDetector = new GestureDetectorCompat(this, new MyGestureListener());
        restartBtn = (Button) findViewById(R.id.restart);
        restartBtn.setOnClickListener(this);
        shareBtn = (Button) findViewById(R.id.share);
        shareBtn.setOnClickListener(this);
        hintBtn = (Button) findViewById(R.id.hint);
        hintBtn.setOnClickListener(this);
        quitBtn = (Button) findViewById(R.id.quit);
        quitBtn.setOnClickListener(this);
        scoreText = (TextView) findViewById(R.id.score);
        maxNumText = (TextView) findViewById(R.id.max_num);

        bestScoreText = (TextView) findViewById(R.id.best_score);
        bestMaxNumText = (TextView) findViewById(R.id.best_max);

        surface.getMyThread().setmListener(new GameThread.ScoreListener() {

            @Override
            public void getScores(int score, int maxNum) {
                Message msg = new Message();
                msg.arg1 = score;
                msg.arg2 = maxNum;
                msg.what = 0;
                handler.sendMessage(msg);
            }

            @Override
            public void gameOver() {
                popupDialog("提示", "游戏结束");
            }

            @Override
            public void gameWin() {
                popupDialog("恭喜！", "你成为本游戏第一个达到2048的人！");
            }
        });

        sp = this.getSharedPreferences(SP_NAME, 0);
        bestScoreText.setText("最高分" + sp.getInt("bestScore", 0));
        bestMaxNumText.setText("最高值" + sp.getInt("bestMax", 0));
        if (sp.getBoolean("isFirstBoot", true)) {
            popupDialog("操作方法", "我原本想提醒你的，算了，自己摸索摸索吧");
            sp.edit().putBoolean("isFirstBoot", false).commit();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onDown(MotionEvent event) {
            Log.d(DEBUG_TAG, "MainonDown: " + event.toString());
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            Log.d(DEBUG_TAG,
                    "MainonFling: " + event1.toString() + event2.toString());
            return surface.startFling(event1, event2, velocityX, velocityY);
        }
    }

    public void popupDialog(String title, String message) {
        AlertDialog.Builder builder = new Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle(title);
        builder.setIcon(R.drawable.ic_launcher_small);
        builder.setMessage(message);
        builder.setPositiveButton("确定", null);
        builder.create().show();

    }

    private void writeToSP() {
        if (score > sp.getInt("bestScore", 0))
            sp.edit().putInt("bestScore", score).apply();
        if (maxNum > sp.getInt("bestMax", 0))
            sp.edit().putInt("bestMax", maxNum).apply();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share:
                Intent intent = new Intent(this, AnimTestActivity.class);
                startActivity(intent);
                break;
            case R.id.quit:
                writeToSP();
                finish();
                break;
            case R.id.restart:
                writeToSP();
                surface.restart();
                bestScoreText.setText("最高分" + sp.getInt("bestScore", 0));
                bestMaxNumText.setText("最高值" + sp.getInt("bestMax", 0));
                break;
            case R.id.hint:
                popupDialog("操作方法", "滑动手指试试？");
                break;
        }
    }

}
