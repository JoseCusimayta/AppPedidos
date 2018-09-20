package yiwo.apppedidos.Data;

import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;

import yiwo.apppedidos.ConexionBD.BDConexionSQL;

public class BDActivarFunciones {
    String TAG="BDActivarFunciones";
    BDConexionSQL bdata = new BDConexionSQL();
    Boolean Actualizar=false;
    public void ActualizarFunciones(){
        if(Actualizar) {
            ActualizarFuncion_udf_list_harticul();
            ActualizarFuncion_udf_list_hpedidoc();
            ActualizarFuncion_sv_list_user_login();
        }
    }


    private void ActualizarFuncion_udf_list_harticul(){
        try {
            Connection connection = bdata.getConnection();

            String stsql = "alter Function udf_list_harticul(                                  \n" +
                    "--Declare                  \n" +
                    "@vi_empresa varchar(20),    \n" +
                    "@vi_articulo varchar(20),    \n" +
                    "@vi_pto_venta varchar(3),    \n" +
                    "@vi_alma varchar(3)    \n" +
                    ")                  \n" +
                    "Returns    \n" +
                    "--Declare    \n" +
                    "@table Table(    \n" +
                    "codigo varchar(20),    \n" +
                    "Nombre varchar(150),    \n" +
                    "cfamilia varchar(20),     \n" +
                    "ccod_subfamilia varchar(20),     \n" +
                    "codmarca varchar(20),     \n" +
                    "modelo varchar(20),     \n" +
                    "color varchar(20),     \n" +
                    "tratamiento varchar(20),     \n" +
                    "fuelle varchar(20),     \n" +
                    "azas varchar(20),     \n" +
                    "solapa varchar(20),     \n" +
                    "cunidad varchar(20),    \n" +
                    "stock decimal(18,2),  \n" +
                    "monto decimal(18,2) ,  \n" +
                    "estado varchar(50) \n" +
                    ")                                  \n" +
                    "as                          \n" +
                    "Begin        \n" +
                    "--IF (@vi_articulo = '')    \n" +
                    "--Begin    \n" +
                    "Insert into @table                              \n" +
                    "Select Harticul.ccod_articulo, Harticul.cnom_articulo,    \n" +
                    "cfamilia, ccod_subfamilia, codmarca, modelo, color, tratamiento, fuelle, azas, solapa, cunidad,    \n" +
                    "Isnull(( Select ROUND(Sum(Hstock.erp_stoart),2)  - ROUND(Sum(Hstock.Erp_stocom),2)\n" +
                    "From Hstock    \n" +
                    "Where Hstock.erp_codemp = @vi_empresa and                \n" +
                    "Hstock.erp_codptv = @vi_pto_venta and                \n" +
                    "Hstock.erp_codalm = @vi_alma and                \n" +
                    "Hstock.erp_codart = Harticul.ccod_articulo                \n" +
                    "),0) as stock,  \n" +
                    "(Select erp_monto  \n" +
                    "FROM Erp_Lista_Precio_Cliente   \n" +
                    "where erp_codemp = @vi_empresa and erp_tipo='12' and erp_unidad = Harticul.cunidad AND erp_codart = Harticul.ccod_articulo and erp_codigo_concepto = '01') as monto,\n" +
                    "estado\n" +
                    "from Harticul    \n" +
                    "--Inner Join Harticul    \n" +
                    "where Harticul.ccod_empresa = @vi_empresa and ccod_articulo like '%'+@vi_articulo+'%'    \n" +
                    "--End    \n" +
                    "return                                  \n" +
                    "End        \n" +
                    "/*  \n" +
                    "SELECT * FROM dbo.udf_list_harticul('01','PRODUCT001','001','001')  \n" +
                    "Select * from harticul where ccod_empresa ='01'  \n" +
                    "Select erp_monto, 0.00 baseimp, erp_desc1, erp_desc2, erp_desc3, erp_desc4, 0.00 basecalc, 0.00 igvcalc, 0.00 importe  \n" +
                    " FROM Erp_Lista_Precio_Cliente   \n" +
                    " where erp_codemp = '01' and erp_tipo='12' and erp_unidad = 'UND' AND erp_codart = 'PRODUCT001' and erp_codigo_concepto = '01'  \n" +
                    "*/";

            PreparedStatement query = connection.prepareStatement(stsql);

            query.execute();

            connection.close();

        } catch (Exception e) {
            Log.d(TAG, "- ActualizarFuncion_udf_list_harticul: " + e.getMessage());
        }
    }
    private void ActualizarFuncion_udf_list_hpedidoc(){
        try {
            Connection connection = bdata.getConnection();

            String stsql = "alter Function udf_list_hpedidoc(\n" +
                    "@vi_codigo_empresa varchar(50),\n" +
                    "@vi_codigo_usuario varchar(50),\n" +
                    "@vi_criterio_busqueda varchar(50),\n" +
                    "@vi_criterio_busqueda_year varchar(50),\n" +
                    "@vi_criterio_busqueda_month varchar(50),\n" +
                    "@vi_criterio_busqueda_day varchar(50)\n" +
                    ")\n" +
                    "Returns\n" +
                    "@table Table(\n" +
                    "Codigo_Empresa varchar(50),\n" +
                    "Codigo_Motivo varchar(150),\n" +
                    "Nombre_Motivo varchar(50),\n" +
                    "Codigo_Pedido varchar(50),\n" +
                    "Codigo_Cliente varchar(50),\n" +
                    "Nombre_Cliente varchar(50),\n" +
                    "RUC_Cliente varchar(50),\n" +
                    "Codigo_FormaPago varchar(50),\n" +
                    "Nombre_FormaPago varchar(50),\n" +
                    "Importe varchar(50),\n" +
                    "IGV varchar(50),\n" +
                    "Total varchar(50),\n" +
                    "Fecha_Emision Date,\n" +
                    "Estado varchar(50),\n" +
                    "Observacion varchar(50),\n" +
                    "Usuario_Vendedor varchar(50),\n" +
                    "Fecha_Registro Date\n" +
                    ")\n" +
                    "as\n" +
                    "Begin\n" +
                    "Insert into @table\n" +
                    "\n" +
                    "Select\n" +
                    "Hpedidoc.ccod_empresa  Codigo_Empresa,\n" +
                    "Hpedidoc.idmotivo_venta  Codigo_Motivo,\n" +
                    "erp_motivos_tramite.erp_nommot  Nombre_Motivo,\n" +
                    "Hpedidoc.cnum_doc  Codigo_Pedido, \n" +
                    "Hpedidoc.ccod_cliente  Codigo_Cliente,\n" +
                    "Hpedidoc.cnom_cliente  Nombre_Cliente,\n" +
                    "Hpedidoc.cnum_ruc_cliente  RUC_Cliente,\n" +
                    "Hpedidoc.ccod_forpago  Codigo_FormaPago,\n" +
                    "Hfor_pag.cnom_forpago  Nombre_FormaPago,\n" +
                    "Hpedidoc.erp_Dimporte  Importe,\n" +
                    "Hpedidoc.erp_Digv  IGV,\n" +
                    "Hpedidoc.erp_Dtotal  Total,\n" +
                    "Hpedidoc.dfecha_doc Fecha_Emision,\n" +
                    "Hpedidoc.aprobado Estado,\n" +
                    "Hpedidoc.observacion Observacion,\n" +
                    "Hpedidoc.Usuario Usuario_Vendedor,\n" +
                    "Hpedidoc.Pc_Fecha Fecha_Registro\n" +
                    " from Hpedidoc\n" +
                    "Inner Join erp_motivos_tramite On\n" +
                    "Hpedidoc.ccod_empresa = erp_motivos_tramite.erp_codemp\n" +
                    "and Hpedidoc.idmotivo_venta = erp_motivos_tramite.erp_codmot\n" +
                    "\n" +
                    "Inner Join Hfor_pag On\n" +
                    "Hpedidoc.ccod_empresa = Hfor_pag.ccod_empresa\n" +
                    "and Hpedidoc.idmotivo_venta = Hfor_pag.ccod_forpago\n" +
                    "where    \n" +
                    "erp_motivos_tramite.erp_codtid = 'PED' and\n" +
                    "comentario7 = 'Celular' and\n" +
                    "\n" +
                    "Hpedidoc.ccod_empresa =@vi_codigo_empresa and\n" +
                    "Hpedidoc.idmotivo_venta ='07' and\n" +
                    "Usuario =@vi_codigo_usuario and\n" +
                    "\n" +
                    "(\n" +
                    "\n" +
                    "\n" +
                    "erp_nommot like '%'+@vi_criterio_busqueda+'%' or\n" +
                    "idmotivo_venta like '%'+@vi_criterio_busqueda+'%' or\n" +
                    "cnum_doc like '%'+@vi_criterio_busqueda+'%' or\n" +
                    "ccod_cliente like '%'+@vi_criterio_busqueda+'%' or\n" +
                    "cnom_cliente like '%'+@vi_criterio_busqueda+'%' or\n" +
                    "cnum_ruc_cliente like '%'+@vi_criterio_busqueda+'%' or\n" +
                    "Hpedidoc.ccod_forpago like '%'+@vi_criterio_busqueda+'%' or\n" +
                    "cnom_forpago like '%'+@vi_criterio_busqueda+'%' or\n" +
                    "aprobado like '%'+@vi_criterio_busqueda+'%' or\n" +
                    "observacion like '%'+@vi_criterio_busqueda+'%' or" +
                    "(DATEPART(yy, dfecha_doc) = @vi_criterio_busqueda_year and DATEPART(mm, dfecha_doc) = @vi_criterio_busqueda_month AND DATEPART(dd, dfecha_doc) = @vi_criterio_busqueda_day)) \n" +
                    "Order by Hpedidoc.Pc_Fecha desc\n" +
                    "return\n" +
                    "End;";

            PreparedStatement query = connection.prepareStatement(stsql);

            query.execute();

            connection.close();

        } catch (Exception e) {
            Log.d(TAG, "- ActualizarFuncion_udf_list_harticul: " + e.getMessage());
        }
    }
    private void ActualizarFuncion_sv_list_user_login(){
        try {
            Connection connection = bdata.getConnection();

            String stsql = "alter view sv_list_user_login As    \n" +
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
                    "and erp_usuario.erp_coduser = Hvended.ccod_vendedor ";

            PreparedStatement query = connection.prepareStatement(stsql);

            query.execute();

            connection.close();

            Log.d(TAG, "- udf_list_harticul actualizada");
        } catch (Exception e) {
            Log.d(TAG, "- ActualizarFuncion_udf_list_harticul: " + e.getMessage());
        }
    }
}
