package cn.link.imageloader;

import android.content.Context;
import android.graphics.ColorMatrixColorFilter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;
import cn.link.imageloader.display.OnLoadingListener;
import cn.link.imageloader.display.BitmapProgressListener;

public class LKImageView extends ImageButton {
    private static ImageLoaderEngine mEngine;
    private DisplayOptions mDisplayOptions;

    private static final float Trans = -25f;

    //按下这个按钮进行的颜色过滤
    private final static float[] BT_SELECTED = new float[]{
            1, 0, 0, 0, Trans,
            0, 1, 0, 0, Trans,
            0, 0, 1, 0, Trans,
            0, 0, 0, 1, 0};
    //按钮恢复原状的颜色过滤
    private final static float[] BT_NOT_SELECTED = new float[]{
            1, 0, 0, 0, 0,
            0, 1, 0, 0, 0,
            0, 0, 1, 0, 0,
            0, 0, 0, 1, 0};
    private ColorMatrixColorFilter mPressFilter;
    private ColorMatrixColorFilter mNormalFilter;


    public LKImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundResource(R.drawable.transparent);
    }

    public LKImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setBackgroundResource(R.drawable.transparent);
    }

    public void setDisplayOptions(DisplayOptions option) {
        this.mDisplayOptions = option;
    }

    public void display(DisplayOptions options) {
        this.mDisplayOptions = options;
        display();
    }

    public void display() {
        mEngine.display(this, mDisplayOptions);
    }

    public void display(BitmapProgressListener listener) {
        this.mDisplayOptions.setProgressListener(listener);
        mEngine.display(this, mDisplayOptions);
    }

    public void display(OnLoadingListener listener) {
        this.mDisplayOptions.setOnLoadingListener(listener);
        mEngine.display(this, mDisplayOptions);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDisplayOptions.isShowPressEffect()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (getDrawable() != null) {
                        if (mPressFilter == null) {
                            mPressFilter = new ColorMatrixColorFilter(BT_SELECTED);
                        }
                        getDrawable().setColorFilter(mPressFilter);
                    }
                    break;

                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (getDrawable() != null) {
                        if (mNormalFilter == null) {
                            mNormalFilter = new ColorMatrixColorFilter(BT_NOT_SELECTED);
                        }
                        getDrawable().setColorFilter(mNormalFilter);
                    }

                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

}
