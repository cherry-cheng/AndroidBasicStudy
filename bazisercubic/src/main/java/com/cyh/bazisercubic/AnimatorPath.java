package com.cyh.bazisercubic;

import android.animation.ObjectAnimator;
import android.view.View;

import java.util.ArrayList;

/**
 * 模仿Path系统工具类的设计
 * 能够存储一系列的指令：moveTo/lineTo/cubicTo(x,y等参数)
 */
public class AnimatorPath {
    ArrayList<PathPoint> pathPointList = new ArrayList<>();
    private View view;

    public void moveTo(float x, float y) {
        pathPointList.add(new PathPoint(PathPoint.MOVE, x, y));
    }

    // 三阶贝塞尔曲线
    public void cubicTo(float c0x, float c0y, float c1x, float c1y, float x, float y) {
        pathPointList.add(new PathPoint(PathPoint.CUBIC, c0x, c0y, c1x, c1y, x, y));
    }

    public void lineTo(float x, float y) {
        pathPointList.add(new PathPoint(PathPoint.LINE, x, y));
    }

    public void startAnimation(View v, int duration) {
        this.view = v;

        /**
         * 如何让view x,y 方向都按照某个规则运动
         * objectAnimator.ofFloat(v, "translationX", 0, 20, 100);
         * objectAnimator.ofFloat(v, "translationY", 0, 100);
         * 属性动画原理：通过反射不断地调用view.setTranslationX()方法
         * 属性动画：本质是控制一个对象身上的任何属性值----------反射setTranslationX, setAlpha()
         */
        ObjectAnimator animator = ObjectAnimator.ofObject(this, "cubic", new PathEvaluator(), pathPointList.toArray());
        animator.setDuration(duration);
        animator.start();
    }

    public void setCubic(PathPoint p) {
        view.setTranslationX(p.mX);
        view.setTranslationY(p.mY);
    }
}
