package cn.peterchen.pets.ui.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by peter on 15-1-29.
 */
public class FlyBackground {

    private Bitmap atlas;
    private float[] frameSize;
    private int frameCount;
    private int currentFrame;
    private Paint paint;
    private RectF dest;

    private long lastms;

    //left, top, width, height
    float[] bgNight = new float[]{0.28515625f, 0.0f, 0.28125f, 0.5f};
    float[] ground = new float[]{0.5703125f, 0.0f, 0.328125f, 0.109375f};

    private Rect bgRect;
    private Rect groundRect;

    public FlyBackground(Bitmap atlas) {
        this.atlas = atlas;

        frameSize = new float[]{atlas.getWidth() * bgNight[2], atlas.getHeight() * bgNight[3]};
        bgRect = new Rect((int) (atlas.getWidth() * bgNight[0]), (int) (atlas.getHeight() * bgNight[1]), (int) (atlas.getWidth() * bgNight[0] + frameSize[0]), (int) (atlas.getHeight() * bgNight[1] + frameSize[1]));

        frameCount = 3;
        paint = new Paint();
        paint.setAntiAlias(true);
        currentFrame = 0;
        dest = new RectF(200, 200, 200 + frameSize[0], 200 + frameSize[1]);
    }

    public void draw(Canvas canvas) {

        canvas.drawBitmap(atlas, bgRect, dest, paint);
        if (System.currentTimeMillis() - lastms > 20) {
            lastms = System.currentTimeMillis();
            currentFrame++;
            if (currentFrame == frameCount) {
                currentFrame = 0;
            }
        }
    }

}
