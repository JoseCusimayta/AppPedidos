package yiwo.apppedidos.Data;

import android.util.Log;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import yiwo.apppedidos.AspectosGenerales.CodigosPreGuardados;
import yiwo.apppedidos.AspectosGenerales.ConfiguracionEmpresa;
import yiwo.apppedidos.ConexionBD.BDConexionSQL;

public class DataEmpresa {
    BDConexionSQL bdata = new BDConexionSQL();
    BDEmpresa bdEmpresa = new BDEmpresa();
    BDMotivo bdMotivo = new BDMotivo();
    String TAG = "DataEmpresa";

    public void CargarDatosEmpresa() {
        try {
            Connection connection = bdata.getConnection();

            ConfiguracionEmpresa.Tipo_Monedas = bdEmpresa.getMonedas(connection);
            ConfiguracionEmpresa.isIncluidoIGV = bdEmpresa.isIncluidoIGV(connection);
            ConfiguracionEmpresa.Moneda_Trabajo = bdEmpresa.getMonedaTrabajo(connection);
            ConfiguracionEmpresa.Tipo_CambioEmpresa = bdEmpresa.getTipoCambio(connection);
            List<String> MotivoPredeterminado = bdMotivo.getList(connection).get(0);
            ConfiguracionEmpresa.Codigo_Motivo = MotivoPredeterminado.get(0);
            ConfiguracionEmpresa.Nombre_Motivo = MotivoPredeterminado.get(1);
            connection.close();
        } catch (Exception e) {
            Log.d(TAG, "CargarDatosEmpresa " + e.getMessage());
        }
    }

    public ArrayList<List<String>> getList(String nombre){
        try {
            if(CodigosPreGuardados.listEmpresas.size()>0 && nombre.equals("")){
                return CodigosPreGuardados.listEmpresas;
            }
            Connection connection = bdata.getConnection();
            return bdEmpresa.getList(connection,nombre);
        } catch (Exception e) {
            Log.d(TAG, "CargarDatosEmpresa " + e.getMessage());
        }
        return null;
    }
}