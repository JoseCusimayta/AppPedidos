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

public class BDFamilia {

    BDConexionSQL bdata= new BDConexionSQL();

    public List<String> getNombres() {
        List<String>listaNombres = new ArrayList<String>();

        try {
            Connection connection = bdata.getConnection();

            String stsql = "select cnom_familia from Hfam_art where ccod_empresa=? and cfamilia!='656' and cfamilia!='655' ";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, CodigosGenerales.Codigo_Empresa); // Codigo de la empresa

            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                listaNombres.add(rs.getString("cnom_familia"));
            }
            connection.close();

        } catch (Exception e) {
            Log.d("BDFamilia", "- getNombres: "+e.getMessage());
        }
        return listaNombres;
    }

    public ArrayList<List<String>> getList(String Nombre) {

        ArrayList<List<String>> arrayList = new ArrayList<>();

        try {

            Connection connection = bdata.getConnection();

            String stsql = "select cfamilia, cnom_familia from Hfam_art where ccod_empresa=? and (cfamilia like ? or cnom_familia like ?) and cfamilia!='656' and cfamilia!='655'";
            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, CodigosGenerales.Codigo_Empresa); // Código de la empresa
            query.setString(2, Nombre+"%"); //Código de la Familia
            query.setString(3, Nombre+"%"); //Nombre de la Familia

            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                arrayList.add(Arrays.asList(
                        rs.getString("cfamilia"),       //Código de Familia
                        rs.getString("cnom_familia") //Nombre de Familia
                ));
            }
            connection.close();
        } catch (Exception e) {
            Log.d("BDFamilia", "- getList: "+e.getMessage());
        }

        return arrayList;
    }
}
