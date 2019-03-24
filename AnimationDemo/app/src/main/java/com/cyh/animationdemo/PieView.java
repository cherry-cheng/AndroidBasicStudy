package com.cyh.animationdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class PieView extends View {
    private Paint mPaint = new Paint();
    private List<PieData> mList;
    private float startAngle = 0;
    private int mWidth, mHeight;

    public PieView(Context context) {
        this(context, null);
    }

    public PieView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null == mList) {
            return;
        }
        float currentStartAngle = startAngle;
        canvas.translate(mWidth / 2, mHeight / 2); //将画布坐标原点移动到中心位置
        float r = (float) (Math.min(mWidth, mHeight) / 2 * 0.8);  //饼状图半径
        RectF rectF = new RectF(-r, -r, r, r);
        for (int i = 0; i < mList.size(); i++) {
            PieData pie = mList.get(i);
            mPaint.setColor(pie.getColor());
            canvas.drawArc(rectF, currentStartAngle, pie.getAngle(), true, mPaint);
            currentStartAngle = currentStartAngle + pie.getAngle();
        }
    }

    // 设置起始角度
    public void setStartAngle(int mStartAngle) {
        this.startAngle = mStartAngle;
        invalidate();
    }

    public void setmList(ArrayList<PieData> mData) {
        this.mList = mData;
        initData(mData);
        invalidate();
    }

    private void initData(ArrayList<PieData> mData) {
        if (null == mData || mData.size() == 0) {
            return;
        }
        float sumValue = 0;
        for (int i = 0; i < mData.size(); i++) {
            PieData pieData = mData.get(i);
            sumValue += pieData.getValue();
        }

        for (int i = 0; i< mData.size(); i++) {
            PieData pieData = mData.get(i);
            float percentage = pieData.getValue() / sumValue;
            float angle = percentage * 360;
            pieData.setPercentage(percentage);
            pieData.setAngle(angle);
        }
    }
}
