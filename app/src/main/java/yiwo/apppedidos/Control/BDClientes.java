package yiwo.apppedidos.Control;

import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.AspectosGenerales.ConfiguracionEmpresa;
import yiwo.apppedidos.AspectosGenerales.DatosCliente;
import yiwo.apppedidos.AspectosGenerales.DatosUsuario;
import yiwo.apppedidos.ConexionBD.BDConexionSQL;

public class BDClientes {

    BDConexionSQL bdata = new BDConexionSQL();
    String TAG = "BDClientes";

    public ArrayList<List<String>> getList(String Nombre) {
        ArrayList<List<String>> arrayList = new ArrayList<>();
        try {
            Connection connection = bdata.getConnection();
/*
SELECT
TOP (10)
Hcliente.ccod_cliente,
cnom_cliente,
cdireccion,
cnum_ruc,
cnum_dni,
lista_precios,
Hforpag_provee.ccod_forpago as ccod_forpago,
cnom_forpago,
nro_dias,
ccod_pais,
nlinea_credito_mn as linea_disponible,
nombre_comercial,
ctelefonos,
Hpercontactocli.ccod_contacto,
Hpercontactocli.cnom_contacto,
Hpercontactocli.telefono,
ce_mail,
Hvended.ccod_vendedor,
Hvended.cnom_vendedor,
Erp_Observacion01
From Hcliente
inner join Hforpag_provee on
ccod_proveedor=ccod_cliente and
Hforpag_provee.ccod_empresa=Hcliente.ccod_empresa and
Hforpag_provee.tipo=Hcliente.cgrupo_cliente
inner join Hfor_pag on
hforpag_provee.ccod_empresa = Hfor_pag.ccod_empresa And
hforpag_provee.ccod_forpago = Hfor_pag.ccod_forpago
left join Hpercontactocli on
Hcliente.ccod_empresa = Hpercontactocli.ccod_empresa and
Hcliente.ccod_cliente =Hpercontactocli.ccod_cliente AND
Hpercontactocli.Erp_Predeterminado='S'
inner join Hvended on
Hcliente.ccod_empresa= Hvended.ccod_empresa and
Hcliente.ccod_vendedor= Hvended.ccod_vendedor
and Hcliente.ccod_vendedor=Hvended.ccod_vendedor
Where
Hcliente.ccod_empresa = ?
and Hcliente.cgrupo_cliente = '12'
and (Hcliente.ccod_cliente like ? or cnom_cliente like ? )
and Hforpag_provee.selec = 'S'
and estado='Activo'
and Hvended.ccod_vendedor=?

 */
            String stsql = "SELECT \n" +
                    "TOP (10) \n" +
                    "Hcliente.ccod_cliente, \n" +
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
                    "nombre_comercial,\n" +
                    "ctelefonos,\n" +
                    "Hpercontactocli.ccod_contacto,\n" +
                    "Hpercontactocli.cnom_contacto,\n" +
                    "Hpercontactocli.telefono,\n" +
                    "ce_mail,\n" +
                    "Hvended.ccod_vendedor,\n" +
                    "Hvended.cnom_vendedor,\n" +
                    "Erp_Observacion01\n" +
                    "From Hcliente\n" +
                    "inner join Hforpag_provee on\n" +
                    "ccod_proveedor=ccod_cliente and\n" +
                    "Hforpag_provee.ccod_empresa=Hcliente.ccod_empresa and\n" +
                    "Hforpag_provee.tipo=Hcliente.cgrupo_cliente\n" +
                    "inner join Hfor_pag on\n" +
                    "hforpag_provee.ccod_empresa = Hfor_pag.ccod_empresa And\n" +
                    "hforpag_provee.ccod_forpago = Hfor_pag.ccod_forpago \n" +
                    "left join Hpercontactocli on\n" +
                    "Hcliente.ccod_empresa = Hpercontactocli.ccod_empresa and\n" +
                    "Hcliente.ccod_cliente =Hpercontactocli.ccod_cliente AND\n" +
                    "Hpercontactocli.Erp_Predeterminado='S'\n" +
                    "inner join Hvended on \n" +
                    "Hcliente.ccod_empresa= Hvended.ccod_empresa and\n" +
                    "Hcliente.ccod_vendedor= Hvended.ccod_vendedor\n" +
                    "and Hcliente.ccod_vendedor=Hvended.ccod_vendedor\n" +
                    "Where\n" +
                    "Hcliente.ccod_empresa = ?\n" +
                    "and Hcliente.cgrupo_cliente = '12' \n" +
                    "and (Hcliente.ccod_cliente like ? or cnom_cliente like ? )\n" +
                    "and Hforpag_provee.selec = 'S' \n" +
                    "and estado='Activo'\n" +
                    "and Hvended.ccod_vendedor=?" ;

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, ConfiguracionEmpresa.Codigo_Empresa); // Codigo de la empresa
            query.setString(2, Nombre + "%"); //Código del Cliente
            query.setString(3, Nombre + "%"); //Código del Cliente
            query.setString(4, DatosUsuario.Codigo_Vendedor); //Código del Vendedor

            Log.d(TAG,"Codigo_Vendedor: "+DatosUsuario.Codigo_Vendedor);
            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                String listaPrecios=rs.getString("lista_precios");
                if(listaPrecios.isEmpty() || listaPrecios==null || listaPrecios.trim().equals(""))
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
                        rs.getString("nombre_comercial"),
                        rs.getString("ctelefonos"),
                        rs.getString("ccod_contacto"),
                        rs.getString("cnom_contacto"),
                        rs.getString("telefono"),
                        rs.getString("ce_mail"),
                        rs.getString("ccod_vendedor"),
                        rs.getString("cnom_vendedor"),
                        rs.getString("Erp_Observacion01")
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

    public Boolean actualizarCorreoTelefono(String correo, String telefono) {
        Boolean resultado=false;

        try {
            DatosCliente.Cliene_Correo=correo;
            DatosCliente.Cliente_Telefono=telefono;
            Connection connection = bdata.getConnection();

            String stsql =
                    "update hcliente set \n" +
                            "ctelefonos=?\n" +
                            ",ce_mail=? \n" +
                            "where \n" +
                            "ccod_empresa=? \n" +
                            "and ccod_cliente=?";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, DatosCliente.Cliente_Telefono); // Codigo de la empresa
            query.setString(2, DatosCliente.Cliene_Correo); // Codigo de la empresa
            query.setString(3, ConfiguracionEmpresa.Codigo_Empresa); // Codigo de la empresa
            query.setString(4, DatosCliente.Codigo_Cliente); // Codigo de la empresa
            Boolean sdsad= query.execute();
            Log.d(TAG,"Cliente_Telefono"+DatosCliente.Cliente_Telefono);
            Log.d(TAG,"Cliene_Correo"+DatosCliente.Cliene_Correo);
            Log.d(TAG,"Codigo_Empresa"+ConfiguracionEmpresa.Codigo_Empresa);
            Log.d(TAG,"Codigo_Cliente"+DatosCliente.Codigo_Cliente);
            Log.d(TAG,"stsql"+stsql);


            connection.close();
        } catch (Exception e) {
            Log.d(TAG, "- actualizarCorreoTelefono: " + e.getMessage());
        }
        return resultado;
    }
}