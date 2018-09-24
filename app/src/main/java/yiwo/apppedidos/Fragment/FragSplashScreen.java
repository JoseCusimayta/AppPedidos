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

import java.sql.Connection;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.AspectosGenerales.ConfiguracionEmpresa;
import yiwo.apppedidos.ConexionBD.BDConexionSQL;
import yiwo.apppedidos.ConexionBD.BDConexionSQLite;
import yiwo.apppedidos.ConexionBD.RedDisponible;
import yiwo.apppedidos.Data.BDActivarFunciones;
import yiwo.apppedidos.Data.BDEmpresa;
import yiwo.apppedidos.R;


public class FragSplashScreen extends Fragment  {

    BDConexionSQLite myDb;
    TextView tv_link;
    String TAG = "FragSplashScreen";
    BDActivarFunciones bdActivarFunciones = new BDActivarFunciones();
    Boolean isConfigurate, isLogin;
    Fragment fragment;
    BDEmpresa bdEmpresa = new BDEmpresa();
    BDConexionSQL bdConexionSQL= new BDConexionSQL();
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

            RedDisponible.isLAN(getActivity());
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
            while (datos_login.moveToNext()) {
                CodigosGenerales.Codigo_Empresa = datos_login.getString(1);//Obtener el Codigo_Empresa
                CodigosGenerales.Codigo_PuntoVenta = datos_login.getString(2);//Obtener el Codigo_PuntoVenta
                CodigosGenerales.Codigo_Almacen = datos_login.getString(3);//Obtener el Codigo_Almacen
                CodigosGenerales.Codigo_Usuario = datos_login.getString(4);//Obtener el Codigo_Usuario
                CodigosGenerales.Codigo_CentroCostos = datos_login.getString(5);//Obtener el Codigo_CentroCostos
                CodigosGenerales.Codigo_UnidadNegocio = datos_login.getString(6);//Obtener el Codigo_UnidadNegocio
                CodigosGenerales.Moneda_Empresa = datos_login.getString(7);//Obtener el Tipo_Moneda
                CodigosGenerales.Direccion_Almacen = datos_login.getString(8);//Obtener la direccion del almacen
                CodigosGenerales.Nombre_Vendedor = datos_login.getString(9);//Obtener el Nombre del Vendedor
                CodigosGenerales.Celular_Vendedor = datos_login.getString(10);//Obtener el Celular del Vendedor
                CodigosGenerales.email_Vendedor = datos_login.getString(11);//Obtener el email del Vendedor
            }

            try {
                Connection connection = bdConexionSQL.getConnection();

                ConfiguracionEmpresa.Tipo_CambioEmpresa = bdEmpresa.getTipoCambio(connection); //Obtener el tipo de cambio actualL
                CodigosGenerales.Moneda_Empresa = bdEmpresa.getMonedaTrabajo(connection);
                connection.close();
            } catch (Exception e){
                Log.d(TAG,"connection "+e.getMessage());
            }
            Log.d(TAG, "Tipo_CambioEmpresa " + ConfiguracionEmpresa.Tipo_CambioEmpresa);
            //endregion

            Log.d(TAG, "datos_usuario - " + CodigosGenerales.Codigo_Empresa);


            fragment = new FragMenuPrincipal();

        } else {
            CodigosGenerales.TipoArray = "Empresa";
            CodigosGenerales.getArrayList("");
        }
        datos_login.close();        //Cerrar el cursor para liberar el sqlite
    }
}