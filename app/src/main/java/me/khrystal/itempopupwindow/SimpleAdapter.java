package me.khrystal.itempopupwindow;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 16/8/23
 * update time:
 * email: 723526676@qq.com
 */
public class SimpleAdapter extends RecyclerView.Adapter<SimpleViewHolder> {

    List<String> dataList;
    Object parent;


    public SimpleAdapter(Object parent, List<String> dataList) {
        this.dataList = dataList;
        this.parent = parent;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        final SimpleViewHolder holder = new SimpleViewHolder(v, parent);
        // TODO: 16/4/28
        if (mClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(dataList.get(holder.getLayoutPosition()),v,holder.getLayoutPosition());
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        String item = dataList.get(position);
        holder.bind(item);
    }

    public void append(String[] items) {
        int pos = dataList.size();
        for (String item : items) {
            if (!dataList.contains(item))
                dataList.add(item);
        }
        notifyItemRangeInserted(pos, items.length);
    }

    public void append(List<String> items){
        int pos = dataList.size();
        for (String item : items){
            if (!dataList.contains(item))
                dataList.add(item);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void remove(int position) {
        if (dataList.size() > 0) {
            dataList.remove(position);
            this.notifyItemRemoved(position);
        }
    }

    public void clear() {
        int size = dataList.size();
        dataList.clear();
        this.notifyItemRangeRemoved(0, size);
    }

    // TODO: 16/4/28
    protected OnItemClickListener mClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(T data,View view, int position);
        void onItemLongClick(T data, View view, int position);
    }
}
