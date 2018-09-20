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

public class BDArticulos {

    BDConexionSQL bdata = new BDConexionSQL();

    public ArrayList<List<String>> getList(String Nombre) {

        ArrayList<List<String>> arrayList = new ArrayList<>();

        try {
            if (CodigosGenerales.listArticulos.size() > 0 && Nombre.equals("")) {
                return CodigosGenerales.listArticulos;
            } else {
                Connection connection = bdata.getConnection();

                String stsql = "SELECT TOP(100) FROM dbo.udf_list_harticul (?,?,?,?) where codigo like ? or Nombre like ?  and cfamilia!='656' and cfamilia!='655' and estado='Activo' order by Nombre";

                PreparedStatement query = connection.prepareStatement(stsql);

                query.setString(1, CodigosGenerales.Codigo_Empresa); // Codigo de la empresa
                query.setString(2, "");         //Codigo del producto
                query.setString(3, CodigosGenerales.Codigo_PuntoVenta);//Punto de Venta
                query.setString(4, CodigosGenerales.Codigo_Almacen);//Almacen
                query.setString(5, Nombre + "%"); //Codigo del producto
                query.setString(6, Nombre + "%"); //Nombre del producto

                ResultSet rs = query.executeQuery();

                while (rs.next()) {
                    arrayList.add(Arrays.asList(
                            rs.getString("codigo"),
                            rs.getString("Nombre"),
                            rs.getString("cunidad"),
                            rs.getString("stock"),
                            rs.getString("monto")
                    ));
                }

                connection.close();

                if (Nombre.equals(""))
                    CodigosGenerales.listArticulos = arrayList;
            }
        } catch (Exception e) {
            Log.d("BDArticulos", "- getList: " + e.getMessage());
        }
        return arrayList;
    }

    public ArrayList<List<String>> getListLan(String Nombre) {

        ArrayList<List<String>> arrayList = new ArrayList<>();

        try {
            Connection connection = bdata.getConnection();

            String stsql = "SELECT * FROM dbo.udf_list_harticul (?,?,?,?) where codigo like ? or Nombre like ?  and cfamilia!='656' and cfamilia!='655' and estado='Activo' order by Nombre";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, CodigosGenerales.Codigo_Empresa); // Codigo de la empresa
            query.setString(2, "");         //Codigo del producto
            query.setString(3, CodigosGenerales.Codigo_PuntoVenta);//Punto de Venta
            query.setString(4, CodigosGenerales.Codigo_Almacen);//Almacen
            query.setString(5, Nombre + "%"); //Codigo del producto
            query.setString(6, Nombre + "%"); //Nombre del producto

            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                arrayList.add(Arrays.asList(
                        rs.getString("codigo"),
                        rs.getString("Nombre"),
                        rs.getString("cunidad"),
                        rs.getString("stock"),
                        rs.getString("monto")
                ));
            }
            connection.close();

        } catch (Exception e) {
            Log.d("BDArticulos", "- getList: " + e.getMessage());
        }
        return arrayList;
    }

    public ArrayList<List<String>> getListFamilia(String Nombre, String Codigo_Familia) {

        return getListFiltrado("cfamilia=?", Nombre, Codigo_Familia);
    }

    public ArrayList<List<String>> getListSubFamilia(String Nombre, String Codigo_SubFamilia) {

        return getListFiltrado("ccod_subfamilia=?", Nombre, Codigo_SubFamilia);
    }

    public ArrayList<List<String>> getListConcepto(String Nombre, String Codigo_Concepto) {

        String stsql = "";

        switch (CodigosGenerales.ID_Concepto) {
            case 1:
                stsql += " codmarca = ? ";
                break;
            case 2:
                stsql += " modelo = ? ";
                break;
            case 3:
                stsql += " color = ? ";
                break;
            case 4:
                stsql += " tratamiento = ? ";
                break;
            case 5:
                stsql += " fuelle = ? ";
                break;
            case 6:
                stsql += " azas = ? ";
                break;
            case 7:
                stsql += " solapa = ? ";
                break;
        }

        return getListFiltrado(stsql, Nombre, Codigo_Concepto);
    }

    private ArrayList<List<String>> getListFiltrado(String stsql, String Nombre, String Codigo) {
        ArrayList<List<String>> arrayList = new ArrayList<>();

        try {
            String sql = "SELECT * FROM dbo.udf_list_harticul (?,?,?,?) where (codigo like ? or Nombre like ? ) and " + stsql + " and estado='Activo' order by Nombre";
            //Log.d("Asda",CodigosGenerales.Codigo_Empresa+" - "+CodigosGenerales.Codigo_PuntoVenta+ " - " +CodigosGenerales.Codigo_Almacen +" - "+Nombre +" - "+stsql +" - "+Codigo);
            Connection connection = bdata.getConnection();
            PreparedStatement query = connection.prepareStatement(sql);
            query.setString(1, CodigosGenerales.Codigo_Empresa); // Código de la empresa
            query.setString(2, "");         //Código del producto
            query.setString(3, CodigosGenerales.Codigo_PuntoVenta);//Punto de Venta
            query.setString(4, CodigosGenerales.Codigo_Almacen);//Almacen
            query.setString(5, Nombre + "%"); //Código del producto
            query.setString(6, Nombre + "%"); //Nombre del producto
            query.setString(7, Codigo); //Código de Familia

            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                arrayList.add(Arrays.asList(
                        rs.getString("codigo"),//Codigo
                        rs.getString("Nombre"),//Nombre
                        rs.getString("cunidad"),//Tipo de Unidad
                        rs.getString("stock"), //¨cantidad
                        rs.getString("monto")//precio
                ));
            }
            connection.close();

        } catch (Exception e) {
            Log.d("BDArticulos", "- getListFiltrado: " + e.getMessage());
        }
        return arrayList;
    }

    public List<String> getDescripcionArticulo(String Codigo_Articulo) {
        List<String> arrayArticuloSeleccionado = new ArrayList<>();
        try {
            Connection connection = bdata.getConnection();
            String stsql = "SELECT * FROM dbo.udf_list_harticul (?,?,?,?) ";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, CodigosGenerales.Codigo_Empresa);
            query.setString(2, Codigo_Articulo);
            query.setString(3, CodigosGenerales.Codigo_PuntoVenta);//Punto de Venta
            query.setString(4, CodigosGenerales.Codigo_Almacen);//Almacen

            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                arrayArticuloSeleccionado.add(rs.getString("codigo"));
                arrayArticuloSeleccionado.add(rs.getString("Nombre"));
                arrayArticuloSeleccionado.add(rs.getString("cunidad"));
                arrayArticuloSeleccionado.add(rs.getString("stock"));
                arrayArticuloSeleccionado.add(rs.getString("monto"));

            }
            connection.close();
        } catch (Exception e) {
            Log.d("BDArticulos", "- getDescripcionArticulo: " + e.getMessage());
        }
        return arrayArticuloSeleccionado;
    }


    public Integer getCantidadTotal() {

        Integer cantidad_articulos = 0;

        try {
            Connection connection = bdata.getConnection();

            String stsql = "SELECT Count(*) as cantidad FROM dbo.udf_list_harticul (?,'','','') where estado='Activo' ";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, CodigosGenerales.Codigo_Empresa); // Codigo de la empresa

            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                cantidad_articulos = rs.getInt("cantidad");
            }
            connection.close();

        } catch (Exception e) {
            Log.d("BDArticulos", "- getLength: " + e.getMessage());
        }
        return cantidad_articulos;
    }

    public ArrayList<List<String>> getFichaTecnica(String Codigo_Articulo) {
        try {
            ArrayList<List<String>> ficha_tecnica = new ArrayList<>();
            Connection connection = bdata.getConnection();

            String stsql = "select erp_titulo, erp_descripcion from Erp_Articulo_Datos_Tecnicos Where erp_codemp = ? and erp_codart = ?";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, CodigosGenerales.Codigo_Empresa); // Codigo de la empresa
            query.setString(2, Codigo_Articulo); // Codigo Articulo

            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                ficha_tecnica.add( Arrays.asList(
                        rs.getString("erp_titulo"),
                        rs.getString("erp_descripcion")
                        ));
            }
            connection.close();
            return ficha_tecnica;
        } catch (Exception e) {
            Log.d("BDArticulos", "- getFichaTecnica: " + e.getMessage());
        }
        return null;
    }

    public List<String> getPromociones(String Codigo_Articulo, String Cantidad) {
        try {
            List<String> ficha_tecnica = new ArrayList<>();
            Connection connection = bdata.getConnection();

            String stsql = "\n" +
                    "\t\t select \n" +
                    "\t\t erp_cant_ini, erp_cant_fin, desc01,desc02,desc03,desc04 \n" +
                    "\t\t from \n" +
                    "\t\t erp_promociones \n" +
                    "\t\t where \n" +
                    "\t\t erp_codemp=? \n" +
                    "\t\t and erp_codigo=? \n" +
                    "\t\t and erp_motivo='RM' \n" +
                    "\t\t and erp_vcto_ini<GETDATE() and erp_vcto_fin>GETDATE()\n" +
                    "\t\t and erp_tipo='Item'\n" +
                    "\t\t and erp_cant_ini<? and erp_cant_fin>?";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, CodigosGenerales.Codigo_Empresa); // Codigo de la empresa
            query.setString(2, Codigo_Articulo); // Codigo Articulo
            query.setString(3, Cantidad); // Cantidad
            query.setString(4, Cantidad); // Cantidad

            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                ficha_tecnica.add(rs.getString("erp_cant_ini")); //Cantidad de Inicio
                ficha_tecnica.add(rs.getString("erp_cant_fin")); //Cantidad Final
                ficha_tecnica.add(rs.getString("desc01")); //Descuento 1
                ficha_tecnica.add(rs.getString("desc02")); //Descuento 2
                ficha_tecnica.add(rs.getString("desc03")); //Descuento 3
                ficha_tecnica.add(rs.getString("desc04")); //Descuento 4
            }
            connection.close();

        } catch (Exception e) {
            Log.d("BDArticulos", "- getPromociones: " + e.getMessage());
        }
        return null;
    }

    public Double getIGVArticulo(String Codigo_Articulo, String Tipo_Unidad){
        Double IGV=0.00;
        try {
            Connection connection = bdata.getConnection();

            String stsql = "select nigv from Harticul where ccod_empresa=? and ccod_articulo=? and cunidad=?";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, CodigosGenerales.Codigo_Empresa); // Codigo de la empresa
            query.setString(2, Codigo_Articulo); // Codigo Articulo
            query.setString(3, Tipo_Unidad); // Codigo Articulo

            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                IGV= rs.getDouble("nigv");
            }
            connection.close();
            return IGV;
        } catch (Exception e) {
            Log.d("BDArticulos", "- getFichaTecnica: " + e.getMessage());
        }
        return IGV;
    }
}