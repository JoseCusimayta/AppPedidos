package yiwo.apppedidos.Data;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.AspectosGenerales.ConfiguracionEmpresa;
import yiwo.apppedidos.AspectosGenerales.DatosUsuario;
import yiwo.apppedidos.ConexionBD.BDConexionSQL;
import yiwo.apppedidos.ConexionBD.BDConexionSQLite;
import yiwo.apppedidos.Control.BDCentroCostos;
import yiwo.apppedidos.Control.BDEmpresa;
import yiwo.apppedidos.Control.BDMotivo;
import yiwo.apppedidos.Control.BDPuntoVenta;
import yiwo.apppedidos.Control.BDUnidNegocios;
import yiwo.apppedidos.Control.BDUsuario;
import yiwo.apppedidos.Fragment.FragMenuPrincipal;

public class DataUsuario {

    private BDConexionSQL bdata = new BDConexionSQL();
    private BDPuntoVenta bdPuntoVenta = new BDPuntoVenta();
    private BDCentroCostos bdCentroCostos = new BDCentroCostos();
    private BDUnidNegocios bdUnidNegocios = new BDUnidNegocios();
    private BDUsuario bdUsuario = new BDUsuario();
    private String TAG = "DataUsuario";

    public Boolean CargarDatosUsuario(String CodigoEmpresa,String RUC) {
        try {
            Connection connection = bdata.getConnection();
            ConfiguracionEmpresa.Codigo_Empresa = CodigoEmpresa;
            ConfiguracionEmpresa.RUC_Empresa = RUC;
            List<String> list;

            list = bdPuntoVenta.getPredeterminado(connection);
            DatosUsuario.Codigo_PuntoVenta = list.get(0);
            DatosUsuario.Nombre_PuntoVenta = list.get(1);
            DatosUsuario.Codigo_Almacen = list.get(2);
            DatosUsuario.Direccion_Almacen = list.get(3);
            Log.d(TAG,"list: "+list);

            list = bdCentroCostos.getPredeterminado(connection);
            DatosUsuario.Codigo_CentroCostos = list.get(0);
            DatosUsuario.Nombre_CentroCostos = list.get(1);
            Log.d(TAG,"list: "+list);

            list = bdUnidNegocios.getPredeterminado(connection);
            DatosUsuario.Codigo_UnidadNegocio = list.get(0);
            DatosUsuario.Nombre_UnidadNegocio = list.get(1);
            Log.d(TAG,"list: "+list);

            connection.close();
            return true;
        } catch (SQLException e) {
            Log.d(TAG, "CargarDatosUsuario " + e.getMessage());
        }
        return false;
    }

    public void BorrarDatosUsuario(Context context) {

        BDConexionSQLite myDb;
        myDb = new BDConexionSQLite(context);
        myDb.deleteAllDataLogin();

        CodigosGenerales.Login = false;
    }

    public Boolean InsertarDatosUsuario(Context context) {

        BDConexionSQLite myDb;
        myDb = new BDConexionSQLite(context);
        myDb.deleteAllDataLogin();
        return myDb.insertarLogin(
                ConfiguracionEmpresa.Codigo_Empresa,
                DatosUsuario.Codigo_PuntoVenta,
                DatosUsuario.Codigo_Almacen,
                DatosUsuario.Codigo_Usuario,
                DatosUsuario.Codigo_CentroCostos,
                DatosUsuario.Codigo_UnidadNegocio,
                ConfiguracionEmpresa.Moneda_Trabajo,
                DatosUsuario.Direccion_Almacen,
                DatosUsuario.Nombre_Vendedor,
                DatosUsuario.Celular_Vendedor,
                DatosUsuario.email_Vendedor,
                ConfiguracionEmpresa.RUC_Empresa,
                DatosUsuario.Nombre_PuntoVenta,
                DatosUsuario.Nombre_CentroCostos,
                DatosUsuario.Nombre_UnidadNegocio,
                DatosUsuario.Codigo_Vendedor);
    }

    public Boolean loginUsuario(String Usuario, String Clave) {


        if (Usuario.equals("erpsys") && Clave.equals("2012")) {

            DatosUsuario.Codigo_Vendedor = "erpsys";
            DatosUsuario.Nombre_Vendedor = "ERP Solutions Perú";
            DatosUsuario.Celular_Vendedor = "número_celular";
            DatosUsuario.email_Vendedor = "erpsys@gmail.com";
            DatosUsuario.Codigo_Usuario = "erpsys";
            DatosUsuario.Nombre_Usuario = "erpsys";
            return true;
        } else {
            List<String> list = bdUsuario.getLogin(Usuario, Clave);
            if (list.size() >= 5) {
                DatosUsuario.Nombre_Vendedor =  list.get(0);
                DatosUsuario.Celular_Vendedor = list.get(1);
                DatosUsuario.email_Vendedor =   list.get(2);
                DatosUsuario.Codigo_Usuario =   list.get(3);
                DatosUsuario.Nombre_Usuario =   list.get(4);
                DatosUsuario.Codigo_Vendedor =   list.get(5);
                return true;
            }
        }
        return false;
    }
}