package cn.peterchen.pets.ui.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by peter on 15-1-29.
 */
public class GameBackground {

    private float[] bgSize;
    private float[] landSize;
    private Paint paint;
    private RectF bgDest;
    private RectF landDest;

    //left, top, width, height
    float[] background = new float[]{0.0f, 0.0f, 0.28125f, 0.5f,};
    float[] land = new float[]{0.5703125f, 0.0f, 0.328125f, 0.109375f};

    private Rect bgRect;
    private Rect landRect;
    private Rect screenRect;

    private boolean isMoving;
    private float startX;

    private float ratio;

    private Bitmap backgroundBitmap;

    public GameBackground(Bitmap atlas, Rect screenRect) {
        this.screenRect = screenRect;

        paint = new Paint();
        paint.setAntiAlias(true);

        bgSize = new float[]{atlas.getWidth() * background[2], atlas.getHeight() * background[3]};
        landSize = new float[]{atlas.getWidth() * land[2], atlas.getHeight() * land[3]};
        bgRect = new Rect((int) (atlas.getWidth() * background[0]), (int) (atlas.getHeight() * background[1]), (int) (atlas.getWidth() * background[0] + bgSize[0]), (int) (atlas.getHeight() * background[1] + bgSize[1]));
        landRect = new Rect((int) (atlas.getWidth() * land[0]), (int) (atlas.getHeight() * land[1]), (int) (atlas.getWidth() * land[0] + landSize[0]), (int) (atlas.getHeight() * land[1] + landSize[1]));

        ratio = bgSize[0] / screenRect.width();

        bgDest = new RectF(0, 0, bgRect.width() + 1, bgRect.height());
        backgroundBitmap = Bitmap.createBitmap((int) bgSize[0] * 2, (int) bgSize[1], Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(backgroundBitmap);
        canvas.drawBitmap(atlas, bgRect, bgDest, paint);
        bgDest = new RectF(bgRect.width() - 1, 0, bgRect.width() * 2, bgRect.height());
        canvas.drawBitmap(atlas, bgRect, bgDest, paint);

        landDest = new RectF(0, bgSize[1] - landSize[1], bgRect.width() + 1, bgSize[1]);
        canvas.drawBitmap(atlas, landRect, landDest, paint);
        landDest = new RectF(bgRect.width() - 1, bgSize[1] - landSize[1], bgRect.width() * 2, bgSize[1]);
        canvas.drawBitmap(atlas, landRect, landDest, paint);

    }

    public void draw(Canvas canvas) {
        if (isMoving) {
            startX += 5 * ratio;
            if (startX > bgSize[0]) {
                startX = startX - bgSize[0];
            }
        } else {
            startX = 0;
        }

        bgRect.left = (int) startX;
        bgRect.right = (int) (startX + bgSize[0]);
        canvas.drawBitmap(backgroundBitmap, bgRect, screenRect, paint);

    }

    public void setMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }
}
