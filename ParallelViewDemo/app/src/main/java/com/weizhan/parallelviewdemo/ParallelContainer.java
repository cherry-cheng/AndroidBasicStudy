package com.weizhan.parallelviewdemo;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/4/17.
 */

public class ParallelContainer extends FrameLayout implements ViewPager.OnPageChangeListener{

    private ImageView iv_man;

    public void setIv_man(ImageView iv_man) {
        this.iv_man = iv_man;
    }

    private List<ParallelFragment> fragments;
    private ParallelPagerAdapter adapter;

    public ParallelContainer(@NonNull Context context) {
        super(context);
    }

    public ParallelContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ParallelContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setUp(int... childIds) {
        //根据布局文件数组，初始化所有的fragment
        fragments = new ArrayList<ParallelFragment>();
        for (int i=0; i < childIds.length; i++) {
            ParallelFragment f = new ParallelFragment();
            Bundle args = new Bundle();
            //Fragment中需要加载的布局文件id
            args.putInt("layoutId", childIds[i]);
            f.setArguments(args);
            fragments.add(f);
        }
        //实例化适配器
        MainActivity activity = (MainActivity)getContext();
        //实例化ViewPager
        ViewPager vp = new ViewPager(getContext());
        vp.setId(R.id.parallax_pager);
        adapter = new ParallelPagerAdapter(activity.getSupportFragmentManager(), fragments);
        vp.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        //绑定
        vp.setAdapter(adapter);
        addView(vp, 0);
        vp.setOnPageChangeListener(this);
    }

    //根据用户滑动的距离  去显示动画
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //position接下来的位置
        ParallelFragment inFragment = null;
        int containerWidth = getWidth();
        ParallelFragment outFragment = null;

        try {
            outFragment = fragments.get(position - 1);
        } catch (Exception e) {}
        try {
            inFragment = fragments.get(position);
        } catch (Exception e) {}
        if (outFragment != null) {
            List<View> outViews = outFragment.getParallelViews();
            if (outViews != null) {
                for (View view : outViews) {
                    //获取标签， 从标签上获取所有的动画参数
                    ParallelViewTag tag = (ParallelViewTag) view.getTag(R.id.parallax_view_tag);
                    if (tag == null) {
                        continue;
                    }

                    ViewHelper.setTranslationX(view, containerWidth - positionOffsetPixels * tag.xIn);
                    ViewHelper.setTranslationY(view, containerWidth - positionOffsetPixels * tag.yIn);
                }
            }
        }
        if (inFragment != null) {
            List<View> outViews = inFragment.getParallelViews();
            if (outViews != null) {
                for (View view : outViews) {
                    ParallelViewTag tag = (ParallelViewTag) view.getTag(R.id.parallax_view_tag);
                    if (tag == null) {
                        continue;
                    }
                    //仔细观察退出的fragment中view从原始位置开始向上移动，translationY应为负数
                    ViewHelper.setTranslationY(view, 0 - positionOffsetPixels * tag.yOut);
                    ViewHelper.setTranslationX(view, 0 - positionOffsetPixels * tag.xOut);
                }
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (position == adapter.getCount() - 1) {
            iv_man.setVisibility(INVISIBLE);
        } else {
            iv_man.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        AnimationDrawable animation = (AnimationDrawable) iv_man.getBackground();
        switch (state) {
            case ViewPager.SCROLL_STATE_DRAGGING:
                animation.start();
                break;
            case ViewPager.SCROLL_STATE_IDLE:
                animation.stop();
                break;
                default:
                    break;
        }
    }
}
