package support.ui.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import java.io.File;

import support.utils.FilePaths;

/**
 * Created by guotengqian on 2015/12/28.
 */
public class UnverisalImageLoader extends ImageLoaderProvider {
    Bitmap cache;
    String cacheUrl;

    public UnverisalImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .diskCacheExtraOptions(480, 800, null)
                .threadPoolSize(3) // default
                .threadPriority(Thread.NORM_PRIORITY - 1) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default

                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCache(new UnlimitedDiskCache(new File(FilePaths.getCameraSaveFilePath()).getParentFile())) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(context)) // default
                .imageDecoder(new BaseImageDecoder(true)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs()

                .build();
        ImageLoader.getInstance().init(config);


    }

    @Override
    public void loadBitmap(Context context, ImageView view, String url, int bmpWidth, int bmpHeight, Bitmap bmpLoading, Bitmap bmpFail, final GImageLoader.ImageLoadingListener listener) {
        ImageSize size = null;
        if (bmpWidth != 0 && bmpHeight != 0) {
            size = new ImageSize(bmpWidth, bmpHeight);
        }

        //显示图片的配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()

                .showImageOnLoading(new BitmapDrawable(bmpLoading))
                .showImageOnFail(new BitmapDrawable(bmpFail))
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();


        if (listener != null) {

            ImageLoader.getInstance().displayImage(url, view, options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                    listener.onLoadingStarted(s, view);
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    listener.onLoadingFailed(s, view, failReason);
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    cacheUrl = s;
                    cache = bitmap;
                    listener.onLoadingComplete(s, view, bitmap);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                    if (s.equals(cacheUrl) && cache != null) {
                        listener.onLoadingComplete(s, view, cache);
                    }
                    listener.onLoadingCancelled(s, view);
                }
            }, new ImageLoadingProgressListener() {
                @Override
                public void onProgressUpdate(String s, View view, int current, int total) {
                    listener.onProgressUpdate(s, view, current, total);
                }
            });
        } else {
            ImageLoader.getInstance().displayImage(url, view, options);
        }
    }

    @Override
    public void loadBitmap(Context context, String url, final GImageLoader.ImageLoadingListener listener) {

        //显示图片的配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()


                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().loadImage(url, options, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String s, View view) {
                listener.onLoadingStarted(s, view);
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                listener.onLoadingFailed(s, view, failReason);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                cacheUrl = imageUri;
                cache = loadedImage;
                listener.onLoadingComplete(imageUri, view, loadedImage);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
                if (s.equals(cacheUrl) && cache != null) {
                    listener.onLoadingComplete(s, view, cache);
                }
                listener.onLoadingCancelled(s, view);
            }
        });
    }
}
