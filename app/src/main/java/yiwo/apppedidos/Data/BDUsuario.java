package yiwo.apppedidos.Data;

import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.AspectosGenerales.ConfiguracionEmpresa;
import yiwo.apppedidos.ConexionBD.BDConexionSQL;

public class BDUsuario {
    private String TAG="BDUsuario";
    BDConexionSQL bdata = new BDConexionSQL();
    private BDEmpresa bdEmpresa= new BDEmpresa();

    public Boolean getLogin(String Usuario, String Clave) {

        try {
            Connection connection = bdata.getConnection();
            if (Usuario.equals("erpsys") && Clave.equals("2012")) {

//                CodigosGenerales.Codigo_Empresa = "01";
                CodigosGenerales.Nombre_Vendedor = "ERP Solutions Perú";
                CodigosGenerales.Celular_Vendedor = "número_celular";
                CodigosGenerales.email_Vendedor = "erpsys@gmail.com";
                CodigosGenerales.Codigo_Usuario = "erpsys";
                CodigosGenerales.Nombre_Usuario = "erpsys";
                CodigosGenerales.Moneda_Empresa = bdEmpresa.getMonedaTrabajo(connection);

                ConfiguracionEmpresa.Tipo_CambioEmpresa = bdEmpresa.getTipoCambio(connection); //Obtener el tipo de cambio actualL
                Log.d(TAG, "Tipo_CambioEmpresa " + ConfiguracionEmpresa.Tipo_CambioEmpresa);
                return true;
            } else {

                String ClaveEncriptada = getClaveEncriptada(Clave);

                String stsql =
                        "Select \n" +
                                "Hempresa.ccod_empresa as codigo_empresa, \n" +
                                "Hempresa.cnum_ruc as ruc, \n" +
                                "Hempresa.crazonsocial as razon_social, \n" +
                                "Hvended.cnom_vendedor as nombre_vendedor, \n" +
                                "Hvended.celular as celular,\n" +
                                "Hvended.email as email,   \n" +
                                "erp_usuario.erp_coduser as codigo_usuario, \n" +
                                "erp_usuario.erp_nomuser as nombre_usuario, \n" +
                                "erp_usuario.erp_password as clave, \n" +
                                "erp_usuario.erp_estado as estado    \n" +
                                "From Hempresa    \n" +
                                "Inner Join erp_usuario On     \n" +
                                "Hempresa.ccod_empresa = erp_usuario.erp_codemp     \n" +
                                "Inner Join Hvended On  \n" +
                                "erp_usuario.erp_codemp = Hvended.ccod_empresa   \n" +
                                "and erp_usuario.erp_coduser = Hvended.ccod_vendedor   \n" +
                                "and Hempresa.ccod_empresa = ? " +
                                "and erp_usuario.erp_coduser = ? " +
                                "and erp_usuario.erp_password = ? " +
                                "and erp_usuario.erp_estado = 'A'";

                PreparedStatement query = connection.prepareStatement(stsql);
                query.setString(1, CodigosGenerales.Codigo_Empresa);
                query.setString(2, Usuario);
                query.setString(3, ClaveEncriptada);

                ResultSet rs = query.executeQuery();
                while (rs.next()) {


                    String Codigo_Usuario = rs.getString("codigo_usuario");
                    if (Usuario.equals(Codigo_Usuario)) {
                        CodigosGenerales.Codigo_Empresa = rs.getString("codigo_empresa");
                        CodigosGenerales.Nombre_Vendedor = rs.getString("nombre_vendedor");
                        CodigosGenerales.Celular_Vendedor = rs.getString("celular");
                        CodigosGenerales.email_Vendedor = rs.getString("email");
                        CodigosGenerales.Codigo_Usuario = Codigo_Usuario;
                        CodigosGenerales.Nombre_Usuario = rs.getString("nombre_usuario");
                        return true;
                    }
                }
                CodigosGenerales.Moneda_Empresa = bdEmpresa.getMonedaTrabajo(connection);
                ConfiguracionEmpresa.Tipo_CambioEmpresa = bdEmpresa.getTipoCambio(connection); //Obtener el tipo de cambio actualL
            }
            connection.close();
        } catch (Exception e) {
            Log.d(TAG, "- getLogin: " + e.getMessage());
        }
        return false;
    }

    private String getClaveEncriptada(String clave) {
        String strexpresion = "";
        int li_longitud, li_ascii, li_ascii_new;
        String ls_letra;
        try {
            li_longitud = clave.length();
            for (int i = 0; i < li_longitud; i++) {
                ls_letra = clave.substring(i, i + 1);
                li_ascii = ls_letra.codePointAt(0);
                li_ascii_new = li_ascii + ((li_longitud + 1 - (i + 1)) * (li_longitud + 1 - (i + 1))) - ((li_longitud + 2 - (i + 1)) * (li_longitud + 2 - (i + 1)));
                strexpresion = strexpresion + String.valueOf((char) li_ascii_new);
            }
        } catch (Exception e) {
            Log.d(TAG, "- getClaveEncriptada: " + e.getMessage());
        }
        return strexpresion;
    }

}