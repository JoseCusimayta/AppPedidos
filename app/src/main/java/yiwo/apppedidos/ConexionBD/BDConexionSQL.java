package yiwo.apppedidos.ConexionBD;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import yiwo.apppedidos.AspectosGenerales.ConfiguracionEmpresa;
import yiwo.apppedidos.AspectosGenerales.DatosConexiones;

public class BDConexionSQL {
    private DatosConexiones datosConexiones= new DatosConexiones();


    public Connection getConnection() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Connection connection = null;

        String ip;

        if (ConfiguracionEmpresa.isLAN && ConfiguracionEmpresa.isLANAviable)
            ip = datosConexiones.getIP_LAN();
        else if (!ConfiguracionEmpresa.isLAN && ConfiguracionEmpresa.isPublicaAviable)
            ip = datosConexiones.getIP_Publica();
        else
            return null;

        ip = ip + "/" + datosConexiones.getServerSQL();
        String TAG = "BConexionSQL";
        Log.d(TAG, "Conectandose a: " + ip);

        try {
            Class.forName(datosConexiones.getDriverSQL());
            String ConnectionURL = "jdbc:jtds:sqlserver://" + ip + ";"
                    + "databaseName=" + datosConexiones.getBD_Empresa() + ";user=" + datosConexiones.getUsuarioSQL() + ";password="
                    + datosConexiones.getPasswordSQL() + ";";
            connection = DriverManager.getConnection(ConnectionURL);
        } catch (SQLException se) {
            Log.e(TAG, "ERROR SQLException - " + se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "ERROR ClassNotFound - " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "ERROR Exception - " + e.getMessage());
        }

        return connection;
    }

}