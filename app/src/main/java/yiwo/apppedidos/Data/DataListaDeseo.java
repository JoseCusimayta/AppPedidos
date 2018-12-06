package yiwo.apppedidos.Data;

import android.util.Log;
import android.widget.TextView;

import java.sql.Connection;
import java.util.List;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.ConexionBD.BDConexionSQL;
import yiwo.apppedidos.Control.BDListDeseo;

public class DataListaDeseo {
    private BDListDeseo bdListDeseo = new BDListDeseo();
    private BDConexionSQL bdata = new BDConexionSQL();
    private String TAG = "DataListaDeseo";

    public Boolean GuardarListaDeseo(String CodigoArticulo, String NombreArticulo, String Cantidad, String Unidad, String PrecioUnitario, String ListaPrecios, String PorcentajeIGV) {
        try {
            Boolean exito;
            Connection connection = bdata.getConnection();
            if (bdListDeseo.isExists(connection, CodigoArticulo, Unidad)) {
                Log.d(TAG, "GuardarListaDeseo + isExists");
                exito = bdListDeseo.UpdateArticulo(connection, CodigoArticulo, Cantidad, Unidad);
            } else {
                Log.d(TAG, "GuardarListaDeseo + NoExists");
                exito = bdListDeseo.InsertarNuevoArticulo(connection, CodigoArticulo, NombreArticulo, Cantidad, Unidad, PrecioUnitario, ListaPrecios, PorcentajeIGV);
            }
            connection.close();
            return exito;
        } catch (Exception e) {
            Log.d(TAG, "GuardarListaDeseo " + e.getMessage());
        }
        return false;
    }

    public void setResumen(TextView cantidad, TextView importe_total) {
        List<String> resumen = bdListDeseo.getResumen();
        if (resumen != null) {
            importe_total.setText("Total: " +  CodigosGenerales.RedondearDecimalesFormateado (resumen.get(2)));
            cantidad.setText("Filas: " + resumen.get(0) + " \n Can: " + CodigosGenerales.RedondearDecimalesFormateado (resumen.get(1)));
        }
    }
}
