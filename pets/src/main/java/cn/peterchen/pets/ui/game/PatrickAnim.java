package cn.peterchen.pets.ui.game;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * This class is where the main animations stores.
 * It should not be collected by the dalvik.
 * <p/>
 * Anim 12: patrik stands still
 * Anim 18: patrik swiming
 * Anim 19: patrik jumping
 * Created by peter on 15-2-9.
 */
public class PatrickAnim {

    private static final int SCALE = 5;
    public static final int STATE_STILL = -1;
    public static final int STATE_NORMAL = 12;//this is the normal anim that patrik will show
    private static final int TOTAL_ANIMS = 19;

    public static final int ANIM_RANDOM = TOTAL_ANIMS + 1;
    private static final String DIRECTORY_PRE = "patrik/anim";

    private static final double GRAVITY = 9.8f; //9.8m2/s
    private static final double INITIAL_JUMPING_SPEED = -28; //20m/s

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

    private long nextAnimTime;
    private boolean isAnimating;
//    private int nextAnimType;

    private int jumpOffset;
    private boolean isJumping = false;
    private long jumpStartTime;

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
        currentAnim = STATE_STILL;
        initRects();
        paint = new Paint();
        paint.setAntiAlias(true);
        //already used the context, and unrelate this instance to the context,
        //so that the activity could be collected to avoid memory leak.
        this.context = null;
        isAnimating = false;
    }

    private void initRects() {
        screenRect = new Rect();
        srcRect = new Rect();
        context.getWindowManager().getDefaultDisplay().getRectSize(screenRect);
        destRect = new Rect();
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
        if (currentAnim == STATE_STILL) {
            if (!isAnimating) {
                calculateRect(animBitmaps.get(STATE_NORMAL)[0], SCALE);
                canvas.drawBitmap(animBitmaps.get(STATE_NORMAL)[0], srcRect, destRect, paint);
                if (nextAnimTime == 0) {
                    nextAnimTime = System.currentTimeMillis() + (long) ((Math.random() * 5) * 1000);
                }
                if (System.currentTimeMillis() >= nextAnimTime) {
                    //draw anim
                    isAnimating = true;
                    currentFrame = 0;
                }
            } else {
                if (currentFrame == animBitmaps.get(STATE_NORMAL).length) {
                    currentFrame = 0;
                    isAnimating = false;
                    nextAnimTime = 0;
                    calculateRect(animBitmaps.get(STATE_NORMAL)[currentFrame], SCALE);
                    canvas.drawBitmap(animBitmaps.get(STATE_NORMAL)[currentFrame], srcRect, destRect, paint);
                } else {
                    calculateRect(animBitmaps.get(STATE_NORMAL)[currentFrame], SCALE);
                    canvas.drawBitmap(animBitmaps.get(STATE_NORMAL)[currentFrame], srcRect, destRect, paint);
                    nextFrame();
                }
            }
        } else if (currentAnim == 18) {
            drawRuning(canvas);
        } else if (currentAnim == 19) {
            drawJumping(canvas);
        } else {
            calculateRect(animBitmaps.get(currentAnim)[currentFrame], SCALE);
            canvas.drawBitmap(animBitmaps.get(currentAnim)[currentFrame], srcRect, destRect, paint);
            nextFrame();
        }
    }

    private void drawJumping(Canvas canvas) {
        if (!isJumping) {
            jumpStartTime = System.currentTimeMillis();
            isJumping = true;
        }
        double t = ((double) (System.currentTimeMillis() - jumpStartTime)) / (double) 200;
//        Log.i("mInfo", String.valueOf(t));
        jumpOffset = (int) ((INITIAL_JUMPING_SPEED * t * 10 + 0.5 * GRAVITY * t * t * 10));
        if (jumpOffset > 0) {
            isJumping = false;
            currentAnim = 18;
            currentFrame = 0;
        }
        //anim 18 direction was wrong, so mirror it
        canvas.save();
        canvas.scale(-1f, 1f, screenRect.width() / 2, screenRect.height() / 2);
        calculateRect(animBitmaps.get(currentAnim)[currentFrame], 2);
        destRect.top += jumpOffset;
        destRect.bottom += jumpOffset;
        canvas.drawBitmap(animBitmaps.get(currentAnim)[currentFrame], srcRect, destRect, paint);
        canvas.restore();
        nextFrame();
//        if (currentFrame == animBitmaps.get(currentAnim).length - 1) {
//            currentAnim = 18;
//        }
    }

    private void drawRuning(Canvas canvas) {
        //anim 18 direction was wrong, so mirror it
        canvas.save();
        canvas.scale(-1f, 1f, screenRect.width() / 2, screenRect.height() / 2);
        calculateRect(animBitmaps.get(currentAnim)[currentFrame], 2);
        canvas.drawBitmap(animBitmaps.get(currentAnim)[currentFrame], srcRect, destRect, paint);
        canvas.restore();
        nextFrame();
    }

    private void calculateRect(Bitmap tmp, int scale) {
        srcRect.left = 0;
        srcRect.top = 0;
        srcRect.right = tmp.getWidth();
        srcRect.bottom = tmp.getHeight();
        int drawWidth = tmp.getWidth() * scale;
        int drawHeight = tmp.getHeight() * scale;
        int drawStartX = (screenRect.width() - drawWidth) / 2;
        int drawStartY = (screenRect.height() - drawHeight) / 2 + 180;
        destRect.left = drawStartX;
        destRect.top = drawStartY;
        destRect.right = drawStartX + drawWidth;
        destRect.bottom = drawStartY + drawHeight;
    }

    private void nextFrame() {
        if (currentAnim != STATE_STILL) {
            if (System.currentTimeMillis() - lastMs > 50) {
                lastMs = System.currentTimeMillis();
                currentFrame++;
//            Log.i("mInfo", "currentAnim= " + currentAnim + "currentFrame=" + currentFrame);
                if (currentFrame == animBitmaps.get(currentAnim).length) {
                    if (currentAnim == STATE_STILL || currentAnim == 18 || currentAnim == 19) {
                        currentFrame = 0;
                    } else {
                        currentFrame = 0;
                        currentAnim = STATE_STILL;
                    }
                }
            }
        }
    }

    public Rect getCurrentPosition() {
        return destRect;
    }

    public void changeAnim(int animType) {
        if (currentAnim != animType) {
            if (animType == ANIM_RANDOM) {
                currentAnim = new Random().nextInt(TOTAL_ANIMS - 1);
                if (currentAnim == 18 || currentAnim == 19 || currentAnim == 0) {
                    currentAnim = 1;
                }
                Log.i("mInfo", currentAnim + "");
                currentFrame = 0;
            } else {
                currentAnim = animType;
                currentFrame = 0;
            }
        }

    }

}
