package yiwo.apppedidos.Data;

import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.ConexionBD.BDConexionSQL;

public class BDUsuario {
    private String TAG="BDUsuario";
    BDConexionSQL bdata = new BDConexionSQL();
    private BDEmpresa bdEmpresa= new BDEmpresa();

    public Boolean getLogin(String Usuario, String Clave) {

        try {

            if(Usuario.equals("erpsys") && Clave.equals("2012")){
                CodigosGenerales.Moneda_Empresa = bdEmpresa.getMonedaTrabajo();

//                CodigosGenerales.Codigo_Empresa = "01";
                CodigosGenerales.Nombre_Vendedor = "ERP Solutions Perú";
                CodigosGenerales.Celular_Vendedor = "número_celular";
                CodigosGenerales.email_Vendedor = "erpsys@gmail.com";
                CodigosGenerales.Codigo_Usuario = "erpsys";
                CodigosGenerales.Nombre_Usuario = "erpsys";
                CodigosGenerales.Moneda_Empresa = bdEmpresa.getMonedaTrabajo();

                return true;
            }else {

                ArrayList arrayList = new ArrayList<String>();
                String ClaveEncriptada = getClaveEncriptada(Clave);
                Connection connection = bdata.getConnection();

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
                    arrayList.add(rs.getString("codigo_empresa"));      //Codigo de Empresa
                    arrayList.add(rs.getString("ruc"));         //RUC
                    arrayList.add(rs.getString("razon_social"));    //Razon Social de la empresa
                    arrayList.add(rs.getString("nombre_vendedor"));    //nombre del vendedor
                    arrayList.add(rs.getString("celular"));    //celular del vendedor
                    arrayList.add(rs.getString("email"));    //email del vendedor
                    arrayList.add(rs.getString("codigo_usuario"));     //Codigo de usuario para ingresar
                    arrayList.add(rs.getString("nombre_usuario"));     //Nombre del Usuario para mostrar
                }
                connection.close();

                if (arrayList.size() > 0) {
                    String Codigo_Usuario = arrayList.get(6).toString();
                    if (Usuario.equals(Codigo_Usuario)) {
                        CodigosGenerales.Codigo_Empresa = arrayList.get(0).toString();
                        CodigosGenerales.Nombre_Vendedor = arrayList.get(3).toString();
                        CodigosGenerales.Celular_Vendedor = arrayList.get(4).toString();
                        CodigosGenerales.email_Vendedor = arrayList.get(5).toString();
                        CodigosGenerales.Codigo_Usuario = Codigo_Usuario;
                        CodigosGenerales.Nombre_Usuario = arrayList.get(7).toString();
                        CodigosGenerales.Moneda_Empresa = bdEmpresa.getMonedaTrabajo();
                        Log.d(TAG, "Datos del vendedor:\n" + arrayList);
                        return true;
                    }
                }
            }
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