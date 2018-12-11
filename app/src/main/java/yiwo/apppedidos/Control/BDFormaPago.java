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
import yiwo.apppedidos.AspectosGenerales.DatosCliente;
import yiwo.apppedidos.ConexionBD.BDConexionSQL;

public class BDFormaPago {

    BDConexionSQL bdata = new BDConexionSQL();

    public ArrayList<List<String>> getList(String Nombre) {

        ArrayList<List<String>> arrayList = new ArrayList<>();

        try {

            Connection connection = bdata.getConnection();

            String stsql="Select \n" +
                    "hforpag_provee.ccod_forpago as Codigo, \n" +
                    "Hfor_pag.cnom_forpago as Nombre, \n" +
                    "Hfor_pag.nro_dias as ndias,  \n" +
                    "hforpag_provee.selec  \n" +
                    "From hforpag_provee Inner Join Hfor_pag On    \n" +
                    "hforpag_provee.ccod_empresa = Hfor_pag.ccod_empresa And    \n" +
                    "hforpag_provee.ccod_forpago = Hfor_pag.ccod_forpago     \n" +
                    "Where \n" +
                    "hforpag_provee.ccod_empresa = ?\n" +
                    "And hforpag_provee.tipo = '12' \n" +
                    "And hforpag_provee.ccod_proveedor = ?" +
                    "And (Hfor_pag.cnom_forpago like ? or hforpag_provee.ccod_forpago like ?)";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, ConfiguracionEmpresa.Codigo_Empresa); // Código de la empresa
            query.setString(2, DatosCliente.Codigo_Cliente);   //Código del Cliente
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

            String stsql="Select \n" +
                    "hforpag_provee.ccod_forpago as Codigo, \n" +
                    "Hfor_pag.cnom_forpago as Nombre, \n" +
                    "Hfor_pag.nro_dias as ndias,  \n" +
                    "hforpag_provee.selec  \n" +
                    "From hforpag_provee Inner Join Hfor_pag On    \n" +
                    "hforpag_provee.ccod_empresa = Hfor_pag.ccod_empresa And    \n" +
                    "hforpag_provee.ccod_forpago = Hfor_pag.ccod_forpago     \n" +
                    "Where \n" +
                    "hforpag_provee.selec ='S' and hforpag_provee.ccod_empresa = ?\n" +
                    "And hforpag_provee.tipo = '12' \n" +
                    "And hforpag_provee.ccod_proveedor = ?" +
                    "And (Hfor_pag.cnom_forpago like ? or hforpag_provee.ccod_forpago like ?)";


            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, ConfiguracionEmpresa.Codigo_Empresa); // Código de la empresa
            query.setString(2, DatosCliente.Codigo_Cliente);   //Código del Cliente

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

    public ArrayList<List<String>> getSmallList( String Codigo_Cliente) {

        ArrayList<List<String>> arrayList = new ArrayList<>();

        try {

            Connection connection = bdata.getConnection();

            String stsql="" +
                    "select top(3)\n" +
                    "Hforpag_provee.ccod_forpago as ccod_forpago,\n" +
                    "cnom_forpago, \n" +
                    "nro_dias\n" +
                    "From Hcliente\n" +
                    "inner join Hforpag_provee on\n" +
                    "ccod_proveedor=ccod_cliente and\n" +
                    "Hforpag_provee.ccod_empresa=Hcliente.ccod_empresa and\n" +
                    "Hforpag_provee.tipo=Hcliente.cgrupo_cliente\n" +
                    "inner join Hfor_pag on\n" +
                    "hforpag_provee.ccod_empresa = Hfor_pag.ccod_empresa And    \n" +
                    "hforpag_provee.ccod_forpago = Hfor_pag.ccod_forpago \n" +
                    "where \n" +
                    "Hcliente.ccod_empresa=?\n" +
                    "and cgrupo_cliente='12'\n" +
                    "and ccod_cliente=? \n"+
                    "and Hforpag_provee.ccod_forpago <> '00'";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, ConfiguracionEmpresa.Codigo_Empresa); // Código de la empresa
            query.setString(2, Codigo_Cliente);   //Código del Cliente

            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                arrayList.add(Arrays.asList(
                        rs.getString("ccod_forpago"),
                        rs.getString("cnom_forpago"),
                        rs.getString("nro_dias")//Cantidad de Dias para sumar
                ));
            }
            connection.close();

        } catch (Exception e) {
            Log.d("BDFormaPago", "- getSmallList: " + e.getMessage());
        }
        return arrayList;
    }
}