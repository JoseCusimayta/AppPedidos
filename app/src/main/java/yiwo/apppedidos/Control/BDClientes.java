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

            String stsql =
                            "SELECT \n" +
                            "ccod_cliente, cnom_cliente, cdireccion, cnum_ruc, cnum_dni, " +
                            "lista_precios, Hforpag_provee.ccod_forpago as ccod_forpago,cnom_forpago, nro_dias, ccod_pais  \n" +
                            "From Hcliente\n" +
                            "inner join Hforpag_provee on\n" +
                            "ccod_proveedor=ccod_cliente and\n" +
                            "Hforpag_provee.ccod_empresa=Hcliente.ccod_empresa and\n" +
                            "Hforpag_provee.tipo=Hcliente.cgrupo_cliente\n" +
                            "inner join Hfor_pag on\n" +
                            "hforpag_provee.ccod_empresa = Hfor_pag.ccod_empresa And    \n" +
                            "hforpag_provee.ccod_forpago = Hfor_pag.ccod_forpago \n" +
                            "Where\n" +
                            "Hcliente.ccod_empresa = ? \n" +
                            "And Hcliente.cgrupo_cliente = '12' \n" +
                            "and (ccod_cliente like ? or cnom_cliente like ? ) \n"+
                            "and selec='S'";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, ConfiguracionEmpresa.Codigo_Empresa); // Codigo de la empresa
            query.setString(2, Nombre + "%"); //Código del Cliente
            query.setString(3, Nombre + "%"); //Código del Cliente

            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                arrayList.add(Arrays.asList(
                        rs.getString("ccod_cliente"),
                        rs.getString("cnom_cliente"),
                        rs.getString("cdireccion"),
                        rs.getString("cnum_ruc"),
                        rs.getString("cnum_dni"),
                        rs.getString("lista_precios"),
                        rs.getString("ccod_forpago"),
                        rs.getString("cnom_forpago"),
                        rs.getString("nro_dias"),
                        rs.getString("ccod_pais")
                ));
            }
            connection.close();
        } catch (Exception e) {
            Log.d(TAG, "- getList: " + e.getMessage());
        }
        return arrayList;
    }
}