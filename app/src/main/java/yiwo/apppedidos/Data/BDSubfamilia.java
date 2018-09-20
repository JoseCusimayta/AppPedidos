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

public class BDSubfamilia    {

    BDConexionSQL bdata= new BDConexionSQL();

    public List<String> getNombres() {
        List<String>listaNombres = new ArrayList<String>();

        try {
            Connection connection = bdata.getConnection();


            String stsql = "select cnom_subfamilia from Hsubfamilia_art where ccod_empresa=? ";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, CodigosGenerales.Codigo_Empresa); // Codigo de la empresa

            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                listaNombres.add(rs.getString("cnom_subfamilia"));
            }
            connection.close();

        } catch (Exception e) {
            Log.d("BDSubfamilia", "- getNombres: "+e.getMessage());
        }
        return listaNombres;
    }

    public ArrayList<List<String>> getList(String Nombre) {

        ArrayList<List<String>> arrayList = new ArrayList<>();

        try {

            Connection connection = bdata.getConnection();

            String stsql = "select ccod_subfamilia, cnom_subfamilia from Hsubfamilia_art where ccod_empresa=? and (ccod_subfamilia like ? or cnom_subfamilia like ?)";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, CodigosGenerales.Codigo_Empresa); // Código de la empresa
            query.setString(2, Nombre+"%"); //Código de la SubFamilia
            query.setString(3, Nombre+"%"); //Nombre de la SubFamilia

            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                arrayList.add(Arrays.asList(
                        rs.getString("ccod_subfamilia"),       //Código de Familia
                        rs.getString("cnom_subfamilia"))); //Nombre de Familia
            }
            connection.close();

        } catch (Exception e) {
            Log.d("BDSubfamilia", "- getList: "+e.getMessage());
        }
        return arrayList;
    }
}
