package cn.link.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageButton;

import java.io.IOException;
import java.io.InputStream;

public class LKImageView extends ImageButton {
    private static ImageLoaderEngine mEngine;
    private DisplayOptions mDisplayOptions;

    public LKImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LKImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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

}
