package cn.link.imageloader;

import android.graphics.Bitmap;
import android.graphics.ColorMatrixColorFilter;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import cn.link.imageloader.assist.ImageSize;
import cn.link.imageloader.assist.ImageSizeUtils;
import cn.link.imageloader.assist.ViewScaleType;

import java.io.*;
import java.net.URL;

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

        Bitmap bitmap = sConfigOptions.mMemoryCache.get(mDisplayOptions.getDisplayUrl());
        if (bitmap != null) {
            mDisplayOptions.getDisplayer().display(bitmap, imageView);
        } else {
            new ImageDisplayTask(imageView, mDisplayOptions).execute();
        }
    }

    private static class ImageDisplayTask extends SimpleAsyncTask<Void, Void, Bitmap> {

        ImageView mImageView;
        DisplayOptions mDisplayOptions;
        Exception mException;

        public ImageDisplayTask(ImageView imageView, DisplayOptions mDisplayOptions) {
            this.mImageView = imageView;
            this.mDisplayOptions = mDisplayOptions;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mDisplayOptions.getOnLoadingListener() != null) {
                mDisplayOptions.getOnLoadingListener().onStart();
            }
            mImageView.setImageResource(mDisplayOptions.getResetImage());
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            mDisplayOptions.mTargetSize = ImageSizeUtils.defineTargetSizeForView(mImageView, 1080, 1920);
            mDisplayOptions.mViewScalType = ViewScaleType.fromImageView(mImageView);
            Bitmap bitmap = null;
            try {
                bitmap = tryGetBitmap(mDisplayOptions);
            } catch (IOException e) {
                e.printStackTrace();
                mException = e;
            }
            return bitmap;  //ToDo
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (mException != null) {
                if (mDisplayOptions.getOnLoadingListener() != null) {
                    mDisplayOptions.getOnLoadingListener().onFailed(mException);
                }
                return;
            }

            if (bitmap != null && mImageView != null) {
                mDisplayOptions.getDisplayer().display(bitmap, mImageView);
            }

            if (mDisplayOptions.getOnLoadingListener() != null) {
                mDisplayOptions.getOnLoadingListener().onComplete();
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
        InputStream inputStream = sConfigOptions.mDownloader.getStream(options.getDisplayUrl(), options);

        bitmap = sConfigOptions.mDiscCache.decodeAndWrite(inputStream, options, sConfigOptions.mDecoder);
        if (bitmap != null && options.ifCacheInMemory()) {
            sConfigOptions.mMemoryCache.put(options.getDisplayUrl(), bitmap);
            return bitmap;
        }
        return bitmap;  //ToDo
    }

//    public static void saveBitmap(String url, File targetFile) throws IOException {
//
//        Bitmap bitmap = sConfigOptions.mMemoryCache.get(url);
//        if (bitmap == null) {
//            String fileName = sConfigOptions.mFileNameGenerator.generate(url);
//            File image = new File(sConfigOptions.mDiscCache.getCacheDir(), fileName);
//            if (!image.exists()) {
//                image.createNewFile();
//                BufferedOutputStream fout = new BufferedOutputStream(new FileOutputStream(image));
//                InputStream input = new URL(url).openStream();
//                byte[] buf1 = new byte[4098];
//                int len = 0;
//                while ((len = input.read(buf1)) != -1) {
//                    fout.write(buf1, 0, len);
//                }
//                fout.flush();
//                fout.close();
//
//                if (input != null) {
//                    input.close();
//                }
//            }
//        }
//    }
}
