package com.cyh.ioclibrary.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class RViewAdapter<T> extends RecyclerView.Adapter<RViewHolder> {
    private RView.OnItemClickListener<T> mOnItemClickListener;
    private RView.OnItemLongClickListener<T> mOnItemLongClickListener;
    private List<T> datas;

    public RViewAdapter(List<T> datas) {
        this.datas = datas;
    }

    void setmOnItemClickListener(RView.OnItemClickListener<T> mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    void setmOnItemLongClickListener(RView.OnItemLongClickListener<T> mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public abstract int getLayoutId();
    public abstract void convert(RViewHolder holder, T t);

    @NonNull
    @Override
    public RViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int layoutId = getLayoutId();
        RViewHolder holder = RViewHolder.createViewHolder(viewGroup.getContext(), viewGroup, layoutId);
        setListener(holder);
        return holder;
    }

    private void setListener(final RViewHolder holder) {
        if (holder == null) {
            return;
        }
        holder.getmConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = holder.getAdapterPosition();
                    if (position != -1) {
                        mOnItemClickListener.onItemClick(v, datas.get(position), position);
                    }
                }
            }
        });

        holder.getmConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListener != null) {
                    int position = holder.getAdapterPosition();
                    return mOnItemLongClickListener.onItemLongClick(v, datas.get(position), position);
                }
                return false;
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull RViewHolder holder, int i) {
        convert(holder, datas.get(i));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
}
