package com.lybgo.circularfloatingmenu;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * 圆形弹出菜单
 *
 * @author 刘远彬
 * @date 2015/8/19
 */
public class CircularFloatingMenu extends RelativeLayout {
    private static final String TAG = "CircularFloatingMenu";
    int mItemCount = 0;
    private OnItemClickListener mOnItemClickListener;
    boolean mIsOpen = false;
    int mRadius;// 弹出Item的半径
    int mDegrees;// Items的扇形大小(角度)
    int mStartDegree;// item从哪个位置开始排列(角度)
    boolean mIsItemClickClose;// item点击时是否缩回去
    View mVMenu;
    float mPerDegree;

    public CircularFloatingMenu(Context context) {
        super(context);
    }

    public CircularFloatingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
    }

    public CircularFloatingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircularFloatingMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttr(context, attrs);
    }

    /**
     * 设置菜单及菜单项的点击事件监听
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    /**
     * 设置菜单项移动动画事件监听，用于用户自定义移动动画
     * @param listener
     */
    public void setOnItemTranslationListener(OnItemTranslationListener listener) {
        mOnItemTranslationListener = listener;
    }

    /**
     * 设置菜单项的半径
     * @param radius
     */
    public void setRadius(int radius) {
        mRadius = radius;
    }

    /**
     * 设置菜单项扇形大小(角度)
     * @param degrees
     */
    public void setDegrees(int degrees) {
        mDegrees = degrees;
        mPerDegree = mDegrees * 1.0f / (mItemCount - 1);
    }

    /**
     * 设置菜单项开始排列的位置(角度)
     * @param degrees
     */
    public void setStartDegrees(int degrees) {
        mStartDegree = degrees;
    }

    /**
     * 设置菜单项点击时是否缩回去
     * @param isItemClickClose
     */
    public void setIsItemClickClose(boolean isItemClickClose) {
        mIsItemClickClose = isItemClickClose;
    }


    //------------------------------------------------------//
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        mInited = false;
        init();
    }

    @Override
    public void addView(View child, int index) {
        super.addView(child, index);
        mInited = false;
        init();
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        mInited = false;
        init();
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        mInited = false;
        init();
    }

    @Override
    public void addView(View child, int width, int height) {
        super.addView(child, width, height);
        mInited = false;
        init();
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
        mInited = false;
        init();
    }

    @Override
    public void removeViewAt(int index) {
        super.removeViewAt(index);
        mInited = false;
        init();
    }

    @Override
    public void removeViews(int start, int count) {
        super.removeViews(start, count);
        mInited = false;
        init();
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircularFloatingMenu);
        mRadius = typedArray.getDimensionPixelSize(R.styleable.CircularFloatingMenu_radius, -1);
        if (mRadius == -1) {
            mRadius = getResources().getDimensionPixelSize(R.dimen.CFMDefaultRadius);
        }
        mStartDegree = typedArray.getInteger(R.styleable.CircularFloatingMenu_startDegrees, -1);
        if (mStartDegree == -1) {
            mStartDegree = getResources().getInteger(R.integer.CFMDefaultStartDegrees);
        }
        mDegrees = typedArray.getInteger(R.styleable.CircularFloatingMenu_degrees, -1);
        if (mDegrees == -1) {
            mDegrees = getResources().getInteger(R.integer.CFMDefaultDegrees);
        }
        mIsItemClickClose = typedArray.getBoolean(R.styleable.CircularFloatingMenu_isCloseWhenItemClick, true);
        typedArray.recycle();
    }

    boolean mInited = false;

    private void init() {
        if (mInited) {
            return;
        }
        mInited = true;
        mItemCount = getChildCount() - 1;
        Log.w(TAG, "init itemCount:" + mItemCount);
        if (mItemCount < 2) {
            return;
        }
        for (int i = 0; i < mItemCount; i++) {
            View item = getChildAt(i);
            item.setTag(i);
            item.setOnClickListener(mClickListener);
        }
        mPerDegree = mDegrees * 1.0f / (mItemCount - 1);
        mVMenu = getChildAt(mItemCount);
        mVMenu.setOnClickListener(mMenuClickListener);
    }


    OnClickListener mMenuClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!mIsOpen) {
                for (int i = 0; i < mItemCount; i++) {
                    int x = (int) (mRadius * Math.cos((mStartDegree + mPerDegree * i) * Math.PI / 180));
                    int y = (int) (mRadius * Math.sin((mStartDegree + mPerDegree * i) * Math.PI / 180));
                    View item = getChildAt(i);
                    if (mOnItemTranslationListener == null) {
                        ViewHelper.setTranslationX(item, x);
                        ViewHelper.setTranslationY(item, y);
                    } else {
                        mOnItemTranslationListener.translationItem(item, x, y, true);
                    }
                }
            } else {
                for (int i = 0; i < mItemCount; i++) {
                    View item = getChildAt(i);
                    if (mOnItemTranslationListener == null) {
                        ViewHelper.setTranslationX(getChildAt(i), 0);
                        ViewHelper.setTranslationY(getChildAt(i), 0);
                    } else {
                        mOnItemTranslationListener.translationItem(item, 0, 0, false);
                    }
                }
            }
            mIsOpen = !mIsOpen;
            if (mOnItemClickListener == null) {
                return;
            }
            mOnItemClickListener.onMenuClick(v, mIsOpen);
        }
    };


    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
            }
            if (mIsItemClickClose) {
                mMenuClickListener.onClick(mVMenu);
            }
        }
    };
    private OnItemTranslationListener mOnItemTranslationListener = new OnItemTranslationListener() {
        Interpolator outInterpolator = new OvershootInterpolator();
        Interpolator inInterpolator = new AccelerateDecelerateInterpolator();
        float defaultRotation = -180;
        float defaultAlpha = 0f;

        @Override
        public void translationItem(View v, int x, int y, boolean isOpen) {
            if (isOpen) {
                ViewHelper.setRotation(v, defaultRotation);
                ViewHelper.setAlpha(v, defaultAlpha);
            }
            Interpolator interpolator = isOpen ? outInterpolator : inInterpolator;
            float toRotation = isOpen ? 0 : defaultRotation;
            float toAlpha = isOpen ? 1 : defaultAlpha;

            ViewPropertyAnimator.animate(v).translationX(x).translationY(y).rotation(toRotation).alpha(toAlpha)
                    .setInterpolator(interpolator).setDuration(500).start();
        }
    };

    public interface OnItemClickListener {
        void onItemClick(View view, int index);
        void onMenuClick(View view, boolean isOpen);
    }

    public interface OnItemTranslationListener {
        void translationItem(View v, int x, int y, boolean isOpen);
    }
}
