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
import cn.link.imageloader.decode.BaseImageDecoder;
import cn.link.imageloader.decode.ImageDecoder;
import cn.link.imageloader.disc.DiscCacheAware;
import cn.link.imageloader.download.ImageDownloader;
import cn.link.imageloader.memory.MemoryCacheAware;

public final class ConfigOptions {
    final Context mContext;
    final MemoryCacheAware<String, Bitmap> mMemoryCache;
    final DiscCacheAware mDiscCache;
    final DiscCacheAware mReserveDiscCache;
    final ImageDownloader mDownloader;
    final ImageDecoder mDecoder;
    final DisplayOptions mDefaultDisplayImageOptions;
    final boolean isloggingEnabled;


    private ConfigOptions(final Builder builder) {
        mContext = builder.mContent;
        mDiscCache = builder.mDiscCache;
        mMemoryCache = builder.mMemoryCache;
        mDownloader = builder.mDownloader;
        mDecoder = builder.mDecoder;
        mDefaultDisplayImageOptions = builder.mDefaultDisplayImageOptions;
        mReserveDiscCache = DefaultConfigurationFactory.createReserveDiscCache(mContext);
        isloggingEnabled = builder.isLoggingEnabled;
    }

    public static ConfigOptions createDefault(Context context) {
        return new Builder(context).build();
    }

    public static class Builder {
        private Context mContent;
        private MemoryCacheAware<String, Bitmap> mMemoryCache;
        private DiscCacheAware mDiscCache;
        private FileNameGenerator mDiscCacheFileNameGenerator;
        private ImageDownloader mDownloader;
        private DisplayOptions mDefaultDisplayImageOptions;

        private boolean isLoggingEnabled = false;
        public ImageDecoder mDecoder;

        public Builder(Context context) {
            this.mContent = context;
        }

        public Builder discCacheFileNameGenerator(FileNameGenerator fileNameGenerator) {
            this.mDiscCacheFileNameGenerator = fileNameGenerator;
            return this;
        }

        public Builder imageDownloader(ImageDownloader imageDownloader) {
            this.mDownloader = imageDownloader;
            return this;
        }


        public Builder discCache(DiscCacheAware discCache) {
            this.mDiscCache = discCache;
            return this;
        }

        public Builder decoder(ImageDecoder decoder) {
            this.mDecoder = decoder;
            return this;
        }


        public Builder defaultDisplayImageOptions(DisplayOptions defaultDisplayImageOptions) {
            this.mDefaultDisplayImageOptions = defaultDisplayImageOptions;
            return this;
        }

        public Builder enableLogging() {
            this.isLoggingEnabled = true;
            return this;
        }

        public ConfigOptions build() {
            initEmptyFiledsWithDefaultValues();
            return new ConfigOptions(this);
        }

        private void initEmptyFiledsWithDefaultValues() {
            if (mDiscCache == null) {
                if (mDiscCacheFileNameGenerator == null) {
                    mDiscCacheFileNameGenerator = DefaultConfigurationFactory.createFileNameGenerator();
                }
                mDiscCache = DefaultConfigurationFactory.createDiscCache(mContent, "cache");
            }
            if (mMemoryCache == null) {
                mMemoryCache = DefaultConfigurationFactory.createMemoryCache(8 * 1024 * 1024);
            }
            if (mDownloader == null) {
                mDownloader = DefaultConfigurationFactory.createImageDownloader(mContent);
            }
            if (mDefaultDisplayImageOptions == null) {
                mDefaultDisplayImageOptions = DisplayOptions.createSimple();
            }

            if (mDecoder == null) {
                mDecoder = new BaseImageDecoder();
            }
        }
    }
}
