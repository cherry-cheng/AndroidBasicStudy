package com.cyh.ioclibrary.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class RView extends RecyclerView {

    private RViewAdapter adapter;

    public RView(@NonNull Context context) {
        super(context);
    }

    public RView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setRViewAdapter(RViewAdapter adapter) {
        this.adapter = adapter;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(View view, T entity, int position);
    }

    public interface OnItemLongClickListener<T> {
        boolean onItemLongClick(View view, T entity, int position);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        adapter.setmOnItemClickListener(onItemClickListener);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        adapter.setmOnItemLongClickListener(onItemLongClickListener);
    }




}
