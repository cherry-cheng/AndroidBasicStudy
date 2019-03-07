package com.cyh.bazisercubic;

import android.animation.TypeConverter;
import android.animation.TypeEvaluator;

class PathEvaluator  implements TypeEvaluator<PathPoint> {
    @Override
    public PathPoint evaluate(float fraction, PathPoint startValue, PathPoint endValue) {
        // fraction:    动画执行百分比（0～1）
        float x=0,y=0;
        if (endValue.mOperation == PathPoint.CUBIC) {//贝塞尔曲线类型
            float oneMinusT = 1 - fraction;
            // 以下是三阶贝塞尔曲线的公式
            x = oneMinusT * oneMinusT * oneMinusT * startValue.mX +
                    3 * oneMinusT * oneMinusT * fraction * endValue.mControl0X +
                    3 * oneMinusT * fraction * fraction * endValue.mControl1X +
                    fraction * fraction * fraction * endValue.mX;
            y = oneMinusT * oneMinusT * oneMinusT * startValue.mY +
                    3 * oneMinusT * oneMinusT * fraction * endValue.mControl0Y +
                    3 * oneMinusT * fraction * fraction * endValue.mControl1Y +
                    fraction * fraction * fraction * endValue.mY;
        } else if (endValue.mOperation == PathPoint.LINE) {//直线类型
            x = startValue.mX + fraction*(endValue.mX - startValue.mX);
            y = startValue.mY + fraction*(endValue.mY - startValue.mY);
        } else {//Move类型
            x = endValue.mX;
            y = endValue.mY;
        }
        return new PathPoint(PathPoint.MOVE, x, y);
    }
}
