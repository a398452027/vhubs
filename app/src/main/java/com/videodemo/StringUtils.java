package com.videodemo;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;


public class StringUtils {


    /**
     * @param minBytes
     * @param maxBytes
     * @return String
     */
    public static String conversionByteToPer(long minBytes, long maxBytes) {
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后1位
        numberFormat.setMaximumFractionDigits(1);
        String result = numberFormat.format((float) minBytes / (float) maxBytes
                * 100);
        return result + "%";
    }

    /**
     * 转换成日期+时间
     *
     * @param time
     * @return yyyy-MM-dd HH:mm
     */
    public static String formatTimeToDateTime(long time) {
        return formatTimeToFormat("yyyy-MM-dd HH:mm", time);
    }

    /**
     * 转换成日期
     *
     * @param time
     * @return yyyy-MM-dd
     */
    public static String formatTimeToDate(long time) {
        return formatTimeToFormat("yyyy-MM-dd", time);
    }

    public static String string2Date(String ymd) {
        String month = ymd.substring(0, ymd.indexOf("月"));
        String day = ymd.substring(ymd.indexOf("月") + 1, ymd.indexOf("日"));
        if (Integer.valueOf(month) < 10) {
            month = "0" + month;
        }
        if (Integer.valueOf(day) < 10) {
            day = "0" + day;
        }
        LogUtils.i("2014-" + month + "-" + day);
        return "2014-" + month + "-" + day;
    }

    public static String string2_Date(String furnTime) {
        try {
            if (!StringUtils.isEmpty(furnTime)) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = format.parse(furnTime);
                return formatTimeToFormat("yyyy.MM.dd", date.getTime());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 转换成日期
     *
     * @param time
     * @return MM-dd HH:mm
     */
    public static String formatTimeToShortDateTime(long time) {
        return formatTimeToFormat("yyyy.MM.dd HH:mm", time);
    }

    /**
     * 转换成日期
     *
     * @param time
     * @return HH:mm
     */
    public static String formatTimeToShortTime(long time) {
        return formatTimeToFormat("HH:mm", time);
    }


    /**
     * 获取时间戳
     */

    public static String getTimeString() {
        String str = "";
        Date d = new Date();
        str = d.getTime() + "";
        return str.substring(0, 10);
    }

    /**
     * 时间转换
     *
     * @param format
     * @param time
     * @return
     */
    public static String formatTimeToFormat(String format, long time) {
        if (TextUtils.isEmpty(format))
            return String.valueOf(time);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (String.valueOf(time).length() < 13) {
            return sdf.format(time * 1000l);
        } else {
            return sdf.format(time);
        }
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    public static int parseInt(String intVal) {
        return parseInt(intVal, 0);
    }

    public static int parseInt(String intVal, int defaultIntVal) {
        int val = defaultIntVal;
        if (TextUtils.isEmpty(intVal) || intVal.equalsIgnoreCase("null")) {
            return val;
        } else {
            try {
                val = Integer.parseInt(intVal);
            } catch (NumberFormatException e) {
                LogUtils.e(e.getMessage());
            }
        }
        return val;
    }

    public static long parseLong(String longVal) {
        return parseLong(longVal, 0l);
    }

    public static long parseLong(String longVal, long defaultLongVal) {
        long val = defaultLongVal;
        if (TextUtils.isEmpty(longVal) || longVal.equalsIgnoreCase("null")) {
            return val;
        } else {
            try {
                val = Long.parseLong(longVal);
            } catch (NumberFormatException e) {
                LogUtils.e(e.getMessage());
            }
        }
        return val;
    }

    public static float parseFloat(String floatVal) {
        return parseFloat(floatVal, 0.0f);
    }

    public static float parseFloat(String floatVal, float defaultLongVal) {
        float val = defaultLongVal;
        if (TextUtils.isEmpty(floatVal) || floatVal.equalsIgnoreCase("null")) {
            return val;
        } else {
            try {
                val = Float.parseFloat(floatVal);
            } catch (NumberFormatException e) {
                LogUtils.e(e.getMessage());
            }
        }
        return val;
    }


    public static void showShortToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static String formatScore(float score) {
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(score);
    }

    public static String formatScore(double score) {
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(score);
    }

    public static boolean isEmpty(CharSequence value) {
        if (TextUtils.isEmpty(value)
                || value.toString().equalsIgnoreCase("null"))
            return true;
        else
            return false;
    }

    public static String formatPrice(float score) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(score);
    }
}
