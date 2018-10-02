package yiwo.apppedidos.Control;

import android.util.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.AspectosGenerales.ConfiguracionEmpresa;
import yiwo.apppedidos.AspectosGenerales.DatosUsuario;
import yiwo.apppedidos.ConexionBD.BDConexionSQL;

public class BDFiltros {

    BDConexionSQL bdata= new BDConexionSQL();
    public ArrayList<List<String>> getList(List<String> Familia,
                                                     List<String> SubFamilia,
                                                     List<String> Concepto1,
                                                     List<String> Concepto2,
                                                     List<String> Concepto3,
                                                     List<String> Concepto4,
                                                     List<String> Concepto5,
                                                     List<String> Concepto6,
                                                     List<String> Concepto7,
                                                     String Nombre) {

         ArrayList<List<String>> arrayArticulos = new ArrayList<>();

        String sql=null;
        try {
            Connection connection = bdata.getConnection();

            sql= "SELECT * FROM dbo.udf_list_harticul ('"+ ConfiguracionEmpresa.Codigo_Empresa+"','%','"+ DatosUsuario.Codigo_PuntoVenta+"','"+DatosUsuario.Codigo_Almacen+"') where ";

            if(Familia.size()>0) {
                sql +=" cfamilia in ( ";
                for (int i = 0; i < Familia.size(); i++) {
                    sql+="'" + Familia.get(i) + "'";
                    if (i + 1 < Familia.size())
                        sql += " , ";
                }
                sql +=" ) or";
            }


            if(SubFamilia.size()>0) {
                sql +=" ccod_subfamilia in ( ";
                for (int i = 0; i < SubFamilia.size(); i++) {
                    sql+="'" + SubFamilia.get(i) + "'";
                    if (i + 1 < SubFamilia.size())
                        sql += " , ";
                }
                sql +=" ) or";
            }

            if(Concepto1.size()>0) {
                sql +=" codmarca in ( ";
                for (int i = 0; i < Concepto1.size(); i++) {
                    sql+="'" + Concepto1.get(i) + "'";
                    if (i + 1 < Concepto1.size())
                        sql += " , ";
                }
                sql +=" ) or";
            }

            if(Concepto2.size()>0) {
                sql +=" modelo in ( ";
                for (int i = 0; i < Concepto2.size(); i++) {
                    sql+="'" + Concepto2.get(i) + "'";
                    if (i + 1 < Concepto2.size())
                        sql += " , ";
                }
                sql +=" ) or";
            }

            if(Concepto3.size()>0) {
                sql +=" color in ( ";
                for (int i = 0; i < Concepto3.size(); i++) {
                    sql+="'" + Concepto3.get(i) + "'";
                    if (i + 1 < Concepto3.size())
                        sql += " , ";
                }
                sql +=" ) or";
            }

            if(Concepto4.size()>0) {
                sql +=" tratamiento in ( ";
                for (int i = 0; i < Concepto4.size(); i++) {
                    sql+="'" + Concepto4.get(i) + "'";
                    if (i + 1 < Concepto4.size())
                        sql += " , ";
                }
                sql +=" ) or";
            }

            if(Concepto5.size()>0) {
                sql +=" fuelle in ( ";
                for (int i = 0; i < Concepto5.size(); i++) {
                    sql+="'" + Concepto5.get(i) + "'";
                    if (i + 1 < Concepto5.size())
                        sql += " , ";
                }
                sql +=" ) or";
            }

            if(Concepto6.size()>0) {
                sql +=" azas in ( ";
                for (int i = 0; i < Concepto6.size(); i++) {
                    sql+="'" + Concepto6.get(i) + "'";
                    if (i + 1 < Concepto6.size())
                        sql += " , ";
                }
                sql +=" ) or";
            }

            if(Concepto7.size()>0) {
                sql +=" solapa in ( ";
                for (int i = 0; i < Concepto7.size(); i++) {
                    sql+="'" + Concepto7.get(i) + "'";
                    if (i + 1 < Concepto7.size())
                        sql += " , ";
                }
                sql +=" ) or";
            }
            sql= sql.substring(0,sql.length()-2);
            sql += "  and (Nombre like '"+Nombre+"%' or codigo like '"+Nombre+"%' ) order by Nombre";
//            Log.d("SQL",sql+"");
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                arrayArticulos.add(Arrays.asList(
                        rs.getString("codigo"),
                        rs.getString("Nombre"),
                        rs.getString("cunidad"),
                        rs.getString("stock"),
                        rs.getString("monto")
                ));
            }
            connection.close();

        } catch (Exception e) {
            Log.d("BDFiltros","getList: SQL: "+sql);
            Log.d("BDFiltros", "- getList: "+e.getMessage());
        }
        return arrayArticulos;
    }
}
