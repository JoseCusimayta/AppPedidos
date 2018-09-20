package yiwo.apppedidos.ConexionBD;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDSQLiteTablaConfiguracion {
    public static final String TABLE_NAME = "configuraciones";
    public static final String COL_1 = "RUC_Emprsa";
    public static final String COL_2 = "ip_lan";
    public static final String COL_3 = "ip_publica";
    public static final String COL_4 = "isIP_Lan";


    public static final String Create = "create table " + TABLE_NAME + "" +
            " (" +
            "" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "" + COL_2 + " TEXT, " +
            "" + COL_3 + " TEXT, " +
            "" + COL_4 + " TEXT " +
            ")";

    public static final String Drop = "DROP TABLE IF EXISTS " + TABLE_NAME;


    public static boolean insert(String RUC_Emprsa,
                                 String ip_lan,
                                 String ip_publica,
                                 String isIP_Lan,
                                 SQLiteOpenHelper sqLiteOpenHelper) {
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, RUC_Emprsa);
        contentValues.put(COL_2, ip_lan);
        contentValues.put(COL_3, ip_publica);
        contentValues.put(COL_4, isIP_Lan);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }


    public static Cursor getAllData(SQLiteOpenHelper sqLiteOpenHelper) {
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        return db.rawQuery("select * from " + TABLE_NAME, null);
    }


    public boolean update(String Ruc_Emppresa, SQLiteOpenHelper sqLiteOpenHelper) {
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, Ruc_Emppresa);
        db.update(TABLE_NAME, contentValues, COL_1 + " = ?", new String[]{Ruc_Emppresa});
        return true;
    }

    public Integer deleteData(String Ruc_Emppresa, SQLiteOpenHelper sqLiteOpenHelper) {
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        return db.delete(TABLE_NAME, COL_1 + " = ?", new String[]{Ruc_Emppresa});
    }

    public void deleteAllData(SQLiteOpenHelper sqLiteOpenHelper) {
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        db.execSQL("Delete from " + TABLE_NAME);
    }
}
