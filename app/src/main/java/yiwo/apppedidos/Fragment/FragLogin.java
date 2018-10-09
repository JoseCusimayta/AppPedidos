package yiwo.apppedidos.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.AspectosGenerales.DatosConexiones;
import yiwo.apppedidos.AspectosGenerales.DatosUsuario;
import yiwo.apppedidos.Control.BDUsuario;
import yiwo.apppedidos.Data.DataEmpresa;
import yiwo.apppedidos.Data.DataUsuario;
import yiwo.apppedidos.InterfacesPerzonalidas.CodNomDialog;
import yiwo.apppedidos.InterfacesPerzonalidas.EmpresaDialog;
import yiwo.apppedidos.InterfacesPerzonalidas.PuntoVentaDialog;
import yiwo.apppedidos.R;


public class FragLogin extends Fragment implements View.OnClickListener, EmpresaDialog.FinalizarEmpresaDialog, PuntoVentaDialog.FinalizarPuntoVentaDialog, CodNomDialog.FinalizarCodNomDialog {

    public static final String TAG = "FragLogin";
    Button b_ingresar;
    EditText et_usuario, et_clave, et_rucEmp, et_punto_venta, et_centro_costo, et_unidad_negocio;
    ProgressBar progressBar;
    BDUsuario data = new BDUsuario();
    LinearLayout ly_login_datos;
    DatosConexiones datosConexiones = new DatosConexiones();
    DataEmpresa dataEmpresa = new DataEmpresa();
    DataUsuario dataUsuario = new DataUsuario();
    View view;
    String TipoArray;

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
        view = inflater.inflate(R.layout.frag_login, container, false);
        b_ingresar = view.findViewById(R.id.b_ingresar);
        et_rucEmp = view.findViewById(R.id.et_rucEmp);
        et_usuario = view.findViewById(R.id.et_usuario);
        et_clave = view.findViewById(R.id.et_clave);
        progressBar = view.findViewById(R.id.progressBar);
        et_punto_venta = view.findViewById(R.id.et_punto_venta);
        et_centro_costo = view.findViewById(R.id.et_centro_costo);
        et_unidad_negocio = view.findViewById(R.id.et_unidad_negocio);
        ly_login_datos = view.findViewById(R.id.ly_login_datos);
        b_ingresar.setOnClickListener(this);

        et_usuario.setText(datosConexiones.getUsuarioAPP());
        et_clave.setText(datosConexiones.getClaveAPP());

        et_rucEmp.setOnClickListener(this);
        et_punto_venta.setOnClickListener(this);
        et_centro_costo.setOnClickListener(this);
        et_unidad_negocio.setOnClickListener(this);

        dataUsuario.BorrarDatosUsuario(getContext());
        CodigosGenerales.BloquearMenuLaeral(getActivity());
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
                if (isStoragePermissionGranted()) {
                    new EmpresaDialog(getActivity(), this);
                }
                break;
            case (R.id.et_punto_venta):
                new PuntoVentaDialog(getActivity(), this);
                break;
            case (R.id.et_centro_costo):
                TipoArray = "CentroCostos";
                new CodNomDialog(getActivity(), this, TipoArray);
                break;
            case (R.id.et_unidad_negocio):
                TipoArray = "UnidadNegocio";
                new CodNomDialog(getActivity(), this, TipoArray);
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
    public void ResultadoEmpresaDialog(String cod, String name, String ruc) {
        if (dataUsuario.CargarDatosUsuario(cod)) {
            et_punto_venta.setText(DatosUsuario.Codigo_PuntoVenta + "-" + DatosUsuario.Nombre_PuntoVenta);
            et_centro_costo.setText(DatosUsuario.Codigo_CentroCostos + "-" + DatosUsuario.Nombre_CentroCostos);
            et_unidad_negocio.setText(DatosUsuario.Codigo_UnidadNegocio + "-" + DatosUsuario.Nombre_UnidadNegocio);
            ly_login_datos.setVisibility(View.VISIBLE);
            et_rucEmp.setText(ruc);
            et_usuario.requestFocus();
        }
    }

    @Override
    public void ResultadoPuntoVentaDialog(String ccod_almacen, String cnom_almacen, String erp_codalmacen_ptovta, String cdireccion) {
        DatosUsuario.Codigo_PuntoVenta = ccod_almacen;
        DatosUsuario.Nombre_PuntoVenta = cnom_almacen;
        DatosUsuario.Codigo_Almacen = erp_codalmacen_ptovta;
        DatosUsuario.Direccion_Almacen = cdireccion;
        et_punto_venta.setText(ccod_almacen + "-" + cnom_almacen);
        et_usuario.requestFocus();
    }

    @Override
    public void ResultadoCodNomDialog(String cod, String name) {
        switch (TipoArray) {
            case "CentroCostos":
                DatosUsuario.Codigo_CentroCostos = cod;
                DatosUsuario.Nombre_CentroCostos = name;
                et_centro_costo.setText(cod + "-" + name);
                break;
            case "UnidadNegocio":
                DatosUsuario.Codigo_UnidadNegocio = cod;
                DatosUsuario.Nombre_UnidadNegocio = name;
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
                if(dataUsuario.loginUsuario(et_usuario.getText().toString(), et_clave.getText().toString())){
                    dataEmpresa.CargarDatosEmpresa();
                    exito = true;
                } else
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

                CodigosGenerales.DesBloquearMenuLaeral(getActivity());


                boolean isInserted =dataUsuario.InsertarDatosUsuario(getContext());

                if (isInserted) {
                    Mensaje = "Bienvenido: " + DatosUsuario.Nombre_Usuario;
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