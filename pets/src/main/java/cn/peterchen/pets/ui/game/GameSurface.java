package cn.peterchen.pets.ui.game;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by peter on 15-1-29.
 */
public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread gameThread;
    private Context context;
    private SurfaceHolder holder;

    public GameSurface(Context context) {
        super(context);
        this.context = context;
        init();
    }


    public GameSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public GameSurface(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    private void init() {
        holder = this.getHolder();
        holder.addCallback(this);
        gameThread = new GameThread(context, holder, this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("mInfo", "surface created");
        if (gameThread == null) {
            gameThread = new GameThread(context, holder, this);
        }
        gameThread.setRun(true);
        gameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        gameThread.setRun(false);
        gameThread = null;
    }
}
