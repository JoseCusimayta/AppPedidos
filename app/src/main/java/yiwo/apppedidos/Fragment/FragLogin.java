package yiwo.apppedidos.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.Data.BDCentroCostos;
import yiwo.apppedidos.Data.BDPuntoVenta;
import yiwo.apppedidos.Data.BDUnidNegocios;
import yiwo.apppedidos.Data.BDUsuario;
import yiwo.apppedidos.InterfacesPerzonalidas.CustomDialog2Datos;
import yiwo.apppedidos.R;
import yiwo.apppedidos.ConexionBD.BDConexionSQLite;


public class FragLogin extends Fragment implements View.OnClickListener, CustomDialog2Datos.FinalizoCuadroDialogo2Datos {

    public static final String TAG = "FragLogin";
    Button b_ingresar;
    EditText et_usuario, et_clave, et_rucEmp, et_punto_venta, et_centro_costo, et_unidad_negocio;
    DrawerLayout drawer;
    ProgressBar progressBar;
    LinearLayout ly_login_datos;
    BDUsuario data = new BDUsuario();
    BDCentroCostos bdCentroCostos = new BDCentroCostos();
    BDPuntoVenta bdPuntoVenta = new BDPuntoVenta();
    BDUnidNegocios bdUnidNegocios = new BDUnidNegocios();

    BDConexionSQLite myDb;

    public FragLogin() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_login, container, false);
        b_ingresar = view.findViewById(R.id.b_ingresar);
        et_rucEmp = view.findViewById(R.id.et_rucEmp);
        et_usuario = view.findViewById(R.id.et_usuario);
        et_clave = view.findViewById(R.id.et_clave);
        progressBar = view.findViewById(R.id.progressBar);
        ly_login_datos = view.findViewById(R.id.ly_login_datos);
        et_punto_venta = view.findViewById(R.id.et_punto_venta);
        et_centro_costo = view.findViewById(R.id.et_centro_costo);
        et_unidad_negocio = view.findViewById(R.id.et_unidad_negocio);

        drawer = getActivity().findViewById(R.id.drawer_layout);

        b_ingresar.setOnClickListener(this);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);//Bloquear el menu lateral

//        et_usuario.setText("Admin");
//        et_clave.setText("12345678");

        et_rucEmp.setOnClickListener(this);
        et_punto_venta.setOnClickListener(this);
        et_centro_costo.setOnClickListener(this);
        et_unidad_negocio.setOnClickListener(this);


        myDb = new BDConexionSQLite(getContext());
        myDb.deleteAllDataLogin();

        CodigosGenerales.Login = false;

        return view;

    }

    @Override
    public void onClick(View v) {
        CodigosGenerales.CantidadDatosDialog = 3;
        switch (v.getId()) {
            case (R.id.b_ingresar):
                b_ingresar.setEnabled(false);
                BackGroundTask task = new BackGroundTask();
                task.execute("");
                break;
            case (R.id.et_rucEmp):
                try{
                if (isStoragePermissionGranted()) {
                    CodigosGenerales.TipoArray = "Empresa";
                    new CustomDialog2Datos(getActivity(), this);
                }}catch (Exception e){
                    Toast.makeText(getContext(), "No se puede conectar a Internet", Toast.LENGTH_SHORT).show();
                }
                break;
            case (R.id.et_punto_venta):
                CodigosGenerales.CantidadDatosDialog=4;
                CodigosGenerales.TipoArray = "PuntoVenta";
                new CustomDialog2Datos(getActivity(), this);
                CodigosGenerales.CantidadDatosDialog=3;
                break;
            case (R.id.et_centro_costo):
                CodigosGenerales.TipoArray = "CentroCosto";
                new CustomDialog2Datos(getActivity(), this);
                break;
            case (R.id.et_unidad_negocio):
                CodigosGenerales.TipoArray = "UnidadNegocio";
                new CustomDialog2Datos(getActivity(), this);
                break;
        }
    }

    public void CambiarFragment(Fragment fragment) {
        assert getFragmentManager() != null;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        transaction.replace(R.id.frag_contenedor, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void ResultadoCuadroDialogo2Datos(String cod, String name, String ruc, String direccion, String listaPrecios, String dni, String dato_invisible) {
        CodigosGenerales.hideSoftKeyboard(getActivity());
        switch (CodigosGenerales.TipoArray) {
            case "Empresa":
                try {
                    CodigosGenerales.Codigo_Empresa = cod;
                    et_rucEmp.setText(ruc);
                    List<String> list;

                    list = bdPuntoVenta.getPredeterminado(cod);
                    CodigosGenerales.Codigo_PuntoVenta = list.get(0);
                    CodigosGenerales.Codigo_Almacen = list.get(2);
                    CodigosGenerales.Direccion_Almacen=list.get(3);
                    et_punto_venta.setText(list.get(0) + "-" + list.get(1));

                    list = bdCentroCostos.getPredeterminado(cod);
                    CodigosGenerales.Codigo_CentroCostos = list.get(0);
                    et_centro_costo.setText(list.get(0) + "-" + list.get(1));

                    list = bdUnidNegocios.getPredeterminado(cod);
                    CodigosGenerales.Codigo_UnidadNegocio = list.get(0);
                    et_unidad_negocio.setText(list.get(0) + "-" + list.get(1));

                    ly_login_datos.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    Log.d("FragLogin", "Empresa: " + e.getMessage());
                }
                break;
            case "PuntoVenta":
                CodigosGenerales.Codigo_PuntoVenta = cod;
                CodigosGenerales.Codigo_Almacen = ruc;
                CodigosGenerales.Direccion_Almacen=direccion;
                et_punto_venta.setText(cod + "-" + name);
                break;
            case "CentroCosto":
                CodigosGenerales.Codigo_CentroCostos = cod;
                et_centro_costo.setText(cod + "-" + name);
                break;
            case "UnidadNegocio":
                CodigosGenerales.Codigo_UnidadNegocio = cod;
                et_unidad_negocio.setText(cod + "-" + name);
                break;
        }
    }


    public class BackGroundTask extends AsyncTask<String, String, String> {
        String Mensaje;
        Boolean exito = false;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            et_usuario.setEnabled(false);
            et_clave.setEnabled(false);
            b_ingresar.setEnabled(false);

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                if (data.getLogin(et_usuario.getText().toString(), et_clave.getText().toString()))
                    exito = true;
                else
                    Mensaje = "Usuario y/o contraseña";
            } catch (Exception e) {
                Mensaje = "Error en las credenciales";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);
            if (exito) {
                et_rucEmp.setText("");
                et_usuario.setText("");
                et_clave.setText("");
                et_usuario.setEnabled(true);
                et_clave.setEnabled(true);
                b_ingresar.setEnabled(true);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);//Desbloquear el menu lateral

                Mensaje = "Bienvenido: " + CodigosGenerales.Nombre_Usuario;

                boolean isInserted =
                        myDb.insertarLogin(
                                CodigosGenerales.Codigo_Empresa,
                                CodigosGenerales.Codigo_PuntoVenta,
                                CodigosGenerales.Codigo_Almacen,
                                CodigosGenerales.Codigo_Usuario,
                                CodigosGenerales.Codigo_CentroCostos,
                                CodigosGenerales.Codigo_UnidadNegocio,
                                CodigosGenerales.Moneda_Empresa,
                                CodigosGenerales.Direccion_Almacen,
                                CodigosGenerales.Nombre_Vendedor,
                                CodigosGenerales.Celular_Vendedor,
                                CodigosGenerales.email_Vendedor);

                if (isInserted) {
                    et_rucEmp.setText("");
                    et_rucEmp.setEnabled(true);

                    Fragment fragment = new FragMenuPrincipal();
                    CambiarFragment(fragment);
                }
            } else {
                et_usuario.setEnabled(true);
                et_clave.setEnabled(true);
                b_ingresar.setEnabled(true);
                Toast.makeText(getActivity(), Mensaje, Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(s);
        }
    }


    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }
}