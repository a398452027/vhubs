package org.gtq.vhubs.core;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


import org.gtq.vhubs.R;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashMap;
import java.util.Map;

public class CrashHandler implements UncaughtExceptionHandler {

    private static CrashHandler INSTANCE = new CrashHandler();

    private UncaughtExceptionHandler mDefaultHandler;

    private Context mContext;
    private Map<String, String> info = new HashMap<String, String>();// 用来存储设备信息和异常信息
    //private SimpleDateFormat 				format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss",Locale.getDefault());

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();// 获取系统默认的UncaughtException处理器
        Thread.setDefaultUncaughtExceptionHandler(this);// 设置该CrashHandler为程序的默认处理器
    }

    public void uncaughtException(Thread thread, Throwable ex) {
        handleException(ex);
        final Intent intent = VApplication.getInstance().getPackageManager().getLaunchIntentForPackage
                (VApplication.getInstance().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        VApplication.getInstance().startActivity(intent);
        mDefaultHandler.uncaughtException(thread, ex);
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex 异常信息
     * @return true 如果处理了该异常信息;否则返回false.
     */
    public boolean handleException(Throwable ex) {
        if (ex == null)
            return false;

        Toast.makeText(VApplication.getInstance(), R.string.uncaught_exception_handler,Toast.LENGTH_SHORT);
        return true;
    }


}