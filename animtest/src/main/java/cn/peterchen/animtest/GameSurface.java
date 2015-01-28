package cn.peterchen.animtest;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class GameSurface extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private GameThread myThread;
    private Context context;

    public GameSurface(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public GameSurface(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    public GameSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = ((Activity) context).getWindowManager().getDefaultDisplay()
                .getWidth();
        int height = width;
        this.setMeasuredDimension(width, height);
    }

    private void init() {
        holder = this.getHolder();
        holder.addCallback(this);
        myThread = new GameThread(context, this, holder);// ����һ����ͼ�߳�
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("mInfo", "surface created");

        myThread.isRun = true;
        myThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("mInfo", "surface destoryed");
        myThread.isRun = false;
    }

    public boolean startFling(MotionEvent event1, MotionEvent event2,
                              float velocityX, float velocityY) {
        Log.d("mInfo",
                "onSurfaceFling: " + event1.toString() + event2.toString());
        if (myThread.isAnimating())
            return true;
        if (velocityX > 0 && velocityX >= Math.abs(velocityY)) {
            myThread.setDirection(GameThread.DIRECTION_RIGHT);
            myThread.setAnimating(true);
            myThread.onKeyRight();
            // myThread.generateNewNum();
        } else if (velocityX < 0 && Math.abs(velocityX) >= Math.abs(velocityY)) {
            myThread.setDirection(GameThread.DIRECTION_LEFT);
            myThread.setAnimating(true);
            myThread.onKeyLeft();
            // myThread.generateNewNum();
        } else if (velocityY > 0 && velocityY > Math.abs(velocityX)) {
            myThread.setDirection(GameThread.DIRECTION_DOWN);
            myThread.setAnimating(true);
            myThread.onKeyDown();
            // myThread.getMainLogic().generateNewNum();
        } else if (velocityY < 0 && Math.abs(velocityY) > Math.abs(velocityX)) {
            myThread.setDirection(GameThread.DIRECTION_UP);
            myThread.setAnimating(true);
            myThread.onKeyUp();
            // myThread.getMainLogic().generateNewNum();
        }
        return true;
    }

    public void restart() {
        myThread.setMatrixInit(false);
    }

    public GameThread getMyThread() {
        return myThread;
    }

    public void setMyThread(GameThread myThread) {
        this.myThread = myThread;
    }

}
