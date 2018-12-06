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
import yiwo.apppedidos.AspectosGenerales.DatosUsuario;
import yiwo.apppedidos.ConexionBD.BDConexionSQL;

public class BDArticulos {

    BDConexionSQL bdata = new BDConexionSQL();
    String TAG = "BDArticulos";

    private ArrayList<List<String>> getArticulos(
            String Nombre,
            String Busqueda_categoria) {

        ArrayList<List<String>> arrayList = new ArrayList<>();

        try {
            if (CodigosGenerales.listArticulos.size() > 0 && Nombre.equals("") && Busqueda_categoria.equals("")) {
                Log.d(TAG, "regresando lista guardada...");
                return CodigosGenerales.listArticulos;
            } else {
                Log.d(TAG, "generando nueva lista...");
                Connection connection = bdata.getConnection();
/*

                                select top(50)
                                ccod_articulo,
                                cnom_articulo,
                                cfamilia,
                                ccod_subfamilia,
                                codmarca,
                                modelo,
                                color,
                                tratamiento,
                                fuelle,
                                azas,
                                solapa,
                                cmoneda_precio,
                                erp_monto,
                                cunidad,
                                Isnull(( SUM(ERP_STOART) - SUM(ERP_STOCOM)),0) as stock,
                                Harticul.ccod_almacen,
                                nigv
                                from Harticul
                                inner join Hstock
                                on
                                Harticul.ccod_articulo=HSTOCK.ERP_CODART and
                                Harticul.ccod_empresa=HSTOCK.ERP_CODEMP
                                and Harticul.ccod_almacen=HSTOCK.ERP_CODALM
                                inner join Erp_Lista_Precio_Cliente
                                on
                                Harticul.ccod_articulo=Erp_Lista_Precio_Cliente.ERP_CODART and
                                Harticul.ccod_empresa=Erp_Lista_Precio_Cliente.ERP_CODEMP and
                                Harticul.cunidad=Erp_Lista_Precio_Cliente.erp_unidad
                                where
                                ccod_empresa = ?
                                and ERP_CODPTV = ?
                                and ERP_CODALM = ?
                                and erp_tipo = '12 '
                                and erp_codigo_concepto = ?
                                and (
                                (ccod_articulo like ? or cnom_articulo like ? )
                                 Busqueda_categoria
                                 )
                                group by
                                ccod_articulo,
                                cnom_articulo,
                                cfamilia,
                                ccod_subfamilia,
                                codmarca,
                                modelo,
                                color,
                                tratamiento,
                                fuelle,
                                azas,
                                solapa,
                                cmoneda_precio,
                                erp_monto,
                                cunidad,
                                ccod_almacen,
                                nigv
 */
                String stsql =
                                "select top(50) \n" +
                                "ccod_articulo, \n" +
                                "cnom_articulo, \n" +
                                "cfamilia,\n" +
                                "ccod_subfamilia,\n" +
                                "codmarca,\n" +
                                "modelo,\n" +
                                "color,\n" +
                                "tratamiento,\n" +
                                "fuelle,\n" +
                                "azas,\n" +
                                "solapa,\n" +
                                "cmoneda_precio,\n" +
                                "erp_monto,\n" +
                                "cunidad,\n" +
                                "Isnull(( SUM(ERP_STOART) - SUM(ERP_STOCOM)),0) as stock,\n" +
                                "Harticul.ccod_almacen, \n" +
                                "nigv \n" +
                                "from Harticul \n" +
                                "inner join Hstock\n" +
                                "on \n" +
                                "Harticul.ccod_articulo=HSTOCK.ERP_CODART and\n" +
                                "Harticul.ccod_empresa=HSTOCK.ERP_CODEMP \n" +
                                "and Harticul.ccod_almacen=HSTOCK.ERP_CODALM\n" +
                                "inner join Erp_Lista_Precio_Cliente\n" +
                                "on\n" +
                                "Harticul.ccod_articulo=Erp_Lista_Precio_Cliente.ERP_CODART and\n" +
                                "Harticul.ccod_empresa=Erp_Lista_Precio_Cliente.ERP_CODEMP and \n" +
                                "Harticul.cunidad=Erp_Lista_Precio_Cliente.erp_unidad\n" +
                                "where \n" +
                                "ccod_empresa = ? \n" +
                                "and ERP_CODPTV = ? \n" +
                                "and ERP_CODALM = ? \n" +
                                "and erp_tipo = '12 '\n" +
                                "and erp_codigo_concepto = ? \n" +
                                "and (\n" +
                                "(ccod_articulo like ? or cnom_articulo like ? ) \n"
                                + Busqueda_categoria +
                                " )" +
                                "group by \n" +
                                "ccod_articulo, \n" +
                                "cnom_articulo,\n" +
                                "cfamilia,\n" +
                                "ccod_subfamilia,\n" +
                                "codmarca,\n" +
                                "modelo,\n" +
                                "color,\n" +
                                "tratamiento,\n" +
                                "fuelle,\n" +
                                "azas,\n" +
                                "solapa,\n" +
                                "cmoneda_precio, \n" +
                                "erp_monto, \n" +
                                "cunidad, \n" +
                                "ccod_almacen, \n" +
                                "nigv";
//                String stsql = "SELECT TOP(100) * FROM dbo.udf_list_harticul (?,?,?,?) where codigo like ? or Nombre like ?  and cfamilia!='656' and cfamilia!='655' and estado='Activo' order by Nombre";

                PreparedStatement query = connection.prepareStatement(stsql);

                Log.d(TAG, "Codigo_Empresa: " + ConfiguracionEmpresa.Codigo_Empresa);
                Log.d(TAG, "Codigo_PuntoVenta: " + DatosUsuario.Codigo_PuntoVenta);
                Log.d(TAG, "Codigo_Almacen: " + DatosUsuario.Codigo_Almacen);
                Log.d(TAG, "Lista_Precio: " + DatosCliente.Codigo_ListaPrecios);
                if(DatosCliente.Codigo_ListaPrecios==null || DatosCliente.Codigo_ListaPrecios.isEmpty()){
                    DatosCliente.Codigo_ListaPrecios="01";
                }
                Log.d(TAG, "Nombre: " + Nombre);
                query.setString(1, ConfiguracionEmpresa.Codigo_Empresa); // Codigo de la empresa
                query.setString(2, DatosUsuario.Codigo_PuntoVenta);//ERP_CODPTV Punto de Venta
                query.setString(3, DatosUsuario.Codigo_Almacen);//ERP_CODALM Almacen
                query.setString(4, DatosCliente.Codigo_ListaPrecios);//erp_codigo_concepto Lista de precios del cliente
                query.setString(5, Nombre + "%"); //Codigo del producto
                query.setString(6, Nombre + "%"); //Nombre del producto

                ResultSet rs = query.executeQuery();

                while (rs.next()) {
                    Double precio_productos = rs.getDouble("erp_monto");
                    String moneda=rs.getString("cmoneda_precio");
                    if(moneda.trim().equals("S/")){
                        if(!moneda.trim().equals(ConfiguracionEmpresa.Moneda_Empresa)){
                            precio_productos=precio_productos / ConfiguracionEmpresa.ValorTipoCambio;
                        }
                    }else{
                        if(!moneda.trim().equals(ConfiguracionEmpresa.Moneda_Empresa)){
                            precio_productos=precio_productos * ConfiguracionEmpresa.ValorTipoCambio;
                        }
                    }
                   // Log.d(TAG,"cnom_articulo: "+rs.getString("cnom_articulo"));
                   // Log.d(TAG,"stock: "+rs.getString("stock"));
                    arrayList.add(Arrays.asList(
                            rs.getString("ccod_articulo"),
                            rs.getString("cnom_articulo"),
//                           rs.getString("cfamilia"),
//                           rs.getString("ccod_subfamilia"),
//                           rs.getString("codmarca"),
//                           rs.getString("modelo"),
//                           rs.getString("color"),
//                           rs.getString("tratamiento"),
//                           rs.getString("fuelle"),
//                           rs.getString("azas"),
//                           rs.getString("solapa"),
                            rs.getString("stock"),
                            rs.getString("cunidad"),
                            precio_productos.toString(),
                            rs.getString("cmoneda_precio"),
                            rs.getString("ccod_almacen"),
                            rs.getString("nigv")
                    ));
                }

                connection.close();

                if (Nombre.equals(""))
                    CodigosGenerales.listArticulos = arrayList;
            }
        } catch (Exception e) {
            Log.d("BDArticulos", "- getArticulos: " + e.getMessage());
        }
        Log.d(TAG, "Final...");
        return arrayList;
    }

    public ArrayList<List<String>> getArticulosFiltrado(
            String Nombre,
            String Busqueda_categoria) {

        Log.d(TAG, "getArticulosFiltrado...");
        ArrayList<List<String>> arrayList = new ArrayList<>();

        try {
            if (CodigosGenerales.listArticulos.size() > 0 && Nombre.equals("") && Busqueda_categoria.equals("")) {
                return CodigosGenerales.listArticulos;
            } else {
                Busqueda_categoria=" and "+Busqueda_categoria;
                Connection connection = bdata.getConnection();

                String stsql =
                                "select top(50) \n" +
                                "ccod_articulo, \n" +
                                "cnom_articulo, \n" +
                                "cnom_familia,\n" +
                                "cnom_subfamilia,\n" +
                                "erp_concepto1.erp_nomcon as 'codmarca',\n" +
                                "erp_concepto2.erp_nomcon as 'modelo',\n" +
                                "erp_concepto3.erp_nomcon as 'color',\n" +
                                "erp_concepto4.erp_nomcon as 'tratamiento',\n" +
                                "erp_concepto5.erp_nomcon as 'fuelle',\n" +
                                "erp_concepto6.erp_nomcon as 'azas',\n" +
                                "erp_concepto7.erp_nomcon as 'solapa',\n" +
                                "cmoneda_precio,\n" +
                                "erp_monto,\n" +
                                "cunidad,\n" +
                                "Isnull(( SUM(ERP_STOART) - SUM(ERP_STOCOM)),0) as stock,\n" +
                                "Harticul.ccod_almacen, \n" +
                                "nigv \n" +
                                "from Harticul \n" +
                                "inner join Hstock\n" +
                                "on \n" +
                                "Harticul.ccod_articulo=HSTOCK.ERP_CODART and\n" +
                                "Harticul.ccod_empresa=HSTOCK.ERP_CODEMP \n" +
                                "and Harticul.ccod_almacen=HSTOCK.ERP_CODALM\n" +
                                "inner join Erp_Lista_Precio_Cliente\n" +
                                "on\n" +
                                "Harticul.ccod_articulo=Erp_Lista_Precio_Cliente.ERP_CODART and\n" +
                                "Harticul.ccod_empresa=Erp_Lista_Precio_Cliente.ERP_CODEMP and \n" +
                                "Harticul.cunidad=Erp_Lista_Precio_Cliente.erp_unidad\n" +
                                "inner join Hfam_art\n" +
                                "on\n" +
                                "Hfam_art.ccod_empresa=Harticul.ccod_empresa and\n" +
                                "Hfam_art.cfamilia=Harticul.cfamilia\n" +
                                "inner join Hsubfamilia_art\n" +
                                "on\n" +
                                "Hsubfamilia_art.ccod_empresa=Harticul.ccod_empresa and\n" +
                                "Hsubfamilia_art.ccod_subfamilia=Harticul.ccod_subfamilia\n" +
                                "inner join erp_concepto1\n" +
                                "on\n" +
                                "erp_concepto1.erp_codemp=Harticul.ccod_empresa and\n" +
                                "erp_concepto1.erp_codcon=Harticul.codmarca\n" +
                                "inner join erp_concepto2\n" +
                                "on\n" +
                                "erp_concepto2.erp_codemp=Harticul.ccod_empresa and\n" +
                                "erp_concepto2.erp_codcon=Harticul.modelo\n" +
                                "inner join erp_concepto3\n" +
                                "on\n" +
                                "erp_concepto3.erp_codemp=Harticul.ccod_empresa and\n" +
                                "erp_concepto3.erp_codcon=Harticul.color\n" +
                                "inner join erp_concepto4\n" +
                                "on\n" +
                                "erp_concepto4.erp_codemp=Harticul.ccod_empresa and\n" +
                                "erp_concepto4.erp_codcon=Harticul.tratamiento\n" +
                                "inner join erp_concepto5\n" +
                                "on\n" +
                                "erp_concepto5.erp_codemp=Harticul.ccod_empresa and\n" +
                                "erp_concepto5.erp_codcon=Harticul.fuelle\n" +
                                "inner join erp_concepto6\n" +
                                "on\n" +
                                "erp_concepto6.erp_codemp=Harticul.ccod_empresa and\n" +
                                "erp_concepto6.erp_codcon=Harticul.azas\n" +
                                "inner join erp_concepto7\n" +
                                "on\n" +
                                "erp_concepto7.erp_codemp=Harticul.ccod_empresa and\n" +
                                "erp_concepto7.erp_codcon=Harticul.solapa\n" +
                                "\n" +
                                "where \n" +
                                "Harticul.ccod_empresa = ?\n" +
                                "and ERP_CODPTV = ?\n" +
                                "and ERP_CODALM = ? \n" +
                                "and erp_tipo = '12' \n" +
                                "and erp_codigo_concepto = ?\n" +
                                "and (\n" +
                                "(ccod_articulo like ? or cnom_articulo like ? ) \n"
                                + Busqueda_categoria +
                                " )" +
                                "group by \n" +
                                "ccod_articulo, \n" +
                                "cnom_articulo,\n" +
                                "cnom_familia,\n" +
                                "cnom_subfamilia,\n" +
                                "erp_concepto1.erp_nomcon,\n" +
                                "erp_concepto2.erp_nomcon,\n" +
                                "erp_concepto3.erp_nomcon,\n" +
                                "erp_concepto4.erp_nomcon,\n" +
                                "erp_concepto5.erp_nomcon,\n" +
                                "erp_concepto6.erp_nomcon,\n" +
                                "erp_concepto7.erp_nomcon,\n" +
                                "cmoneda_precio, \n" +
                                "erp_monto, \n" +
                                "cunidad, \n" +
                                "ccod_almacen, \n" +
                                "nigv";
//                String stsql = "SELECT TOP(100) * FROM dbo.udf_list_harticul (?,?,?,?) where codigo like ? or Nombre like ?  and cfamilia!='656' and cfamilia!='655' and estado='Activo' order by Nombre";
                Log.d(TAG,"SQL "+stsql);
                PreparedStatement query = connection.prepareStatement(stsql);

                Log.d(TAG, "Codigo_Empresa: " + ConfiguracionEmpresa.Codigo_Empresa);
                Log.d(TAG, "Codigo_PuntoVenta: " + DatosUsuario.Codigo_PuntoVenta);
                Log.d(TAG, "Codigo_Almacen: " + DatosUsuario.Codigo_Almacen);
                Log.d(TAG, "Lista_Precio: " + DatosCliente.Codigo_ListaPrecios);
                if (DatosCliente.Codigo_ListaPrecios == null || DatosCliente.Codigo_ListaPrecios.isEmpty()) {
                    DatosCliente.Codigo_ListaPrecios = "01";
                }
                Log.d(TAG, "Nombre: " + Nombre);
                query.setString(1, ConfiguracionEmpresa.Codigo_Empresa); // Codigo de la empresa
                query.setString(2, DatosUsuario.Codigo_PuntoVenta);//ERP_CODPTV Punto de Venta
                query.setString(3, DatosUsuario.Codigo_Almacen);//ERP_CODALM Almacen
                query.setString(4, DatosCliente.Codigo_ListaPrecios);//erp_codigo_concepto Lista de precios del cliente
                query.setString(5, Nombre + "%"); //Codigo del producto
                query.setString(6, Nombre + "%"); //Nombre del producto

                ResultSet rs = query.executeQuery();

                while (rs.next()) {
                    arrayList.add(Arrays.asList(
                            rs.getString("ccod_articulo"),
                            rs.getString("cnom_articulo"),
                            rs.getString("cnom_familia"),
                            rs.getString("cnom_subfamilia"),
                            rs.getString("codmarca"),
                            rs.getString("modelo"),
                            rs.getString("color"),
                            rs.getString("tratamiento"),
                            rs.getString("fuelle"),
                            rs.getString("azas"),
                            rs.getString("solapa"),
                            rs.getString("stock"),
                            rs.getString("cunidad"),
                            rs.getString("erp_monto"),
                            rs.getString("cmoneda_precio"),
                            rs.getString("ccod_almacen"),
                            rs.getString("nigv")
                    ));
                }

                connection.close();

                if (Nombre.equals(""))
                    CodigosGenerales.listArticulos = arrayList;
            }
        } catch (Exception e) {
            Log.d("BDArticulos", "- getArticulosFiltrado: " + e.getMessage());
        }
        Log.d(TAG, "Final...");
        return arrayList;
    }


    public ArrayList<List<String>> getList(String Nombre) {
        String Codigo = CodigosGenerales.Codigo_Categoria;
        Log.d(TAG, "TipoArray " + CodigosGenerales.TipoArray);
        Log.d(TAG, "Codigo_Categoria " + CodigosGenerales.Codigo_Categoria);
        Log.d(TAG, "Nombre_Categoria " + CodigosGenerales.Nombre_Categoria);
        if (Codigo != null)
            Codigo = Codigo.replace("'", "''");
        switch (CodigosGenerales.TipoArray) {
            case "Articulos":
                return getArticulos(Nombre, "");
            case "Familia":
                return getArticulos(Nombre, " and cfamilia = '" + Codigo + "'");
            case "SubFamilia":
                return getArticulos(Nombre, " and ccod_subfamilia = '" + Codigo + "'");
            case "Concepto":
                String stsql = "";
                switch (CodigosGenerales.ID_Concepto) {
                    case 1:
                        stsql += " codmarca ";
                        break;
                    case 2:
                        stsql += " modelo ";
                        break;
                    case 3:
                        stsql += " color ";
                        break;
                    case 4:
                        stsql += " tratamiento ";
                        break;
                    case 5:
                        stsql += " fuelle ";
                        break;
                    case 6:
                        stsql += " azas ";
                        break;
                    case 7:
                        stsql += " solapa ";
                        break;
                }
                return getArticulos(Nombre, " and " + stsql + " = '" + Codigo + "'");

        }
        return null;

    }

    public ArrayList<List<String>> getListFull() {

        ArrayList<List<String>> arrayList = new ArrayList<>();

        try {
            Connection connection = bdata.getConnection();

            String stsql = "select ccod_articulo from Harticul where ccod_empresa = ? and ccod_almacen = ?";

            PreparedStatement query = connection.prepareStatement(stsql);
            Log.d(TAG, "Codigo_PuntoVenta " + DatosUsuario.Codigo_PuntoVenta);
            Log.d(TAG, "Codigo_Almacen " + DatosUsuario.Codigo_Almacen);
            query.setString(1, ConfiguracionEmpresa.Codigo_Empresa); // Codigo de la empresa
            query.setString(2, DatosUsuario.Codigo_PuntoVenta);//Punto de Venta
//                query.setString(4, CodigosGenerales.Codigo_Almacen);//Almacen
            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                arrayList.add(Arrays.asList(
                        rs.getString("ccod_articulo")
                ));
            }

            connection.close();

        } catch (Exception e) {
            Log.d("BDArticulos", "- getList: " + e.getMessage());
        }
        return arrayList;
    }


    public ArrayList<List<String>> getFichaTecnica(String Codigo_Articulo) {
        try {
            ArrayList<List<String>> ficha_tecnica = new ArrayList<>();
            Connection connection = bdata.getConnection();

            String stsql = "select erp_titulo, erp_descripcion from Erp_Articulo_Datos_Tecnicos Where erp_codemp = ? and erp_codart = ?";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, ConfiguracionEmpresa.Codigo_Empresa); // Codigo de la empresa
            query.setString(2, Codigo_Articulo); // Codigo Articulo

            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                ficha_tecnica.add(Arrays.asList(
                        rs.getString("erp_titulo"),
                        rs.getString("erp_descripcion")
                ));
            }
            connection.close();
            return ficha_tecnica;
        } catch (Exception e) {
            Log.d(TAG, "- getFichaTecnica: " + e.getMessage());
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
            query.setString(1, ConfiguracionEmpresa.Codigo_Empresa); // Codigo de la empresa
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
            Log.d(TAG, "- getPromociones: " + e.getMessage());
        }
        return null;
    }

}