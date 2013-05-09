package cn.link.imageloader.disc;

import android.graphics.Bitmap;
import cn.link.imageloader.DisplayOptions;
import cn.link.imageloader.decode.ImageDecoder;

import java.io.IOException;
import java.io.InputStream;

public interface DiscCacheAware {
    Bitmap read(String key, DisplayOptions options, ImageDecoder decoder) throws IOException;

    Bitmap decodeAndWrite(InputStream input, DisplayOptions options, ImageDecoder decoder)
            throws IOException;

    void clear();
}
