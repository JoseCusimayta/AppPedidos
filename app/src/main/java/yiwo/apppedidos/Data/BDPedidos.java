package yiwo.apppedidos.Data;

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
import yiwo.apppedidos.ConexionBD.BDConexionSQL;

public class BDPedidos {
    private String TAG = "BDPedidos";
    private BDConexionSQL bdata = new BDConexionSQL();
    private BDMotivo bdMotivo = new BDMotivo();
    private BDListDeseo bdListDeseo = new BDListDeseo();
    private BDEmpresa bdEmpresa= new BDEmpresa();
    private Double Precio = 0.00, SubTotal = 0.00, Descuento = 0.00, IGV = 0.00, Importe = 0.00;
    private ArrayList<List<String>> precios_productos = new ArrayList<>();

    public ArrayList<List<String>> getList(String Nombre) {

        ArrayList<List<String>> arrayList = new ArrayList<>();

        try {

            Connection connection = bdata.getConnection();
            String stsql = "SELECT * FROM udf_list_hpedidoc(?,?,?,?,?,?)";


            String year = "0", mes = "0", dia = "0";
            try {
                if (Nombre.contains("/") && Nombre.length() > 9) {
                    dia = Nombre.substring(0, 2);
                    mes = Nombre.substring(3, 5);
                    year = Nombre.substring(6, Nombre.length());
                } else if (Nombre.substring(0, 3).equals("Año")) {
                    year = Nombre.substring(4, Nombre.length());
                } else if (Nombre.substring(0, 3).equals("Mes")) {
                    mes = Nombre.substring(4, Nombre.length());
                } else if (Nombre.substring(0, 3).equals("Dia")) {
                    dia = Nombre.substring(4, Nombre.length());
                }
                Log.d("Fecha", year + "-" + mes + "-" + dia);

            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }
            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, CodigosGenerales.Codigo_Empresa); // vi_codigo_empresa
            query.setString(2, CodigosGenerales.Codigo_Usuario); // vi_codigo_usuario
            query.setString(3, Nombre + "%"); // vi_criterio_busqueda
            query.setString(4, year); // vi_criterio_busqueda_year
            query.setString(5, mes); // vi_criterio_busqueda_month
            query.setString(6, dia); // vi_criterio_busqueda_day


            ResultSet rs = query.executeQuery();
            while (rs.next()) {
                arrayList.add(Arrays.asList(
                        rs.getString("Codigo_Empresa"),     //Codigo_Empresa
                        rs.getString("Codigo_Motivo"),       //Codigo_Motivo
                        rs.getString("Nombre_Motivo"),   //Nombre_Motivo
                        rs.getString("Codigo_Pedido"),   //Codigo_Pedido
                        rs.getString("Codigo_Cliente"),       //Codigo_Cliente
                        rs.getString("Nombre_Cliente"), //Nombre_Cliente
                        rs.getString("RUC_Cliente"),     //RUC_Cliente
                        rs.getString("Codigo_FormaPago"),       //Codigo_FormaPago
                        rs.getString("Nombre_FormaPago"),   //Nombre_FormaPago
                        rs.getString("Importe"),   //Importe
                        rs.getString("IGV"),       //IGV
                        rs.getString("Total"), //Total
                        rs.getString("Fecha_Emision"),     //Fecha_Emision
                        rs.getString("Estado"),       //Estado
                        rs.getString("Observacion"),   //Observacion
                        rs.getString("Usuario_Vendedor"),   //Usuario_Vendedor
                        CodigosGenerales.FormatoFechas.format(rs.getDate("Fecha_Registro"))                              //Fecha_Registro
                ));
            }

            Log.d(TAG,"Lista- "+arrayList);

            connection.close();

        } catch (Exception e) {
            Log.d(TAG, "- getList: " + e.getMessage());
        }
        return arrayList;
    }


    public ArrayList<List<String>> getDetalle(String Cod_Pedido, String Cod_Motivo) {

        ArrayList<List<String>> arrayList = new ArrayList<>();

        try {

            Connection connection = bdata.getConnection();

            String stsql = "select " +
                    "nitem, " +
                    "ccod_articulo, " +
                    "cnom_articulo, " +
                    "cunidad, " +
                    "ncantidad, " +
                    "nprecio, " +
                    "igv_art, " +
                    "porc_descuento, " +
                    "desc2, " +
                    "desc3, " +
                    "erp_desc4, " +
                    "erp_lpn " +
                    "from Hpedidod where cnum_doc =? and idmotivo_venta =? and ccod_empresa =? and  ccod_almacen =?";
        /*    Log.d("Cod_Pedido", Cod_Pedido);
            Log.d("Cod_Motivo",Cod_Motivo);
            Log.d("Codigo_Empresa", CodigosGenerales.Codigo_Empresa);
            Log.d("Codigo_Almacen", CodigosGenerales.Codigo_Almacen);*/
            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, Cod_Pedido);         //Código del Pedido
            query.setString(2, Cod_Motivo);         //Código del Motivo
            query.setString(3, CodigosGenerales.Codigo_Empresa); // Código de la Empresa
            query.setString(4, CodigosGenerales.Codigo_Almacen);    //Código del Almacen

            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                arrayList.add(Arrays.asList(
                         rs.getString("nitem"),
                         rs.getString("ccod_articulo"),
                         rs.getString("cnom_articulo"),
                         rs.getString("cunidad"),
                         rs.getString("ncantidad"),
                         rs.getString("nprecio"),
                         rs.getString("igv_art"),
                         rs.getString("porc_descuento"),
                         rs.getString("desc2"),
                         rs.getString("desc3"),
                         rs.getString("erp_desc4"),
                         rs.getString("erp_lpn")
                        ));

            }
            connection.close();

        } catch (Exception e) {
            Log.d("BDPedidos", "- getDetalle: " + e.getMessage());
        }
        return arrayList;
    }


    public Boolean EnviarPedidos(String idmotivo_venta, String Cod_Cliente, String Nom_Cliente,
                                 String Ruc_Cliente, String Cod_FormaPago, String Tipo_Moneda, String SI_IGV,
                                 String Tipo_Cambio, String Importe,
                                 String Descuento, String Lista_Precios, String Centro_Costo, String Unidad_Negocio,
                                 String Lugar_Entrega,
                                 String Erp_Subtotal, String Erp_Descuento, String Erp_IGV, String Erp_Importe,
                                 String Erp_Percepcion, String Erp_Total, String Erp_dfecha_val, String Comentario, String cTipoCambio) {
        try {

            Connection connection = bdata.getConnection();
            String CodPedido = bdMotivo.getNuevoCodigoPedido(connection);
            if (Cod_Cliente.isEmpty() || CodPedido.isEmpty())
                return false;
            if (
                    GuardarPedido(connection,idmotivo_venta, CodPedido, Cod_Cliente, Nom_Cliente,
                            Ruc_Cliente, Cod_FormaPago, Tipo_Moneda, SI_IGV,
                            Tipo_Cambio, Importe,
                            Descuento, Lista_Precios, Centro_Costo, Unidad_Negocio,
                            Lugar_Entrega,
                            Erp_Subtotal, Erp_Descuento, Erp_IGV, Erp_Importe,
                            Erp_Percepcion, Erp_Total, Erp_dfecha_val, Comentario, cTipoCambio)
                            && bdMotivo.ActualizarCorrelativo(connection)
                            && LlenarDetalle(connection,idmotivo_venta, CodPedido)
                    ) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.d("BDPedidos", "- EnviarPedidos: " + e.getMessage());
        }
        return false;
    }


    private Boolean GuardarPedido(Connection connection ,
                                  String idmotivo_venta, String CodPedido, String Cod_Cliente, String Nom_Cliente,
                                  String Ruc_Cliente, String Cod_FormaPago, String Tipo_Moneda, String SI_IGV,
                                  String Tipo_Cambio, String Importe,
                                  String Descuento, String Lista_Precios, String Centro_Costo, String Unidad_Negocio,
                                  String Lugar_Entrega,
                                  String Erp_Subtotal, String Erp_Descuento, String Erp_IGV, String Erp_Importe,
                                  String Erp_Percepcion, String Erp_Total, String Erp_dfecha_val, String Comentario, String cTipoCambio) {
        try {
            Log.d("Codigo_Empresa", CodigosGenerales.Codigo_Empresa + "");
            Log.d("Unidad_Negocio", CodigosGenerales.Codigo_UnidadNegocio + "");
            Log.d(TAG,"Tipo_Moneda "+Tipo_Moneda);
            String sql = "exec insertarHpedidoC "
                    + "'" + CodigosGenerales.Codigo_Empresa + "', "                             //ccod_empresa
                    + "'" + idmotivo_venta + "', "                  //idmotivo_venta
                    + "'" + CodPedido + "', "                   //cnum_doc
                    + "'" + CodigosGenerales.Codigo_Almacen + "', "                 //ccod_almacen
                    + "'" + CodigosGenerales.FechaActual + "', "                    //dfecha_doc
                    + "'" + CodigosGenerales.FechaActual + "', "                    //dfecha_entr
                    + "'" + Cod_Cliente + "', "                 //ccod_cliente
                    + "'" + Nom_Cliente + "', "                 //cnom_cliente
                    + "'" + Ruc_Cliente + "', "                 //cnum_ruc_cliente
                    + "'" + Cod_FormaPago + "', "                   //ccod_forpago
                    + "'" + CodigosGenerales.Codigo_Usuario + "', "//BDUsuario.CodUsu                   //ccod_person
                    + "'" + Tipo_Moneda + "', "                 //cmoneda
                    + "'" + SI_IGV + "', "                  //si_igv
                    + "'" + Tipo_Cambio + "', "                 //tipo_cambio
                    + "'" + Importe + "', "                 //nimporte
                    + "'" + Descuento + "', "                   //dscto
                    + "'" + Lista_Precios + "', "                   //lista_precios
                    + "'" + Centro_Costo + "', " //ccod_cencos                  //ccod_cencos
                    + "'" + Unidad_Negocio + "', " //erp_codune                 //erp_codune
                    + "'" + CodigosGenerales.Direccion_Almacen + "', "                  //punto_partida
                    + "'" + Lugar_Entrega + "', "                   //lugar_entrega
                    + "'" + CodigosGenerales.Codigo_Usuario + "', "                 //Usuario
                    + "'" + CodigosGenerales.FechaActual + "', "                    //Pc_Fecha
                    + "'" + Erp_Subtotal + "', "                    //erp_Dsubtotal
                    + "'" + Erp_Descuento + "', "                   //erp_Ddescuento
                    + "'" + Erp_IGV + "', "                 //erp_Digv
                    + "'" + Erp_Importe + "', "                 //erp_Dimporte
                    + "'" + Erp_Percepcion + "', "                  //erp_Dpercepcion
                    + "'" + Erp_Total + "', "                   //erp_Dtotal
                    + "'" + Erp_dfecha_val + "', "//Fecha de entrega ed la factura                  //dfecha_val
                    + "'" + Comentario + "', "                  //observacion
                    + "'" + cTipoCambio + "' "; //Tipo de cambio Vta Especial o Compra                  //ctipo_cambio
            Log.d(TAG,"sql- "+sql);
            Statement statement = connection.createStatement();
            statement.execute(sql);
            return true;
        } catch (Exception e) {
            if (e.getMessage().equals("Infracción de la restricción PRIMARY KEY 'Pk_Hpedidoc'. No se puede insertar una clave duplicada en el objeto 'dbo.Hpedidoc'."))
                bdMotivo.ActualizarCorrelativo(connection);

            Log.d("BDPedidos", "- GuardarPedido: " + e.getMessage());
        }
        return false;
    }


    private Boolean LlenarDetalle( Connection connection, String idmotivo_venta, String Codigo_Pedido) {
        try {

            ArrayList<List<String>> listaDeseos = bdListDeseo.getList();

            for (int i = 0; i < listaDeseos.size(); i++) {
                Log.d(TAG,"asd"+listaDeseos);


                String nitem=String.valueOf (i+1);
                String ccod_articulo=listaDeseos.get(i).get(1);
                String nom_articulo=listaDeseos.get(i).get(2);
                String cunidad=listaDeseos.get(i).get(3);
                Double ncantidad=Double.parseDouble(listaDeseos.get(i).get(4));
                Double precio_unitario=Double.parseDouble(listaDeseos.get(i).get(5));
                Double IGV_Articulo=Double.parseDouble(listaDeseos.get(i).get(6));
                Double descuento_1=Double.parseDouble(listaDeseos.get(i).get(7));
                Double descuento_2=Double.parseDouble(listaDeseos.get(i).get(8));
                Double descuento_3=Double.parseDouble(listaDeseos.get(i).get(9));
                Double descuento_4=Double.parseDouble(listaDeseos.get(i).get(10));
                //String LP=listaDeseos.get(i).get(11);
                Double BaseCalculada, BaseImponible,Descuento_Unico,MontoIGV, ImporteTotal, MontoADescontar;

                Descuento_Unico = CodigosGenerales.getDescuenetoUnico(descuento_1,descuento_2,descuento_3,descuento_4);
                BaseImponible = precio_unitario * ncantidad;

                if(ConfiguracionEmpresa.isIncluidoIGV) {
                    MontoADescontar=(BaseImponible / (1 + (IGV_Articulo / 100)))*(Descuento_Unico)/100;
                    BaseCalculada = (BaseImponible / (1 + (IGV_Articulo / 100)))*(100-Descuento_Unico)/100;
                }else {
                    MontoADescontar=BaseImponible*(Descuento_Unico)/100;
                    BaseCalculada=BaseImponible*(100-Descuento_Unico)/100;
                }
                MontoIGV=BaseCalculada*IGV_Articulo/100;
                ImporteTotal = BaseCalculada + MontoIGV;

                String sql = "exec insertarHpedidoD  ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                        " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                        " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";

                PreparedStatement query = connection.prepareStatement(sql);
                query.setString(1, CodigosGenerales.Codigo_Empresa);            //@ccod_empresa
                query.setString(2, CodigosGenerales.Codigo_Almacen);            //@ccod_almacen
                query.setString(3, idmotivo_venta);                             //@idmotivo_venta
                query.setString(4, Codigo_Pedido);                              //@cnum_doc
                query.setString(5, nitem);                                      //@nitem
                query.setString(6, ccod_articulo);                             //@ccod_articulo
                query.setString(7, nom_articulo);                                     //@cnom_articulo
                query.setString(8, cunidad);                        //@cunidad
                query.setString(9, "1");                    //@factor
                query.setString(10, ncantidad.toString());                    //@ncantidad
                query.setString(11, IGV_Articulo.toString());//@nigv
                query.setString(12, precio_unitario.toString());//@npreciouni
                query.setString(13,  String.valueOf (precio_unitario*ncantidad));//@nprecio
                query.setString(14, BaseImponible.toString());//@base_imp
                query.setString(15, BaseCalculada.toString());//@base_calculada
                query.setString(16, ImporteTotal.toString());//@nimporte
                query.setString(17, IGV_Articulo.toString());//@igv_art
                query.setString(18, precio_unitario.toString());//@precio_original
                query.setString(19, descuento_1.toString());//@porc_descuento
                query.setString(20, descuento_2.toString());//@desc2
                query.setString(21, MontoADescontar.toString());//@monto_descuento
                query.setString(22, "N");//@blote
                query.setString(23, "00");//@cnro_lote
                query.setString(24, "12-12-12");//@vcto
                query.setString(25, "N");//@bserie
                query.setString(26, "0");//@cnro_serie
                query.setString(27, "S");//@barticulo
                query.setString(28, "0");//@ptovta_cotizacion
                query.setString(29, "0");//@idmotivo_cotizacion
                query.setString(30, "0");//@numero_cotizacion
                query.setString(31, "0");//@nitem_ref
                query.setString(32, CodigosGenerales.Codigo_CentroCostos);//@ccencos
                query.setString(33, "00");//@ot
                query.setString(34, CodigosGenerales.Codigo_UnidadNegocio);//@erp_codune
                query.setString(35, "0");//@erp_codage
                query.setString(36, "N");//@bonificacion
                query.setString(37, "0");//@largo
                query.setString(38, "0");//@ancho
                query.setString(39, CodigosGenerales.Codigo_Almacen);//@ccod_almacen_org
                query.setString(40, descuento_3.toString());//@desc3
                query.setString(41, "N");//@erp_percepcion_sn
                query.setString(42, "0");//@erp_percepcion_uni
                query.setString(43, "0");//@erp_percepcion_porc
                query.setString(44, "0");//@Cod_Referencia
                query.setString(45, "0");//@Bon_Pro
                query.setString(46, "0");//@ItemBon
                query.setString(47, "N");//@erp_boni_sn
                query.setString(48, "N");//@erp_promo_sn
                query.setString(49, "0");//@erp_item_boni
                query.setString(50, "00");//@erp_presupuesto
                query.setString(51, descuento_4.toString());//@erp_desc4
                query.setString(52, "0");//@erp_peso
                query.setString(53, BaseCalculada.toString());//@erp_base_calc_dec
                query.setString(54, BaseImponible.toString());//@erp_base_imp_dec
                query.setString(55, MontoIGV.toString());//@erp_igv_dec
                query.setString(56, ImporteTotal.toString());//@erp_importe_dec
                query.setString(57, "0");//@erp_comision_porc
                query.setString(58,"0");//@erp_comision_monto
                query.setString(59, "01");//@erp_lpn

                query.execute();
            }

            return true;
        } catch (Exception e) {
            Log.d("BDPedidos", "- LlenarDetalle: " + e.getMessage());
        }
        return false;
    }

    public List<String> getPreciosPedido() {
        Precio = 0.00;
        SubTotal = 0.00;
        Descuento = 0.00;
        IGV = 0.00;
        Importe = 0.00;
        List<String> List = new ArrayList<>();
        try {
            for (int i = 0; i < CodigosGenerales.Codigos_Pedido.size(); i++) {
                Connection connection = bdata.getConnection();
                String stsql = "Select * from udf_generar_precio (?,'12', ?, ?, ?, '01', 'CALCULAR')";

                PreparedStatement query = connection.prepareStatement(stsql);

                query.setString(1, CodigosGenerales.Codigo_Empresa); // Código de la empresa
                query.setString(2, CodigosGenerales.Codigos_Pedido.get(i).get(0)); // Código del producto
                query.setString(3, CodigosGenerales.Codigos_Pedido.get(i).get(1)); // Cantidad del producto
                query.setString(4, CodigosGenerales.Codigos_Pedido.get(i).get(2)); // Código del producto


                ResultSet rs = query.executeQuery();
                while (rs.next()) {
                    Precio += Double.parseDouble(rs.getString("baseimp"));
                    SubTotal += Double.parseDouble(rs.getString("basecalc"));
                    Descuento += Double.parseDouble(rs.getString("montodesc"));
                    IGV += Double.parseDouble(rs.getString("igvcalc"));
                    Importe += Double.parseDouble(rs.getString("importe"));
                    precios_productos.add(Arrays.asList(
                            rs.getString("basecalc"),
                            rs.getString("montodesc"),
                            rs.getString("igvcalc"),
                            rs.getString("importe"),
                            rs.getString("baseimp"),
                            rs.getString("monto"),
                            rs.getString("comiporc"),
                            rs.getString("comimont"),
                            rs.getString("desc1")

                    ));
                }
                connection.close();
            }
            List.add(SubTotal.toString());
            List.add(Descuento.toString());
            List.add(IGV.toString());
            List.add(Importe.toString());

            CodigosGenerales.Precio_SubTotalPedido = SubTotal;
            CodigosGenerales.Precio_DescuentoPedido = Descuento;
            CodigosGenerales.Precio_IGVCalcPedido = IGV;
            CodigosGenerales.Precio_ImporteTotalPedido = Importe;
        } catch (Exception e) {

            Log.d("BDPedidos", "- getPreciosPedido: " + e.getMessage());
        }
        return List;
    }

}