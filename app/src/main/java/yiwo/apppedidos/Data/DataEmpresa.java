package yiwo.apppedidos.Data;

import android.util.Log;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.AspectosGenerales.CodigosPreGuardados;
import yiwo.apppedidos.AspectosGenerales.ConfiguracionEmpresa;
import yiwo.apppedidos.ConexionBD.BDConexionSQL;
import yiwo.apppedidos.Control.BDEmpresa;
import yiwo.apppedidos.Control.BDMotivo;

public class DataEmpresa {
    private BDConexionSQL bdata = new BDConexionSQL();
    private BDEmpresa bdEmpresa = new BDEmpresa();
    private BDMotivo bdMotivo = new BDMotivo();
    private String TAG = "DataEmpresa";

    public void CargarDatosEmpresa() {
        try {
            Connection connection = bdata.getConnection();
            List<String> DatosTipoCambio=bdEmpresa.getDatosTipoCambio(connection);
            ConfiguracionEmpresa.CodigoTipoCambio=DatosTipoCambio.get(0);
            ConfiguracionEmpresa.ValorTipoCambio= CodigosGenerales.tryParseDouble(DatosTipoCambio.get(1));

            ConfiguracionEmpresa.Tipo_Monedas = bdEmpresa.getMonedas(connection);
            ConfiguracionEmpresa.Moneda_Trabajo = bdEmpresa.getMonedaTrabajo(connection);
            List<String> MotivoPredeterminado = bdMotivo.getList(connection).get(0);
            ConfiguracionEmpresa.Codigo_Motivo = MotivoPredeterminado.get(0);
            ConfiguracionEmpresa.Nombre_Motivo = MotivoPredeterminado.get(1);
            ConfiguracionEmpresa.ifIGV = bdEmpresa.isIncluidoIGV(connection);
            ConfiguracionEmpresa.isIncluidoIGV=ConfiguracionEmpresa.ifIGV.equals("S");
            connection.close();
        } catch (Exception e) {
            Log.d(TAG, "CargarDatosEmpresa " + e.getMessage());
        }
    }

    public ArrayList<List<String>> getList(String nombre){
        ArrayList<List<String>> list= new ArrayList<>();
        try {
            if(CodigosPreGuardados.listEmpresas.size()>0 && nombre.equals("")){
                list= CodigosPreGuardados.listEmpresas;
            }
            Connection connection = bdata.getConnection();
            list= bdEmpresa.getList(connection,nombre);
            connection.close();
        } catch (Exception e) {
            Log.d(TAG, "CargarDatosEmpresa " + e.getMessage());
        }
        return list;
    }
}