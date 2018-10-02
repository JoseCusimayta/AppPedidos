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

public class BDCentroCostos {

    BDConexionSQL bdata= new BDConexionSQL();

    public ArrayList<List<String>> getList(String Nombre){
        ArrayList<List<String>> arrayList = new ArrayList<>();
        try {
            Connection connection = bdata.getConnection();

            String stsql = "select * from dbo.udf_list_hcencos(?) where codigo like ? or nombre like ? order by codigo";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, ConfiguracionEmpresa.Codigo_Empresa); // Código de la empresa
            query.setString(2, Nombre+"%"); //Código del Centro de Costos
            query.setString(3, Nombre+"%"); //Nombre del Centro de Costos

            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                arrayList.add(Arrays.asList(
                        rs.getString("codigo"),
                        rs.getString("Nombre"),
                        null
                ));
            }
            connection.close();

        } catch (Exception e) {
            Log.d("BDCentroCostos", "- getList: "+e.getMessage());
        }
        return arrayList;
    }


    public List<String> getPredeterminado(Connection connection) {

        List<String> list=new ArrayList<>();

        try {
//            Connection connection = bdata.getConnection();

            String stsql = "select TOP(1) * from dbo.udf_list_hcencos(?) order by codigo";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, ConfiguracionEmpresa.Codigo_Empresa);
            ResultSet rs = query.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("codigo"));
                list.add(rs.getString("nombre"));
            }
//            connection.close();

        } catch (Exception e) {
            Log.d("BDEmpresa", "- getListaArticulos: "+e.getMessage());
        }
        return list;
    }
}
