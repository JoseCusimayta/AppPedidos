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

public class BDPedidos {
    private String TAG = "BDPedidos";
    private BDConexionSQL bdata = new BDConexionSQL();
    private BDMotivo bdMotivo = new BDMotivo();
    private BDListDeseo bdListDeseo = new BDListDeseo();
    private ArrayList<List<String>> precios_productos = new ArrayList<>();

    public ArrayList<List<String>> getList(String Nombre) {

        ArrayList<List<String>> arrayList = new ArrayList<>();
/*
Select
 Hpedidoc.ccod_empresa  Codigo_Empresa,
 Hpedidoc.idmotivo_venta  Codigo_Motivo,
 erp_motivos_tramite.erp_nommot  Nombre_Motivo,
 Hpedidoc.cnum_doc  Codigo_Pedido,
 Hpedidoc.ccod_cliente  Codigo_Cliente,
 Hpedidoc.cnom_cliente  Nombre_Cliente,
 Hpedidoc.cnum_ruc_cliente  RUC_Cliente,
 Hpedidoc.ccod_forpago  Codigo_FormaPago,
 Hfor_pag.cnom_forpago  Nombre_FormaPago,
 Hpedidoc.erp_Dimporte  Importe,
 Hpedidoc.erp_Digv  IGV,
 Hpedidoc.erp_Dtotal  Total,
 Convert(date,Hpedidoc.dfecha_doc) Fecha_Emision,
 Hpedidoc.aprobado Estado,
 Hpedidoc.observacion_aprobacion ObservacionAprobacion,
 Hpedidoc.Usuario Usuario_Vendedor,
 Hpedidoc.Pc_Fecha Fecha_Registro,
 Hpedidoc.cmoneda,
 Hpedidoc.observacion as Comentario
 from
 Hpedidoc
 Inner Join erp_motivos_tramite On
 Hpedidoc.ccod_empresa = erp_motivos_tramite.erp_codemp
 and Hpedidoc.idmotivo_venta = erp_motivos_tramite.erp_codmot
 Inner Join Hfor_pag On
 Hpedidoc.ccod_empresa = Hfor_pag.ccod_empresa
 and Hpedidoc.idmotivo_venta = Hfor_pag.ccod_forpago
 where
 erp_motivos_tramite.erp_codtid = 'PED'
 and comentario7 = 'Celular'
 and  Hpedidoc.ccod_empresa =?
 and Hpedidoc.idmotivo_venta ='07'
 and Usuario =?
 and
 (
 erp_nommot like ?
 or idmotivo_venta like ?
 or cnum_doc like ?
 or ccod_cliente like ?
 or cnom_cliente like ?
 or cnum_ruc_cliente like ?
 or Hpedidoc.ccod_forpago like ?
 or cnom_forpago like ?
 or aprobado like ?
 or observacion like ?
 or
 (
 DATEPART(yy, dfecha_doc) = ?
 and DATEPART(mm, dfecha_doc) = ?
 AND DATEPART(dd, dfecha_doc) = ?
 )
 )
 Order by Hpedidoc.Pc_Fecha desc
 */
        try {
            Connection connection = bdata.getConnection();
            String stsql =
                    "Select \n" +
                    " Hpedidoc.ccod_empresa  Codigo_Empresa, \n" +
                    " Hpedidoc.idmotivo_venta  Codigo_Motivo, \n" +
                    " erp_motivos_tramite.erp_nommot  Nombre_Motivo, \n" +
                    " Hpedidoc.cnum_doc  Codigo_Pedido,  \n" +
                    " Hpedidoc.ccod_cliente  Codigo_Cliente, \n" +
                    " Hpedidoc.cnom_cliente  Nombre_Cliente, \n" +
                    " Hpedidoc.cnum_ruc_cliente  RUC_Cliente,\n" +
                    " Hpedidoc.ccod_forpago  Codigo_FormaPago, \n" +
                    " Hfor_pag.cnom_forpago  Nombre_FormaPago,\n" +
                    " Hpedidoc.erp_Dimporte  Importe, \n" +
                    " Hpedidoc.erp_Digv  IGV, \n" +
                    " Hpedidoc.erp_Dtotal  Total, \n" +
                    " Convert(date,Hpedidoc.dfecha_doc) Fecha_Emision, \n" +
                    " Hpedidoc.aprobado Estado, \n" +
                    " Hpedidoc.observacion_aprobacion ObservacionAprobacion, \n" +
                    " Hpedidoc.Usuario Usuario_Vendedor, \n" +
                    " Hpedidoc.Pc_Fecha Fecha_Registro,\n" +
                    " Hpedidoc.cmoneda,\n" +
                    " Hpedidoc.observacion as Comentario\n" +
                    " from \n" +
                    " Hpedidoc \n" +
                    " Inner Join erp_motivos_tramite On \n" +
                    " Hpedidoc.ccod_empresa = erp_motivos_tramite.erp_codemp \n" +
                    " and Hpedidoc.idmotivo_venta = erp_motivos_tramite.erp_codmot  \n" +
                    " Inner Join Hfor_pag On \n" +
                    " Hpedidoc.ccod_empresa = Hfor_pag.ccod_empresa \n" +
                    " and Hpedidoc.idmotivo_venta = Hfor_pag.ccod_forpago \n" +
                    " where \n" +
                    " erp_motivos_tramite.erp_codtid = 'PED' \n" +
                    " and comentario7 = 'Celular' \n" +
                    " and  Hpedidoc.ccod_empresa =?\n" +
                    " and Hpedidoc.idmotivo_venta ='07' \n" +
                    " and Usuario =?\n" +
                    " and  \n" +
                    " (\n" +
                    " erp_nommot like ? \n" +
                    " or idmotivo_venta like ?\n" +
                    " or cnum_doc like ?\n" +
                    " or ccod_cliente like ?\n" +
                    " or cnom_cliente like ?\n" +
                    " or cnum_ruc_cliente like ?\n" +
                    " or Hpedidoc.ccod_forpago like ?\n" +
                    " or cnom_forpago like ?\n" +
                    " or aprobado like ?\n" +
                    " or observacion like ?\n" +
                    " or\n" +
                    " (\n" +
                    " DATEPART(yy, dfecha_doc) = ? \n" +
                    " and DATEPART(mm, dfecha_doc) = ? \n" +
                    " AND DATEPART(dd, dfecha_doc) = ?\n" +
                    " )\n" +
                    " ) \n" +
                    " Order by Hpedidoc.Pc_Fecha desc";

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
            query.setString(1, ConfiguracionEmpresa.Codigo_Empresa); // vi_codigo_empresa
            query.setString(2, DatosUsuario.Codigo_Usuario); // vi_codigo_usuario
            query.setString(3, Nombre + "%"); // criterio de busqueda
            query.setString(4, Nombre + "%"); // criterio de busqueda
            query.setString(5, Nombre + "%"); // criterio de busqueda
            query.setString(6, Nombre + "%"); // criterio de busqueda
            query.setString(7, Nombre + "%"); // criterio de busqueda
            query.setString(8, Nombre + "%"); // criterio de busqueda
            query.setString(9, Nombre + "%"); // criterio de busqueda
            query.setString(10, Nombre + "%"); // criterio de busqueda
            query.setString(11, Nombre + "%"); // criterio de busqueda
            query.setString(12, Nombre + "%"); // criterio de busqueda
            query.setString(13, year); // año
            query.setString(14, mes); // nes
            query.setString(15, dia); // dia


            ResultSet rs = query.executeQuery();
            while (rs.next()) {
                arrayList.add(Arrays.asList(
                        rs.getString("Codigo_Empresa"),
                        rs.getString("Codigo_Motivo"),
                        rs.getString("Nombre_Motivo"),
                        rs.getString("Codigo_Pedido"),
                        rs.getString("Codigo_Cliente"),
                        rs.getString("Nombre_Cliente"),
                        rs.getString("RUC_Cliente"),
                        rs.getString("Codigo_FormaPago"),
                        rs.getString("Nombre_FormaPago"),
                        rs.getString("Importe"),
                        rs.getString("IGV"),
                        rs.getString("Total"),
                        rs.getString("Fecha_Emision"),
                        rs.getString("Estado"),
                        rs.getString("ObservacionAprobacion"),
                        rs.getString("Usuario_Vendedor"),
                        CodigosGenerales.FormatoFechas.format(rs.getDate("Fecha_Registro")),
                        rs.getString("cmoneda"),
                        rs.getString("Comentario")
                ));
            }

            Log.d(TAG, "Lista- " + arrayList);

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
                    "erp_lpn,\n" +
                    "base_imp,\n" +
                    "base_calculada,\n" +
                    "nimporte \n" +
                    "from Hpedidod where cnum_doc =? and idmotivo_venta =? and ccod_empresa =? and  ccod_almacen =?";
        /*    Log.d("Cod_Pedido", Cod_Pedido);
            Log.d("Cod_Motivo",Cod_Motivo);
            Log.d("Codigo_Empresa", CodigosGenerales.Codigo_Empresa);
            Log.d("Codigo_Almacen", CodigosGenerales.Codigo_Almacen);*/
            PreparedStatement query = connection.prepareStatement(stsql);
            query.setString(1, Cod_Pedido);         //Código del Pedido
            query.setString(2, Cod_Motivo);         //Código del Motivo
            query.setString(3, ConfiguracionEmpresa.Codigo_Empresa); // Código de la Empresa
            query.setString(4, DatosUsuario.Codigo_Almacen);    //Código del Almacen

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
                        rs.getString("erp_lpn"),
                        rs.getString("base_imp"),
                        rs.getString("base_calculada"),
                        rs.getString("nimporte")
                ));

            }
            connection.close();

        } catch (Exception e) {
            Log.d("BDPedidos", "- getDetalle: " + e.getMessage());
        }
        return arrayList;
    }


    public Boolean EnviarPedidos(Connection connection,
                                 String Importe,
                                 String Descuento, String Subtotal, String IGV,
                                 String Percepcion, String Fecha_Entrega, String Comentario,
                                 Double TipCambio) {
        try {

            String CodPedido = bdMotivo.getNuevoCodigoPedido(connection);
            if (DatosCliente.Codigo_Cliente.isEmpty() || CodPedido.isEmpty())
                return false;
            if (
                    GuardarPedido(connection, CodPedido,
                            Importe, Descuento, Subtotal, IGV,
                            Percepcion, Fecha_Entrega, Comentario)
                            && bdMotivo.ActualizarCorrelativo(connection)
                            && LlenarDetalle(connection, CodPedido, TipCambio)
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

    /*

    INSERT INTO Hpedidoc
    (ccod_empresa,idmotivo_venta,cnum_doc,ccod_almacen,automatico
    ,dfecha_doc,dfecha_entr,ccod_cliente,cnom_cliente,cnum_ruc_cliente
    ,ccod_forpago,ccod_person,idvendedor2,cmoneda,si_igv
    ,ctipo_cambio,tipo_cambio,nimporte,tipo_pedido,dscto
    ,n_orden,lista_precios,pais,ccod_cencos,erp_codune
    ,erp_codage,Ot,cnum_docref,estado,Atencion
    ,porcentaje,Atencion_Prod,porcentaje_prod,aprobado,fecha_aprobacion
    ,observacion_aprobacion,responsable_aprobacion,ccod_transportista,nombre_transp,ccod_vehiculo
    ,punto_partida,lugar_entrega,ccod_contacto,nom_contacto,motivo_traslado
    ,observacion,comentario2,comentario3,comentario4,comentario5
    ,comentario6,comentario7,Usuario,Pc_User,Pc_Fecha
    ,Pc_Ip,Erp_TipDoc,comentario8,titulo01,titulo02
    ,titulo03,titulo04,titulo05,titulo06,titulo07
    ,titulo08,erp_cod_dest,erp_nom_dest,erp_dir_dest,flag_ruta_contacto
    ,ruta_contacto,erp_presupuesto,erp_dscto_stock,erp_Dsubtotal,erp_Ddescuento
    ,erp_Digv,erp_Dimporte,erp_Dpercepcion,erp_Dtotal,erp_glosa
    ,erp_dias,erp_gestor,dfecha_val,erp_dfecha_val,erp_contacto_vendedor
    )
    VALUES
    (
    @ccod_empresa,@idmotivo_venta,@cnum_doc,@ccod_almacen,'A'
    ,@dfecha_doc,@dfecha_entr,@ccod_cliente,@cnom_cliente,@cnum_ruc_cliente
    ,@ccod_forpago,@ccod_person,'00',@cmoneda,@si_igv
    ,@ctipo_cambio,@tipo_cambio,@nimporte,'PEDIDO DIRECTO',@dscto
    ,'',@lista_precios,'001',@ccod_cencos,@erp_codune
    ,'00','00','','Ingresado','Pendiente'
    ,'0%','Pendiente','0%','Sin Aprobacion','1900-01-01'
    ,' ','00','00',' ','00'
    ,@punto_partida,@lugar_entrega,'codcod','nomcot','00'
    ,@observacion,'',' ',' ',' '
    ,' ','Celular',@Usuario,'CELULAR',@Pc_Fecha
    ,'IP CELULAR','00',null,null,null
    ,null,null,null,null,null
    ,null,null,null,null,null
    ,null,'00','S',@erp_Dsubtotal,@erp_Ddescuento
    ,@erp_Digv,@erp_Dimporte,@erp_Dpercepcion,@erp_Dtotal,' '
    ,'0','00',@dfecha_val,null,'00'
    )
     */
    public Boolean GuardarPedido(Connection connection, String CodPedido,
                                 String Importe, String Descuento, String Subtotal, String IGV,
                                 String Percepcion, String Fecha_Entrega, String Comentario) {
        try {
            Log.d(TAG, "Codigo_Empresa " + ConfiguracionEmpresa.Codigo_Empresa + "");
            Log.d(TAG, "Unidad_Negocio " + DatosUsuario.Codigo_UnidadNegocio + "");
            String sql =
                    "INSERT INTO Hpedidoc\n" +
                            "(ccod_empresa,idmotivo_venta,cnum_doc,ccod_almacen,automatico\n" +
                            ",dfecha_doc,dfecha_entr,ccod_cliente,cnom_cliente,cnum_ruc_cliente\n" +
                            ",ccod_forpago,ccod_person,idvendedor2,cmoneda,si_igv\n" +
                            ",ctipo_cambio,tipo_cambio,nimporte,tipo_pedido,dscto\n" +
                            ",n_orden,lista_precios,pais,ccod_cencos,erp_codune\n" +
                            ",erp_codage,Ot,cnum_docref,estado,Atencion\n" +
                            ",porcentaje,Atencion_Prod,porcentaje_prod,aprobado,fecha_aprobacion\n" +
                            ",observacion_aprobacion,responsable_aprobacion,ccod_transportista,nombre_transp,ccod_vehiculo\n" +
                            ",punto_partida,lugar_entrega,ccod_contacto,nom_contacto,motivo_traslado\n" +
                            ",observacion,comentario2,comentario3,comentario4,comentario5\n" +
                            ",comentario6,comentario7,Usuario,Pc_User,Pc_Fecha\n" +
                            ",Pc_Ip,Erp_TipDoc,comentario8,titulo01,titulo02\n" +
                            ",titulo03,titulo04,titulo05,titulo06,titulo07\n" +
                            ",titulo08,erp_cod_dest,erp_nom_dest,erp_dir_dest,flag_ruta_contacto\n" +
                            ",ruta_contacto,erp_presupuesto,erp_dscto_stock,erp_Dsubtotal,erp_Ddescuento\n" +
                            ",erp_Digv,erp_Dimporte,erp_Dpercepcion,erp_Dtotal,erp_glosa\n" +
                            ",erp_dias,erp_gestor,dfecha_val,erp_dfecha_val,erp_contacto_vendedor\n" +
                            ")\n" +
                            "VALUES\n" +
                            "(\n" +
                            "'" + ConfiguracionEmpresa.Codigo_Empresa + "','" + ConfiguracionEmpresa.Codigo_Motivo + "','" + CodPedido + "','" + DatosUsuario.Codigo_Almacen + "','A'\n" +
                            ",'" + CodigosGenerales.FechaActual + "','" + CodigosGenerales.FechaActual + "','" + DatosCliente.Codigo_Cliente + "','" + DatosCliente.Nombre_Cliente + "','" + DatosCliente.RUC_Cliente + "'\n" +
                            ",'" + DatosCliente.Codigo_FormaPago + "','" + DatosUsuario.Codigo_Usuario + "','00','" + ConfiguracionEmpresa.Moneda_Trabajo + "','" + ConfiguracionEmpresa.ifIGV + "'\n" +
                            ",'" + ConfiguracionEmpresa.CodigoTipoCambio + "','" + ConfiguracionEmpresa.ValorTipoCambio + "','" + Importe + "','PEDIDO DIRECTO','" + Descuento + "'\n" +
                            ",'','" + DatosCliente.Codigo_ListaPrecios + "','" + DatosCliente.Codigo_Pais + "','" + DatosUsuario.Codigo_CentroCostos + "','" + DatosUsuario.Codigo_UnidadNegocio + "'\n" +
                            ",'00','00','','Ingresado','Pendiente'\n" +
                            ",'0%','Pendiente','0%','Sin Aprobacion','1900-01-01'\n" +
                            ",' ','00','00',' ','00'\n" +
                            ",'" + DatosUsuario.Direccion_Almacen + "','" + DatosCliente.Direccion_Cliente + "','codcod','nomcot','00'\n" +
                            ",'" + Comentario + "','',' ',' ',' '\n" +
                            ",' ','Celular','" + DatosUsuario.Codigo_Usuario + "','CELULAR','" + CodigosGenerales.FechaActual + "'\n" +
                            ",'IP CELULAR','00',null,null,null\n" +
                            ",null,null,null,null,null\n" +
                            ",null,null,null,null,null\n" +
                            ",null,'00','S','" + Subtotal + "','" + Descuento + "'\n" +
                            ",'" + IGV + "','" + Importe + "','" + Percepcion + "','" + Importe + "',' '\n" +
                            ",'0','00','" + Fecha_Entrega + "',null,'00'\n" +
                            ")";
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


    public Boolean LlenarDetalle(Connection connection, String Codigo_Pedido, Double TipCambio) {
        try {

            ArrayList<List<String>> listaDeseos = bdListDeseo.getList();

            for (int i = 0; i < listaDeseos.size(); i++) {
                String nitem = String.valueOf(i + 1);
                String ccod_articulo = listaDeseos.get(i).get(1);
                String nom_articulo = listaDeseos.get(i).get(2);
                String cunidad = listaDeseos.get(i).get(3);

                Double ncantidad = Double.parseDouble(listaDeseos.get(i).get(4));

                Double precio_unitario = Double.parseDouble(listaDeseos.get(i).get(5))*TipCambio;
                Double IGV_Articulo = Double.parseDouble(listaDeseos.get(i).get(6));

                Double descuento_1 = Double.parseDouble(listaDeseos.get(i).get(7));
                Double descuento_2 = Double.parseDouble(listaDeseos.get(i).get(8));
                Double descuento_3 = Double.parseDouble(listaDeseos.get(i).get(9));
                Double descuento_4 = Double.parseDouble(listaDeseos.get(i).get(10));
                //String LP=listaDeseos.get(i).get(11);
                Double BaseCalculada, BaseImponible, Descuento_Unico, MontoIGV, ImporteTotal, MontoADescontar;
                // precio_unitario=precio_unitario*TipCambio;
                Descuento_Unico = CodigosGenerales.getDescuenetoUnico(descuento_1, descuento_2, descuento_3, descuento_4);
                BaseImponible = precio_unitario * ncantidad;

                if (!ConfiguracionEmpresa.isIncluidoIGV) {
                    MontoADescontar = (BaseImponible / (1 + (IGV_Articulo / 100))) * (Descuento_Unico) / 100;
                    BaseCalculada = (BaseImponible / (1 + (IGV_Articulo / 100))) * (100 - Descuento_Unico) / 100;
                } else {
                    MontoADescontar = BaseImponible * (Descuento_Unico) / 100;
                    BaseCalculada = BaseImponible * (100 - Descuento_Unico) / 100;
                }
                MontoIGV = BaseCalculada * IGV_Articulo / 100;
                ImporteTotal = BaseCalculada + MontoIGV;

                String sql = "exec insertarHpedidoD  ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                        " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                        " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";

                PreparedStatement query = connection.prepareStatement(sql);
                query.setString(1, ConfiguracionEmpresa.Codigo_Empresa);            //@ccod_empresa
                query.setString(2, DatosUsuario.Codigo_Almacen);            //@ccod_almacen
                query.setString(3, ConfiguracionEmpresa.Codigo_Motivo);                             //@idmotivo_venta
                query.setString(4, Codigo_Pedido);                              //@cnum_doc
                query.setString(5, nitem);                                      //@nitem
                query.setString(6, ccod_articulo);                             //@ccod_articulo
                query.setString(7, nom_articulo);                                     //@cnom_articulo
                query.setString(8, cunidad);                        //@cunidad
                query.setString(9, "1");                    //@factor
                query.setString(10, ncantidad.toString());                    //@ncantidad
                query.setString(11, MontoIGV.toString());//@nigv
                query.setString(12, precio_unitario.toString());//@npreciouni
                query.setString(13, precio_unitario.toString());//@nprecio
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
                query.setString(32, DatosUsuario.Codigo_CentroCostos);//@ccencos
                query.setString(33, "00");//@ot
                query.setString(34, DatosUsuario.Codigo_UnidadNegocio);//@erp_codune
                query.setString(35, "0");//@erp_codage
                query.setString(36, "N");//@bonificacion
                query.setString(37, "0");//@largo
                query.setString(38, "0");//@ancho
                query.setString(39, DatosUsuario.Codigo_Almacen);//@ccod_almacen_org
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
                query.setString(58, "0");//@erp_comision_monto
                query.setString(59, "01");//@erp_lpn

                query.execute();
            }

            return true;
        } catch (Exception e) {
            Log.d("BDPedidos", "- LlenarDetalle: " + e.getMessage());
        }
        return false;
    }
}