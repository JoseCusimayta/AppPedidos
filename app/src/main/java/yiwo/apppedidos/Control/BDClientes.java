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

public class BDClientes {

    BDConexionSQL bdata = new BDConexionSQL();
    String TAG = "BDClientes";

    public ArrayList<List<String>> getList(String Nombre) {
        ArrayList<List<String>> arrayList = new ArrayList<>();
        try {
            Connection connection = bdata.getConnection();
/*

                    ccod_cliente,
                    cnom_cliente,
                    cdireccion,
                    SELECT
                    cnum_ruc,
                    cnum_dni,
                    lista_precios,
                    Hforpag_provee.ccod_forpago as ccod_forpago,
                    cnom_forpago,
                    nro_dias,
                    ccod_pais,
                    nlinea_credito_mn as linea_disponible,
                    nombre_comercial
                    From Hcliente
                    inner join Hforpag_provee on
                    ccod_proveedor=ccod_cliente and
                    Hforpag_provee.ccod_empresa=Hcliente.ccod_empresa and
                    Hforpag_provee.tipo=Hcliente.cgrupo_cliente
                    inner join Hfor_pag on
                    hforpag_provee.ccod_empresa = Hfor_pag.ccod_empresa And
                    hforpag_provee.ccod_forpago = Hfor_pag.ccod_forpago
                    Where
                    Hcliente.ccod_empresa = ?
                    And Hcliente.cgrupo_cliente = '12'
                    and (ccod_cliente like ? or cnom_cliente like ? )
                    and selec='S'
                    and estado='Activo'
                    and estado='Activo'
 */
            String stsql = "" +
                    "SELECT TOP (10) \n" +
                    "ccod_cliente, \n" +
                    "cnom_cliente, \n" +
                    "cdireccion, \n" +
                    "cnum_ruc, \n" +
                    "cnum_dni, \n" +
                    "lista_precios,\n" +
                    "Hforpag_provee.ccod_forpago as ccod_forpago,\n" +
                    "cnom_forpago, \n" +
                    "nro_dias, \n" +
                    "ccod_pais,\n" +
                    "nlinea_credito_mn as linea_disponible,\n" +
                    "nombre_comercial\n" +
                    "From Hcliente\n" +
                    "inner join Hforpag_provee on\n" +
                    "ccod_proveedor=ccod_cliente and\n" +
                    "Hforpag_provee.ccod_empresa=Hcliente.ccod_empresa and\n" +
                    "Hforpag_provee.tipo=Hcliente.cgrupo_cliente\n" +
                    "inner join Hfor_pag on\n" +
                    "hforpag_provee.ccod_empresa = Hfor_pag.ccod_empresa And    \n" +
                    "hforpag_provee.ccod_forpago = Hfor_pag.ccod_forpago \n" +
                    "Where\n" +
                    "Hcliente.ccod_empresa = ?\n" +
                    "And Hcliente.cgrupo_cliente = '12' \n" +
                    "and (ccod_cliente like ? or cnom_cliente like ? )\n" +
                    "and selec='S' \n"+
                    "and estado='Activo' \n"+
                    "and estado='Activo' ";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, ConfiguracionEmpresa.Codigo_Empresa); // Codigo de la empresa
            query.setString(2, Nombre + "%"); //Código del Cliente
            query.setString(3, Nombre + "%"); //Código del Cliente

            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                String listaPrecios=rs.getString("lista_precios");
                if(listaPrecios.isEmpty() || listaPrecios.trim().equals(""))
                    listaPrecios="01";
                Log.d(TAG,"Lista de Precios: "+listaPrecios);
                arrayList.add(Arrays.asList(
                        rs.getString("ccod_cliente"),
                        rs.getString("cnom_cliente"),
                        rs.getString("cdireccion"),
                        rs.getString("cnum_ruc"),
                        rs.getString("cnum_dni"),
                        listaPrecios,
                        rs.getString("ccod_forpago"),
                        rs.getString("cnom_forpago"),
                        rs.getString("nro_dias"),
                        rs.getString("ccod_pais"),
                        rs.getString("linea_disponible"),
                        rs.getString("nombre_comercial")
                ));
            }
            connection.close();
        } catch (Exception e) {
            Log.d(TAG, "- getList: " + e.getMessage());
        }
        return arrayList;
    }

    public String getLineaUsada(String codigo) {
        String lineaUsada = null;
        try {
            Connection connection = bdata.getConnection();

            String stsql =
                    "select SUM(erp_impmn) as linea_usada " +
                    "from hmovialc " +
                    "where" +
                    " ccod_empresa=? " +
                    "and ctip_movimiento='S' " +
                    "and ccod_cliente=?";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, ConfiguracionEmpresa.Codigo_Empresa); // Codigo de la empresa
            query.setString(2, codigo); //Código del Cliente

            ResultSet rs = query.executeQuery();

            Log.d(TAG, "- lineaUsada: " + rs);
            Log.d(TAG, "- lineaUsada: " + query);
            Log.d(TAG, "- lineaUsada: " + codigo);
            Log.d(TAG, "- lineaUsada: " + ConfiguracionEmpresa.Codigo_Empresa);
            while (rs.next()) {
                lineaUsada = rs.getString(1);
            }
            connection.close();
        } catch (Exception e) {
            Log.d(TAG, "- lineaUsada: " + e.getMessage());
        }
        Log.d(TAG, "- lineaUsada: " + lineaUsada);
        return lineaUsada;
    }
}