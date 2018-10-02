package yiwo.apppedidos.Fragment;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.AspectosGenerales.ConfiguracionEmpresa;
import yiwo.apppedidos.AspectosGenerales.DatosUsuario;
import yiwo.apppedidos.ConexionBD.BDConexionSQLite;
import yiwo.apppedidos.ConexionBD.RedDisponible;
import yiwo.apppedidos.Control.BDActivarFunciones;
import yiwo.apppedidos.Data.DataEmpresa;
import yiwo.apppedidos.R;


public class FragSplashScreen extends Fragment {

    BDConexionSQLite myDb;
    TextView tv_link;
    String TAG = "FragSplashScreen";
    BDActivarFunciones bdActivarFunciones = new BDActivarFunciones();
    Boolean isLogin;
    Fragment fragment;
    DataEmpresa dataEmpresa = new DataEmpresa();
    RedDisponible redDisponible = new RedDisponible();

    public FragSplashScreen() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_splash_screen, container, false);
        tv_link = view.findViewById(R.id.tv_link);
        myDb = new BDConexionSQLite(getContext());
        fragment = new FragLogin();
        try {

            BackGroundTask task = new BackGroundTask();
            task.execute("");

        } catch (Exception e) {
            Log.d(TAG, "onCreateView " + e.getMessage());
        }
        return view;
    }

    //region Tarea en Segundo Plano con ListView
    public class BackGroundTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            redDisponible.isLAN(getActivity());
            ComprobarLogin();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            CambiarFragment(fragment);
            super.onPostExecute(s);
        }
    }


    public void CambiarFragment(Fragment fragment) {
        CodigosGenerales.CambiarFragment(fragment, getFragmentManager().beginTransaction());
    }


    public void ComprobarLogin() {

        bdActivarFunciones.ActualizarFunciones();
        Cursor datos_login = myDb.getDataLogin();
        isLogin = datos_login.getCount() != 0;
        //endregion

        //region Verificar si el usuario ha ingresado previamente obteniendo los datos del SQLITE
        if (isLogin) {
            //region Configuración para cuando ya se ha iniciado sesión previamente
            List<String> datos_usuario = new ArrayList();
            while (datos_login.moveToNext()) {
                datos_usuario.add(datos_login.getString(1));//Obtener el Codigo_Empresa
                datos_usuario.add(datos_login.getString(2));//Obtener el Codigo_PuntoVenta
                datos_usuario.add(datos_login.getString(3));//Obtener el Codigo_Almacen
                datos_usuario.add(datos_login.getString(4));//Obtener el Codigo_Usuario
                datos_usuario.add(datos_login.getString(5));//Obtener el Codigo_CentroCostos
                datos_usuario.add(datos_login.getString(6));//Obtener el Codigo_UnidadNegocio
                datos_usuario.add(datos_login.getString(7));//Obtener el Tipo_Moneda
                datos_usuario.add(datos_login.getString(8));//Obtener la direccion del almacen
                datos_usuario.add(datos_login.getString(9));//Obtener el Nombre del Vendedor
                datos_usuario.add(datos_login.getString(10));//Obtener el Celular del Vendedor
                datos_usuario.add(datos_login.getString(11));//Obtener el email del Vendedor
            }
            ConfiguracionEmpresa.Codigo_Empresa = datos_usuario.get(0);   //Guardar el Codigo_Empresa en CodigosGenerales
            DatosUsuario.Codigo_PuntoVenta = datos_usuario.get(1);   //Guardar el Codigo_PuntoVenta en CodigosGenerales
            DatosUsuario.Codigo_Almacen = datos_usuario.get(2);   //Guardar el Codigo_Almacen en CodigosGenerales
            DatosUsuario.Codigo_Usuario = datos_usuario.get(3);   //Guardar el Codigo_Usuario en CodigosGenerales
            DatosUsuario.Codigo_CentroCostos = datos_usuario.get(4);   //Guardar el Codigo_CentroCostos en CodigosGenerales
            DatosUsuario.Codigo_UnidadNegocio = datos_usuario.get(5);   //Guardar el Codigo_UnidadNegocio en CodigosGenerales
            ConfiguracionEmpresa.Moneda_Trabajo = datos_usuario.get(6);   //Guardar el Codigo_UnidadNegocio en CodigosGenerales
            DatosUsuario.Direccion_Almacen = datos_usuario.get(7);   //Guardar la direccion del almacen en CodigosGenerales
            DatosUsuario.Nombre_Vendedor = datos_usuario.get(8);   //Guardar el Nombre del Vendedor
            DatosUsuario.Celular_Vendedor = datos_usuario.get(9);   //Guardar el Celular del Vendedor
            DatosUsuario.email_Vendedor = datos_usuario.get(10);   //Guardar el email del Vendedor
            Log.d(TAG, "Codigo_Empresa " + ConfiguracionEmpresa.Codigo_Empresa);
            dataEmpresa.CargarDatosEmpresa();
            //endregion
            Log.d(TAG, "datos_usuario - " + datos_usuario.get(0));

            fragment = new FragMenuPrincipal();
        } else {
            CodigosGenerales.TipoArray = "Empresa";
            CodigosGenerales.getArrayList("");
        }
        datos_login.close();        //Cerrar el cursor para liberar el sqlite
    }

}