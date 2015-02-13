package cn.peterchen.pets.ui.game;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;

import cn.peterchen.pets.R;

/**
 * This is the main thread of game presentation
 * Created by peter on 15-1-29.
 */
public class GameThread extends Thread implements GameController.GameControlObserver {

    private Context context;
    private final SurfaceHolder holder;
    private GameSurface gameSurface;

    private boolean isRun;

    private Paint imagePaint;

    private Bitmap atlas;

    private Rect screenRect;
    private Bitmap background;
    private Rect backgroundRect;

    //    private Bitmap figure;
    private Rect figureSrc;
    private Rect figureDest;

    private Bitmap skybg;
    private int skyStart;
    private Rect skySrc;

    private GameController gameController;

//    private FlySheep flySheep;

    private PatrickAnim patrickAnim;

    private ObstacleAnim obstacleAnim;

    private GameBackground gameBackground;

    public GameThread(Context context, SurfaceHolder holder, GameSurface gameSurface) {
        this.context = context;
        this.holder = holder;
        this.gameSurface = gameSurface;

        init();
    }

    private void init() {
        GameController.getInstance().attach(this);
        patrickAnim = PatrickAnim.getInstance((Activity) context);
        imagePaint = new Paint();
        imagePaint.setAntiAlias(true);
        screenRect = new Rect();
        ((Activity) context).getWindowManager().getDefaultDisplay().getRectSize(screenRect);

        atlas = BitmapFactory.decodeResource(context.getResources(), R.drawable.atlas);
        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
//        figure = BitmapFactory.decodeResource(context.getResources(), R.drawable.pets);
        skybg = BitmapFactory.decodeResource(context.getResources(), R.drawable.skybg);
        skyStart = 0;
        this.isRun = false;
        gameController = GameController.getInstance();
//        flySheep = new FlySheep(atlas);
        obstacleAnim = new ObstacleAnim((Activity) context, atlas);
        gameBackground = new GameBackground(atlas, screenRect);
        backgroundRect = new Rect(0, 0, background.getWidth(), background.getHeight());
//        figureSrc = new Rect(0, 0, figure.getWidth(), figure.getHeight());
//        int figureStartX = (screenRect.width() - figure.getWidth()) / 2;
//        int figureStartY = (screenRect.height() - figure.getHeight()) / 2;
//        figureDest = new Rect(figureStartX, figureStartY, figureStartX + figure.getWidth(), figureStartY + figure.getHeight());
        skySrc = new Rect();

    }


    private void draw(Canvas canvas) {

        if (gameController.getGameStatus() == GameController.STATUS_MINI_GAME) {
            gameBackground.setMoving(true);
            gameBackground.draw(canvas);

            patrickAnim.draw(canvas);
            obstacleAnim.draw(canvas);

            //Running status
        } else {
            gameBackground.setMoving(false);
            gameBackground.draw(canvas);

            patrickAnim.draw(canvas);
            //Normal Status
        }

//        canvas.saveLayerAlpha(screenRect.left, screenRect.top, screenRect.right, screenRect.bottom, 0xFF, 10);
//        canvas.restore();
    }


    /**
     * Game Main Loop
     */
    @Override
    public void run() {
        while (isRun) {
            Canvas c = null;
            try {
                synchronized (holder) {
                    c = holder.lockCanvas();
                    draw(c);
                    Thread.sleep(10);
                }
            } catch (Exception e) {
                Log.i("mInfo", "Thread error!");
                e.printStackTrace();
            } finally {
                if (c != null) {
                    holder.unlockCanvasAndPost(c);
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

    @Override
    public void update(GameCommand command) {
        switch (command) {
            case START_MINI_GAME:
                obstacleAnim.restart();
                patrickAnim.changeAnim(18);
                break;
            case JUMP:
                patrickAnim.changeAnim(19);
                break;
            case STOP_MINI_GAME:
                patrickAnim.changeAnim(PatrickAnim.STATE_STILL);
                break;
            case PLAY:
                patrickAnim.changeAnim(PatrickAnim.ANIM_RANDOM);
                break;
            case MASTER_EAT:
                //eating requires the following commands:
                //change animation to eat
                //start timer
                //disable eating
                //when finish eating
                break;
            default:
                break;
        }
    }
}
