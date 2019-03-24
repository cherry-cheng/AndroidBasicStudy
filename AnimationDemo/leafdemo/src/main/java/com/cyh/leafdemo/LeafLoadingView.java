package com.cyh.leafdemo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class LeafLoadingView extends View {
    private static final String TAG = "LeafLoadingView";
    private static final int WHITE_COLOR = 0xfffde399;
    private static final int ORANGE_COLOR = 0xffffa800;
    private static final int MIDDLE_AMPLITUDE = 13;
    private static final int AMPLITUDE_DISPARITY = 5;
    private static final int TOTAL_PROGRESS = 100;
    private static final long LEAF_FLOAT_TIME = 3000;
    private static final long LEAF_ROTATE_TIME = 2000;
    private static final int LEFT_MARGIN = 9;
    private static final int RIGHT_MARGIN = 25;
    private int mLeftMargin, mRightMargin;
    private int mMiddleAmplitude = MIDDLE_AMPLITUDE;
    private int mAmplitudeDisparity = AMPLITUDE_DISPARITY;
    private long mLeafFloatTime = LEAF_FLOAT_TIME;
    private long mLeafRotateTime = LEAF_ROTATE_TIME;
    private Resources mResources;
    private Bitmap mLeafBitmap;
    private int mLeafWidth, mLeafHeight;

    private Bitmap mOuterBitmap;
    private Rect mOuterSrcRect, mOuterDestRect;
    private int mOuterWidth, mOuterHeight;
    private int mTotalWidth, mTotalHeight;

    private Paint mBitmapPaint, mWhitePaint, mOrangePaint;
    private RectF mWhiteRectF, mOrangeRectF, mArcRectF;

    private int mProgress;
    private int mProgressWidth;
    private int mCurrentProgressPosition;
    private int mArcRadius;

    private int mArcRightLocation;
    private LeafFactory mLeafFactory;
    private List<Leaf> mLeafInfos;
    private int mAddTime;

    public LeafLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mResources = getResources();
        mLeftMargin = UiUtils.dipToPx(context, LEFT_MARGIN);
        mRightMargin = UiUtils.dipToPx(context, RIGHT_MARGIN);

        mLeafFloatTime = LEAF_FLOAT_TIME;
        mLeafRotateTime = LEAF_ROTATE_TIME;

        initBitmap();
        initPaint();
        mLeafFactory = new LeafFactory();
        mLeafInfos = mLeafFactory.generateLeafs();
    }

    private void initBitmap() {
        mLeafBitmap = ((BitmapDrawable) mResources.getDrawable(R.drawable.leaf)).getBitmap();
        mLeafWidth = mLeafBitmap.getWidth();
        mLeafHeight = mLeafBitmap.getHeight();

        mOuterBitmap = ((BitmapDrawable) mResources.getDrawable(R.drawable.leaf_kuang)).getBitmap();
        mOuterWidth = mOuterBitmap.getWidth();
        mOuterHeight = mOuterBitmap.getHeight();
    }

    private void initPaint() {
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true); //防锯齿
        mBitmapPaint.setDither(true);//防抖动
        mBitmapPaint.setFilterBitmap(true);

        mWhitePaint = new Paint();
        mWhitePaint.setAntiAlias(true);
        mWhitePaint.setColor(WHITE_COLOR);

        mOrangePaint = new Paint();
        mOrangePaint.setAntiAlias(true);
        mOrangePaint.setColor(ORANGE_COLOR);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalWidth = w;
        mTotalHeight = h;
        mProgressWidth = mTotalWidth - mLeftMargin - mRightMargin;
        mArcRadius = (mTotalHeight - 2 * mLeftMargin) / 2;

        mOuterSrcRect = new Rect(0, 0, mOuterWidth, mOuterHeight);
        mOuterDestRect = new Rect(0, 0, mTotalWidth, mTotalHeight);

        mWhiteRectF = new RectF(mLeftMargin + mCurrentProgressPosition, mLeftMargin, mTotalWidth - mRightMargin,
                mTotalHeight - mLeftMargin);
        mOrangeRectF = new RectF(mLeftMargin + mArcRadius, mLeftMargin, mCurrentProgressPosition, mTotalHeight - mLeftMargin);

        mArcRectF = new RectF(mLeftMargin, mLeftMargin, mLeftMargin + 2 * mArcRadius,
                mTotalHeight - mLeftMargin);
        mArcRightLocation = mLeftMargin + mArcRadius;
    }

    private enum StartType {
        LITTLE, MIDDLE, BIG
    }

    private class Leaf {
        float x, y;
        StartType type;
        int rotateAngle;
        int rotateDirection;
        long startTime;
    }

    private class Leafactory {
        private static final int MAX_LEAFS = 8;
        Random random = new Random();

        public Leaf generateLeaf() {
            Leaf leaf = new Leaf();
            int randomType = random.nextInt(3);
            StartType type = StartType.MIDDLE;
            switch (randomType) {
                case 0:
                    break;
                case 1:
                    type = StartType.LITTLE;
                    break;
                case 2:
                    type = StartType.BIG;
                    break;
                default:
                    break;
            }
            leaf.type = type;
            leaf.rotateAngle = random.nextInt(360);
            leaf.rotateDirection = random.nextInt(2);
            mLeafFloatTime = mLeafFloatTime <= 0 ? LEAF_FLOAT_TIME : mLeafFloatTime;
            mAddTime += random.nextInt((int)(mLeafFloatTime * 2));
            leaf.startTime = System.currentTimeMillis() + mAddTime;
            return leaf;

        }

        public List<Leaf> generateLeafs() {
            return generateLeafs(MAX_LEAFS);
        }

        public List<Leaf> generateLeafs(int leafSize) {
            List<Leaf> leafs = new LinkedList<Leaf>();
            for (int i=0; i < leafSize; i++) {
                leafs.add(generateLeaf());
            }
            return leafs;
        }
    }

    public void setMiddleAmplitude(int amplitude) {
        this.mMiddleAmplitude = amplitude;
    }


    public void setMplitudeDisparity(int disparity) {
        this.mAmplitudeDisparity = disparity;
    }

    public int getMiddleAmplitude() {
        return mMiddleAmplitude;
    }

    public int getMplitudeDisparity() {
        return mAmplitudeDisparity;
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
        postInvalidate();
    }

    public void setmLeafFloatTime(long mLeafFloatTime) {
        this.mLeafFloatTime = mLeafFloatTime;
    }

    public void setmLeafRotateTime(long mLeafRotateTime) {
        this.mLeafRotateTime = mLeafRotateTime;
    }

    public long getLeafFloatTime() {
        mLeafFloatTime = mLeafRotateTime == 0 ? LEAF_FLOAT_TIME : mLeafFloatTime;
        return mLeafFloatTime;
    }

    public long getLeafRotateTime() {
        mLeafRotateTime = mLeafRotateTime == 0 ? LEAF_ROTATE_TIME : mLeafRotateTime;
        return mLeafRotateTime;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawProgressAndLeafs(canvas);
    }

    private void drawProgressAndLeafs(Canvas canvas) {
        if (mProgress >= TOTAL_PROGRESS) {
            mProgress = 0;
        }
        mCurrentProgressPosition = mProgressWidth * mProgress / TOTAL_PROGRESS;
        if (mCurrentProgressPosition < mArcRadius) {
            // 1.绘制白色ARC
            canvas.drawArc(mArcRectF, 90, 180, false, mWhitePaint);

            //2.绘制白色矩形
            mWhiteRectF.left = mArcRightLocation;
            canvas.drawRect(mWhiteRectF, mWhitePaint);

            //绘制叶子
            drawLeafs(canvas);

            //3。绘制棕色ARC
            //单边角度
            int angle = (int) Math.toDegrees(Math.acos((mArcRadius - mCurrentProgressPosition)
                    / (float) mArcRadius));
            // 起始位置
            int startAngle = 180 - angle;
            //扫过的角度
            int sweepAngle = 2 * angle;

            canvas.drawArc(mArcRectF, startAngle, sweepAngle, false, mOrangePaint);
        } else {
            //1、绘制white RECT
            //2、绘制Orange ARC
            //3、绘制orange RECT
            //这个层级进行绘制能让叶子感觉是融入棕色进度条中

            //1、绘制white RECT
            mWhiteRectF.left = mCurrentProgressPosition;
            canvas.drawRect(mWhiteRectF, mWhitePaint);
            //绘制叶子
            drawLeafs(canvas);
            //2、绘制Orange ARC
            canvas.drawArc(mArcRectF, 90, 180, false, mOrangePaint);
            //3、绘制orange RECT
            mOrangeRectF.left = mArcRightLocation;
            mOrangeRectF.right = mCurrentProgressPosition;
            canvas.drawRect(mOrangeRectF, mOrangePaint);
        }

    }

    /**
     * 绘制叶子
     * @param canvas
     */
    private void drawLeafs(Canvas canvas) {
        mLeafRotateTime = mLeafRotateTime <= 0 ? LEAF_ROTATE_TIME : mLeafRotateTime;
        long currentTime = System.currentTimeMillis();
        for (int i = 0; i < mLeafInfos.size(); i++) {
            Leaf leaf = mLeafInfos.get(i);
            if (currentTime > leaf.startTime && leaf.startTime != 0) {
                //绘制叶子--根据叶子的类型和当前时间得出叶子的（x, y）
                getLeafLocaton(leaf, currentTime);
                //根据时间计算旋转角度
                canvas.save();
                //通过Matrix控制叶子旋转
                Matrix matrix = new Matrix();
                float transX = mLeftMargin + leaf.x;
                float transY = mLeftMargin + leaf.y;
                matrix.postTranslate(transX, transY);
                // 通过时间关联旋转角度，则可以直接通过修改LEAF_ROTATE_TIME调节叶子旋转快慢
                float rotateFraction = ((currentTime - leaf.startTime) % mLeafRotateTime)
                        / (float) mLeafRotateTime;
                int angle = (int) (rotateFraction * 360);
                // 根据叶子旋转方向确定叶子旋转角度
                int rotate = leaf.rotateDirection == 0 ? angle + leaf.rotateAngle : -angle
                        + leaf.rotateAngle;
                matrix.postRotate(rotate, transX + mLeafWidth / 2, transY + mLeafHeight / 2);
                canvas.drawBitmap(mLeafBitmap, matrix, mBitmapPaint);
                canvas.restore();
            } else {
                continue;
            }
        }
    }

    private void getLeafLocaton(Leaf leaf, long currentTime) {
        long intervalTime = currentTime - leaf.startTime;
        mLeafFloatTime = mLeafFloatTime <= 0 ? LEAF_FLOAT_TIME : mLeafFloatTime;
        if (intervalTime < 0) {
            return;
        } else if (intervalTime > mLeafFloatTime) {
            leaf.startTime = System.currentTimeMillis()
                    + new Random().nextInt((int) mLeafFloatTime);
        }

        float fraction = (float) intervalTime / mLeafFloatTime;
        leaf.x = (int) (mProgressWidth - mProgressWidth * fraction);
        leaf.y = getLocationY(leaf);
    }

    //通过叶子信息获取当前叶子的Y值
    private float getLocationY(Leaf leaf) {
        // y = A(wx+Q)+h
        float w = (float) ((float) 2 * Math.PI / mProgressWidth);
        float a = mMiddleAmplitude;
        switch (leaf.type) {
            case LITTLE:
                // 小振幅 = 中等振幅 - 振幅差
                a = mMiddleAmplitude - mAmplitudeDisparity;
                break;
            case MIDDLE:
                a = mMiddleAmplitude;
                break;
            case BIG:
                a = mMiddleAmplitude + mAmplitudeDisparity;
                break;
            default:
                break;
        }

        return (int) (a * Math.sin(w * leaf.x)) + mArcRadius * 2 / 3;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private class LeafFactory {
        public static final int MAX_LEAFS = 8;
        Random random = new Random();

        public Leaf generateLeaf() {
            Leaf leaf = new Leaf();
            int randomType = random.nextInt(3);
            StartType type = StartType.MIDDLE;
            switch (randomType) {
                case 0:
                    break;
                case 1:
                    type = StartType.LITTLE;
                    break;
                case 2:
                    type = StartType.BIG;
                    break;
                    default:
                        break;
            }
            leaf.type = type;
            leaf.rotateAngle = random.nextInt(360);
            leaf.rotateDirection = random.nextInt(2);
            mLeafFloatTime = mLeafFloatTime <= 0 ? LEAF_FLOAT_TIME : mLeafFloatTime;
            mAddTime += random.nextInt((int)(mLeafFloatTime * 2));
            leaf.startTime = System.currentTimeMillis() + mAddTime;
            return leaf;
        }

        public List<Leaf> generateLeafs() {
            return generateLeafs(MAX_LEAFS);
        }

        public List<Leaf> generateLeafs(int leafSize) {
            List<Leaf> leafs = new LinkedList<Leaf>();
            for (int i = 0; i < leafSize; i++) {
                leafs.add(generateLeaf());
            }
            return leafs;
        }
    }
}
