package yiwo.apppedidos.ConexionBD;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BDConexionSQLite extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Pedidosdb";

    public BDConexionSQLite(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BDSQLiteTablaLogin.Create);
        db.execSQL(BDSQLiteTablaConfiguracion.Create);
        db.execSQL(BDSQLiteTablaInicio.Create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(BDSQLiteTablaLogin.Drop);
        db.execSQL(BDSQLiteTablaConfiguracion.Drop);
        db.execSQL(BDSQLiteTablaInicio.Drop);
        onCreate(db);
    }

    public boolean insertarLogin(String Codigo_Empresa,
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
                                 String Nombre_PuntoVenta,
                                 String Nombre_CentroCostos,
                                 String Nombre_UnidadNegocio,
                                 String Codigo_Vendedor) {
        BDSQLiteTablaInicio.deleteAllData(this);
        return BDSQLiteTablaLogin.insert(
                Codigo_Empresa,
                Codigo_PuntoVenta,
                Codigo_Almacen,
                Codigo_Usuario,
                Codigo_CentroCostos,
                Codigo_UnidadNegocio,
                Tipo_Moneda,
                Direccion_Almacen,
                Nombre_Vendedor,
                Celular_Vendedor,
                email_Vendedor,
                RUC_Empresa,
                Codigo_Vendedor,
                this)
                &&
                BDSQLiteTablaInicio.insert(
                        Codigo_Empresa,
                        RUC_Empresa,
                        Codigo_PuntoVenta,
                        Nombre_PuntoVenta,
                        Codigo_CentroCostos,
                        Nombre_CentroCostos,
                        Codigo_UnidadNegocio,
                        Nombre_UnidadNegocio,
                        Codigo_Almacen,
                        Direccion_Almacen,
                        this);
    }

    public Cursor getDataLogin() {
        return BDSQLiteTablaLogin.getAllData(this);
    }

    public Cursor getDataEmpresa() {
        return BDSQLiteTablaInicio.getAllData(this);
    }

    public void deleteAllDataLogin() {
        BDSQLiteTablaLogin.deleteAllData(this);
    }

    public void deleteAllDataEmpresa() {
        BDSQLiteTablaInicio.deleteAllData(this);
    }

    public boolean insertarConfiguracion(String RUC_Emprsa,
                                         String ip_lan,
                                         String ip_publica,
                                         String isIP_Lan) {

        return BDSQLiteTablaConfiguracion.insert(
                RUC_Emprsa,
                ip_lan,
                ip_publica,
                isIP_Lan,
                this);
    }

    public Cursor getDataConfiguracion() {
        return BDSQLiteTablaConfiguracion.getAllData(this);
    }

}