package yiwo.apppedidos.ConexionBD;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import yiwo.apppedidos.AspectosGenerales.ConfiguracionEmpresa;

public class BDConexionSQL {
    private String TAG = "BConexionSQL";
    private String classs = "net.sourceforge.jtds.jdbc.Driver";
    private int Puerto = 1433;
    private RedDisponible redDisponible = new RedDisponible();

    public Connection getConnection() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Connection connection = null;

        String ip;

        if (ConfiguracionEmpresa.isLAN && ConfiguracionEmpresa.isLANAviable)
            ip = ConfiguracionEmpresa.IP_LAN;
        else if (!ConfiguracionEmpresa.isLAN && ConfiguracionEmpresa.isPublicaAviable)
            ip = ConfiguracionEmpresa.IP_Publica;
        else
            return null;

        ip = ip + "/" + ConfiguracionEmpresa.ServerSQL;
        Log.d(TAG, "Conectandose a: " + ip);

        try {
            Class.forName(classs);
            String ConnectionURL = "jdbc:jtds:sqlserver://" + ip + ";"
                    + "databaseName=" + ConfiguracionEmpresa.BD_Empresa + ";user=" + ConfiguracionEmpresa.UsuarioSQL + ";password="
                    + ConfiguracionEmpresa.PasswordSQL + ";";
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