package org.gtq.vhubs.utils;

import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {


    static String apiaeskey = "Ab0MBbDjPr68xboC";
    static String SERVER_URL = "http://115.28.54.54/api";
    private static int TIMEOUT_CONNECTION = 8000;
    private static int TIMEOUT_SO = 30000;

    public static void setConnectionTimeOut(int time) {
        TIMEOUT_CONNECTION = time;
    }

    public static void setSoTimeOut(int time) {
        TIMEOUT_SO = time;
    }

    public static String encryptByMD5(String strPassword) throws Exception {
        String strPasswordMD5 = "";

        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte buf[] = digest.digest(strPassword.getBytes());
        String stmp = null;
        for (int n = 0; n < buf.length; n++) {
            stmp = Integer.toHexString(buf[n] & 0xff);
            strPasswordMD5 = stmp.length() == 1 ?
                    (strPasswordMD5 + "0" + stmp) : (strPasswordMD5 + stmp);
        }

        return strPasswordMD5;
    }

    public static String doPost(String server, String method, String... params) throws Exception {
        OkHttpClient client = new OkHttpClient.Builder().build();
        JSONObject json = new JSONObject();
        json.put("s", server);
        json.put("m", method);
        JSONArray array = new JSONArray();
        for (int i = 0; i < params.length; i++) {
            array.put(params[i]);
        }
        json.put("p", array);

        String data = AesUtil.encrypt(json.toString(), apiaeskey);
        String auth = encryptByMD5(data + apiaeskey);
        FormBody formBody = new FormBody.Builder()
                .add("auth", auth)
                .add("data", data).
                        build();


        Request request = new Request.Builder()

                .url(SERVER_URL)

                .post(formBody)

                .build();


        Response response = client.newCall(request).execute();


        if (response.isSuccessful()) {

            return AesUtil.decrypt(response.body().string(), apiaeskey);


        } else {

            throw new IOException("Unexpected code " + response);

        }
    }
}
