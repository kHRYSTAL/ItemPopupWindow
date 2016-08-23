package me.khrystal.itempopupwindow;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 16/8/23
 * update time:
 * email: 723526676@qq.com
 */
public class SimpleViewHolder extends BaseViewHolder{

    public TextView mItemText;

    private Context mContext;
    private String mString;

    public SimpleViewHolder(View itemView, Object parent) {
        super(itemView, parent);
        mContext = itemView.getContext();
        mItemText = (TextView)itemView.findViewById(R.id.item_text);
    }

    public void bind(String item){
        if (TextUtils.isEmpty(item))
            return;
        mString = item;
        mItemText.setText(item);
    }
}
