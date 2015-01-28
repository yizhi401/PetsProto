package cn.peterchen.animtest;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by peter on 15-1-28.
 */
public class AnimSurface extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder surfaceHolder;
    private Context context;
    private AnimThread animThread;

    public AnimSurface(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public AnimSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public AnimSurface(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }


    private void init() {
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
        animThread = new AnimThread(context, this, surfaceHolder);
        Log.i("mInfo", "surface inited!");
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("mInfo", "surface Created!");
        animThread.setRun(true);
        animThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("mInfo", "surface destoryed!");
        animThread.setRun(false);
    }
}
