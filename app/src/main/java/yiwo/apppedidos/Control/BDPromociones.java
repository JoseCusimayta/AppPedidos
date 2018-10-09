package yiwo.apppedidos.Control;

import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.AspectosGenerales.ConfiguracionEmpresa;
import yiwo.apppedidos.ConexionBD.BDConexionSQL;

public class BDPromociones {

    String TAG = "BDPromociones";
    BDConexionSQL bdata = new BDConexionSQL();

    public List<String> getPromociones(Connection connection, String Codigo_Producto, String Cantidad) {
        List<String> arrayList = getPromocionesCantidadIntermedia(connection, Codigo_Producto, Cantidad);
        if (arrayList == null || arrayList.isEmpty())
            arrayList = getPromocionesCantidadSuperada(connection, Codigo_Producto, Cantidad);
        return arrayList;
    }

    public List<String> getPromocionesCantidadIntermedia(Connection connection, String Codigo_Producto, String Cantidad) {

        Log.d(TAG,"Codigo_Empresa "+ ConfiguracionEmpresa.Codigo_Empresa);
        Log.d(TAG,"Codigo_Producto "+ Codigo_Producto);
        Log.d(TAG,"Cantidad "+Cantidad);
        List<String> arrayList = new ArrayList<>();
        try {
            String stsql = " select desc01, desc02,desc03,desc04 " +
                    " from Erp_Promociones " +
                    " where " +
                    " erp_codemp=? " +
                    " and erp_tipo='Item' " +
                    " and erp_codigo=? " +
                    " and erp_motivo='RM' and (erp_cant_ini<=? and erp_cant_fin>=?)  " +
                    " and erp_vcto_ini<=GETDATE() and erp_vcto_fin>=GETDATE()";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, ConfiguracionEmpresa.Codigo_Empresa); // Codigo de Empresa
            query.setString(2, Codigo_Producto); // Codigo de Producto
            query.setString(3, Cantidad); // Cantidad de productos ingresados
            query.setString(4, Cantidad); // Cantidad de productos ingresados

            ResultSet rs = query.executeQuery();
            while (rs.next()) {
                String desc01, desc02, desc03, desc04;
                desc01 = rs.getString("desc01");
                desc02 = rs.getString("desc02");
                desc03 = rs.getString("desc03");
                desc04 = rs.getString("desc04");
                Log.d("Descuento1", "--- " + desc01);
                if (desc01 == null || desc01.isEmpty()) {
                    desc01 = "0";
                }
                if (desc02 == null || desc02.isEmpty()) {
                    desc02 = "0";
                }
                if (desc03 == null || desc03.isEmpty()) {
                    desc03 = "0";
                }
                if (desc04 == null || desc04.isEmpty()) {
                    desc04 = "0";
                }

                arrayList.add(desc01);  //desc01
                arrayList.add(desc02);  //desc02
                arrayList.add(desc03);  //desc03
                arrayList.add(desc04);  //desc04
            }

            Log.d(TAG, "getPromocionesCantidadIntermedia- " + arrayList);
        } catch (Exception e) {
            Log.d(TAG, "- getPromocionesCantidadIntermedia: " + e.getMessage());
        }
        return arrayList;
    }
    public List<String> getPromocionesCantidadSuperada(Connection connection, String Codigo_Producto, String Cantidad) {

        Log.d(TAG,"Codigo_Empresa "+ ConfiguracionEmpresa.Codigo_Empresa);
        Log.d(TAG,"Codigo_Producto "+ Codigo_Producto);
        Log.d(TAG,"Cantidad "+Cantidad);
        List<String> arrayList = new ArrayList<>();

        arrayList.add("0");  //descuento 1
        arrayList.add("0");  //descuento 2
        arrayList.add("0");  //descuento 3
        arrayList.add("0");  //descuento 4
        arrayList.add("0");  //descuento único
        try {


            String stsql = " \n" +
                    "select TOP(1)  desc01, desc02,desc03,desc04, erp_cant_fin\n" +
                    "from Erp_Promociones \n" +
                    "where \n" +
                    "erp_codemp=?\n" +
                    "and erp_tipo='Item' \n" +
                    "and erp_codigo=?\n" +
                    "and erp_motivo='RM' and erp_cant_fin<=?\n" +
                    "and erp_vcto_ini<=GETDATE() and erp_vcto_fin>=GETDATE() order by erp_cant_fin desc";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, ConfiguracionEmpresa.Codigo_Empresa); // Codigo de Empresa
            query.setString(2, Codigo_Producto); // Codigo de Producto
            query.setString(3, Cantidad); // Cantidad de productos ingresados

            ResultSet rs = query.executeQuery();
            while (rs.next()) {
                arrayList.clear();
                String desc01, desc02, desc03, desc04;
                desc01 = rs.getString("desc01");
                desc02 = rs.getString("desc02");
                desc03 = rs.getString("desc03");
                desc04 = rs.getString("desc04");
                Log.d("Descuento1", "--- " + desc01);
                if (desc01 == null || desc01.isEmpty()) {
                    desc01 = "0";
                }
                if (desc02 == null || desc02.isEmpty()) {
                    desc02 = "0";
                }
                if (desc03 == null || desc03.isEmpty()) {
                    desc03 = "0";
                }
                if (desc04 == null || desc04.isEmpty()) {
                    desc04 = "0";
                }

                arrayList.add(desc01);  //desc01
                arrayList.add(desc02);  //desc02
                arrayList.add(desc03);  //desc03
                arrayList.add(desc04);  //desc04
            }

            Log.d(TAG, "getPromocionesCantidadSuperada- " + arrayList);
        } catch (Exception e) {
            Log.d(TAG, "- getPromocionesCantidadSuperada: " + e.getMessage());
        }
        return arrayList;
    }
}