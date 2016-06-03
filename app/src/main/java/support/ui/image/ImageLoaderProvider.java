package support.ui.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by guotengqian on 2015/12/28.
 */
public abstract class ImageLoaderProvider {

    public abstract void loadBitmap(Context context, ImageView view, String url,
                                    int bmpWidth, int bmpHeight,
                                    Bitmap bmpLoading, Bitmap bmpFail,GImageLoader.ImageLoadingListener listener);

    public abstract void loadBitmap(Context context, String url,GImageLoader.ImageLoadingListener listener);
}
