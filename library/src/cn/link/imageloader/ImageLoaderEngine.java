package cn.link.imageloader;

import android.graphics.Bitmap;
import android.widget.ImageView;
import cn.link.imageloader.assist.ImageSize;
import cn.link.imageloader.assist.ImageSizeUtils;
import cn.link.imageloader.assist.ViewScaleType;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Link
 * Date: 13-5-9
 * Time: PM3:19
 * To change this template use File | Settings | File Templates.
 */
public class ImageLoaderEngine {

    public static ConfigOptions sConfigOptions;

    public static void init(ConfigOptions options) {
        sConfigOptions = options;
    }

    public static void display(ImageView imageView, DisplayOptions mDisplayOptions) {
        Bitmap bitmap = null;
        if (mDisplayOptions.isDispalyIfInMemory()) {
            bitmap = sConfigOptions.mMemoryCache.get(mDisplayOptions.getDisplayUrl());
            mDisplayOptions.getDisplayer().display(bitmap, imageView);
        } else {
            new ImageDisplayTask(imageView, mDisplayOptions).execute();
        }
    }

    private static class ImageDisplayTask extends SimpleAsyncTask<Void, Void, Bitmap> {

        ImageView mImageView;
        DisplayOptions mDisplayOptions;

        public ImageDisplayTask(ImageView imageView, DisplayOptions mDisplayOptions) {
            this.mImageView = imageView;
            this.mDisplayOptions = mDisplayOptions;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mImageView.setImageResource(mDisplayOptions.getResetImage());
        }

        @Override
        protected Bitmap doInBackground(Void... params) {

            mDisplayOptions.mTargetSize = ImageSizeUtils.defineTargetSizeForView(mImageView, 800, 800);
            mDisplayOptions.mViewScalType = ViewScaleType.fromImageView(mImageView);
            Bitmap bitmap = null;
            try {
                bitmap = tryGetBitmap(mDisplayOptions);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            return bitmap;  //ToDo
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                mDisplayOptions.getDisplayer().display(bitmap, mImageView);
            }
        }
    }

    public static Bitmap tryGetBitmap(DisplayOptions options) throws IOException {
        Bitmap bitmap = sConfigOptions.mMemoryCache.get(options.getDisplayUrl());
        if (bitmap != null) {
            return bitmap;
        }
        bitmap = sConfigOptions.mDiscCache.read(options.getDisplayUrl(), options, sConfigOptions.mDecoder);
        if (bitmap != null) {
            return bitmap;
        }
        InputStream inputStream = sConfigOptions.mDownloader.getStream(options.getDisplayUrl(), null);

        bitmap = sConfigOptions.mDiscCache.decodeAndWrite(inputStream, options, sConfigOptions.mDecoder);
        if (bitmap != null) {
            sConfigOptions.mMemoryCache.put(options.getDisplayUrl(), bitmap);
            return bitmap;
        }
        return bitmap;  //ToDo
    }
}
