package cn.peterchen.pets.ui.game;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is where the main animations stores.
 * It should not be collected by the dalvik.
 * <p/>
 * Anim 12: patrik stands still
 * Created by peter on 15-2-9.
 */
public class PatrickAnim {

    private static final int TOTAL_ANIMS = 18;
    private static final String DIRECTORY_PRE = "patrik/anim";

    private static PatrickAnim instance;
    private Map<Integer, String[]> animFiles;
    private Map<Integer, Bitmap[]> animBitmaps;

    private Activity context;
    private int currentAnim;
    private int currentFrame;

    private Rect srcRect;
    private Rect destRect;
    private Rect screenRect;

    private Paint paint;

    private long lastMs;


    public static PatrickAnim getInstance(Activity context) {
        if (instance == null) {
            synchronized (PatrickAnim.class) {
                instance = new PatrickAnim(context);
            }
        }
        return instance;
    }

    private PatrickAnim(Activity context) {
        this.context = context;
        initBitmaps();
        currentAnim = 12;
        initRects();
        paint = new Paint();
        paint.setAntiAlias(true);
        //already used the context, and unrelate this instance to the context,
        //so that the activity could be collected to avoid memory leak.
        this.context = null;

    }

    private void initRects() {
        Bitmap tmp = animBitmaps.get(1)[0];
        srcRect = new Rect(0, 0, tmp.getWidth(), tmp.getHeight());
        context.getWindowManager().getDefaultDisplay().getRectSize(screenRect);
        int drawWidth = tmp.getWidth() * 3;
        int drawHeight = tmp.getHeight() * 3;
        int drawStartX = (screenRect.width() + drawWidth) / 2;
        int drawStartY = (screenRect.height() + drawHeight) / 2;
        destRect = new Rect(drawStartX, drawStartY, drawStartX + drawWidth, drawStartY + drawHeight);
        currentFrame = 0;
    }

    private void initBitmaps() {
        AssetManager assetManager = context.getAssets();
        String[] tmpPaths;
        Bitmap[] tmpBits;
        InputStream is;
        animFiles = new HashMap<>();
        animBitmaps = new HashMap<>();
        for (int i = 1; i <= TOTAL_ANIMS; i++) {
            try {
                tmpPaths = assetManager.list(DIRECTORY_PRE + String.valueOf(i));
//                tmpPaths = assetManager.list("");
                tmpBits = new Bitmap[tmpPaths.length];
                for (int j = 0; j < tmpPaths.length; j++) {
                    is = assetManager.open(DIRECTORY_PRE + String.valueOf(i) + "/" + tmpPaths[j]);
                    tmpBits[j] = BitmapFactory.decodeStream(is);
                }
                animFiles.put(i, tmpPaths);
                animBitmaps.put(i, tmpBits);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(animBitmaps.get(currentAnim)[currentFrame], srcRect, destRect, paint);
        if (System.currentTimeMillis() - lastMs > 50) {
            lastMs = System.currentTimeMillis();
            currentFrame++;
            if (currentFrame == animBitmaps.get(currentAnim).length) {
                currentFrame = 0;
            }
        }
    }

}
