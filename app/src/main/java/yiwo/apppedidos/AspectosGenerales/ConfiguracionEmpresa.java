package yiwo.apppedidos.AspectosGenerales;

import java.util.ArrayList;
import java.util.List;

public class ConfiguracionEmpresa {

    public static Boolean isLAN=false;
    public static Boolean isLANAviable=false;
    public static Boolean isPublicaAviable=false;
    public static Boolean isIncluidoIGV=false;

    public static String Codigo_Empresa;
    public static String RUC_Empresa;
    public static String Moneda_Trabajo;
    public static String Moneda_Empresa;
    public static String Codigo_Motivo;
    public static String Nombre_Motivo;
    public static String CodigoTipoCambio;
    public static String ifIGV;

    public static String IP;

    public static Double ValorTipoCambio;

    public static ArrayList<List<String>> Tipo_Monedas;


    public static Integer Decimales_Empresa=2;

}