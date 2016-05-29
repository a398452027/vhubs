package org.gtq.vhubs.utils;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesUtil {

    public static String encryptByAes(String strMessage, String keys) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec zeroIv = new IvParameterSpec(new byte[cipher.getBlockSize()]);
            SecretKeySpec key = new SecretKeySpec(keys.getBytes(), "AES");

            cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
            byte[] byteMi = cipher.doFinal(strMessage.getBytes("UTF8"));

            String strMi = Base64.encodeToString(byteMi, Base64.DEFAULT);

            return strMi;
        } catch (Exception e) {

        }
        return null;
    }

    public static String decryptByAes(String keys, String strMi) {
        try {
            byte[] byteMi = Base64.decode(strMi, Base64.DEFAULT);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec zeroIv = new IvParameterSpec(new byte[cipher.getBlockSize()]);
            SecretKeySpec key = new SecretKeySpec(keys.getBytes(), "AES");

            cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
            byte[] byteMing = cipher.doFinal(byteMi);

            String strMing = new String(byteMing, "UTF8");
            return strMing;
        } catch (final Exception e) {

        }
        return null;
    }

    /**
     * AES加密字符串
     *
     * @param input 要加密的原字符串
     * @param key   要加密的key
     * @return 加密之后的数据
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String encrypt(String input, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        byte[] crypted = null;
        SecretKeySpec skey = new SecretKeySpec(key.getBytes("utf-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skey);
        crypted = cipher.doFinal(input.getBytes());
        return Base64.encodeToString(crypted, Base64.DEFAULT);
    }


    /**
     * AES解密字符串
     *
     * @param input 要加密的原字符串
     * @param key   要加密的key
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String decrypt(String input, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        byte[] byteMi = Base64.decode(input.getBytes(), Base64.DEFAULT);
        byte[] output = null;
        SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skey);
        output = cipher.doFinal(byteMi);
        Log.d("AesUtil", "output.length:" + output.length);
        return new String(output, "utf-8");
    }


//	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
//		System.out.println(AesUtil.decrypt("7XHxWI2egdJGGgMt9pKKgrL7hw9QD+g3hJL8/Opc9YMEUJdiOQ9N2WdYdbuLLdZ5k3geP7lZvMDGLfcTXqdD0gr0PKeV2vYvkmxsOFrUhHXvjSthv+JR9T3E6yiHu3VX", "vh1p3PcnZ9eetylx"));
//		System.out.println(AesUtil.encrypt("{\"id\":\"1\",\"username\":\"xiawei\",\"create_time\":\"1462124462\",\"update_time\":\"1462124462\"}", "vh1p3PcnZ9eetylx"));
//	}
}
