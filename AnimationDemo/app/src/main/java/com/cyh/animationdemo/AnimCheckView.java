package com.cyh.animationdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

public class AnimCheckView extends View {
    private static final int ANIM_NULL = 0;
    private static final int ANIM_CHECK = 1;
    private static final int ANIM_UNCHECK = 2;

    private Context mContext;
    private int mWidth, mHeight;
    private Handler mHandler;

    private Paint mPaint;
    private Bitmap okBitmap;

    private int animCurrentPage = -1;
    private int animMaxPage = 13;
    private int animDuration = 500;
    private int animState = ANIM_NULL;

    private boolean isCheck = false;  //是否是选中状态

    public AnimCheckView(Context context) {
        super(context);
    }

    public AnimCheckView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;

        mPaint = new Paint();
        mPaint.setColor(0xffffffff);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        okBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.checkmark);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (animCurrentPage < animMaxPage && animCurrentPage >= 0) {
                    invalidate();
                    if (animState == ANIM_NULL)
                        return;
                    if (animState == ANIM_CHECK) {
                        animCurrentPage++;
                    } else if (animState == ANIM_UNCHECK) {
                        animCurrentPage--;
                    }
                    this.sendEmptyMessageDelayed(0, animDuration / animMaxPage);
                } else {
                    if (isCheck) {
                        animCurrentPage = animMaxPage - 1;
                    } else {
                        animCurrentPage = -1;
                    }

                    invalidate();
                    animState = ANIM_NULL;
                }
            }
        };

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

        canvas.translate(mWidth / 2, mHeight / 2);
        canvas.drawCircle(0, 0, 240, mPaint);

        int slideLength = okBitmap.getHeight();
        Rect src = new Rect(slideLength * animCurrentPage, 0, slideLength * (animCurrentPage + 1), slideLength);
        Rect dst = new Rect(-200, -200, 200, 200);
        canvas.drawBitmap(okBitmap, src, dst, null);
    }

    public void check() {
        if (animState != ANIM_NULL || isCheck)
            return;
        animState = ANIM_CHECK;
        animCurrentPage = 0;
        mHandler.sendEmptyMessageDelayed(0, animDuration / animMaxPage);
        isCheck = true;
    }

    public void unCheck() {
        if (animState != ANIM_NULL || (!isCheck))
            return;
        animState = ANIM_UNCHECK;
        animCurrentPage = animMaxPage - 1;
        mHandler.sendEmptyMessageDelayed(0, animDuration / animMaxPage);
        isCheck = false;
    }
}
