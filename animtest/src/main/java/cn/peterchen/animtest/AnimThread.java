package cn.peterchen.animtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peter on 15-1-28.
 */
public class AnimThread extends Thread {

    private AnimSurface animSurface;
    private final SurfaceHolder surfaceHolder;
    private Context context;

    private boolean isRun;

    private List<Bird> birds;

    public AnimThread(Context context, AnimSurface surface, SurfaceHolder holder) {
        this.animSurface = surface;
        this.context = context;
        this.surfaceHolder = holder;
        init();
    }

    private void init() {
        isRun = true;
        birds = new ArrayList<Bird>();

        for (int i = 0; i <= 100; i++) {
            birds.add(new Bird(context));
        }
    }

    private void draw(Canvas canvas) {
        Log.i("mInfo", "canvas drawing!");
        for (int i = 0; i <= 10; i++) {
            birds.get(i).draw(canvas, i);
        }

    }

    @Override
    public void run() {
        while (isRun) {
            Canvas c = null;
            try {
                synchronized (surfaceHolder) {
                    c = surfaceHolder.lockCanvas();
                    draw(c);
//                    Thread.sleep(100);
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            } finally {
                if (c != null) {
                    surfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }

    public boolean isRun() {
        return isRun;
    }

    public void setRun(boolean isRun) {
        this.isRun = isRun;
    }
}
