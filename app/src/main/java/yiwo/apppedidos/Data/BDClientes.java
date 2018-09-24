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

public class BDClientes {

    BDConexionSQL bdata = new BDConexionSQL();


    public ArrayList<List<String>> getList(String Nombre) {
        ArrayList<List<String>> arrayList = new ArrayList<>();
        try {
            if(CodigosGenerales.listCliente.size()>0 && Nombre.equals("")){
                return  CodigosGenerales.listCliente;
            }else {
                Connection connection = bdata.getConnection();
                String stsql=
                        "Select TOP (50) ccod_cliente, cnom_cliente, cdireccion, cnum_ruc,cnum_dni, lista_precios  \n" +
                        "From Hcliente \n" +
                        "Where \n" +
                        "ccod_empresa = ? And cgrupo_cliente = '12' and lista_precios!='' and (ccod_cliente like ? or cnom_cliente like ?)";

                PreparedStatement query = connection.prepareStatement(stsql);
                query.setString(1, CodigosGenerales.Codigo_Empresa); // Codigo de la empresa
                query.setString(2, Nombre + "%"); //Código del Cliente
                query.setString(3, Nombre + "%"); //Código del Cliente

                ResultSet rs = query.executeQuery();

                while (rs.next()) {
                    arrayList.add(Arrays.asList(
                            rs.getString("ccod_cliente"),
                            rs.getString("cnom_cliente"),
                            rs.getString("cnum_ruc"),
                            rs.getString("cdireccion"),
                            rs.getString("lista_precios"),
                            rs.getString("cnum_dni")
                    ));
                }
                connection.close();

                if (Nombre.equals(""))
                    CodigosGenerales.listCliente=arrayList;
            }

        } catch (Exception e) {
            Log.d("BDClientes", "- getList: " + e.getMessage());
        }
        return arrayList;
    }

}