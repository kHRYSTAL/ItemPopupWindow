package me.khrystal.library;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 16/8/23
 * update time:
 * email: 723526676@qq.com
 */
public class ItemPopupWindow extends PopupWindowContainer {

    private ImageView mArrowUp;
    private ImageView mArrowDown;
    private Animation mShowAnim;
    private LayoutInflater inflater;
    private ViewGroup mTrack;
    private PopupWindow.OnDismissListener mDismissListener;
    private OnActionItemClickListener mItemClickListener;

    private List<ItemAction> mActionItemList = new ArrayList<ItemAction>();

    private boolean mDidAction;
    private boolean mAnimateTrack;

    private int mChildPos;
    private int mAnimStyle;

    public static final int ANIM_GROW_FROM_LEFT = 1;
    public static final int ANIM_GROW_FROM_RIGHT = 2;
    public static final int ANIM_GROW_FROM_CENTER = 3;
    public static final int ANIM_AUTO = 4;

    public ItemPopupWindow(Context context) {
        super(context);

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mShowAnim = AnimationUtils.loadAnimation(context, R.anim.rail);
        mShowAnim.setInterpolator(new Interpolator() {
            @Override
            public float getInterpolation(float input) {
                final float inner = (input * 1.55f) - 1.1f;
                return 1.2f - inner * inner;
            }
        });

        setRootViewId(R.layout.item_pop_layout);

        mAnimStyle = ANIM_AUTO;
        mAnimateTrack = true;
        mChildPos = 0;
    }

    @SuppressWarnings("deprecation")
    public void setRootViewId(int id) {
        mRootView = (ViewGroup) inflater.inflate(id, null);
        mTrack = (ViewGroup) mRootView.findViewById(R.id.tracks);

        mArrowDown = (ImageView) mRootView.findViewById(R.id.arrow_down);
        mArrowUp = (ImageView) mRootView.findViewById(R.id.arrow_up);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
            mPopupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        } else {
            mPopupWindow.setWidth(WindowManager.LayoutParams.FILL_PARENT);
        }
        setContentView(mRootView);
    }

    public void setAnimateTrack(boolean mAnimateTrack) {
        this.mAnimateTrack = mAnimateTrack;
    }

    public void setAnimStyle(int mAnimStyle) {
        this.mAnimStyle = mAnimStyle;
    }

    public void addActionItem(int id, String title) {
        addActionItem(new ItemAction(id, title));
    }

    public void addActionItem(ItemAction action) {
        mActionItemList.add(action);

        String title = action.getTitle();
        View container = (View) inflater.inflate(R.layout.action_item, null);

        TextView text = (TextView) container.findViewById(R.id.tv_title);

        if (title != null) {
            text.setText(title);
            text.setSelected(true);
        } else {
            text.setVisibility(View.GONE);
        }

        final int pos = mChildPos;
        final int actionId = action.getActionId();

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(ItemPopupWindow.this, pos,
                            actionId);
                }

                if (!getActionItem(pos).isSticky()) {
                    mDidAction = true;
                    v.post(new Runnable() {
                        @Override
                        public void run() {
                            dismiss();
                        }
                    });
                }
            }
        });

        container.setFocusable(true);
        container.setClickable(true);

        mTrack.addView(container, mChildPos);

        mChildPos++;
    }

    public ItemAction getActionItem(int index) {
        return mActionItemList.get(index);
    }


    /**
     * Show popup mWindow
     */
    public void show(View anchor) {
        preShow();

        int[] location = new int[2];

        mDidAction = false;

        anchor.getLocationOnScreen(location);

        Rect anchorRect = new Rect(location[0], location[1], location[0]
                + anchor.getWidth(), location[1] + anchor.getHeight());

        mRootView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        int rootWidth = mRootView.getMeasuredWidth();
        int rootHeight = mRootView.getMeasuredHeight();

        int screenWidth = mWindowManager.getDefaultDisplay().getWidth();

        int xPos = (screenWidth - rootWidth) / 2;
        int yPos = anchorRect.top - rootHeight;

        boolean onTop = true;

        // display on bottom
        if (rootHeight > anchor.getTop()) {
            yPos = anchorRect.bottom;
            onTop = false;
        }

        showArrow(((onTop) ? R.id.arrow_down : R.id.arrow_up),
                anchorRect.centerX());

        setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);

        mPopupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);

        if (mAnimateTrack)
            mTrack.startAnimation(mShowAnim);
    }

    public void setOnActionItemClickListener(OnActionItemClickListener listener) {
        mItemClickListener = listener;
    }

    /**
     * Set animation style
     *
     * @param screenWidth Screen width
     * @param requestedX distance from left screen
     * @param onTop
     *
     */
    private void setAnimationStyle(int screenWidth, int requestedX,
                                   boolean onTop) {
        int arrowPos = requestedX - mArrowUp.getMeasuredWidth() / 2;

        switch (mAnimStyle) {
            case ANIM_GROW_FROM_LEFT:
                mPopupWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Left
                        : R.style.Animations_PopDownMenu_Left);
                break;

            case ANIM_GROW_FROM_RIGHT:
                mPopupWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Right
                        : R.style.Animations_PopDownMenu_Right);
                break;

            case ANIM_GROW_FROM_CENTER:
                mPopupWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center
                        : R.style.Animations_PopDownMenu_Center);
                break;

            case ANIM_AUTO:
                if (arrowPos <= screenWidth / 4) {
                    mPopupWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Left
                            : R.style.Animations_PopDownMenu_Left);
                } else if (arrowPos > screenWidth / 4
                        && arrowPos < 3 * (screenWidth / 4)) {
                    mPopupWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center
                            : R.style.Animations_PopDownMenu_Center);
                } else {
                    mPopupWindow.setAnimationStyle((onTop) ? R.style.Animations_PopDownMenu_Right
                            : R.style.Animations_PopDownMenu_Right);
                }

                break;
        }
    }

    /**
     * Show arrow
     *
     * @param whichArrow
     *            arrow type resource id
     * @param requestedX
     *            distance from left screen
     */
    private void showArrow(int whichArrow,final int requestedX) {
        final View showArrow = (whichArrow == R.id.arrow_up) ? mArrowUp
                : mArrowDown;
        final View hideArrow = (whichArrow == R.id.arrow_up) ? mArrowDown
                : mArrowUp;

        final int arrowWidth = mArrowUp.getMeasuredWidth();

        showArrow.setVisibility(View.VISIBLE);

        ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) showArrow
                .getLayoutParams();

        param.leftMargin = requestedX - arrowWidth / 2;
//
//        final int trackWidth = mTrack.getWidth();
//
//        ViewGroup.MarginLayoutParams tracksParam = (ViewGroup.MarginLayoutParams) mTrack
//                .getLayoutParams();
//
//        tracksParam.leftMargin = requestedX - trackWidth / 2;
        mTrack.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final int trackWidth = mTrack.getWidth();
                ViewGroup.MarginLayoutParams tracksParam = (ViewGroup.MarginLayoutParams) mTrack.getLayoutParams();
                tracksParam.leftMargin = requestedX - trackWidth / 2;
            }
        });
//
        hideArrow.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDismiss() {
        if (!mDidAction && mDismissListener != null) {
            mDismissListener.onDismiss();
        }
    }

    public interface OnActionItemClickListener {
        void onItemClick(ItemPopupWindow source, int pos, int actionId);
    }
}
