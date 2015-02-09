package cn.peterchen.pets.ui.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by peter on 15-1-29.
 */

public class FlySheep {

    private GameController gameController;
    private Bitmap atlas;
    private float[] frameSize;
    private int frameCount;
    private int currentFrame;
    private Paint paint;
    private RectF dest;

    private long lastms;

    //left, top, width, height
    float[] bird0 = new float[]{0.0f, 0.9472656f, 0.046875f, 0.046875f};
    float[] bird1 = new float[]{0.0546875f, 0.9472656f, 0.046875f, 0.046875f};
    float[] bird2 = new float[]{0.109375f, 0.9472656f, 0.046875f, 0.046875f};

    private Rect bird0Rect;
    private Rect bird1Rect;
    private Rect bird2Rect;

    private Rect birdSrc;

    private boolean isJumping;
    private int jumpHeight;
    private long jumpStartMS;
    private static final float START_SPEED = 10.0f;
    private static final float G = 9.8f;


    public FlySheep(Bitmap atlas) {
        this.atlas = atlas;

        frameSize = new float[]{atlas.getWidth() * bird0[2], atlas.getHeight() * bird0[3]};
        bird0Rect = new Rect((int) (atlas.getWidth() * bird0[0]), (int) (atlas.getHeight() * bird0[1]), (int) (atlas.getWidth() * bird0[0] + frameSize[0]), (int) (atlas.getHeight() * bird0[1] + frameSize[1]));
        bird1Rect = new Rect((int) (atlas.getWidth() * bird1[0]), (int) (atlas.getHeight() * bird1[1]), (int) (atlas.getWidth() * bird1[0] + frameSize[0]), (int) (atlas.getHeight() * bird1[1] + frameSize[1]));
        bird2Rect = new Rect((int) (atlas.getWidth() * bird2[0]), (int) (atlas.getHeight() * bird2[1]), (int) (atlas.getWidth() * bird2[0] + frameSize[0]), (int) (atlas.getHeight() * bird2[1] + frameSize[1]));

        frameCount = 3;
        paint = new Paint();
        paint.setAntiAlias(true);
        currentFrame = 0;
        dest = new RectF(400, 600, 400 + frameSize[0], 600 + frameSize[1]);
        gameController = GameController.getInstance();
    }

    public void draw(Canvas canvas) {

        switch (currentFrame) {
            case 0:
                birdSrc = bird0Rect;
                break;
            case 1:
                birdSrc = bird1Rect;
                break;
            case 2:
                birdSrc = bird2Rect;
                break;
            default:
                birdSrc = bird0Rect;
                break;
        }

        if (gameController.getGameStatus() == GameController.COMMAND_PRESSED) {
            //用户点击了跳跃
            jumpStartMS = System.currentTimeMillis();
            isJumping = true;
//            gameController.setCommand(GameController.COMMAND_JUMPING);
        }
        if (isJumping) {
            long ms = System.currentTimeMillis() - jumpStartMS;
            jumpHeight = (int) (-0.5f * G * ms * ms + START_SPEED * ms);
            if (jumpHeight <= 0) {
                isJumping = false;
//                gameController.setCommand(GameController.COMMAND_NORMAL);
            }
            jumpHeight = 0;
        }
        dest.top += jumpHeight;
        dest.bottom += jumpHeight;

        canvas.drawBitmap(atlas, birdSrc, dest, paint);
        if (System.currentTimeMillis() - lastms > 50) {
            lastms = System.currentTimeMillis();
            currentFrame++;
            if (currentFrame == frameCount) {
                currentFrame = 0;
            }

        }
    }

}
