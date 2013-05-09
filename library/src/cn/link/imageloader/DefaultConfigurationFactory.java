/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package cn.link.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import cn.link.imageloader.assist.FileNameGenerator;
import cn.link.imageloader.assist.HashCodeFileNameGenerator;
import cn.link.imageloader.assist.StorageUtils;
import cn.link.imageloader.disc.BaseDiscCache;
import cn.link.imageloader.disc.DiscCacheAware;
import cn.link.imageloader.memory.LruCacheImpl;
import cn.link.imageloader.memory.MemoryCacheAware;
import cn.link.imageloader.display.BitmapDisplayer;
import cn.link.imageloader.display.SimpleBitmapDisplayer;
import cn.link.imageloader.download.BaseImageDownloader;
import cn.link.imageloader.download.ImageDownloader;

import java.io.File;

public class DefaultConfigurationFactory {


    public static FileNameGenerator createFileNameGenerator() {
        return new HashCodeFileNameGenerator();
    }

    public static DiscCacheAware createDiscCache(Context context, String cacheDirName) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, cacheDirName);
        return new BaseDiscCache(cacheDir);
    }

    public static DiscCacheAware createReserveDiscCache(Context context) {
        File cacheDir = context.getCacheDir();
        File individualDir = new File(cacheDir, "uil-images");
        if (individualDir.exists() || individualDir.mkdir()) {
            cacheDir = individualDir;
        }
        return new BaseDiscCache(cacheDir);
    }

    public static MemoryCacheAware<String, Bitmap> createMemoryCache(int memoryCacheSize) {
        if (memoryCacheSize == 0) {
            memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        }
        MemoryCacheAware<String, Bitmap> memoryCache;
        memoryCache = new LruCacheImpl(memoryCacheSize);
        return memoryCache;
    }

    public static ImageDownloader createImageDownloader(Context context) {
        return new BaseImageDownloader(context);
    }


    public static BitmapDisplayer createBitmapDisplayer() {
        return new SimpleBitmapDisplayer();
    }

}
