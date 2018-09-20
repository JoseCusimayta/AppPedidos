package yiwo.apppedidos.ConexionBD;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.StrictMode;
import android.util.Log;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;

public class BDConexionSQL {
    private String TAG = "BConexionSQL";
    private String classs = "net.sourceforge.jtds.jdbc.Driver";
    private int Puerto = 1433;
    private RedDisponible redDisponible = new RedDisponible();

    public Connection getConnection() {
        Connection connection;

        //region Conectar a Gumisa
        //connection = getConnectionGumisa();
        //endregion

        //region Conectar a ERP
        connection = getConnectionERP();
        //endregion
        return connection;
        //endregion
    }

    private Connection getConnectionGumisa() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Connection connection = null;

        String ip_lan = "192.168.0.5", ip_publica = "190.187.39.250";
        String ip;
        String Server = "SQLSERVER2008R2";
        if (redDisponible.serverAvailable(CodigosGenerales.getActivity, ip_lan, Puerto))
            ip = ip_lan;
        else if (redDisponible.serverAvailable(CodigosGenerales.getActivity, ip_publica, Puerto))
            ip = ip_publica;
        else
            return null;

        ip = ip + "/" + Server;

        String db = "BD_Gumisa_2018_08_23";
        String un = "sa";
        String password = "Solu123456";

        try {
            Class.forName(classs);
            String ConnectionURL = "jdbc:jtds:sqlserver://" + ip + ";"
                    + "databaseName=" + db + ";user=" + un + ";password="
                    + password + ";loginTimeout=10;socketTimeout=10";
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

    private Connection getConnectionERP() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Connection connection = null;

        String ip_lan = "192.168.1.111", ip_publica = "148.102.21.175";
        String ip;
        String Server = "SQLSERVER2008R2";
        if (redDisponible.serverAvailable(CodigosGenerales.getActivity, ip_lan, Puerto))
            ip = ip_lan;
        else if (redDisponible.serverAvailable(CodigosGenerales.getActivity, ip_publica, Puerto))
            ip = ip_publica;
        else
            return null;

        ip = ip + "/" + Server;
        Log.d(TAG, "Conedctandose a: " + ip);
        String db = "Bd_Consultoria_2015";
        String un = "sa";
        String password = "Solu123456";

        try {
            Class.forName(classs);
            String ConnectionURL = "jdbc:jtds:sqlserver://" + ip + ";"
                    + "databaseName=" + db + ";user=" + un + ";password="
                    + password + ";";
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