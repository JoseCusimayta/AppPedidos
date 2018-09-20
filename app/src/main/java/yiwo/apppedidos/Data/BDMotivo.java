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

public class BDMotivo {

    BDConexionSQL bdata= new BDConexionSQL();

    public ArrayList<List<String>> getList(String Nombre) {

        ArrayList<List<String>> arrayList = new ArrayList<>();

        try {

            Connection connection = bdata.getConnection();

            String stsql = "SELECT erp_codmot, erp_nommot FROM erp_motivos_tramite WHERE erp_codemp = ? and erp_codtid = 'PED' and (erp_codmot like ? or erp_nommot like ?) ORDER BY erp_predeterminado DESC, erp_codmot";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, CodigosGenerales.Codigo_Empresa); // Código de la empresa
            query.setString(2, Nombre+"%"); //Código del Motivo
            query.setString(3, Nombre+"%"); //Nombre del Motivo

            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                arrayList.add(Arrays.asList(
                        rs.getString("erp_codmot"),     //Código del Motivo
                        rs.getString("erp_nommot"),     //Nombre del Motivo
                        null
                        )
                );
            }
            connection.close();

        } catch (Exception e) {
            Log.d("BDMotivo", "- getList: " + e.getMessage());
        }
        return arrayList;
    }

    public Boolean CorrelativoExistente(String CodMotivo) {
        String Nombre = "";
        try {

            Connection connection = bdata.getConnection();

            String stsql = "select erp_nummot from erp_motivos_tramite_detalle where erp_codemp= ?  and erp_sermot= ? and erp_codmot=? ";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, CodigosGenerales.Codigo_Empresa); // Código de la empresa
            query.setString(2, CodigosGenerales.Year); //Año del correlativo
            query.setString(3, CodMotivo); //Codigo del motivo

            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                Nombre = rs.getString("erp_nummot");
            }
            connection.close();
            return Nombre.length() > 0;

        } catch (Exception e) {
            Log.d("BDMotivo", "- CorrelativoExistente: " + e.getMessage());
        }
        return false;
    }


    public boolean ActualizarCorrelativo() {
        try {

            Connection connection = bdata.getConnection();

            String stsql = "update erp_motivos_tramite_detalle set erp_nummot=erp_nummot+1 where erp_codemp=? and erp_codmot=? and erp_sermot=?";
            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, CodigosGenerales.Codigo_Empresa);
            query.setString(2, CodigosGenerales.Codigo_Motivo);
            query.setString(3,CodigosGenerales.Year);

            query.execute();
            return true;
        } catch (Exception e) {
            Log.d("BDMotivo", "- ActualizarCorrelativo: " + e.getMessage());
        }
        return false;
    }


    public String getNuevoCodigoPedido() {
        String NuevoCodigoPedido;
        try {
            Integer Correlativo=0;
            Connection connection = bdata.getConnection();

            String stsql = "select erp_nummot from erp_motivos_tramite_detalle where erp_codemp=? and erp_codmot=? and erp_sermot=? ";
            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, CodigosGenerales.Codigo_Empresa);
            query.setString(2, CodigosGenerales.Codigo_Motivo);
            query.setString(3, CodigosGenerales.Year);

            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                Correlativo= rs.getInt("erp_nummot");
            }
            connection.close();
            Correlativo+=1;
            NuevoCodigoPedido= CodigosGenerales.Year +"-"+String.format("%0"+getCantidadCeros()+"d", Correlativo);
        } catch (Exception e) {
            NuevoCodigoPedido= CodigosGenerales.Year +"-"+String.format("%0"+getCantidadCeros()+"d", 0);
            Log.d("BDPedido", "- getCorrelativo: "+e.getMessage());
        }
        return NuevoCodigoPedido;
    }

    private String getCantidadCeros() {
        String CantidadCeros="0";
        try {

            Connection connection = bdata.getConnection();

            String stsql = "select cantidad_caracteres from Htipdoc where ccod_empresa =? and ctip_doc ='PED' ";
            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, CodigosGenerales.Codigo_Empresa);

            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                CantidadCeros=rs.getString("cantidad_caracteres");
            }
            connection.close();

        } catch (Exception e) {
            Log.d("BDPedido", "- getCantidadCeros: "+e.getMessage());
        }
        return CantidadCeros;
    }
}
