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

public class BDEmpresa {

    BDConexionSQL bdata = new BDConexionSQL();

    public ArrayList<List<String>> getList(String nombre) {

        ArrayList<List<String>> arrayList = new ArrayList<>();

        try {
            if (CodigosGenerales.listEmpresas.size() > 0 && nombre.equals("")) {
                return CodigosGenerales.listEmpresas;
            } else {
                Connection connection = bdata.getConnection();

                //String stsql = "select ccod_empresa,crazonsocial,cnum_ruc from Hempresa where ccod_empresa='02' and (crazonsocial like ?  or ccod_empresa like ?) order by ccod_empresa";
                String stsql = "select ccod_empresa,crazonsocial,cnum_ruc from Hempresa where (crazonsocial like ?  or ccod_empresa like ?) order by ccod_empresa";
                PreparedStatement query = connection.prepareStatement(stsql);
                query.setString(1, nombre + "%"); //Razón Social de la empresa
                query.setString(2, nombre + "%"); //Código de la empresa
                ResultSet rs = query.executeQuery();
                while (rs.next()) {
                    arrayList.add(Arrays.asList(
                            rs.getString("ccod_empresa"),       //Código de la empresa
                            rs.getString("crazonsocial"),       //Razón Social de la empresa
                            rs.getString("cnum_ruc")            //Número de Ruc de la empresa
                    ));
                }
                connection.close();

                if (nombre.equals(""))
                    CodigosGenerales.listEmpresas = arrayList;
            }
        } catch (Exception e) {
            Log.d("BDEmpresa", "- getList: " + e.getMessage());
        }
        return arrayList;
    }

    public String getMonedaTrabajo(Connection connection) {

        String monedaTrabajo = "S/.";

        try {


            String stsql = "select moneda_trabajo from Hconfiguraciones_2 where idempresa=?";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, CodigosGenerales.Codigo_Empresa); //Código de la empresa
            ResultSet rs = query.executeQuery();
            while (rs.next()) {
                monedaTrabajo = rs.getString("moneda_trabajo"); //Tipo de moneda de la empresa
            }

        } catch (Exception e) {
            Log.d("BDEmpresa", "- getMonedaTrabajo: " + e.getMessage());
        }
        return monedaTrabajo;
    }

    /*
    ctip	tc
    VTA	3.272
     */
    public List<String> getTipoCambio(Connection connection) {
        //select ntc_venta, ntc_compra,ntc_especial from hcalenda where  CONVERT(date, dfecha) =CONVERT(date, getdate())
        List<String> list = new ArrayList<>();

        try {


            String stsql =
                    "select\n" +
                            "(Select ctipo_cambio from hconfiguraciones_2 where idempresa = ?) Tipo,\n" +
                            "(Select (CASE ctipo_cambio when 'VTA' then ntc_venta When 'COM' then ntc_compra else ntc_especial END) from hconfiguraciones_2 where idempresa = ?) Valor\n" +
                            "from hcalenda where Convert(varchar, dfecha,111) = convert(varchar,getdate(),111)";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, CodigosGenerales.Codigo_Empresa); //Código de la empresa
            query.setString(2, CodigosGenerales.Codigo_Empresa); //Código de la empresa
            ResultSet rs = query.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("Tipo")); //Valor para el tipo de cambio
                list.add(rs.getString("Valor")); //Tipo de cambio (especial, venta o compra)
            }

        } catch (Exception e) {
            Log.d("BDEmpresa", "- getTipoCambio: " + e.getMessage());
        }
        return list;
    }

    public List<String> getTipoCambio() {
        //select ntc_venta, ntc_compra,ntc_especial from hcalenda where  CONVERT(date, dfecha) =CONVERT(date, getdate())
        List<String> list = new ArrayList<>();

        try {


            Connection connection = bdata.getConnection();
            String stsql =
                    "select\n" +
                            "(Select ctipo_cambio from hconfiguraciones_2 where idempresa = ?) Tipo,\n" +
                            "(Select (CASE ctipo_cambio when 'VTA' then ntc_venta When 'COM' then ntc_compra else ntc_especial END) from hconfiguraciones_2 where idempresa = ?) Valor\n" +
                            "from hcalenda where Convert(varchar, dfecha,111) = convert(varchar,getdate(),111)";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, CodigosGenerales.Codigo_Empresa); //Código de la empresa
            query.setString(2, CodigosGenerales.Codigo_Empresa); //Código de la empresa
            ResultSet rs = query.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("Tipo")); //Valor para el tipo de cambio
                list.add(rs.getString("Valor")); //Tipo de cambio (especial, venta o compra)
            }

            connection.close();
        } catch (Exception e) {
            Log.d("BDEmpresa", "- getTipoCambio: " + e.getMessage());
        }
        return list;
    }

    public ArrayList<List<String>> getMonedas() {
        ArrayList<List<String>> arrayList = new ArrayList<>();

        try {

            Connection connection = bdata.getConnection();

            String stsql = "select * from Htipmon where ccod_empresa = ?";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, CodigosGenerales.Codigo_Empresa); //Código de la empresa
            ResultSet rs = query.executeQuery();
            while (rs.next()) {
                arrayList.add(Arrays.asList(
                        rs.getString("cmoneda"),
                        rs.getString("cdes_tipmon"),
                        rs.getString("ccod_tipmon")
                ));

            }
            connection.close();

        } catch (Exception e) {
            Log.d("BDEmpresa", "- getMonedas: " + CodigosGenerales.Codigo_Empresa + " - " + e.getMessage());
        }
        return arrayList;
    }


    public Boolean isIncluidoIGV() {
        try {
            Connection connection = bdata.getConnection();
            String stsql = "select mas_igv from  Hconfiguraciones_2 where idempresa = ?";
            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, CodigosGenerales.Codigo_Empresa); //Código de la empresa
            ResultSet rs = query.executeQuery();
            String mas_igv = "";
            while (rs.next()) {
                mas_igv = rs.getString("mas_igv");
            }
            connection.close();
            return mas_igv.equals("S");
        } catch (Exception e) {
            Log.d("BDEmpresa", "- isIncluidoIGV: " + e.getMessage());
        }
        return false;
    }
}