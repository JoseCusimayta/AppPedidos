package yiwo.apppedidos.AspectosGenerales;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import yiwo.apppedidos.Control.BDArticulos;
import yiwo.apppedidos.Control.BDCentroCostos;
import yiwo.apppedidos.Control.BDClientes;
import yiwo.apppedidos.Control.BDConceptos;
import yiwo.apppedidos.Control.BDEmpresa;
import yiwo.apppedidos.Control.BDFamilia;
import yiwo.apppedidos.Control.BDFiltros;
import yiwo.apppedidos.Control.BDFormaPago;
import yiwo.apppedidos.Control.BDPuntoVenta;
import yiwo.apppedidos.Control.BDSubfamilia;
import yiwo.apppedidos.Control.BDUnidNegocios;
import yiwo.apppedidos.InterfacesPerzonalidas.CustomDataModel;
import yiwo.apppedidos.R;

public class CodigosGenerales {
    public static SimpleDateFormat FormatoFechas = new SimpleDateFormat("dd-MM-yyyy");
    public static boolean Login = false;
    public static boolean isInicio = false;

    public static Integer ID_Concepto;
    public static String Codigo_Articulo;
    public static String Nombre_Categoria;
    public static String Codigo_Categoria;
    public static String Codigo_Pedido;
    public static String FechaActual = FormatoFechas.format(new Date());

    public static int CantidadDatosDialog;
    public static String Year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

    public static String TipoArray = "";

    private static BDEmpresa bdEmpresa = new BDEmpresa();
    private static BDPuntoVenta bdPuntoVenta = new BDPuntoVenta();
    private static BDCentroCostos bdCentroCostos = new BDCentroCostos();
    private static BDUnidNegocios bdUnidNegocios = new BDUnidNegocios();

    private static BDArticulos bdArticulos = new BDArticulos();
    private static BDFamilia bdFamilia = new BDFamilia();
    private static BDSubfamilia bdSubfamilia = new BDSubfamilia();
    private static BDConceptos bdConceptos = new BDConceptos();

    private static BDClientes bdClientes = new BDClientes();
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


    public static double Precio_TotalPedido;
    public static ArrayList<List<String>> Codigos_Pedido = new ArrayList<>();
    public static ArrayList<CustomDataModel> DataModelsList;
    public static Bitmap ImagenGaleria1;
    public static Bitmap ImagenGaleria2;
    public static Bitmap ImagenGaleria3;
    public static Bitmap ImagenGaleria4;
    public static boolean CancelarTask;

    public static ArrayList<List<String>> listEmpresas = new ArrayList<>();
    public static ArrayList<List<String>> listArticulos = new ArrayList<>();

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


    /**
     * Hides the soft keyboard
     */
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
//            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    /**
     * Shows the soft keyboard
     */
    public static void showSoftKeyboard(View view, Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void CambiarFragment(Fragment fragment, FragmentTransaction transaction) {
        if (transaction != null) {
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
            transaction.replace(R.id.frag_contenedor, fragment);
            transaction.commit();
        }
    }

    public static void CambiarFragment2(Fragment fragment, FragmentTransaction transaction) {
        transaction
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .addToBackStack(null)
                .replace(R.id.frag_contenedor, fragment)
                .commit();
    }

    public static Double tryParseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException nfe) {
            // Log exception.
            return 0.00;
        }
    }

    public static Integer tryParseInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            // Log exception.
            return 0;
        }
    }

    public static void BloquearMenuLaeral(Activity activity) {
        if(activity!=null) {
            DrawerLayout drawer;
            drawer = activity.findViewById(R.id.drawer_layout);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);//Bloquear el menu lateral
        }
    }
    public static void DesBloquearMenuLaeral(Activity activity) {
        if(activity!=null) {
            DrawerLayout drawer;
            drawer = activity.findViewById(R.id.drawer_layout);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);//Bloquear el menu lateral
        }
    }
}