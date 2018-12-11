package yiwo.apppedidos.AspectosGenerales;

import android.os.Environment;

import java.io.File;

public class DatosConexiones {
    private String CarpetaImagenes = "Imagenes";
    private int PuertoSQL = 1433;
    private int PuertoImagenes = 8080;

    private String UsuarioSQL = "sa";
    private String PasswordSQL = "Solu123456";


    private String UsuarioAPP = "Admin";
    private String ClaveAPP = "12345678";

    private String DriverSQL = "net.sourceforge.jtds.jdbc.Driver";

    static String root = Environment.getExternalStorageDirectory().toString(); //Obetner el directorio padre
    public static File myDirectorio = new File(root + "/pedidos"); // Crear una carpeta para guardar las imagenes


//    Configuración ERP
    private String IP_LAN = "192.168.1.111";
    private String IP_Publica = "148.102.21.175";
    private String BD_Empresa = "Bd_Consultoria_2015";
    private String ServerSQL = "SQLSERVER2008R2";

    //Configuración Gumisa
//    private static String IP_LAN="192.168.0.5";
//    private static String IP_Publica="190.187.39.250";
//    private static String BD_Empresa="BD_Gumisa_2018_08_23";
//    private static String ServerSQL="SQLSERVER2008R2";

/*
RUC Empresa: 20600124782
TLF Señor Pun: 962341227
*/


    public String getCarpetaImagenes() {
        return CarpetaImagenes;
    }

    public int getPuertoSQL() {
        return PuertoSQL;
    }

    public int getPuertoImagenes() {
        return PuertoImagenes;
    }

    public String getUsuarioSQL() {
        return UsuarioSQL;
    }

    public String getPasswordSQL() {
        return PasswordSQL;
    }

    public String getUsuarioAPP() {
        return UsuarioAPP;
    }

    public String getClaveAPP() {
        return ClaveAPP;
    }

    public String getIP_LAN() {
        return IP_LAN;
    }

    public String getIP_Publica() {
        return IP_Publica;
    }

    public String getBD_Empresa() {
        return BD_Empresa;
    }

    public String getServerSQL() {
        return ServerSQL;
    }

    public String getDriverSQL() {
        return DriverSQL;
    }
}