package me.khrystal.itempopupwindow;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 16/8/23
 * update time:
 * email: 723526676@qq.com
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder{
    protected Activity mActivity;
    protected Fragment mFragment;
    public View mView;

    public BaseViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public BaseViewHolder(View itemView, Object parent) {
        super(itemView);
        if (parent == null)
            return;
        if (parent instanceof Activity) {
            mActivity = (Activity) parent;
        } else if (parent instanceof Fragment) {
            mFragment = (Fragment) parent;
        }
    }

}
