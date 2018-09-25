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

public class BDListDeseo {

    private BDConexionSQL bdata = new BDConexionSQL();
    private BDPromociones bdPromociones = new BDPromociones();
    private BDArticulos bdArticulos = new BDArticulos();
    private String TAG = "BDListDeseo";
    private String Tabla = "HlistDeseo2";

    public ArrayList<List<String>> getList() {

        Log.d(TAG,"Inicio...");
        ArrayList<List<String>> arrayArticulos = new ArrayList<>();

        try {
            Connection connection = bdata.getConnection();
            String stsql = "select " +
                    "nitem, " +
                    "ccod_articulo, " +
                    "nom_articulo, " +
                    "cunidad," +
                    "ncantidad, " +
                    "precio_unitario,  " +
                    "IGV, " +
                    "descuento_1, " +
                    "descuento_2, " +
                    "descuento_3, " +
                    "descuento_4, " +
                    "Lp " +
                    "from " + Tabla +
                    " where " +
                    "ccod_empresa = ? " +
                    "and erp_coduser = ? ";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, CodigosGenerales.Codigo_Empresa); // Codigo de la empresa
            query.setString(2, CodigosGenerales.Codigo_Usuario); // Codigo del Usuario
            ResultSet rs = query.executeQuery();
            while (rs.next()) {
                arrayArticulos.add(Arrays.asList(
                        rs.getString("nitem"),              //Número de Item
                        rs.getString("ccod_articulo"),      //Código del artículo
                        rs.getString("nom_articulo"),       //Código del artículo
                        rs.getString("cunidad"),            //Tipo de Unidad del artículo
                        rs.getString("ncantidad"),          //Cantidad de artículo
                        rs.getString("precio_unitario"),    //Cantidad de artículo
                        rs.getString("IGV"),                //IGV de artículo
                        rs.getString("descuento_1"),        //descuento_1 de artículo
                        rs.getString("descuento_2"),        //descuento_1 de artículo
                        rs.getString("descuento_3"),        //descuento_1 de artículo
                        rs.getString("descuento_4"),        //descuento_1 de artículo
                        rs.getString("Lp")                  //Tipo de Unidad del artículo
                ));
            }
            connection.close();
        } catch (Exception e) {
            Log.d(TAG, "- getList: " + e.getMessage());
        }
        Log.d(TAG,"Final...");
        return arrayArticulos;
    }

    public boolean GuardarListaDeseo(String CodigoArticulo, String NombreArticulo, String Cantidad, String Unidad, String PrecioUnitario, String ListaPrecios, String PorcentajeIGV) {
        try {
            Connection connection = bdata.getConnection();
            List<String> list = new ArrayList<>();
            String sql =
                    "Select * from " + Tabla +
                            " where " +
                            " ccod_empresa = ?" +
                            " and erp_coduser = ?" +
                            " and ccod_ptovta = ?" +
                            " and ccod_almacen = ? " +
                            " and ccod_articulo = ?" +
                            " and cunidad = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, CodigosGenerales.Codigo_Empresa);
            preparedStatement.setString(2, CodigosGenerales.Codigo_Usuario);
            preparedStatement.setString(3, CodigosGenerales.Codigo_PuntoVenta);
            preparedStatement.setString(4, CodigosGenerales.Codigo_Almacen);
            preparedStatement.setString(5, CodigoArticulo);
            preparedStatement.setString(6, Unidad);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                list.add(rs.getString(1));
            }

            if (list.size() > 0)
                return UpdateArticulo(connection,CodigoArticulo, Cantidad, Unidad);
            else
                return InsertarNuevoArticulo(connection,CodigoArticulo, NombreArticulo, Cantidad, Unidad, PrecioUnitario, ListaPrecios,PorcentajeIGV );



            /*
            Double PrecioTotal = Double.parseDouble(Precio) * Integer.parseInt(Cantidad);
            while (rs.next()) {
                list.add(rs.getString(1));
            }
            if (list.size() > 0) {
                Double Cantidad_Actual= getCantidad(CodigoArticulo)+Double.parseDouble(Cantidad);
                List<String> promociones=bdPromociones.getPromociones(CodigoArticulo,Cantidad_Actual.toString());

                Double Precio_Actual= Double.parseDouble(Precio) * Cantidad_Actual;
                Double Descuento_1=Double.parseDouble(promociones.get(0));
                Double Descuento_2=Double.parseDouble(promociones.get(1));
                Double Descuento_3=Double.parseDouble(promociones.get(2));
                Double Descuento_4=Double.parseDouble(promociones.get(3));

                Double Descuento_Unico=-((((100-Descuento_1)*(100-Descuento_2)*(100-Descuento_3)*(100-Descuento_4))/1000000)-100);
                Precio_Actual=Precio_Actual*(100-Descuento_Unico)/100;
                Log.d(TAG,"Descuento_Unico "+Descuento_Unico);
                Log.d(TAG,"Cantidad_Actual "+Cantidad_Actual);
                Log.d(TAG,"Precio "+Precio);
                Log.d(TAG,"Precio_Actual "+Precio_Actual);
                //Si ya existe, Actualizar cantidad
                sql = "update HlistDeseo set " +
                        " ncantidad=" + Cantidad_Actual + ", " +
                        " precio=" + Precio_Actual + " ," +
                        " descuento_1="+Descuento_1 +","+
                        " descuento_2="+Descuento_2 +","+
                        " descuento_3="+Descuento_3 +","+" descuento_4="+Descuento_4 +
                        " where ccod_empresa=? and ccod_articulo=? and erp_coduser=?";

                PreparedStatement update = connection.prepareStatement(sql);
                update.setString(1, CodigosGenerales.Codigo_Empresa);
                update.setString(2, CodigoArticulo);
                update.setString(3, CodigosGenerales.Codigo_Usuario);
                update.execute();
                connection.close();

                return true;
            } else {
                List<String> promociones=bdPromociones.getPromociones(CodigoArticulo,Cantidad);
                Log.d("PrecioTotalInsert", PrecioTotal + "");
                //No existe=>Insertar Producto en la lista de deseos
                sql = "insert into HListDeseo values (" +
                        "?," +      //Código de Empresa
                        "?," +      //Código de Usuario
                        "?," +      //Código de Punto de Venta
                        "?," +      //Código de Almacen
                        "?," +      //Número de Item
                        "?," +      //Código del Artículo
                        "?," +      //Tipo de Unidad de medida
                        "?," +      //Cantidad de artículos
                        "?," +      //Precio del articulo
                        "?," +      //Descuento 1
                        "?," +      //Descuento 2
                        "?," +      //Descuento 3
                        "?," +      //Descuento 4
                        "?)";       //Lista de Precios

                PreparedStatement insert = connection.prepareStatement(sql);
                insert.setString(1, CodigosGenerales.Codigo_Empresa);//Código de Empresa
                insert.setString(2, CodigosGenerales.Codigo_Usuario);//Código de Usuario
                insert.setString(3, CodigosGenerales.Codigo_PuntoVenta);//Código de Punto de Venta
                insert.setString(4, CodigosGenerales.Codigo_Almacen);//Código de Almacen
                insert.setString(5, getNumeroItem());//Numero del Artículo
                insert.setString(6, CodigoArticulo);//Código del Artículo
                insert.setString(7, Unidad);//Tipo de Unidad de medida
                insert.setString(8, Cantidad);//Cantidad de artículos
                insert.setString(9, PrecioTotal.toString());//Precio del articulo
                insert.setString(10, promociones.get(0));//Descuento 1
                insert.setString(11, promociones.get(1));//Descuento 2
                insert.setString(12, promociones.get(2));//Descuento 3
                insert.setString(13, promociones.get(3));//Descuento 4
                insert.setString(14, ListaPrecios);//Lista de Precios
                insert.execute();
                connection.close();
                return true;
            }*/
        } catch (Exception e) {
            Log.d(TAG, "- GuardarListaDeseo: " + e.getMessage());
        }
        return false;
    }

    public boolean EliminarArticulo(String Codigo_Producto) {
        try {
            Connection connection = bdata.getConnection();

            String sql = "delete from " + Tabla + " where ccod_empresa=? and erp_coduser=? and ccod_ptovta =? and ccod_almacen =? and ccod_articulo =? ";
            PreparedStatement query = connection.prepareStatement(sql);
            query.setString(1, CodigosGenerales.Codigo_Empresa);
            query.setString(2, CodigosGenerales.Codigo_Usuario);
            query.setString(3, CodigosGenerales.Codigo_PuntoVenta);
            query.setString(4, CodigosGenerales.Codigo_Almacen);
            query.setString(5, Codigo_Producto);
            query.execute();
            connection.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "- LimpiarPedido: " + e.getMessage());
        }
        return false;
    }

    private Boolean InsertarNuevoArticulo(Connection connection, String CodigoArticulo, String NombreArticulo, String Cantidad, String Unidad, String PrecioUnitario, String ListaPrecios, String PorcentajeIGV) {
        try {
            //Connection connection = bdata.getConnection();
            List<String> promociones = bdPromociones.getPromociones(connection, CodigoArticulo, Cantidad);
            String sql = "insert into " + Tabla + " values (" +
                    "?," +      //Código de Empresa
                    "?," +      //Código de Usuario
                    "?," +      //Código de Punto de Venta
                    "?," +      //Código de Almacen
                    "?," +      //Número de Item
                    "?," +      //Código del Artículo
                    "?," +      //Nombre del Artículo
                    "?," +      //Tipo de Unidad de medida
                    "?," +      //Cantidad de artículos
                    "?," +      //Precio Unitario del articulo
                    "?," +      //IGV del articulo
                    "?," +      //Descuento 1
                    "?," +      //Descuento 2
                    "?," +      //Descuento 3
                    "?," +      //Descuento 4
                    "?)";       //Lista de Precios

            PreparedStatement insert = connection.prepareStatement(sql);
            insert.setString(1, CodigosGenerales.Codigo_Empresa);//Código de Empresa
            insert.setString(2, CodigosGenerales.Codigo_Usuario);//Código de Usuario
            insert.setString(3, CodigosGenerales.Codigo_PuntoVenta);//Código de Punto de Venta
            insert.setString(4, CodigosGenerales.Codigo_Almacen);//Código de Almacen
            insert.setString(5, getNumeroItem());//Numero del Artículo
            insert.setString(6, CodigoArticulo);//Código del Artículo
            insert.setString(7, NombreArticulo);//Nombre del Artículo
            insert.setString(8, Unidad);//Tipo de Unidad de medida
            insert.setString(9, Cantidad);//Cantidad de artículos
            insert.setString(10, PrecioUnitario);//Precio del articulo
            insert.setString(11, PorcentajeIGV);//IGV del articulo
            insert.setString(12, promociones.get(0));//Descuento 1
            insert.setString(13, promociones.get(1));//Descuento 2
            insert.setString(14, promociones.get(2));//Descuento 3
            insert.setString(15, promociones.get(3));//Descuento 4
            insert.setString(16, ListaPrecios);//Lista de Precios
            insert.execute();
            connection.close();
            return true;
        } catch (Exception e) {
            Log.d("BDListDeseo", "- InsertarNuevoArticulo: " + e.getMessage());
            return false;
        }
    }

    private Boolean UpdateArticulo(Connection connection, String CodigoArticulo, String Cantidad, String Unidad) {
        try {
            //Connection connection = bdata.getConnection();
            Double NuevaCantidad = getCantidad(connection, CodigoArticulo) + Double.parseDouble(Cantidad);
            List<String> promociones = bdPromociones.getPromociones(connection, CodigoArticulo, NuevaCantidad.toString());
            String sql =
                    "update " + Tabla + " set " +
                            " ncantidad=" + NuevaCantidad + ", " +
                            " descuento_1=" + promociones.get(0) + "," +
                            " descuento_2=" + promociones.get(1) + "," +
                            " descuento_3=" + promociones.get(2) + "," +
                            " descuento_4=" + promociones.get(3) +
                            " where " +
                            " ccod_empresa=?" +
                            " and erp_coduser=?" +
                            " and ccod_ptovta=?" +
                            " and ccod_almacen=?" +
                            " and ccod_articulo=?" +
                            " and cunidad=?";

            PreparedStatement insert = connection.prepareStatement(sql);
            insert.setString(1, CodigosGenerales.Codigo_Empresa);//Código de Empresa
            insert.setString(2, CodigosGenerales.Codigo_Usuario);//Código de Usuario
            insert.setString(3, CodigosGenerales.Codigo_PuntoVenta);//Código de Punto de Venta
            insert.setString(4, CodigosGenerales.Codigo_Almacen);//Código de Almacen
            insert.setString(5, CodigoArticulo);//Código del Artículo
            insert.setString(6, Unidad);//Tipo de Unidad de medida
            insert.execute();
            connection.close();
            return true;
        } catch (Exception e) {
            Log.d("BDListDeseo", "- UpdateArticulo: " + e.getMessage());
            return false;
        }
    }

    public String getNumeroItem() {
        Integer NroItem = 1;
        try {
            Connection connection = bdata.getConnection();
            String stsql = "select 1+ ( select TOP(1) nitem from " + Tabla + " where ccod_empresa= ? and erp_coduser=? and ccod_ptovta =? and ccod_almacen=? order by nitem desc)  ";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, CodigosGenerales.Codigo_Empresa); // Codigo de la empresa
            query.setString(2, CodigosGenerales.Codigo_Usuario); // Codigo del Usuario
            query.setString(3, CodigosGenerales.Codigo_PuntoVenta); // Codigo del Punto de Venta
            query.setString(4, CodigosGenerales.Codigo_Almacen); // Codigo del Almacen
            ResultSet rs = query.executeQuery();
            while (rs.next()) {
                NroItem = rs.getInt(1);
            }
            if(NroItem==0)
                NroItem=1;
            connection.close();
        } catch (Exception e) {
            Log.d("BDListDeseo", "- getNumeroItem: " + e.getMessage());
        }
        Log.d(TAG, "NroItem: " + NroItem);
        return NroItem.toString();
    }

    public double getCantidad(Connection connection, String Codigo_Producto) {
        Double Cantidad = 0.00;
        try {
            //Connection connection = bdata.getConnection();
            String stsql = "select ncantidad from " + Tabla + " where ccod_empresa=? and erp_coduser=? and ccod_ptovta=? and ccod_almacen=? and ccod_articulo=?";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, CodigosGenerales.Codigo_Empresa); // Codigo de la empresa
            query.setString(2, CodigosGenerales.Codigo_Usuario); // Codigo del Usuario
            query.setString(3, CodigosGenerales.Codigo_PuntoVenta); // Codigo del Punto de Venta
            query.setString(4, CodigosGenerales.Codigo_Almacen); // Codigo del Almacen
            query.setString(5, Codigo_Producto); // Codigo del Producto
            ResultSet rs = query.executeQuery();
            while (rs.next()) {
                Cantidad = rs.getDouble(1);
            }
            //connection.close();
        } catch (Exception e) {
            Log.d("BDListDeseo", "- getCantidad: " + e.getMessage());
        }
        Log.d(TAG, "Cantidad: " + Cantidad);
        return Cantidad;

    }

    public double getPrecio(String Codigo_Producto) {
        Double Precio = 0.00;
        try {
            Connection connection = bdata.getConnection();
            String stsql = "select precio from " + Tabla + " where ccod_empresa=? and erp_coduser=? and ccod_ptovta=? and ccod_almacen=? and ccod_articulo=?";

            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, CodigosGenerales.Codigo_Empresa); // Codigo de la empresa
            query.setString(2, CodigosGenerales.Codigo_Usuario); // Codigo del Usuario
            query.setString(3, CodigosGenerales.Codigo_PuntoVenta); // Codigo del Punto de Venta
            query.setString(4, CodigosGenerales.Codigo_Almacen); // Codigo del Almacen
            query.setString(5, Codigo_Producto); // Codigo del Producto

            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                Precio = rs.getDouble(1);
            }

            connection.close();

        } catch (Exception e) {
            Log.d("BDListDeseo", "- getPrecio: " + e.getMessage());
        }
        Log.d(TAG, "getPrecio: " + Precio);
        return Precio;

    }

    public Boolean LimpiarListaDeseo() {
        try {

            Connection connection = bdata.getConnection();

            String sql = "delete from "+Tabla+" where ccod_empresa = ? and erp_coduser = ? and ccod_ptovta = ? and ccod_almacen = ?";
            PreparedStatement query = connection.prepareStatement(sql);
            query.setString(1, CodigosGenerales.Codigo_Empresa);
            query.setString(2, CodigosGenerales.Codigo_Usuario);
            query.setString(3, CodigosGenerales.Codigo_PuntoVenta);
            query.setString(4, CodigosGenerales.Codigo_Almacen);

            query.execute();
            connection.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "- LimpiarListaDeseo: " + e.getMessage());
        }
        return false;
    }
}