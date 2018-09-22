package yiwo.apppedidos.AspectosGenerales;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import yiwo.apppedidos.Data.BDArticulos;
import yiwo.apppedidos.Data.BDCentroCostos;
import yiwo.apppedidos.Data.BDClientes;
import yiwo.apppedidos.Data.BDConceptos;
import yiwo.apppedidos.Data.BDEmpresa;
import yiwo.apppedidos.Data.BDFamilia;
import yiwo.apppedidos.Data.BDFiltros;
import yiwo.apppedidos.Data.BDFormaPago;
import yiwo.apppedidos.Data.BDMotivo;
import yiwo.apppedidos.Data.BDPuntoVenta;
import yiwo.apppedidos.Data.BDSubfamilia;
import yiwo.apppedidos.Data.BDUnidNegocios;
import yiwo.apppedidos.InterfacesPerzonalidas.CustomDataModel;
import yiwo.apppedidos.R;

public class CodigosGenerales {
    public static boolean Login = false;
    public static boolean isInicio = false;
    public static boolean isLogin;

    public static Integer ID_Concepto;
    public static String Codigo_Empresa;
    public static String Codigo_Usuario;
    public static String Nombre_Usuario;
    public static String Codigo_Cliente;
    public static String Codigo_Motivo;
    public static String Nombre_Motivo;
    public static String Codigo_PuntoVenta;
    public static String Codigo_CentroCostos;
    public static String Codigo_UnidadNegocio;
    public static String Codigo_Almacen;
    public static String Codigo_Articulo;
    public static String Nombre_Categoria;
    public static String Codigo_Categoria;
    public static String Codigo_Pedido;
    public static String Codigo_ListaPrecios;
    public static String Direccion_Cliente;
    public static int CantidadDatosDialog;
    public static String Year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
    public static SimpleDateFormat FormatoFechas = new SimpleDateFormat("dd-MM-yyyy");
    public static String FechaActual = FormatoFechas.format(new Date());
    public static String Nombre_Cliente;

    public static String TipoArray = "";
    public static ArrayList<List<String>> arrayListEmpresas = new ArrayList<>();
    public static ArrayList<CustomDataModel> dataModels;

    private static BDEmpresa bdEmpresa = new BDEmpresa();
    private static BDPuntoVenta bdPuntoVenta = new BDPuntoVenta();
    private static BDCentroCostos bdCentroCostos = new BDCentroCostos();
    private static BDUnidNegocios bdUnidNegocios = new BDUnidNegocios();

    private static BDArticulos bdArticulos = new BDArticulos();
    private static BDFamilia bdFamilia = new BDFamilia();
    private static BDSubfamilia bdSubfamilia = new BDSubfamilia();
    private static BDConceptos bdConceptos = new BDConceptos();

    private static BDClientes bdClientes = new BDClientes();
    private static BDMotivo bdMotivo = new BDMotivo();
    private static BDFormaPago bdFormaPago = new BDFormaPago();
    private static BDFiltros bdFiltros = new BDFiltros();

    public static Boolean Filtro = false;
    public static List<String> list_familia = new ArrayList<>();
    public static List<String> list_subFamilia = new ArrayList<>();
    public static List<String> list_concepto1 = new ArrayList<>();
    public static List<String> list_concepto2 = new ArrayList<>();
    public static List<String> list_concepto3 = new ArrayList<>();
    public static List<String> list_concepto4 = new ArrayList<>();
    public static List<String> list_concepto5 = new ArrayList<>();
    public static List<String> list_concepto6 = new ArrayList<>();
    public static List<String> list_concepto7 = new ArrayList<>();

    //public static String DOWNLOAD_URL = "http://192.168.1.111:8080/192.168.1.111/Imagenes/";//IP LAN ERP SOLUTIONS
    //public static String DOWNLOAD_URL_ERP = "http://192.168.1.111:8080/192.168.1.111/";//IP LAN ERP SOLUTIONS
    public static String DOWNLOAD_URL = "http://148.102.21.175:8080/192.168.1.111/Imagenes/";//IP Publica ERP SOLUTIONS
//    public static String DOWNLOAD_URL = "http://190.187.39.250:8080/192.168.0.5/Imagenes/";//IP Publica Gumisa
    //public static String DOWNLOAD_URL = "http://192.168.0.5:8080/192.168.0.5/Imagenes/";//IP Lan Gumisa para Pruebas
//    public static String DOWNLOAD_URL_Lan = "http://192.168.0.5:8080/192.168.0.5/Imagenes/";//IP LAN Gumisa

    //    public static String DOWNLOAD_URL = "http://148.102.21.175:8080/192.168.1.111/Imagenes/";//IP Publica ERP SOLUTIONS
    public static String DOWNLOAD_URL_Lan = "http://192.168.1.111:8080/192.168.1.111/Imagenes/";//IP LAN ERP SOLUTIONS
    public static boolean ImagenesMenuCargadas = false;
    //192.168.0.5\Imagenes
    static String root = Environment.getExternalStorageDirectory().toString(); //Obetner el directorio padre
    public static File myDirectorio = new File(root + "/pedidos"); // Crear una carpeta para guardar las imagenes
    //public static File myDirectorio_ERP = new File(root + "/pedidos2"); // Crear una carpeta para guardar las imagenes
    public static String Moneda_Empresa;
    public static double Precio_TotalPedido;
    public static ArrayList<List<String>> Codigos_Pedido = new ArrayList<>();
    public static double Precio_SubTotalPedido;
    public static double Precio_DescuentoPedido;
    public static double Precio_IGVCalcPedido;
    public static double Precio_ImporteTotalPedido;
    public static String Direccion_Almacen;
    public static ArrayList<CustomDataModel> DataModelsList;
    public static Bitmap ImagenGaleria1;
    public static Bitmap ImagenGaleria2;
    public static Bitmap ImagenGaleria3;
    public static Bitmap ImagenGaleria4;
    public static boolean CancelarTask;

    public static ArrayList<List<String>> listCliente = new ArrayList<>();
    public static ArrayList<List<String>> listEmpresas = new ArrayList<>();
    public static ArrayList<List<String>> listArticulos = new ArrayList<>();
    public static String Nombre_Vendedor;
    public static String Celular_Vendedor;
    public static String email_Vendedor;
    public static String Lista_Precio = "01";

    public static ArrayList<List<String>> getArrayList(String Nombre) {
        switch (TipoArray) {
            case "Empresa":
                return bdEmpresa.getList(Nombre);
            case "PuntoVenta":
                return bdPuntoVenta.getList(Nombre);
            case "CentroCosto":
                return bdCentroCostos.getList(Nombre);
            case "UnidadNegocio":
                return bdUnidNegocios.getList(Nombre);
            case "Articulos":
                return bdArticulos.getList(Nombre);
            case "Familia":
                return bdFamilia.getList(Nombre);
            case "SubFamilia":
                return bdSubfamilia.getList(Nombre);
            case "Concepto":
                return bdConceptos.getList(Nombre);
            case "Cliente":
                return bdClientes.getList(Nombre);
            case "Motivo":
                return bdMotivo.getList(Nombre);
            case "FormaPago":
                return bdFormaPago.getList(Nombre);
            case "Moneda":
                return bdEmpresa.getMonedas();
            //Concepto
        }
        return null;
    }

    public static ArrayList<List<String>> getArrayListArticulos(String Nombre) {

        if (Filtro) {
            return bdFiltros.getList(list_familia,
                    list_subFamilia,
                    list_concepto1,
                    list_concepto2,
                    list_concepto3,
                    list_concepto4,
                    list_concepto5,
                    list_concepto6,
                    list_concepto7, Nombre);
        } else {
            switch (TipoArray) {
                case "Familia":
                    return bdArticulos.getListFamilia(Nombre, Codigo_Categoria);
                case "SubFamilia":
                    return bdArticulos.getListSubFamilia(Nombre, Codigo_Categoria);
                case "Concepto":
                    return bdArticulos.getListConcepto(Nombre, Codigo_Categoria);
                case "Articulos":
                    return bdArticulos.getList(Nombre);
            }
        }
        return null;
    }


    public static Date sumarRestarDiasFecha(Date fecha, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0
        return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos
    }

    public static Double getDescuenetoUnico(Double descuento_1, Double descuento_2, Double descuento_3, Double descuento_4) {
        return -((((100 - descuento_1) * (100 - descuento_2) * (100 - descuento_3) * (100 - descuento_4)) / 1000000) - 100);
    }

    public static String RedondearDecimales(Double Monto, Integer Decimales) {
        return String.format("%." + Decimales + "f", Monto);
    }

    public static Double tryParseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException nfe) {
            // Log exception.
            return 0.00;
        }
    }


    /**
     * Hides the soft keyboard
     */
    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Shows the soft keyboard
     */
    public static void showSoftKeyboard(View view, Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }


    public static void CambiarFragment(Fragment fragment, FragmentTransaction transaction) {
        if (transaction != null) {
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
            transaction.replace(R.id.frag_contenedor, fragment);
            transaction.commit();
        }
    }
}