package support.utils;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class DBUtils {
	
	public static boolean tabbleIsExist(String tableName,SQLiteDatabase db) {
		boolean result = false;
		Cursor cursor = null;
		try {
			String sql = "select count(*) as c from Sqlite_master  where type ='table' and name ='" + tableName.trim() + "' ";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}
		} catch (Exception e) {
			Log.e("", "tabbleIsExist: ", e);
		} finally{
			if(cursor != null){
				cursor.close();
			}
		}
		return result;
	}
}
