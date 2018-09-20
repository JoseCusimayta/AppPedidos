package yiwo.apppedidos.Data;

import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.ConexionBD.BDConexionSQL;

public class BDPuntoVenta {

    BDConexionSQL bdata = new BDConexionSQL();

    public ArrayList<List<String>> getList(String Nombre) {

        ArrayList<List<String>> arrayList = new ArrayList<>();
        try {
            Connection connection = bdata.getConnection();

            String stsql = "Select ccod_almacen, cnom_almacen, erp_codalmacen_ptovta from Halmacen where ccod_empresa = ? and  ( cnom_almacen like ? or ccod_almacen like ? )  order by ccod_almacen";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, CodigosGenerales.Codigo_Empresa); // Codigo de la empresa
            query.setString(2, Nombre + "%"); //Codigo del Punto de Venta
            query.setString(3, Nombre + "%"); //Nombre del Punto de Venta

            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                arrayList.add(Arrays.asList(
                        rs.getString("ccod_almacen"),
                        rs.getString("cnom_almacen"),
                        rs.getString("erp_codalmacen_ptovta"),
                        rs.getString("cdireccion")));
            }
            connection.close();

        } catch (Exception e) {
            Log.d("BDPuntoVenta", "- getList: " + e.getMessage());
        }
        return arrayList;
    }

    public List<String> getPredeterminado(String cod_emp) {

        List<String> list = new ArrayList<>();

        try {
            Connection connection = bdata.getConnection();

            String stsql = "Select TOP(1) ccod_almacen, cnom_almacen, erp_codalmacen_ptovta, cdireccion from Halmacen where ccod_empresa = ?  and ccod_almacen!='00' order by ccod_almacen";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, CodigosGenerales.Codigo_Empresa); // Codigo de la empresa

            ResultSet rs = query.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("ccod_almacen"));
                list.add(rs.getString("cnom_almacen"));
                list.add(rs.getString("erp_codalmacen_ptovta"));
                list.add(rs.getString("cdireccion"));
            }
            connection.close();

        } catch (Exception e) {
            Log.d("BDEmpresa", "- getPuntoVenta: " + e.getMessage());
        }
        return list;
    }
}