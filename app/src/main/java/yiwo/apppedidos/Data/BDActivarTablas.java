package yiwo.apppedidos.Data;

import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;

import yiwo.apppedidos.ConexionBD.BDConexionSQL;

public class BDActivarTablas {
    String TAG="BDActivarFunciones";
    BDConexionSQL bdata = new BDConexionSQL();
    Boolean Actualizar=false;
    public void ActualizarTablas(){
        if(Actualizar) {
            Actualizar_Tabla_HListDeseo();
        }
    }
    private void Actualizar_Tabla_HListDeseo(){
        try {
            Connection connection = bdata.getConnection();

            String stsql = "\n" +
                    "create table HListDeseo(\n" +
                    "ccod_empresa varchar (5),\n" +
                    "erp_coduser varchar (20),\n" +
                    "ccod_ptovta varchar (5),\n" +
                    "ccod_almacen varchar (5),\n" +
                    "nitem int,\n" +
                    "ccod_articulo varchar (35),\n" +
                    "cunidad varchar (10),\n" +
                    "ncantidad decimal (18,2),\n" +
                    "precio decimal (18,4),\n" +
                    "descuento_1  decimal (18,2),\n" +
                    "descuento_2  decimal (18,2),\n" +
                    "descuento_3  decimal (18,2),\n" +
                    "descuento_4  decimal (18,2),\n" +
                    "Lp varchar (5)\n" +
                    ")\n";

            PreparedStatement query = connection.prepareStatement(stsql);

            query.execute();

            connection.close();

        } catch (Exception e) {
            Log.d(TAG, "- ActualizarFuncion_udf_list_harticul: " + e.getMessage());
        }
    }
}
