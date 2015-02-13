package cn.peterchen.pets.ui.game;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by peter on 15-2-11.
 */
public class ObstacleAnim {

    //  pipe2_down 52 320 0.0 0.6308594 0.05078125 0.3125
    //  pipe2_up 52 320 0.0546875 0.6308594 0.05078125 0.3125
    //  pipe_down 52 320 0.109375 0.6308594 0.05078125 0.3125
    //  pipe_up 52 320 0.1640625 0.6308594 0.05078125 0.3125
    private float[] pipePos = new float[]{0.1640625f, 0.6308594f, 0.05078125f, 0.3125f};
    private float[] frameSize;
    private Rect pipeRect;
    private Bitmap atlas;

    private Paint paint;

    private int bottom;
    private Activity context;
    private Rect screenRect;
    private List<Pipe> pipes;
    private PatrickAnim patrick;


    public ObstacleAnim(Activity context, Bitmap atlas) {
        patrick = PatrickAnim.getInstance(context);
        this.atlas = atlas;
        frameSize = new float[]{atlas.getWidth() * pipePos[2], atlas.getHeight() * pipePos[3]};
        pipeRect = new Rect((int) (atlas.getWidth() * pipePos[0]), (int) (atlas.getHeight() * pipePos[1]), (int) (atlas.getWidth() * pipePos[0] + frameSize[0]), (int) (atlas.getHeight() * pipePos[1] + frameSize[1]));
        paint = new Paint();
        paint.setAntiAlias(true);
        screenRect = new Rect();
        context.getWindowManager().getDefaultDisplay().getRectSize(screenRect);
        bottom = screenRect.height() - 275;
        pipes = new LinkedList<>();
        generateRandomPipe();
    }

    private void generateRandomPipe() {
        Pipe tmp = new Pipe();
        int height = (int) (Math.random() * frameSize[1]);
        if (height < 30) {
            height = 30;
        }
        tmp.srcRect = new Rect(pipeRect.left, pipeRect.top, pipeRect.right, pipeRect.top + height);
        tmp.destRect = new RectF(screenRect.width(), bottom - height, screenRect.width() + frameSize[0], bottom);
        pipes.add(tmp);
    }

    public void draw(Canvas canvas) {
        Iterator<Pipe> iterator = pipes.iterator();
        while (iterator.hasNext()) {
            Pipe tmp = iterator.next();
            tmp.destRect.left -= 5;
            tmp.destRect.right -= 5;
            //TODO Unhandled concurrent map error
            if (tmp.destRect.right < 0) {
                iterator.remove();
                generateRandomPipe();
            } else {
                canvas.drawBitmap(atlas, tmp.srcRect, tmp.destRect, paint);
            }
        }
        if (checkCollision()) {
            GameController.getInstance().setCommand(GameCommand.STOP_MINI_GAME);
        }
    }

    /**
     * @return isCollided
     */
    private boolean checkCollision() {
        Rect rectA = patrick.getCurrentPosition();
        int middleX = (rectA.left + rectA.right) / 2;
        int middleY = (rectA.top + rectA.bottom) / 2;
        rectA = new Rect(middleX - 60, middleY - 60, middleX + 60, middleY + 60);
        Iterator<Pipe> iterator = pipes.iterator();
        while (iterator.hasNext()) {
            Pipe tmp = iterator.next();
            RectF rectB = new RectF(tmp.destRect.left + 10, tmp.destRect.top + 10, tmp.destRect.right - 10, tmp.destRect.bottom - 10);
            if (isCollided(rectA, rectB)) {
                return true;
            }
        }
        return false;
    }

    private boolean isCollided(Rect rect1, RectF rect2) {
        if (rect1.left < rect2.right &&
                rect1.right > rect2.left &&
                rect1.top < rect2.bottom &&
                rect1.bottom > rect2.top) {
            return true;
        }
        return false;
    }

    public void restart() {
        pipes.clear();
        generateRandomPipe();
    }


    private class Pipe {
        private Rect srcRect;
        private RectF destRect;
    }
}
