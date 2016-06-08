package org.gtq.vhubs.core;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import org.gtq.vhubs.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.SoftReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import support.db.DatabaseManager;
import support.db.IMModule;
import support.db.PublicDatabaseManager;
import support.ui.image.GImageLoader;

/**
 * Created by guotengqian on 2016/5/31.
 */
public class VApplication extends Application {

    public static VApplication mInstance;

    private static SparseArray<SoftReference<Bitmap>> mapResIdToBitmap = new SparseArray<SoftReference<Bitmap>>();

    protected Class<? extends DatabaseManager> mPublicDatabaseManagerClass;

    protected static int bmpMaxWidth;
    protected static int bmpMaxHeight;
    private static int sScreenWidth;
    private static int sScreenHeight;
    private static int sScreenDpi;

    public static VApplication getInstance() {
        return mInstance;
    }

    private void initDB() {
        mPublicDatabaseManagerClass = PublicDatabaseManager.class;
        try {
            IMModule dm = null;
            Constructor<? extends DatabaseManager> c = mPublicDatabaseManagerClass.getDeclaredConstructor((Class[]) null);
            c.setAccessible(true);
            dm = (DatabaseManager) c.newInstance();
            dm.initial(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        sScreenWidth = dm.widthPixels;
        sScreenHeight = dm.heightPixels;
        sScreenDpi = dm.densityDpi;
        bmpMaxWidth = getScreenWidth();
        bmpMaxHeight = bmpMaxWidth;
        CrashHandler.getInstance().init(this);
        //非捕捉异常
        initDB();
    }

    public static void setBitmapEx(String url, GImageLoader.ImageLoadingListener loadingListener) {
        GImageLoader.loadBitmap(getInstance(), url, loadingListener);
    }

    public static int getScreenWidth() {
        return sScreenWidth;
    }

    public static int getScreenHeight() {
        return sScreenHeight;
    }

    public static int getScreenDpi() {
        return sScreenDpi;
    }

    public static void setBitmapEx(final View view, String url) {
        setBitmapEx(view, url, R.mipmap.loading);
    }

    /**
     * can use anywhere
     */
    public static void setBitmapEx(final View view, String url, int defaultResId) {


        if (TextUtils.isEmpty(url) && defaultResId != 0) {
            if (view instanceof ImageView) {
                final ImageView iv = (ImageView) view;
                iv.setImageResource(defaultResId);
            } else {
                view.setBackgroundResource(defaultResId);
            }
        } else {
            Bitmap bmpLoading = getResBitmap(defaultResId);
            Bitmap bmpFail = bmpLoading;
            //加载图片
            GImageLoader.loadBitmap(getInstance(), (ImageView) view, url, bmpMaxWidth, bmpMaxHeight, bmpLoading,
                    bmpFail, null);

        }
    }

    public static void setBitmapEx(final View view, String url, int bmpWidth, int bmpHeight, int defaultResId) {
        if (TextUtils.isEmpty(url) && defaultResId != 0) {
            if (view instanceof ImageView) {
                final ImageView iv = (ImageView) view;
                iv.setImageResource(defaultResId);
            } else {
                view.setBackgroundResource(defaultResId);
            }
        } else {
            Bitmap bmpLoading = getResBitmap(defaultResId);
            Bitmap bmpFail = bmpLoading;
            //加载图片
            GImageLoader.loadBitmap(getInstance(), (ImageView) view, url, bmpWidth, bmpHeight, bmpLoading, bmpFail,
                    null);
        }
    }

    protected static Bitmap getResBitmap(int resId) {
        if (resId == 0) {
            return null;
        }
        Bitmap bmp;
        SoftReference<Bitmap> sf = mapResIdToBitmap.get(resId);
        bmp = sf == null ? null : sf.get();
        if (bmp == null) {
            try {
                final Drawable d = mInstance.getResources().getDrawable(resId);
                if (d instanceof BitmapDrawable) {
                    bmp = ((BitmapDrawable) d).getBitmap();
                }
                if (bmp != null) {
                    mapResIdToBitmap.put(resId, new SoftReference<Bitmap>(bmp));
                }
            } catch (OutOfMemoryError e) {


            }
        }
        return bmp;
    }

    public static void toast(String text) {
        Toast.makeText(getInstance(), text, Toast.LENGTH_SHORT).show();
    }

    public static void toastJsonError(JSONObject jo) {
        try {
            toast(jo.getString("errMsg"));
        } catch (JSONException e) {
            toast(getInstance().getString(R.string.net_fail));
        }
    }

}
