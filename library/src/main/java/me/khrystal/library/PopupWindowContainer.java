package me.khrystal.library;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 16/8/23
 * update time:
 * email: 723526676@qq.com
 */
public class PopupWindowContainer {

    protected Context mContext;
    protected PopupWindow mPopupWindow;
    protected View mRootView;
    protected Drawable mBackground = null;
    protected WindowManager mWindowManager;

    public PopupWindowContainer(Context context) {
        mContext = context;
        mPopupWindow = new PopupWindow(context);
        mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    mPopupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });

        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    public void setContentView(View root) {
        mRootView = root;

        mPopupWindow.setContentView(root);
    }

    public void setContentView(int layoutResId) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setContentView(inflater.inflate(layoutResId, null));
    }

    public void setBackgroundDrawable(Drawable backgroundDrawable) {
        mBackground = backgroundDrawable;
    }

    protected void onDismiss() {}

    protected void onShow() {}

    @SuppressWarnings("deprecation")
    protected void preShow() {

        if (mRootView == null) {
            throw new NullPointerException(
                    "ItemPopupWindow's rootView must be not null!"
            );
        }

        onShow();

        if (mBackground == null) {
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        } else {
            mPopupWindow.setBackgroundDrawable(mBackground);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mRootView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            mRootView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setContentView(mRootView);
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener listener) {
        mPopupWindow.setOnDismissListener(listener);
    }

    public void dismiss() {
        mPopupWindow.dismiss();
    }
}
