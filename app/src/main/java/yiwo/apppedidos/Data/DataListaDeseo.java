package yiwo.apppedidos.Data;

import android.util.Log;

import java.sql.Connection;

import yiwo.apppedidos.ConexionBD.BDConexionSQL;
import yiwo.apppedidos.Control.BDListDeseo;

public class DataListaDeseo {
    private BDListDeseo bdListDeseo= new BDListDeseo();
    private BDConexionSQL bdata= new BDConexionSQL();
    private String TAG="DataListaDeseo";

    public Boolean GuardarListaDeseo(String CodigoArticulo, String NombreArticulo, String Cantidad, String Unidad, String PrecioUnitario, String ListaPrecios, String PorcentajeIGV) {
        try{
            Boolean exito;
            Connection connection= bdata.getConnection();
            if(bdListDeseo.isExists(connection,CodigoArticulo,Unidad)){
                Log.d(TAG,"GuardarListaDeseo + isExists");
                exito= bdListDeseo.UpdateArticulo(connection,CodigoArticulo,Cantidad,Unidad);
            }else{
                Log.d(TAG,"GuardarListaDeseo + NoExists");
                exito= bdListDeseo.InsertarNuevoArticulo(connection,CodigoArticulo,NombreArticulo,Cantidad,Unidad,PrecioUnitario,ListaPrecios,PorcentajeIGV);
            }
            connection.close();
            return exito;
        }catch (Exception e){
            Log.d(TAG,"GuardarListaDeseo "+e.getMessage());
        }
        return false;
    }
}
