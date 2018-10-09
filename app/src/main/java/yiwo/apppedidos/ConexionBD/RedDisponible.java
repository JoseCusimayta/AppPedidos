package yiwo.apppedidos.ConexionBD;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import yiwo.apppedidos.AspectosGenerales.ConfiguracionEmpresa;
import yiwo.apppedidos.AspectosGenerales.DatosConexiones;

public class RedDisponible {

    private DatosConexiones datosConexiones = new DatosConexiones();

    public boolean isNetDisponible(Activity activity) {

        ConnectivityManager connectivityManager = (ConnectivityManager)
                activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();
        return (actNetInfo != null && actNetInfo.isConnected());
    }

    public Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

            int val = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    public static boolean isConnectedWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static boolean isConnectedMobile(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    public void isLAN() {

        if(isServerAvailable(datosConexiones.getIP_Publica(), datosConexiones.getPuertoSQL()))
            ConfiguracionEmpresa.IP=datosConexiones.getIP_Publica();
        else if(isServerAvailable(datosConexiones.getIP_LAN(), datosConexiones.getPuertoSQL()))
            ConfiguracionEmpresa.IP=datosConexiones.getIP_LAN();
        else
            ConfiguracionEmpresa.IP=null;
    }


    public void isConnectionLAN(Activity activity) {
        ConfiguracionEmpresa.isLANAviable = serverAvailable(activity, datosConexiones.getIP_LAN(), datosConexiones.getPuertoSQL());
        ConfiguracionEmpresa.isPublicaAviable = serverAvailable(activity, datosConexiones.getIP_Publica(), datosConexiones.getPuertoSQL());
        if (ConfiguracionEmpresa.isLANAviable)
            ConfiguracionEmpresa.isLAN = true;
        if (ConfiguracionEmpresa.isPublicaAviable)
            ConfiguracionEmpresa.isLAN = false;
    }
    public boolean serverAvailable(Activity activity, String DATABASE_ADDR, int DATABASE_PORT) {
        final int TIMEOUT_MS = 2000; // 2 seconds
        // First check if we have network connectivity
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo == null) {
            return false;
        }

        // Attempt to bind to database port
        boolean available = false;
        SocketAddress sockAddr = new InetSocketAddress(DATABASE_ADDR, DATABASE_PORT);
        Socket sock = new Socket();

        // On timeout, SocketTimeoutException is thrown.
        try {
            sock.connect(sockAddr, TIMEOUT_MS);
            available = true;
        } catch (Exception e) {
        } finally {
            try {
                sock.close();
            } catch (Exception e2) {
            }
        }

        return available;
    }


    public boolean isServerAvailable(String DATABASE_ADDR, int DATABASE_PORT) {
        final int TIMEOUT_MS = 2000; // 2 seconds
        // First check if we have network connectivity

        // Attempt to bind to database port
        boolean available = false;
        SocketAddress sockAddr = new InetSocketAddress(DATABASE_ADDR, DATABASE_PORT);
        Socket sock = new Socket();

        // On timeout, SocketTimeoutException is thrown.
        try {
            sock.connect(sockAddr, TIMEOUT_MS);
            available = true;
        } catch (Exception e) {
        } finally {
            try {
                sock.close();
            } catch (Exception e2) {
            }
        }

        return available;
    }
}