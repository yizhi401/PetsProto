package cn.peterchen.animtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by peter on 15-1-28.
 */
public class Bird {

    private Bitmap bird;
    private int[] frameSize;
    private int frameCount;
    private int currentFrame;
    private Context contex;
    private Paint paint;

    public Bird(Context context) {
        bird = BitmapFactory.decodeResource(context.getResources(), R.drawable.animtest);
        frameSize = new int[]{141, 85};
        frameCount = 8;
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    public void draw(Canvas canvas, int num) {
        num = 10 * num;
        int left;
        int top;
        if (currentFrame <= 3) {
            top = 0;
            left = frameSize[0] * currentFrame;
        } else {
            top = frameSize[1];
            left = frameSize[0] * (currentFrame - 4);
        }
        Rect src = new Rect(top, left, top + frameSize[1], left + frameSize[0]);
        canvas.drawBitmap(bird, src, new Rect(num, num, num + frameSize[0], num + frameSize[1]), paint);
        currentFrame++;
        if (currentFrame == frameCount) {
            currentFrame = 0;
        }
    }

}
