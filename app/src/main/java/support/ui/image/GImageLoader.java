package support.ui.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.FailReason;

/**
 * 图片加载器
 * Created by guotengqian on 2015/12/28.
 */
public class GImageLoader {

    static ImageLoaderProvider mImageLoaderProvider;

    /**
     * def 加载图片
     * param
     * return
     * 2015/12/28 14:04
     */
    public static void loadBitmap(Context context, ImageView view, String url,
                                  int bmpWidth, int bmpHeight,
                                  Bitmap bmpLoading, Bitmap bmpFail,ImageLoadingListener listener) {
        if (mImageLoaderProvider == null) {
            mImageLoaderProvider = new UnverisalImageLoader(context);
        }
        mImageLoaderProvider.loadBitmap(context, view, url, bmpWidth, bmpHeight, bmpLoading, bmpFail,listener);
    }

    /**
     * def 获得bitmap对象
     * param
     * return
     * 2015/12/28 17:29
     */
    public static void loadBitmap(Context context, String url,ImageLoadingListener listener) {
        if (mImageLoaderProvider == null) {
            mImageLoaderProvider = new UnverisalImageLoader(context);
        }
         mImageLoaderProvider.loadBitmap(context, url,listener);
    }


    public static void registerProvider(ImageLoaderProvider imageLoaderProvider) {
        mImageLoaderProvider = imageLoaderProvider;
    }

//    static ImageLoaderProvider getImageLoaderProvider(){
//        if (mImageLoaderProvider==null){
//            mImageLoaderProvider=new UnverisalImageLoader();
//        }
//        return mImageLoaderProvider;
//    }

    public interface ImageLoadingListener {

        public void onLoadingStarted(String imageUri, View view);

        public void onLoadingFailed(String imageUri, View view,
                                    FailReason failReason);

        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage);

        public void onLoadingCancelled(String imageUri, View view);

        public void onProgressUpdate(String imageUri, View view, int current,
                                     int total);
    }


}
