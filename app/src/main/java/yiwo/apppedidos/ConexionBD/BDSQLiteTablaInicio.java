package yiwo.apppedidos.ConexionBD;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BDSQLiteTablaInicio {
    public static final String TABLE_NAME = "Inicio";
    public static final String COL_1 = "Codigo_Emprsa";
    public static final String COL_2 = "RUC_Emprsa";
    public static final String COL_3 = "Codigo_PuntoVenta";
    public static final String COL_4 = "Nombre_PuntoVenta";
    public static final String COL_5 = "Codigo_CentroCostos";
    public static final String COL_6 = "Nombre_CentroCostos";
    public static final String COL_7 = "Codigo_UnidadNegocio";
    public static final String COL_8 = "Nombre_UnidadNegocio";
    public static final String COL_9 = "Codigo_Almacen";
    public static final String COL_10 = "Direccion_Almacen";


    public static final String Create = "create table " + TABLE_NAME + "" +
            " (" +
            "" + COL_1 + " TEXT PRIMARY KEY, " +
            "" + COL_2 + " TEXT, " +
            "" + COL_3 + " TEXT, " +
            "" + COL_4 + " TEXT, " +
            "" + COL_5 + " TEXT, " +
            "" + COL_6 + " TEXT, " +
            "" + COL_7 + " TEXT, " +
            "" + COL_8 + " TEXT, " +
            "" + COL_9 + " TEXT, " +
            "" + COL_10 + " TEXT " +
            ")";

    public static final String Drop = "DROP TABLE IF EXISTS " + TABLE_NAME;


    public static boolean insert(
            String Codigo_Emprsa,
            String RUC_Emprsa,
            String Codigo_PuntoVenta,
            String Nombre_PuntoVenta,
            String Codigo_CentroCostos,
            String Nombre_CentroCostos,
            String Codigo_UnidadNegocio,
            String Nombre_UnidadNegocio,
            String Codigo_Almacen,
            String Direccion_Almacen,

            SQLiteOpenHelper sqLiteOpenHelper) {
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, Codigo_Emprsa);
        contentValues.put(COL_2, RUC_Emprsa);
        contentValues.put(COL_3, Codigo_PuntoVenta);
        contentValues.put(COL_4, Nombre_PuntoVenta);
        contentValues.put(COL_5, Codigo_CentroCostos);
        contentValues.put(COL_6, Nombre_CentroCostos);
        contentValues.put(COL_7, Codigo_UnidadNegocio);
        contentValues.put(COL_8, Nombre_UnidadNegocio);
        contentValues.put(COL_9, Codigo_Almacen);
        contentValues.put(COL_10, Direccion_Almacen);
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

    public static void deleteAllData(SQLiteOpenHelper sqLiteOpenHelper) {
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        db.execSQL("Delete from " + TABLE_NAME);
    }
}