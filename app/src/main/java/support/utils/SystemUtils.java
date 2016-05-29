package support.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore.Images;
import android.provider.Settings.Secure;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;



import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StreamCorruptedException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SuppressWarnings("deprecation")
public class SystemUtils {

    private static float sDensity = 0;

    private static int sArmArchitecture = -1;

    /**
     * 没有网络
     */
    public static final int NETWORKTYPE_INVALID = 0;
    /**
     * wap网络
     */
    public static final int NETWORKTYPE_WAP = 1;
    /**
     * 2G网络
     */
    public static final int NETWORKTYPE_2G = 2;
    /**
     * 3G和3G以上网络，或统称为快速网络
     */
    public static final int NETWORKTYPE_3G = 3;
    /**
     * wifi网络
     */
    public static final int NETWORKTYPE_WIFI = 4;


    /**
     * def double转字符串
     * param 保留多少位小数
     * return
     * 2016/4/25 18:47
     */
    public static String doubleToString(double en, int num) {
        DecimalFormat formater = new DecimalFormat();
        //保留几位小数
        formater.setMaximumFractionDigits(num);
        //模式  四舍五入
        formater.setRoundingMode(RoundingMode.UP);
        return formater.format(en);
    }


    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * Convert hex string to byte[]
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 通过byte数组取到short
     *
     * @param b
     * @param index 第几位开始取
     * @return
     */
    public static short byteArrayToShort(byte[] b, int index) {
        return (short) (((b[index + 0] << 8) | b[index + 1] & 0xff));
    }

    /**
     * def 判断是否存在导航栏
     * param
     * return
     * 2016/4/14 17:23
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {

        //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
        boolean hasMenuKey = ViewConfiguration.get(context)
                .hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap
                .deviceHasKey(KeyEvent.KEYCODE_BACK);
        if (!hasMenuKey && !hasBackKey) {
            // 做任何你需要做的,这个设备有一个导航栏
            return true;
        }
        return false;
    }

    /**
     * def 获得导航栏高度
     * param
     * return
     * 2016/4/14 17:22
     */
    public static int getNavigationBarHeight(Context context) {

        int resourceId = context.getResources().getIdentifier("navigation_bar_height",
                "dimen", "android");
        //获取NavigationBar的高度
        int height = context.getResources().getDimensionPixelSize(resourceId);
        return height;
    }

    /**
     * def byte转int
     * param
     * return
     * 2016/2/25 20:23
     */
    public static int byteArrayToInt(byte[] buf) {
        if (buf.length != 4) {
            throw new RuntimeException("需要计算的长度的字节数组长度不能大于4");
        }
        int result = 0;
        int size = buf.length;
        for (int i = 0; i < size; i++) {
            byte b = buf[i];
            int temp = (b & 0xff) << (8 * (3 - i));
            result = result | temp;
        }
        return result;
    }

    /**
     * def 遍历map
     * param
     * return
     * 2016/2/3 10:14
     */
    public static <K, V> void traMap(Map<K, V> map, List<K> keyList, List<V> valueList) {
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            K key = (K) entry.getKey();
            V val = (V) entry.getValue();
            if (keyList != null) {
                keyList.add(key);
            }
            if (valueList != null) {
                valueList.add(val);
            }
        }
    }

    /**
     * def 获取视频总时长
     * param
     * return
     * 2016/1/25 19:25
     */
    public static long getVideoDuration(String path) {
        MediaPlayer mp = new MediaPlayer();
        try {
            mp.setDataSource(path);
            mp.prepare();
            return mp.getDuration();
        } catch (IOException e) {

        }
        return 0;
    }


    /**
     * def 如果输入法在窗口上已经显示，则隐藏，反之则显示
     * param
     * return
     * 2016/1/14 12:38
     */
    public static void changeKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * 图片虚化
     *
     * @param bkg
     * @param view
     * @param radius
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void blur(Context context,Bitmap bkg, View view, float radius) {
        float scaleFactor = 10;

        Bitmap overlay = Bitmap.createBitmap(
                (int) (view.getMeasuredWidth() / scaleFactor),
                (int) (view.getMeasuredHeight() / scaleFactor),
                Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop()
                / scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);
//
//        overlay = doBlur(overlay, (int) radius, true);
//        view.setBackground(new BitmapDrawable(XApplication.getApplication()
//                .getResources(), overlay));


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
//         Bitmap overlay = Bitmap.createBitmap(view.getMeasuredWidth(),
//         view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
//         Canvas canvas = new Canvas(overlay);
//         canvas.drawBitmap(bkg, -view.getLeft(), -view.getTop(), null);
            RenderScript rs = RenderScript
                    .create(context);
            Allocation overlayAlloc = Allocation.createFromBitmap(rs, overlay);
            ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs,
                    overlayAlloc.getElement());
            blur.setInput(overlayAlloc);
            blur.setRadius(radius);
            blur.forEach(overlayAlloc);
            overlayAlloc.copyTo(overlay);
            view.setBackground(new BitmapDrawable(context
                    .getResources(), overlay));
            rs.destroy();
        } else {
            try {
                overlay = doBlur(overlay, (int) radius, true);
                view.setBackground(new BitmapDrawable(context
                        .getResources(), overlay));
            } catch (Exception e) {

                view.setBackgroundResource(android.R.color.white);
            }

        }
    }

    /**
     * 4.2以下图片虚化
     *
     * @param sentBitmap
     * @param radius
     * @return
     */
    public static Bitmap doBlur(Bitmap sentBitmap, int radius,
                                boolean canReuseInBitmap) {


        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
                        | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }

    /**
     * def 缩放图片
     * param
     * return
     * 2016/1/4 12:35
     */
    public static Bitmap Zoom(Bitmap bitmap, int w, int h) {


        float wScale = (float) w / (float) bitmap.getWidth();
        float hScale = (float) h / (float) bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(wScale, hScale); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }

    /**
     * 图片变圆
     *
     * @param
     * @return
     */
    public static Bitmap createCircleImage(Bitmap source, int min) {

        source = Zoom(source, min, min);
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(min, min, Config.ARGB_8888);
        /**
         * 产生一个同样大小的画布
         */
        Canvas canvas = new Canvas(target);
        /**
         * 首先绘制圆形
         */
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        /**
         * 使用SRC_IN
         */
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        /**
         * 绘制图片
         */
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }


    /**
     * def 截取view的截图
     * param
     * return
     * 2016/3/11 17:32
     */
    public static Bitmap takeScreenShot(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        Bitmap b = Bitmap.createBitmap(b1, 0, 0, view.getMeasuredWidth(), view.getMeasuredHeight()
        );
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return b;
    }

    public static void saveMyBitmap(String bitName, Bitmap mBitmap) {
        File f = new File("/sdcard/" + bitName + ".png");
        try {
            f.createNewFile();
        } catch (IOException e) {

        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {

        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 截取当前界面截图
     *
     * @param activity
     * @return
     */
    public static Bitmap takeScreenShot(Activity activity) {

        Log.i("TAG", "tackScreenShot");
        // View是你须要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        // 获取状况栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Log.i("TAG", "statusBarHeight = " + statusBarHeight);

        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = b1.getHeight();

        // 去掉题目栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    /**
     * final Activity activity ：调用该方法的Activity实例 long milliseconds ：震动的时长，单位是毫秒
     * long[] pattern ：自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]时长的单位是毫秒
     * boolean isRepeat ： 是否反复震动，如果是true，反复震动，如果是false，只震动一次
     */

    public static void Vibrate(final Activity activity, long milliseconds) {
        Vibrator vib = (Vibrator) activity
                .getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    public static void Vibrate(final Activity activity, long[] pattern,
                               boolean isRepeat) {
        Vibrator vib = (Vibrator) activity
                .getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }

    public static int getArmArchitecture() {
        if (sArmArchitecture != -1) {
            return sArmArchitecture;
        }
        try {
            InputStream is = new FileInputStream("/proc/cpuinfo");
            InputStreamReader ir = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(ir);
            try {
                String name = "CPU architecture";
                while (true) {
                    String line = br.readLine();
                    String[] pair = line.split(":");
                    if (pair.length != 2) {
                        continue;
                    }
                    String key = pair[0].trim();
                    String val = pair[1].trim();
                    if (key.compareToIgnoreCase(name) == 0) {
                        String n = val.substring(0, 1);
                        sArmArchitecture = Integer.parseInt(n);
                        break;
                    }
                }
            } finally {
                br.close();
                ir.close();
                is.close();
                if (sArmArchitecture == -1) {
                    sArmArchitecture = 6;
                }
            }
        } catch (Exception e) {
            sArmArchitecture = 6;
        }
        return sArmArchitecture;
    }

    public static boolean isNum(String str) {
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

    /**
     * 秒转时间
     *
     * @param time
     * @return
     */
    public static String getTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":"
                        + unitFormat(second);
            }
        }
        return timeStr;
    }

    private static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    // bitmap 图片文件的压缩
    public static Bitmap compressImage(Bitmap image, int q) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, q, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    // bitmap 图片内存的压缩
    public static Bitmap compressImageMemory(String path, int w, int h) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, newOpts);
        newOpts.inJustDecodeBounds = false;
        int rawW = newOpts.outWidth;
        int rawH = newOpts.outHeight;

        // 计算出取样率
        newOpts.inSampleSize = (int) (rawH / h) > (int) (rawW / w) ? (int) (rawW / w)
                : (int) (rawH / h);
        bitmap = BitmapFactory.decodeFile(path, newOpts);
        return bitmap;
    }

    // // bitmap 图片内存的压缩
    // public static Bitmap compressImageMemory(String path, int w,int h) {
    // BitmapFactory.Options newOpts = new BitmapFactory.Options();
    // newOpts.inJustDecodeBounds = true ;
    // Bitmap bitmap = BitmapFactory.decodeFile(path,newOpts);
    // newOpts.inJustDecodeBounds = false ;
    // int rawW = newOpts.outWidth;
    // int rawH = newOpts.outHeight;
    //
    // //计算出取样率
    // newOpts.inSampleSize =
    // (int)(rawH/h)>(int)(rawW/w)?(int)(rawW/w):(int)(rawH/h);
    // bitmap = BitmapFactory.decodeFile(path, newOpts);
    // return bitmap;
    // }
    public static int getNetWorkType(Context context) {

        int mNetWorkType = 0;
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();

            if (type.equalsIgnoreCase("WIFI")) {
                mNetWorkType = NETWORKTYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {

                mNetWorkType = isFastMobileNetwork(((TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE))
                        .getNetworkType()) ? NETWORKTYPE_3G : NETWORKTYPE_2G;

            }
        } else {
            mNetWorkType = NETWORKTYPE_INVALID;
        }

        return mNetWorkType;
    }

    public static int getNetWorkTypeBySub(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if (info != null) {
            int mNetWorkType = isFastMobileNetwork(info.getSubtype()) ? NETWORKTYPE_3G
                    : NETWORKTYPE_2G;
            return mNetWorkType;
        } else {
            return NETWORKTYPE_2G;
        }

    }

    private static boolean isFastMobileNetwork(int type) {
        // TelephonyManager telephonyManager =
        // (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        // Log.d("typetypetypetypetypetype", "type");

        // int type=telephonyManager.getNetworkType();
        switch (type) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true; // ~ 400-7000 kbps
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true; // ~ 1-2 Mbps
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true; // ~ 5 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true; // ~ 10-20 Mbps
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false; // ~25 kbps
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true; // ~ 10+ Mbps
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return true;
            default:
                return true;
        }

    }

    public static boolean isInBackground(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> listAppProcessInfo = am
                .getRunningAppProcesses();
        if (listAppProcessInfo != null) {
            final String strPackageName = context.getPackageName();
            for (RunningAppProcessInfo pi : listAppProcessInfo) {

                if (pi.processName.equals(strPackageName)) {
                    if (pi.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                            && pi.importance != RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }
        return false;
    }

    /**
     * def home到主页面
     * param
     * return
     * 2016/4/28 17:49
     */
    public static void launchHome(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(intent);
    }

    public static int randomRange(int nStart, int nEnd) {
        if (nStart >= nEnd) {
            return nEnd;
        }
        return nStart + (int) (Math.random() * (nEnd - nStart));
    }

    /**
     * def 储存空间是否已满
     * param
     * return
     * 2016/4/28 17:49
     */
    public static boolean isExternalStorageMounted() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }

    public static String getExternalCachePath(Context context) {
        return Environment.getExternalStorageDirectory().getPath()
                + "/Android/data/" + context.getPackageName() + "/cache";
    }

    public static int dipToPixel(Context context, int nDip) {
        if (sDensity == 0) {
            final WindowManager wm = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            sDensity = dm.density;
        }
        return (int) (sDensity * nDip);
    }

    public static String getDeviceUUID(Context context) {
        final TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String strIMEI = tm.getDeviceId();
        if (TextUtils.isEmpty(strIMEI)) {
            strIMEI = "1";
        }

        String strMacAddress = getMacAddress(context);
        if (TextUtils.isEmpty(strMacAddress)) {
            strMacAddress = "1";
        }

        return strIMEI + strMacAddress;
    }

    /**
     * def mac地址
     * param
     * return
     * 2016/4/28 17:50
     */
    public static String getMacAddress(Context context) {
        final WifiManager wm = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        return wm.getConnectionInfo().getMacAddress();
    }

    public static void printCallStack() {
        for (StackTraceElement e : new Throwable().getStackTrace()) {
            System.out.println(e.toString());
        }
    }

    public static void copyToClipBoard(Context context, String strText) {
        final ClipboardManager manager = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        manager.setText(strText);
    }

    public static boolean isEmail(String strEmail) {
        Pattern pattern = Pattern
                .compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(strEmail);
        return matcher.matches();
    }

    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info;
            info = manager.getPackageInfo(context.getPackageName(), 0);

            // return (2.0 + ((float) info.versionCode / 100.0)) + "";
            return info.versionName;
        } catch (NameNotFoundException e) {

        }
        return null;
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {

        }
        return null;
    }


    public static void addEditTextLengthFilter(EditText editText,
                                               int nLengthLimit) {
        InputFilter filters[] = editText.getFilters();
        if (filters == null) {
            editText.getEditableText().setFilters(
                    new InputFilter[]{new InputFilter.LengthFilter(
                            nLengthLimit)});
        } else {
            final int nSize = filters.length + 1;
            InputFilter newFilters[] = new InputFilter[nSize];
            int nIndex = 0;
            for (InputFilter filter : filters) {
                newFilters[nIndex++] = filter;
            }
            newFilters[nIndex] = new InputFilter.LengthFilter(nLengthLimit);
            editText.getEditableText().setFilters(newFilters);
        }
    }


    public static boolean getCursorBoolean(Cursor cursor, int nColumnIndex) {
        return cursor.getInt(nColumnIndex) == 1 ? true : false;
    }

    public static void safeSetImageBitmap(ImageView iv, String path) {
        BitmapFactory.Options op = new BitmapFactory.Options();
        computeSampleSize(op, path, 512, 512 * 512);
        try {
            iv.setImageBitmap(BitmapFactory.decodeFile(path, op));
        } catch (OutOfMemoryError e) {

        }
    }



    public static Bitmap getVideoThumbnail(String filePath, int maxSize) {
        Bitmap bmp = ThumbnailUtils.createVideoThumbnail(filePath,
                Images.Thumbnails.MINI_KIND);
        if (bmp != null) {
            final int width = bmp.getWidth();
            final int height = bmp.getHeight();
            if (width > maxSize || height > maxSize) {
                int fixWidth = 0;
                int fixHeight = 0;
                if (height > width) {
                    fixHeight = maxSize;
                    fixWidth = width * fixHeight / height;
                } else {
                    fixWidth = maxSize;
                    fixHeight = height * fixWidth / width;
                }
                bmp = Bitmap
                        .createScaledBitmap(bmp, fixWidth, fixHeight, false);
            }
        }
        return bmp;
    }

    public static int nextPowerOf2(int n) {
        n -= 1;
        n |= n >>> 16;
        n |= n >>> 8;
        n |= n >>> 4;
        n |= n >>> 2;
        n |= n >>> 1;
        return n + 1;
    }

    public static int computeSampleSize(BitmapFactory.Options options,
                                        String path, int minSideLength, int maxNumOfPixels) {
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        if (options.outWidth != -1) {
            options.inJustDecodeBounds = false;

            int initialSize = computeInitialSampleSize(options, minSideLength,
                    maxNumOfPixels);

            int roundedSize;
            if (initialSize <= 8) {
                roundedSize = 1;
                while (roundedSize < initialSize) {
                    roundedSize <<= 1;
                }
            } else {
                roundedSize = (initialSize + 7) / 8 * 8;
            }

            options.inSampleSize = roundedSize;

            return roundedSize;
        } else {
            return -1;
        }
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels < 0) ? 1 : (int) Math.ceil(Math.sqrt(w
                * h / maxNumOfPixels));
        int upperBound = (minSideLength < 0) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if (maxNumOfPixels < 0 && minSideLength < 0) {
            return 1;
        } else if (minSideLength < 0) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }



    public static Bitmap decodeSampledBitmapFromFilePath(String path,
                                                         int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPurgeable = true;
        BitmapFactory.decodeFile(path, options);
        if (options.outWidth > 0) {
            options.inSampleSize = calculateInSampleSize(options, reqWidth,
                    reqHeight);
            options.inJustDecodeBounds = false;
            try {
                return BitmapFactory.decodeFile(path, options);
            } catch (OutOfMemoryError e) {

            }
        }
        return null;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }

            final float totalPixels = width * height;

            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    public static boolean isArrayContain(Object[] objs, Object item) {
        for (Object obj : objs) {
            if (obj.equals(item)) {
                return true;
            }
        }
        return false;
    }

    public static String throwableToString(Throwable e) {
        StringBuffer sb = new StringBuffer();
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        e.printStackTrace(pw);
        Throwable cause = e.getCause();

        while (cause != null) {
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        pw.close();
        String result = writer.toString();
        result = result.replaceAll("\n", "\r\n");
        sb.append(result);
        final String ret = sb.toString();
        if (TextUtils.isEmpty(ret)) {
            return e.getMessage();
        }
        return ret;
    }

    public static byte[] objectToByteArray(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream bos = new ObjectOutputStream(baos);
        bos.writeObject(obj);
        try {
            return baos.toByteArray();
        } finally {
            bos.close();
        }
    }

    public static Object byteArrayToObject(byte[] data)
            throws StreamCorruptedException, IOException,
            ClassNotFoundException {
        if (data == null) {
            return null;
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais);
        try {
            return ois.readObject();
        } finally {
            ois.close();
        }
    }

    public static int getPictureExifRotateAngle(String path) {
        int rotate = 0;
        try {
            ExifInterface ei = new ExifInterface(path);
            int ori = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            if (ori == ExifInterface.ORIENTATION_ROTATE_180) {
                rotate = 180;
            } else if (ori == ExifInterface.ORIENTATION_ROTATE_270) {
                rotate = 270;
            } else if (ori == ExifInterface.ORIENTATION_ROTATE_90) {
                rotate = 90;
            }
        } catch (Exception e) {

        }
        return rotate;
    }

    public static class SimDoubleInfo {

        private boolean isdoublesim = false;
        //
        private int SimId_1 = 0;
        private int SimId_2 = 0;
        //
        private String Imsi_1 = "";
        private String Imsi_2 = "";
        //
        private String Imei_1 = "";
        private String Imei_2 = "";
        //
        private int PhoneType_1 = 0;
        private int PhoneType_2 = 0;
        //
        private String DefaultImsi = "";
        //
        private String doublesim_system = "";

        public void setIsDoubleSim(boolean isdoublesim) {
            this.isdoublesim = isdoublesim;
            if (this.isdoublesim) {
                if (TextUtils.isEmpty(Imsi_1) && TextUtils.isEmpty(Imsi_2)
                        && TextUtils.isEmpty(Imei_1)
                        && TextUtils.isEmpty(Imei_2)) {
                    this.isdoublesim = false;
                }
            }
        }

        public boolean isDoubleSim() {
            return this.isdoublesim;
        }

        public int getSimId_1() {
            return SimId_1;
        }

        public void setSimId_1(int SimId_1) {
            this.SimId_1 = SimId_1;
        }

        public int getSimId_2() {
            return SimId_2;
        }

        public void setSimId_2(int SimId_2) {
            this.SimId_2 = SimId_2;
        }

        public String getImsi_1() {
            return Imsi_1;
        }

        public void setImsi_1(String Imsi_1) {
            this.Imsi_1 = Imsi_1;
        }

        public String getImsi_2() {
            return Imsi_2;
        }

        public void setImsi_2(String Imsi_2) {
            this.Imsi_2 = Imsi_2;
        }

        public String getImei_1() {
            return Imei_1;
        }

        public void setImei_1(String Imei_1) {
            this.Imei_1 = Imei_1;
        }

        public String getImei_2() {
            return Imei_2;
        }

        public void setImei_2(String Imei_2) {
            this.Imei_2 = Imei_2;
        }

        public int getPhoneType_1() {
            return PhoneType_1;
        }

        public void setPhoneType_1(int PhoneType_1) {
            this.PhoneType_1 = PhoneType_1;
        }

        public int getPhoneType_2() {
            return PhoneType_2;
        }

        public void setPhoneType_2(int PhoneType_2) {
            this.PhoneType_2 = PhoneType_2;
        }

        public String getDefaultImsi() {
            return DefaultImsi;
        }

        public void setDefaultImsi(String DefaultImsi) {
            this.DefaultImsi = DefaultImsi;
        }

        public String getDoublesim_system() {
            return doublesim_system;
        }

        public void setDoublesim_system(String doublesim_system) {
            this.doublesim_system = doublesim_system;
        }
    }

    public static SimDoubleInfo initMtkDoubleSim(Context mContext) {
        SimDoubleInfo mtkDoubleInfo = new SimDoubleInfo();
        try {
            TelephonyManager tm = (TelephonyManager) mContext
                    .getSystemService(Context.TELEPHONY_SERVICE);
            Class<?> c = Class.forName("com.android.internal.telephony.Phone");
            Field fields1 = c.getField("GEMINI_SIM_1");
            fields1.setAccessible(true);
            mtkDoubleInfo.setSimId_1((Integer) fields1.get(null));
            Field fields2 = c.getField("GEMINI_SIM_2");
            fields2.setAccessible(true);
            mtkDoubleInfo.setSimId_2((Integer) fields2.get(null));
            Method m = TelephonyManager.class.getDeclaredMethod(
                    "getSubscriberIdGemini", int.class);
            mtkDoubleInfo.setImsi_1((String) m.invoke(tm,
                    mtkDoubleInfo.getSimId_1()));
            mtkDoubleInfo.setImsi_2((String) m.invoke(tm,
                    mtkDoubleInfo.getSimId_2()));
            Method m1 = TelephonyManager.class.getDeclaredMethod(
                    "getDeviceIdGemini", int.class);
            mtkDoubleInfo.setImei_1((String) m1.invoke(tm,
                    mtkDoubleInfo.getSimId_1()));
            mtkDoubleInfo.setImei_2((String) m1.invoke(tm,
                    mtkDoubleInfo.getSimId_2()));
            Method mx = TelephonyManager.class.getDeclaredMethod(
                    "getPhoneTypeGemini", int.class);
            mtkDoubleInfo.setPhoneType_1((Integer) mx.invoke(tm,
                    mtkDoubleInfo.getSimId_1()));
            mtkDoubleInfo.setPhoneType_2((Integer) mx.invoke(tm,
                    mtkDoubleInfo.getSimId_2()));
            if (TextUtils.isEmpty(mtkDoubleInfo.getImsi_1())
                    && (!TextUtils.isEmpty(mtkDoubleInfo.getImsi_2()))) {
                mtkDoubleInfo.setDefaultImsi(mtkDoubleInfo.getImsi_2());
            }
            if (TextUtils.isEmpty(mtkDoubleInfo.getImsi_2())
                    && (!TextUtils.isEmpty(mtkDoubleInfo.getImsi_1()))) {
                mtkDoubleInfo.setDefaultImsi(mtkDoubleInfo.getImsi_1());
            }
        } catch (Exception e) {

            mtkDoubleInfo.setIsDoubleSim(false);
            return mtkDoubleInfo;
        }
        mtkDoubleInfo.setDoublesim_system("MTK");
        mtkDoubleInfo.setIsDoubleSim(true);
        return mtkDoubleInfo;
    }

    public static SimDoubleInfo initQualcommDoubleSim(Context mContext) {
        SimDoubleInfo gaotongDoubleInfo = new SimDoubleInfo();
        gaotongDoubleInfo.setSimId_1(0);
        gaotongDoubleInfo.setSimId_2(1);
        try {
            Class<?> cx = Class
                    .forName("android.telephony.MSimTelephonyManager");
            Object obj = mContext.getSystemService("phone_msim");
            Method md = cx.getMethod("getDeviceId", int.class);
            Method ms = cx.getMethod("getSubscriberId", int.class);
            gaotongDoubleInfo.setImei_1((String) md.invoke(obj,
                    gaotongDoubleInfo.getSimId_1()));
            gaotongDoubleInfo.setImei_2((String) md.invoke(obj,
                    gaotongDoubleInfo.getSimId_2()));
            gaotongDoubleInfo.setImsi_1((String) ms.invoke(obj,
                    gaotongDoubleInfo.getSimId_1()));
            gaotongDoubleInfo.setImsi_2((String) ms.invoke(obj,
                    gaotongDoubleInfo.getSimId_2()));
        } catch (Exception e) {

            gaotongDoubleInfo.setIsDoubleSim(false);
            return gaotongDoubleInfo;
        }
        gaotongDoubleInfo.setDoublesim_system("GAOTONG");
        gaotongDoubleInfo.setIsDoubleSim(true);
        return gaotongDoubleInfo;
    }

    /**
     * * @param c * @return 返回平台数据
     *
     * @param context
     * @return
     */
    private static SimDoubleInfo s_phone_SimInfo = null;

    public static synchronized SimDoubleInfo getSimInfo(Context context) {
        if (s_phone_SimInfo != null) {
            return s_phone_SimInfo;
        }
        SimDoubleInfo gaotongDoubleInfo = initQualcommDoubleSim(context);
        boolean isGaoTongCpu = gaotongDoubleInfo.isDoubleSim();
        if (isGaoTongCpu) {
            // 高通芯片双卡
            s_phone_SimInfo = gaotongDoubleInfo;
            return s_phone_SimInfo;
        } else {
            SimDoubleInfo mtkDoubleInfo = initMtkDoubleSim(context);
            boolean isMtkCpu = mtkDoubleInfo.isDoubleSim();
            if (isMtkCpu) {
                // MTK芯片双卡
                s_phone_SimInfo = mtkDoubleInfo;
                return s_phone_SimInfo;
            } else {
                SimDoubleInfo oneSimInfo = new SimDoubleInfo();
                oneSimInfo.setIsDoubleSim(false);
                try {
                    TelephonyManager TelephonyMgr = (TelephonyManager) context
                            .getSystemService(Context.TELEPHONY_SERVICE);
                    String szImei = TelephonyMgr.getDeviceId();
                    String imsi = TelephonyMgr.getSubscriberId();
                    oneSimInfo.setImei_1(szImei);
                    oneSimInfo.setImsi_1(imsi);
                    oneSimInfo.setDefaultImsi(imsi);

                    s_phone_SimInfo = oneSimInfo;
                    return s_phone_SimInfo;
                } catch (Exception e) {

                }
            }
        }
        return null;
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {

                return appProcess.processName;
            }
        }
        return null;
    }

    private static String s_phone_szUniqueID = null;
    private static String s_onecode_salt = "20141209_voip_37call_wlbhh3656j_57skjfvgry37xma5921q7kyk";

    public static synchronized String getOneCode(Context context) {
        return getOneCode(context, s_onecode_salt);
    }

    public static synchronized String getOneCode(Context context, String salt) {
        if (s_phone_szUniqueID != null && s_phone_szUniqueID.length() > 0) {
            return s_phone_szUniqueID;
        }
        TelephonyManager TelephonyMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        //
        String operator = TelephonyMgr.getSimOperator();
        //
        String szImei = "";
        String imsi = "";
        SimDoubleInfo sims = getSimInfo(context);
        if (sims != null) {
            if (sims.isDoubleSim()) {

                szImei = sims.getImei_1() + "_" + sims.getImei_2();
                imsi = sims.getImsi_1() + "_" + sims.getImsi_2();
            } else {

                szImei = sims.getImei_1();
                imsi = sims.getImsi_1();
            }
        } else {

            szImei = TelephonyMgr.getDeviceId();
            imsi = TelephonyMgr.getSubscriberId();
        }

        String m_szDevIDShort = "201411161507:"
                + // we make this look like a valid IMEI
                Build.BOARD + "_" + Build.FINGERPRINT + "_" + Build.BRAND + "_"
                + Build.CPU_ABI + "_" + Build.DEVICE + "_" + Build.DISPLAY
                + "_" + Build.HOST + "_" + Build.ID + "_" + Build.MANUFACTURER
                + "_" + Build.MODEL + "_" + Build.PRODUCT + "_" + Build.TAGS
                + "_" + Build.TYPE + "_" + Build.USER + "_" + "";// 13 digits

        String m_szAndroidID = Secure.getString(context.getContentResolver(),
                Secure.ANDROID_ID);

        // WifiManager wm =
        // (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        // String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
        // BluetoothAdapter m_BluetoothAdapter = null; // Local Bluetooth
        // adapter
        // String m_szBTMAC = "";
        // try {
        // m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // m_szBTMAC = m_BluetoothAdapter.getAddress();
        // } catch (Exception e) {
        // // TODO: handle exception
        // LogUtil.info("蓝牙MAC获取失败(蓝牙被禁用)：" + e.getMessage());
        // GLog.e(e, e);
        // m_szBTMAC = "";
        // }
        String m_szLongID = salt + "_" + szImei + "_" + m_szDevIDShort + "_"
                + m_szAndroidID + "_" + imsi + "_" + operator;
        // compute md5
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {

        }
        m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
        // get md5 bytes
        byte p_md5Data[] = m.digest();
        // create a hex string
        String m_szUniqueID = new String();
        for (int i = 0; i < p_md5Data.length; i++) {
            int b = (0xFF & p_md5Data[i]);
            // if it is a single digit, make sure it have 0 in front (proper
            // padding)
            if (b <= 0xF) {
                m_szUniqueID += "0";
            }
            // add number to string
            m_szUniqueID += Integer.toHexString(b);
        } // hex string to uppercase
        m_szUniqueID = m_szUniqueID.toUpperCase();
        s_phone_szUniqueID = m_szUniqueID;
        return s_phone_szUniqueID;
    }

    /**
     * 判断软件是否在前台
     *
     * @param context
     * @return
     */
    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;

    }


    /**
     * def 创建内存dump
     * param
     * return
     * 2016/4/28 17:53
     */
    public static boolean createDumpFile(Context context) {
        // hprof-conv dump.hprof converted-dump.hprof
        String LOG_PATH = Environment.getExternalStorageDirectory().getPath()
                + "/Android/data/" + context.getPackageName() + File.separator
                + "dump";
        boolean bool = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ssss");
        String createTime = sdf.format(new Date(System.currentTimeMillis()));
        String state = Environment.getExternalStorageState();
        // 判断SdCard是否存在并且是可用的
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File file = new File(LOG_PATH);
            if (!file.exists()) {
                file.mkdirs();
            }
            String hprofPath = file.getAbsolutePath();
            if (!hprofPath.endsWith("/")) {
                hprofPath += "/";
            }
            hprofPath += createTime + ".hprof";
            try {

                android.os.Debug.dumpHprofData(hprofPath);
                bool = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            bool = false;
        }
        return bool;
    }

    /**
     * def 去除非数字
     * param
     * return
     * 2016/1/12 12:40
     */
    public static String filterPhoneNum(String phone) {
        return phone.replaceAll("[^\\d]*", "");
    }

    public static String removeFZ(String phone) {
        try {
            String nozeroPhone = String.valueOf(Long.valueOf(phone));
            if (isMobilePhone(nozeroPhone)) {
                return nozeroPhone;
            } else {
                return phone;
            }
        } catch (Exception e) {
            // TODO: handle exception
            return phone;
        }
    }

    /**
     * def 判断是否是中国手机
     * param
     * return
     * 2016/4/28 17:53
     */
    public static boolean isMobilePhone(String phone) {
        if (phone.startsWith("86")) {
            phone = phone.replaceFirst("86", "");
        } else if (phone.startsWith("+86")) {
            phone = phone.replaceFirst("\\+86", "");
        }

        Pattern pattern = Pattern
                .compile("^((13[0-9])|(14[0-9])|(17[0-9])|(15([0-3]|[5-9]))|(18[0-9]))\\d{8}$");
        Matcher matcher = pattern.matcher(phone);

        if (matcher.matches()) {
            return true;
        }
        return false;

    }

    /**
    * def 键盘隐藏或开启
    * param
    * return
    *2016/4/28 17:58
    */
    public static void inputMethodChange(boolean open, Context con, EditText et
    ) {
        InputMethodManager imm = (InputMethodManager) con.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (open) {
            imm.showSoftInput(et, InputMethodManager.SHOW_FORCED);
        } else {
            imm.hideSoftInputFromWindow(et.getWindowToken(), 0); //强制隐藏键盘
        }
    }
}
