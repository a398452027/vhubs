package org.gtq.vhubs.utils;

import java.io.File;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.view.View;

import org.gtq.vhubs.R;
import org.gtq.vhubs.core.VApplication;
import org.json.JSONException;
import org.json.JSONObject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import support.ui.activity.VBaseActivity;

@SuppressWarnings("deprecation")
@SuppressLint({"DefaultLocale", "SimpleDateFormat"})
public class Function_Utility {



    /**
     * 下载到SD卡地址
     */
    public static String getUpgradePath() {
        String filePath = getAppRootPath() + "/upgrade/";
        File file = new File(filePath);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        file = null;
        return filePath;
    }

    public static String getAppRootPath() {
        String filePath = "/weimicommunity";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            filePath = Environment.getExternalStorageDirectory() + filePath;
        } else {
            filePath = VApplication.getInstance().getCacheDir() + filePath;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = null;
        File nomedia = new File(filePath + "/.nomedia");
        if (!nomedia.exists())
            try {
                nomedia.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return filePath;
    }

    public static boolean checkUpdate(final VBaseActivity activity) {
        final int code = getVerCode(activity);
        Observable.just("").doOnSubscribe(new Action0() {
            @Override
            public void call() {

            }
        }).observeOn(Schedulers.io())
                .map(new Func1<String, JSONObject>() {
                    @Override
                    public JSONObject call(String s) {


                        try {
                            return new JSONObject(HttpUtils.doPost("version", "currentVersion",code+""));
                        } catch (Exception e) {
                            VApplication.toast(activity.getString(R.string.net_fail));
                        }
                        return null;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JSONObject>() {
                    @Override
                    public void call(JSONObject bean) {
                        try {
                            if (bean.getInt("code") != 0) {
                                VApplication.toastJsonError(bean);
                            } else {

                                final JSONObject verData = bean.getJSONObject("data").getJSONObject("current_version");
                                if (code != verData.getInt("id")) {
                                    activity.createYesNoDialog(activity, activity.getString(R.string.updata), activity.getString(android.R.string.cancel)
                                            , activity.getString(R.string.updata_msg, verData.getString("version_info")), 0, activity.getString(R.string.updata), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if (which == -1) {
                                                        Intent service = new Intent(activity, DownloadService.class);
                                                        try {
                                                            String url=verData.getString("down_url");
//                                                            String url = "http://ftp-apk.pconline.com.cn/85cafce5e1a734c38ee97a0d4ddd2963/pub/download/201010/Root_Explorer_v4.0.apk";
                                                            service.putExtra(DownloadService.INTENT_URL, url);
                                                            activity.startService(service);
                                                        } catch (Exception e) {
                                                            VApplication.toast(activity.getString(R.string.net_fail));
                                                        }


                                                    }
                                                }
                                            }).show();
                                }
                            }
                        } catch (JSONException e) {
                            VApplication.toast(activity.getString(R.string.net_fail));
                        }


                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        VApplication.toast(activity.getString(R.string.net_fail));
                    }
                });


        return false;
    }

    /**
     * 获得版本号
     */
    public static int getVerCode(Context context) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo("org.gtq.vhubs", 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {

        }
        return verCode;
    }
}
