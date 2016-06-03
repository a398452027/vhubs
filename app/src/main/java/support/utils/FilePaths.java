package support.utils;

import android.text.TextUtils;


import org.gtq.vhubs.core.VApplication;

import java.io.File;


public class FilePaths {

	public static String getAvatarSaveFilePath(String userId){
		return SystemUtils.getExternalCachePath(VApplication.getInstance()) +
				File.separator + userId+".jpg";
	}
	
	public static String getCameraSaveFilePath(){
		return SystemUtils.getExternalCachePath(VApplication.getInstance()) +
				File.separator + "camera.jpg";
	}
	
	public static String getPictureChooseFilePath(){
		return SystemUtils.getExternalCachePath(VApplication.getInstance()) +
				File.separator + "choose.jpg";
	}
	
	public static String getUrlFileCachePath(String strUrl){
		if(TextUtils.isEmpty(strUrl)){
			return SystemUtils.getExternalCachePath(VApplication.getInstance()) +
					File.separator + "urlfile";
		}
		return SystemUtils.getExternalCachePath(VApplication.getInstance()) +
				File.separator + "urlfile" + File.separator +strUrl;
	}
	
	public static String getQrcodeSavePath(String strIMUser){
		if(TextUtils.isEmpty(strIMUser)){
			return SystemUtils.getExternalCachePath(VApplication.getInstance()) +
					File.separator + "qrcode";
		}
		return SystemUtils.getExternalCachePath(VApplication.getInstance()) +
				File.separator + "qrcode" + File.separator + strIMUser;
	}
	
	public static String getImportFolderPath(){
		return SystemUtils.getExternalCachePath(VApplication.getInstance()) +
				File.separator + "importfile" + File.separator;
	}
	
	public static String getCameraVideoFolderPath(){
		return SystemUtils.getExternalCachePath(VApplication.getInstance()) +
				File.separator + "videos" + File.separator;
	}
}
