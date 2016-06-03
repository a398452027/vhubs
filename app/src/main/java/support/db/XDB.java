package support.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;
import java.util.List;

import support.bean.IDObject;
import support.utils.ClassUtils;
import support.utils.DBUtils;
import support.utils.SystemUtils;

public class XDB {

    public static XDB getInstance() {
        if (sInstance == null) {
            sInstance = new XDB();
        }
        return sInstance;
    }

    private static XDB sInstance;

    private XDB() {
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> readAll(Class<T> clazz, boolean bPrivate) {
        DatabaseManager dm = getDatabaseManager(bPrivate);
        SQLiteDatabase db = dm.lockReadableDatabase();
        final String tableName = ClassUtils.getTableName(clazz);
        Cursor c = null;
        List<T> list = new ArrayList<T>();
        try {
            c = db.query(tableName, null, null,
                    null, null, null, null);
            if (c != null && c.moveToFirst()) {
                do {
                    list.add((T) SystemUtils.byteArrayToObject(c.getBlob(c.getColumnIndex("data"))));
                } while (c.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            if (c != null) {
                c.close();
            }
            dm.unlockReadableDatabase(db);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public <T> T readById(String id, Class<T> clazz, boolean bPrivate) {
        DatabaseManager dm = getDatabaseManager(bPrivate);
        SQLiteDatabase db = dm.lockReadableDatabase();
        final String tableName = ClassUtils.getTableName(clazz);
        Cursor c = null;
        try {
            c = db.query(tableName, null, "id='" + id + "'",
                    null, null, null, null);
            if (c != null && c.moveToFirst()) {
                Object o = SystemUtils.byteArrayToObject(c.getBlob(c.getColumnIndex("data")));
                if (clazz.isInstance(o)) {
                    return (T) o;
                }
            }
        } catch (Exception e) {
//			LogUtil.error(e,e,false);
        } finally {
            if (c != null) {
                c.close();
            }
            dm.unlockReadableDatabase(db);
        }
        return null;
    }

    public void delete(IDObject item, boolean bPrivate) {
        DatabaseManager dm = getDatabaseManager(bPrivate);
        SQLiteDatabase db = dm.lockWritableDatabase();
        final String tableName = getTableName(item);
        try {
            db.delete(tableName, "id='" + item.getmId() + "'", null);
        } catch (Exception e) {

        } finally {
            dm.unlockWritableDatabase(db);
        }
    }

    public void deleteAll(Object item, boolean bPrivate) {
        DatabaseManager dm = getDatabaseManager(bPrivate);
        SQLiteDatabase db = dm.lockWritableDatabase();
        final String tableName = getTableName(item);
        try {
            db.delete(tableName, null, null);
        } catch (Exception e) {

        } finally {
            dm.unlockWritableDatabase(db);
        }
    }

    public void updateOrInsert(IDObject item, boolean bPrivate) {
        DatabaseManager dm = getDatabaseManager(bPrivate);
        SQLiteDatabase db = dm.lockWritableDatabase();
        final String tableName = getTableName(item);
        ContentValues cv = new ContentValues();
        try {
            cv.put("data", SystemUtils.objectToByteArray(item));
            int ret = db.update(tableName, cv,
                    "id='" + item.getmId() + "'", null);
            if (ret <= 0) {
                cv.put("id", item.getmId());
                db.insert(tableName, null, cv);
            }
        } catch (Exception e) {

            //e.printStackTrace();
            if (!DBUtils.tabbleIsExist(tableName, db)) {
                db.execSQL("CREATE TABLE " + tableName + " (" +
                        "id" + " TEXT PRIMARY KEY, " +
                        "data" + " BLOB);");
                if (cv.size() > 0) {
                    cv.put("id", item.getmId());
                    db.insert(tableName, null, cv);
                }
            }
        } finally {
            dm.unlockWritableDatabase(db);
        }
    }

    protected DatabaseManager getDatabaseManager(boolean bPrivate) {
        return PublicDatabaseManager.getInstance();
    }

    protected String getTableName(Object item) {
        return ClassUtils.getTableName(item.getClass());
    }
}
