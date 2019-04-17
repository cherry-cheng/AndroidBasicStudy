package com.weizhan.parallelviewdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/4/17.
 */

public class ParallelFragment extends Fragment{
    private List<View> parallelViews = new ArrayList<View>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        int layoutId = args.getInt("layoutId");
        ParallelLayoutInflater parallelLayoutInflater = new ParallelLayoutInflater(inflater, getActivity(), this);
        return parallelLayoutInflater.inflate(layoutId, null);
    }

    public List<View> getParallelViews() {
        return parallelViews;
    }
}
