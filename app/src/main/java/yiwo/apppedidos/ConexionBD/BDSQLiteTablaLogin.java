package yiwo.apppedidos.ConexionBD;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BDSQLiteTablaLogin {
    public static final String TABLE_NAME = "login";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "Codigo_Empresa";
    public static final String COL_3 = "Codigo_PuntoVenta";
    public static final String COL_4 = "Codigo_Almacen";
    public static final String COL_5 = "Codigo_Usuario";
    public static final String COL_6 = "Codigo_CentroCostos";
    public static final String COL_7 = "Codigo_UnidadNegocio";
    public static final String COL_8 = "Tipo_Moneda";
    public static final String COL_9 = "Direccion_Almacen";
    public static final String COL_10 = "Nombre_Vendedor";
    public static final String COL_11 = "Celular_Vendedor";
    public static final String COL_12 = "email_Vendedor";
    public static final String COL_13 = "RUC_Empresa";

    public static final String Create="create table " + TABLE_NAME +"" +
            " (" +
            ""+COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ""+COL_2+" TEXT, " +
            ""+COL_3+" TEXT, " +
            ""+COL_4+" TEXT, " +
            ""+COL_5+" TEXT, " +
            ""+COL_6+" TEXT, " +
            ""+COL_7+" TEXT, " +
            ""+COL_8+" TEXT, " +
            ""+COL_9+" TEXT, " +
            ""+COL_10+" TEXT, " +
            ""+COL_11+" TEXT, " +
            ""+COL_12+" TEXT, " +
            ""+COL_13+" TEXT" +
            ")";
    public static final String Drop="DROP TABLE IF EXISTS "+TABLE_NAME;


    public static boolean insert(String Codigo_Empresa,
                              String Codigo_PuntoVenta,
                              String Codigo_Almacen,
                              String Codigo_Usuario,
                              String Codigo_CentroCostos,
                              String Codigo_UnidadNegocio,
                              String Tipo_Moneda,
                              String Direccion_Almacen,
                              String Nombre_Vendedor,
                              String Celular_Vendedor,
                              String email_Vendedor,
                              String RUC_Empresa,
                              SQLiteOpenHelper sqLiteOpenHelper) {
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,Codigo_Empresa);
        contentValues.put(COL_3,Codigo_PuntoVenta);
        contentValues.put(COL_4,Codigo_Almacen);
        contentValues.put(COL_5,Codigo_Usuario);
        contentValues.put(COL_6,Codigo_CentroCostos);
        contentValues.put(COL_7,Codigo_UnidadNegocio);
        contentValues.put(COL_8,Tipo_Moneda);
        contentValues.put(COL_9,Direccion_Almacen);
        contentValues.put(COL_10,Nombre_Vendedor);
        contentValues.put(COL_11,Celular_Vendedor);
        contentValues.put(COL_12,email_Vendedor);
        contentValues.put(COL_13,RUC_Empresa);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        return result != -1;
    }


    public static Cursor getAllData(SQLiteOpenHelper sqLiteOpenHelper)  {
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_NAME,null);
    }
    public static boolean update(String id, String CodEmp,SQLiteOpenHelper sqLiteOpenHelper) {
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,CodEmp);
        db.update(TABLE_NAME, contentValues, COL_1+" = ?",new String[] { id });
        return true;
    }

    public static Integer deleteData ( String id, SQLiteOpenHelper sqLiteOpenHelper) {
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        return db.delete(TABLE_NAME, COL_1+" = ?",new String[] {id});
    }
    public static void deleteAllData ( SQLiteOpenHelper sqLiteOpenHelper) {
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        db.execSQL("Delete from "+TABLE_NAME);
    }
}
