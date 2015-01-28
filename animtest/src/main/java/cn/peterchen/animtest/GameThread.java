package cn.peterchen.animtest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

    public static final int DIRECTION_UP = 0;
    public static final int DIRECTION_DOWN = 1;
    public static final int DIRECTION_LEFT = 2;
    public static final int DIRECTION_RIGHT = 3;

    public static final int ANIM_STATE_NO_ANIM = 0;
    public static final int ANIM_STATE_TRIMING = 1;
    public static final int ANIM_STATE_PLUSING = 2;
    public static final int ANIM_STATE_TRIMING2 = 3;
    public static final int ANIM_STATE_GENERATING = 4;

    private static final int SPEED = 40;

    private static final double ALPHA_SPEED = 20;

    private static final int PLUS_MAX = 30;

    private static final int PLUS_SPEED = 2;

    private static final int SPACING = 2;

    private SurfaceHolder holder;
    private Context context;
    public boolean isRun;

    private GameSurface surface;

    private int direction;

    private Paint boxPaint;
    private Paint numPaint;

    private int screenWidth;
    private int screenHeight;
    private int mainBoxWidth;
    private int inBoxWidth;

    private int mainBoxLeft;
    private int mainBoxRight;
    private int mainBoxTop;
    private int mainBoxBottom;

    private boolean isAnimating;

    private boolean isChangedFlag;

    private Rect gameRect;
    private int[][] matrix;
    private int[][] startMatrix;

    private List<Position> emptyList;

    private Random ran;

    private boolean isMatrixInit;

    private List<AnimMovePosition> trim1List;
    private boolean isTrim1Finished;
    private List<AnimMovePosition> trim2List;
    private boolean isTrim2Finished;
    private List<AnimMovePosition> plusList;
    private boolean isPlusFinished;

    private AnimAppearPosition newNumberAnim;

    private int score;
    private int maxNum;
    private ScoreListener mListener;

    private boolean isGameOver;

    // private List<AnimAppearPosition> appearAnimList;

    public interface ScoreListener {
        void getScores(int score, int maxNum);

        void gameOver();

        void gameWin();
    }

    public GameThread(Context context, GameSurface surface, SurfaceHolder holder) {
        this.context = context;
        this.surface = surface;
        this.holder = holder;
        init();
    }

    private void init() {
        isMatrixInit = false;
        isRun = true;

        screenWidth = ((Activity) context).getWindowManager()
                .getDefaultDisplay().getWidth();
        screenHeight = ((Activity) context).getWindowManager()
                .getDefaultDisplay().getHeight();
        mainBoxWidth = screenWidth - 2 * SPACING;
        inBoxWidth = (mainBoxWidth - 5 * SPACING) / 4;
        mainBoxLeft = SPACING;
        mainBoxRight = mainBoxLeft + mainBoxWidth;
        // mainBoxTop = (screenHeight - mainBoxWidth) / 2;
        mainBoxTop = SPACING;
        mainBoxBottom = mainBoxTop + mainBoxWidth;

        boxPaint = new Paint();
        numPaint = new Paint();
        numPaint.setTextSize(inBoxWidth / 2);
        numPaint.setTextAlign(Align.CENTER);
        numPaint.setColor(Color.BLACK);
        numPaint.setStyle(Style.FILL);
        numPaint.setAntiAlias(true);
        isAnimating = false;

        trim1List = new ArrayList<AnimMovePosition>();
        trim2List = new ArrayList<AnimMovePosition>();
        plusList = new ArrayList<AnimMovePosition>();
        // appearAnimList = new ArrayList<AnimAppearPosition>();

    }

    private void initMatrix(Canvas canvas) {
        canvas.drawColor(context.getResources().getColor(R.color.main_bg));// ���û���������ɫ
        boxPaint.setColor(context.getResources().getColor(R.color.main_box));
        gameRect = new Rect(mainBoxLeft, mainBoxTop, mainBoxRight,
                mainBoxBottom);
        canvas.drawRect(gameRect, boxPaint);

        matrix = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                matrix[i][j] = 0;
                drawRect(canvas, new Position(i, j), 0);
            }
        }
        startMatrix = new int[4][4];
        emptyList = new ArrayList<Position>();
        ran = new Random();
        isChangedFlag = true;
        isTrim1Finished = true;
        isPlusFinished = true;
        isTrim2Finished = true;
        isGameOver = false;
        score = 0;
        maxNum = 0;
        generateNewNum(canvas);
        // generateNewNum(canvas);

    }

    @Override
    public void run() {
        while (isRun) {
            Canvas c = null;
            try {
                synchronized (holder) {
                    if (!isMatrixInit) {
                        c = holder.lockCanvas();
                        initMatrix(c);
                        isMatrixInit = true;
                    } else {
                        c = holder.lockCanvas(gameRect);
                        drawMatrix(c);
                    }
                    Thread.sleep(10);
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            } finally {
                if (c != null) {
                    holder.unlockCanvasAndPost(c);
                }
            }
        }

    }

    /**
     * @param canvas
     */
    public void drawMatrix(Canvas canvas) {
        if (isAnimating) {
            drawEmpty(canvas);
            drawAnimation(canvas);
        } else {
            if (isGameOver)
                normalDraw(canvas);
        }
    }

    /**
     * @param canvas
     * @param position
     * @param num
     */
    private void drawRect(Canvas canvas, Position position, int num) {
        setPaintStyle(num);
        Position temp = this.getRectPosByIndex(position.x, position.y);
        Rect rect = new Rect(temp.x, temp.y, temp.x + inBoxWidth, temp.y
                + inBoxWidth);
        canvas.drawRect(rect, boxPaint);
        temp = this.getNumPosByRect(temp.x, temp.y);
        if (num != 0) {
            canvas.drawText(num + "", temp.x, temp.y, numPaint);
        }
    }

    /**
     * @param canvas
     */
    private void drawNewNumber(Canvas canvas) {
        setPaintStyle(newNumberAnim.num);
        Position temp = this
                .getRectPosByIndex(newNumberAnim.x, newNumberAnim.y);
        Rect rect = new Rect(temp.x, temp.y, temp.x + inBoxWidth, temp.y
                + inBoxWidth);
        newNumberAnim.alpha += this.ALPHA_SPEED;
        if (newNumberAnim.alpha >= 255) {
            newNumberAnim.alpha = 255;
        }
        boxPaint.setAlpha(newNumberAnim.alpha);
        canvas.drawRect(rect, boxPaint);
        temp = this.getNumPosByRect(temp.x, temp.y);
        numPaint.setAlpha(newNumberAnim.alpha);
        canvas.drawText(newNumberAnim.num + "", temp.x, temp.y, numPaint);
    }

    /**
     * @param plusAnim
     * @param canvas
     */
    private void drawPlusAnim(AnimPlusPosition plusAnim, Canvas canvas) {

    }

    /**
     * @param canvas
     */
    public void normalDraw(Canvas canvas) {
        boxPaint.setColor(context.getResources().getColor(R.color.main_box));
        canvas.drawRect(gameRect, boxPaint);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                drawRect(canvas, new Position(i, j), matrix[i][j]);
            }
        }
    }

    /**
     * @param canvas
     */
    public void drawEmpty(Canvas canvas) {
        Rect rect;
        Position temp;
        boxPaint.setColor(context.getResources().getColor(R.color.main_box));
        canvas.drawRect(gameRect, boxPaint);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                setPaintStyle(0);
                temp = this.getRectPosByIndex(i, j);
                rect = new Rect(temp.x, temp.y, temp.x + inBoxWidth, temp.y
                        + inBoxWidth);
                canvas.drawRect(rect, boxPaint);
            }
        }
    }

    /**
     * @param i
     * @param j
     * @return
     */
    public Position getRectPosByIndex(int i, int j) {
        Position temp = new Position();
        temp.y = mainBoxTop + i * inBoxWidth + (i + 1) * SPACING;
        temp.x = mainBoxLeft + j * inBoxWidth + (j + 1) * SPACING;
        return temp;
    }

    /**
     * @param x
     * @param y
     * @return
     */
    public Position getNumPosByRect(int x, int y) {
        Position temp = new Position();
        temp.x = x + inBoxWidth / 2;
        temp.y = (int) ((y + y + inBoxWidth) / 2 - (numPaint.descent() + numPaint
                .ascent()) / 2);
        return temp;
    }

    /**
     */
    private void drawAnimation(Canvas canvas) {

        if (!isTrim1Finished) {
            isTrim1Finished = true;
            for (int i = 0; i < trim1List.size(); i++) {
                moveRect(trim1List.get(i), canvas);
                if (!trim1List.get(i).isDone) {
                    isTrim1Finished = false;
                }
            }
            if (isTrim1Finished)
                trim1List.clear();
        }
        // else if (!isPlusFinished) {
        // isPlusFinished = true;
        // for (int i = 0; i < plusList.size(); i++) {
        // moveRect(plusList.get(i), canvas);
        // if (!plusList.get(i).isDone) {
        // isPlusFinished = false;
        // }
        // }
        // if(isPlusFinished)
        // plusList.clear();
        // }
        else if (!isTrim2Finished) {
            isTrim2Finished = true;
            for (int i = 0; i < trim2List.size(); i++) {
                moveRect(trim2List.get(i), canvas);
                if (!trim2List.get(i).isDone) {
                    isTrim2Finished = false;
                }
            }
            if (isTrim2Finished) {
                trim2List.clear();
                generateNewNum(canvas);
            }
        } else if (newNumberAnim.alpha < 255) {
            normalDraw(canvas);
            drawNewNumber(canvas);
        } else {
            matrix[newNumberAnim.x][newNumberAnim.y] = newNumberAnim.num;
            isAnimating = false;
            normalDraw(canvas);
        }
    }

    public void moveRect(AnimMovePosition animPos, Canvas canvas) {
        Position currentPos = getRectPosByIndex(animPos.x1, animPos.y1);
        Position targetPos = getRectPosByIndex(animPos.x2, animPos.y2);
        if (!animPos.isDone) {
            animPos.offset += SPEED;
            switch (direction) {
                case DIRECTION_LEFT:
                    currentPos.x -= animPos.offset;
                    if (currentPos.x <= targetPos.x)
                        animPos.isDone = true;
                    break;
                case DIRECTION_RIGHT:
                    currentPos.x += animPos.offset;
                    if (currentPos.x >= targetPos.x)
                        animPos.isDone = true;
                    break;
                case DIRECTION_UP:
                    currentPos.y -= animPos.offset;
                    if (currentPos.y <= targetPos.y)
                        animPos.isDone = true;
                    break;
                case DIRECTION_DOWN:
                    currentPos.y += animPos.offset;
                    if (currentPos.y >= targetPos.y)
                        animPos.isDone = true;
                    break;
                default:
                    break;
            }
        }
        Rect rect;
        if (animPos.isDone) {
            setPaintStyle(animPos.num);
            rect = new Rect(targetPos.x, targetPos.y, targetPos.x + inBoxWidth,
                    targetPos.y + inBoxWidth);
            canvas.drawRect(rect, boxPaint);
            targetPos = this.getNumPosByRect(targetPos.x, targetPos.y);
            canvas.drawText(animPos.num + "", targetPos.x, targetPos.y,
                    numPaint);

        } else {
            setPaintStyle(animPos.num);
            rect = new Rect(currentPos.x, currentPos.y, currentPos.x
                    + inBoxWidth, currentPos.y + inBoxWidth);
            canvas.drawRect(rect, boxPaint);
            currentPos = this.getNumPosByRect(currentPos.x, currentPos.y);
            canvas.drawText(animPos.num + "", currentPos.x, currentPos.y,
                    numPaint);
        }
    }

    private void setPaintStyle(int i) {
        switch (i) {
            case 0:
                boxPaint.setColor(context.getResources().getColor(R.color.num_0_bg));
                break;
            case 2:
                numPaint.setColor(context.getResources().getColor(R.color.num_deep));
                numPaint.setTextSize(inBoxWidth / 2);
                boxPaint.setColor(context.getResources().getColor(R.color.num_2_bg));
                break;
            case 4:
                numPaint.setColor(context.getResources().getColor(R.color.num_deep));
                numPaint.setTextSize(inBoxWidth / 2);
                boxPaint.setColor(context.getResources().getColor(R.color.num_4_bg));
                break;
            case 8:
                numPaint.setColor(context.getResources()
                        .getColor(R.color.num_light));
                numPaint.setTextSize(inBoxWidth / 2);
                boxPaint.setColor(context.getResources().getColor(R.color.num_8_bg));
                break;
            case 16:
                numPaint.setColor(context.getResources()
                        .getColor(R.color.num_light));
                numPaint.setTextSize(inBoxWidth / 2);
                boxPaint.setColor(context.getResources()
                        .getColor(R.color.num_16_bg));
                break;
            case 32:
                numPaint.setColor(context.getResources()
                        .getColor(R.color.num_light));
                numPaint.setTextSize(inBoxWidth / 2);
                boxPaint.setColor(context.getResources()
                        .getColor(R.color.num_32_bg));
                break;
            case 64:
                numPaint.setColor(context.getResources()
                        .getColor(R.color.num_light));
                numPaint.setTextSize(inBoxWidth / 2);
                boxPaint.setColor(context.getResources()
                        .getColor(R.color.num_64_bg));
                break;
            default:
                numPaint.setColor(context.getResources()
                        .getColor(R.color.num_light));
                numPaint.setTextSize(inBoxWidth / 3);
                boxPaint.setColor(context.getResources().getColor(
                        R.color.num_big_bg));
                break;
        }
    }

    public boolean isAnimating() {
        return isAnimating;
    }

    public void setAnimating(boolean isAnimating) {
        this.isAnimating = isAnimating;
        if (isAnimating) {
            isTrim1Finished = false;
            isPlusFinished = false;
            isTrim2Finished = false;
        }
    }

    public int[][] reverseMatrix(int[][] originalMatrix) {
        int[][] newMatrix = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                newMatrix[i][j] = originalMatrix[j][i];
            }
        }
        return newMatrix;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    // //////////////////////////////////////////////////
    //
    //
    //
    // /////////////////////////////////////////////////
    public void onKeyRight() {
        synchronized (matrix) {
            if (checkGameOver()) {
                System.out.println("游戏结束");
                isGameOver = true;
                mListener.gameOver();
            } else {
                cloneMatrix(matrix, startMatrix);
                for (int i = 0; i < 4; i++) {
                    int[] arr = new int[4];
                    for (int j = 3; j >= 0; j--) {
                        arr[3 - j] = startMatrix[i][j];
                    }
                    trimSpaceInArr(arr, i, true);
                    // for (int j = 3; j >=0; j--) {
                    // trimedMatrix[i][j] = arr[3-j];
                    // }
                    moveArray(arr, i);
                    // for (int j = 3; j >=0; j--) {
                    // plusedMatrix[i][j] = arr[3-j];
                    // }
                    trimSpaceInArr(arr, i, false);
                    for (int j = 3; j >= 0; j--) {
                        matrix[i][j] = arr[3 - j];
                    }
                }
                isChangedFlag = compareMatrix(startMatrix, matrix);

            }
        }

    }

    public void onKeyLeft() {

        synchronized (matrix) {
            if (checkGameOver()) {
                System.out.println("游戏结束");
                isGameOver = true;
                mListener.gameOver();
            } else {
                cloneMatrix(matrix, startMatrix);
                for (int i = 0; i < 4; i++) {
                    int[] arr = new int[4];
                    for (int j = 0; j < 4; j++) {
                        arr[j] = startMatrix[i][j];
                    }
                    trimSpaceInArr(arr, i, true);
                    // for (int j = 0; j < 4; j++) {
                    // trimedMatrix[i][j] = arr[j];
                    // }
                    moveArray(arr, i);
                    // for (int j = 0; j < 4; j++) {
                    // plusedMatrix[i][j] = arr[j];
                    // }
                    trimSpaceInArr(arr, i, false);
                    for (int j = 0; j < 4; j++) {
                        matrix[i][j] = arr[j];
                    }
                }
                isChangedFlag = compareMatrix(startMatrix, matrix);

            }
        }

    }

    public void onKeyDown() {

        synchronized (matrix) {
            if (checkGameOver()) {
                System.out.println("游戏结束");
                isGameOver = true;
                mListener.gameOver();
            } else {
                cloneMatrix(matrix, startMatrix);
                for (int i = 0; i < 4; i++) {
                    int[] arr = new int[4];
                    for (int j = 3; j >= 0; j--) {
                        arr[3 - j] = startMatrix[j][i];
                    }
                    trimSpaceInArr(arr, i, true);
                    // for (int j = 3; j >=0; j--) {
                    // trimedMatrix[j][i] = arr[3-j];
                    // }
                    moveArray(arr, i);
                    // for (int j = 3; j >=0; j--) {
                    // plusedMatrix[j][i] = arr[3-j];
                    // }
                    trimSpaceInArr(arr, i, false);
                    for (int j = 3; j >= 0; j--) {
                        matrix[j][i] = arr[3 - j];
                    }
                }

                isChangedFlag = compareMatrix(startMatrix, matrix);

            }
        }

    }

    public void onKeyUp() {

        synchronized (matrix) {
            if (checkGameOver()) {
                System.out.println("��Ϸ����");
                isGameOver = true;
                mListener.gameOver();
            } else {
                cloneMatrix(matrix, startMatrix);
                for (int i = 0; i < 4; i++) {
                    int[] arr = new int[4];
                    for (int j = 0; j < 4; j++) {
                        arr[j] = startMatrix[j][i];
                    }
                    trimSpaceInArr(arr, i, true);
                    // for (int j = 0; j < 4; j++) {
                    // trimedMatrix[j][i] = arr[j];
                    // }
                    moveArray(arr, i);
                    // for (int j = 0; j < 4; j++) {
                    // plusedMatrix[j][i] = arr[j];
                    // }
                    trimSpaceInArr(arr, i, false);
                    for (int j = 0; j < 4; j++) {
                        matrix[j][i] = arr[j];
                    }
                }

                isChangedFlag = compareMatrix(startMatrix, matrix);
            }
        }

    }

    public void moveArray(int[] arr, int row) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] != 0 && arr[i] == arr[i + 1]) {
                AnimMovePosition temp = new AnimMovePosition();
                temp.isDone = false;
                temp.offset = 0;
                temp.num = arr[i];
                arr[i] *= 2;
                arr[i + 1] = 0;
                score += arr[i];
                switch (direction) {
                    case DIRECTION_LEFT:
                        // ��row,j �ƶ��� row,j-step
                        temp.x1 = row;
                        temp.x2 = row;
                        temp.y1 = i;
                        temp.y2 = i + 1;
                        break;
                    case DIRECTION_RIGHT:
                        // ��row,4-j�ƶ���row,4-(j-step)
                        temp.x1 = row;
                        temp.x2 = row;
                        temp.y1 = 3 - i;
                        temp.y2 = 3 - (i + 1);
                        break;
                    case DIRECTION_UP:
                        // ��j,row �ƶ��� j-step,row
                        temp.y1 = row;
                        temp.y2 = row;
                        temp.x1 = i;
                        temp.x2 = i + 1;
                        break;
                    case DIRECTION_DOWN:
                        // ��4-j,row�ƶ���4-(j-step),row
                        temp.y1 = row;
                        temp.y2 = row;
                        temp.x1 = 3 - i;
                        temp.x2 = 3 - (i + 1);
                        break;
                }
                plusList.add(temp);

                i++;
            }
        }

        getMaxNum();
        if (maxNum == 2048)
            mListener.gameWin();
        mListener.getScores(score, maxNum);
    }

    private void getMaxNum() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (matrix[i][j] > maxNum)
                    maxNum = matrix[i][j];
            }
        }
    }

    /**
     * ȥ�ո�
     *
     * @param arr
     */
    private void trimSpaceInArr(int[] arr, int row, boolean isTrim1) {
        // ���ƶ��ĸ���ԭ�ػ�����
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 0)
                break;
            AnimMovePosition temp = new AnimMovePosition();
            temp.isDone = true;
            temp.offset = 0;
            temp.num = arr[i];
            switch (direction) {
                case DIRECTION_LEFT:
                    // ��row,j �ƶ��� row,j-step
                    temp.x1 = row;
                    temp.x2 = row;
                    temp.y1 = i;
                    temp.y2 = i;
                    break;
                case DIRECTION_RIGHT:
                    // ��row,4-j�ƶ���row,4-(j-step)
                    temp.x1 = row;
                    temp.x2 = row;
                    temp.y1 = 3 - i;
                    temp.y2 = 3 - i;
                    break;
                case DIRECTION_UP:
                    // ��j,row �ƶ��� j-step,row
                    temp.y1 = row;
                    temp.y2 = row;
                    temp.x1 = i;
                    temp.x2 = i;
                    break;
                case DIRECTION_DOWN:
                    // ��4-j,row�ƶ���4-(j-step),row
                    temp.y1 = row;
                    temp.y2 = row;
                    temp.x1 = 3 - i;
                    temp.x2 = 3 - i;
                    break;
            }
            if (isTrim1)
                trim1List.add(temp);
            else
                trim2List.add(temp);

        }
        // ��ʼ�ҳ��ƶ��ĸ���
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] == 0) {
                // ǰ�� ���ҵ���һ���������
                int t = i + 1;

                while (t < arr.length) {
                    if (arr[t] != 0) {
                        break;
                    }
                    t++;
                }
                if (t < arr.length) {
                    // ��ʱa[t]��Ϊ��
                    int step = t - i;
                    for (int j = i + step; j < arr.length; j++) {
                        arr[j - step] = arr[j];
                        AnimMovePosition temp = new AnimMovePosition();
                        temp.isDone = false;
                        temp.offset = 0;
                        temp.num = arr[j];
                        switch (direction) {
                            case DIRECTION_LEFT:
                                // ��row,j �ƶ��� row,j-step
                                temp.x1 = row;
                                temp.x2 = row;
                                temp.y1 = j;
                                temp.y2 = j - step;
                                break;
                            case DIRECTION_RIGHT:
                                // ��row,4-j�ƶ���row,4-(j-step)
                                temp.x1 = row;
                                temp.x2 = row;
                                temp.y1 = 3 - j;
                                temp.y2 = 3 - (j - step);
                                break;
                            case DIRECTION_UP:
                                // ��j,row �ƶ��� j-step,row
                                temp.y1 = row;
                                temp.y2 = row;
                                temp.x1 = j;
                                temp.x2 = j - step;
                                break;
                            case DIRECTION_DOWN:
                                // ��4-j,row�ƶ���4-(j-step),row
                                temp.y1 = row;
                                temp.y2 = row;
                                temp.x1 = 3 - j;
                                temp.x2 = 3 - (j - step);
                                break;
                        }
                        if (temp.num != 0)
                            if (isTrim1)
                                trim1List.add(temp);
                            else
                                trim2List.add(temp);
                    }
                    for (int j = arr.length - 1; j >= arr.length - step; j--) {
                        arr[j] = 0;
                        // ���治��Ĳ���
                        AnimAppearPosition temp2 = new AnimAppearPosition();
                        temp2.num = 0;
                        temp2.alpha = 0;
                        switch (direction) {
                            case DIRECTION_LEFT:
                                temp2.x = row;
                                temp2.y = j;
                                break;
                            case DIRECTION_RIGHT:
                                temp2.x = row;
                                temp2.y = 3 - j;
                                break;
                            case DIRECTION_UP:
                                temp2.x = j;
                                temp2.y = row;
                                break;
                            case DIRECTION_DOWN:
                                temp2.x = 3 - j;
                                temp2.y = row;
                                break;
                        }
                        // appearAnimList.add(temp2);
                    }
                    i--;
                }
            }
        }

    }

    private boolean checkGameOver() {
        emptyList.clear();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (matrix[i][j] == 0) {
                    Position temp = new Position();
                    temp.x = i;
                    temp.y = j;
                    emptyList.add(temp);
                }
            }
        }
        if (emptyList.size() > 0) {
            return false;
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i < 3)
                    if (matrix[i][j] == matrix[i + 1][j])
                        return false;

                if (j < 3)
                    if (matrix[i][j] == matrix[i][j + 1])
                        return false;
            }
        }
        return true;
    }

    public void generateNewNum(Canvas canvas) {
        // ���û�ı䣬�Ͳ�����µ�����
        if (!isChangedFlag)
            return;
        emptyList.clear();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (matrix[i][j] == 0) {
                    Position temp = new Position();
                    temp.x = i;
                    temp.y = j;
                    emptyList.add(temp);
                }
            }
        }

        if (emptyList.size() > 0) {
            Position p = emptyList.get(ran.nextInt(emptyList.size()));
            if (ran.nextFloat() < 0.9) {
                // matrix[p.x][p.y] = 2;
                newNumberAnim = new AnimAppearPosition();
                newNumberAnim.alpha = 0;
                newNumberAnim.x = p.x;
                newNumberAnim.y = p.y;
                newNumberAnim.num = 2;
                isAnimating = true;
                // drawRect(canvas, p, 2);
            } else {
                // matrix[p.x][p.y] = 4;
                newNumberAnim = new AnimAppearPosition();
                newNumberAnim.alpha = 0;
                newNumberAnim.x = p.x;
                newNumberAnim.y = p.y;
                newNumberAnim.num = 4;
                isAnimating = true;
                // drawRect(canvas, p, 4);
            }
        }

    }

    /**
     * ����иı䣬����true û�иı䷵��false
     *
     * @param startMatrix2
     * @param matrix2
     * @return
     */
    private boolean compareMatrix(int[][] startMatrix2, int[][] matrix2) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (startMatrix[i][j] != matrix[i][j])
                    return true;
            }
        }
        return false;
    }

    private void cloneMatrix(int[][] sourceMatrix, int[][] targetMatrix) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                targetMatrix[i][j] = sourceMatrix[i][j];
            }
        }

    }

    public boolean isMatrixInit() {
        return isMatrixInit;
    }

    public void setMatrixInit(boolean isMatrixInit) {
        this.isMatrixInit = isMatrixInit;
        score = 0;
        maxNum = 0;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public ScoreListener getmListener() {
        return mListener;
    }

    public void setmListener(ScoreListener mListener) {
        this.mListener = mListener;
    }

    class Position {
        int x;
        int y;

        Position() {
        }

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    class AnimMovePosition {
        int num;
        int x1;// ��һ������������x
        int y1;// ��һ������������y
        int x2;// �ڶ�������������x
        int y2;// �ڶ�������������y
        int offset;
        boolean isDone;
    }

    class AnimAppearPosition {
        int num;
        int x;// ��һ������������x
        int y;// ��һ������������y
        int alpha;// ���ֵ�͸���ȣ����㵽1
    }

    class AnimPlusPosition {
        int num;
        int x;// ��һ������������x
        int y;// ��һ������������y
        int offset;// ���ֵ�͸���ȣ����㵽1
    }

}
