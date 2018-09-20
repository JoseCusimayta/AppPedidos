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

public class BDConceptos {

    BDConexionSQL bdata= new BDConexionSQL();


    public List<String>  getNombres() {
        List<String>  listaNombres = new ArrayList<String>();
        try {
            Connection connection = bdata.getConnection();

            String stsql = "select erp_nomcon from Erp_concepto"+CodigosGenerales.ID_Concepto+" where erp_codemp=? ";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, CodigosGenerales.Codigo_Empresa); // Codigo de la empresa

            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                listaNombres.add(rs.getString("erp_nomcon"));
            }
            connection.close();

        } catch (Exception e) {
            Log.d("BDConcepto", "- getNombres: "+e.getMessage());
        }
        return listaNombres;
    }
    public ArrayList<List<String>> getNombresConceptos() {

        ArrayList<List<String>> arrayList = new ArrayList<>();

        try {
            Connection connection = bdata.getConnection();

            String stsql = "select * from Htableconceptos_erp where ccod_empresa=?";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, CodigosGenerales.Codigo_Empresa); // Codigo de la empresa

            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                arrayList.add(Arrays.asList(
                        rs.getString("ccod_empresa"),       //Código de la empresa
                        rs.getString("nombre"),             //Nombre del Concepto
                        rs.getString("id")                  //Número del Concepto
                ));
            }
            connection.close();

        } catch (Exception e) {
            Log.d("BDConcepto", "- getNombresConceptos: "+e.getMessage());
        }
        return arrayList;
    }

    public ArrayList<List<String>> getList(String Nombre) {

        ArrayList<List<String>> arrayList = new ArrayList<>();

        try {
            Connection connection = bdata.getConnection();

            String stsql = "select * from Erp_concepto"+CodigosGenerales.ID_Concepto +" where erp_codemp=? and (erp_codcon like ? or erp_nomcon like ?)";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, CodigosGenerales.Codigo_Empresa); // Codigo de la empresa
            query.setString(2, Nombre+"%"); //Codigo del Concepto
            query.setString(3, Nombre+"%"); //Nombre del Concepto

            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                arrayList.add(Arrays.asList(
                        rs.getString("erp_codcon"),
                        rs.getString("erp_nomcon"),
                        rs.getString("erp_abrcon")));
            }
            connection.close();

        } catch (Exception e) {
            Log.d("BDConcepto", "- getList: "+e.getMessage());
        }
        return arrayList;
    }
}
