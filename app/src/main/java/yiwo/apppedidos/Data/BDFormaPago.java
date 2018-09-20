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

public class BDFormaPago {

    BDConexionSQL bdata = new BDConexionSQL();

    public ArrayList<List<String>> getList(String Nombre) {

        ArrayList<List<String>> arrayList = new ArrayList<>();

        try {

            Connection connection = bdata.getConnection();

            String stsql = "Select * from dbo.udf_list_hformapg(?,?) where Nombre like ? or codigo like ? ";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, CodigosGenerales.Codigo_Empresa); // Código de la empresa
            query.setString(2, CodigosGenerales.Codigo_Cliente);   //Código del Cliente
            query.setString(3, Nombre + "%"); //Código de la Forma de Pago
            query.setString(4, Nombre + "%"); //Nombre de la Forma de Pago

            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                arrayList.add(Arrays.asList(
                        rs.getString("codigo"),
                        rs.getString("Nombre"),
                        rs.getString("ndias")//Cantidad de Dias para sumar
                ));
            }
            connection.close();

        } catch (Exception e) {
            Log.d("BDFormaPago", "- getList: " + e.getMessage());
        }
        return arrayList;
    }

    public List<String> getPredeterminado() {

        List<String> arrayList = new ArrayList<>();

        try {

            Connection connection = bdata.getConnection();

            String stsql = "Select * from dbo.udf_list_hformapg(?,?) where sel =? ";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, CodigosGenerales.Codigo_Empresa); // Código de la empresa
            query.setString(2, CodigosGenerales.Codigo_Cliente);   //Código del Cliente
            query.setString(3, "S"); //Predeterminado

            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                arrayList.add(rs.getString( "codigo"));
                arrayList.add(rs.getString("Nombre"));
                arrayList.add(rs.getString("ndias"));//Cantidad de Dias para sumar
            }
            connection.close();

        } catch (Exception e) {
            Log.d("BDFormaPago", "- getList: " + e.getMessage());
        }
        return arrayList;
    }
}