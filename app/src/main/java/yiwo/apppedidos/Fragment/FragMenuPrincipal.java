package yiwo.apppedidos.Fragment;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.AspectosGenerales.ConfiguracionEmpresa;
import yiwo.apppedidos.AspectosGenerales.DatosConexiones;
import yiwo.apppedidos.AspectosGenerales.DatosUsuario;
import yiwo.apppedidos.ConexionBD.BDDescargarImagenes;
import yiwo.apppedidos.ConexionBD.RedDisponible;
import yiwo.apppedidos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragMenuPrincipal extends Fragment implements View.OnClickListener {

    //region Declaración de variables a usar
    String TAG = "FragMenuPrincipal";
    View view;
    ImageView iv_articulos, iv_familia, iv_subfamilia, iv_concepto1, iv_concepto2, iv_concepto3, iv_concepto4, iv_concepto5, iv_concepto6, iv_concepto7;
    LinearLayout ly_menu;
    AppBarLayout app_barLayout;

    //region Variables para guardar y recibir imagenes
    Integer imagenescargadas = 0;
    String setTag = "articulos";
    DrawerLayout drawer;
    //endregion

    BDDescargarImagenes bdDescargarImagenes=new BDDescargarImagenes();
    RedDisponible redDisponible= new RedDisponible();
    DatosConexiones datosConexiones= new DatosConexiones();
    //endregion


    public FragMenuPrincipal() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_menu_principal, container, false);

        //region Poniendo valores a las variables
        ly_menu = view.findViewById(R.id.ly_menu);
        iv_articulos = view.findViewById(R.id.iv_articulos);
        iv_familia = view.findViewById(R.id.iv_familia);
        iv_subfamilia = view.findViewById(R.id.iv_subfamilia);
        iv_concepto1 = view.findViewById(R.id.iv_concepto1);
        iv_concepto2 = view.findViewById(R.id.iv_concepto2);
        iv_concepto3 = view.findViewById(R.id.iv_concepto3);
        iv_concepto4 = view.findViewById(R.id.iv_concepto4);
        iv_concepto5 = view.findViewById(R.id.iv_concepto5);
        iv_concepto6 = view.findViewById(R.id.iv_concepto6);
        iv_concepto7 = view.findViewById(R.id.iv_concepto7);
        app_barLayout = Objects.requireNonNull(getActivity()).findViewById(R.id.app_barLayout);
        //endregion

        //region GenerarAccion de "Click"
        iv_articulos.setOnClickListener(this);
        iv_familia.setOnClickListener(this);
        iv_subfamilia.setOnClickListener(this);
        iv_concepto1.setOnClickListener(this);
        iv_concepto2.setOnClickListener(this);
        iv_concepto3.setOnClickListener(this);
        iv_concepto4.setOnClickListener(this);
        iv_concepto5.setOnClickListener(this);
        iv_concepto6.setOnClickListener(this);
        iv_concepto7.setOnClickListener(this);
        //endregion

        //region Reactivando el ToolBar
        app_barLayout.setVisibility(View.VISIBLE);
        //endregion

        try {
            ImageView iv_logo= getActivity().findViewById(R.id.iv_logo);
            iv_logo.setEnabled(false);
            CodigosGenerales.Filtro = false;

            drawer = getActivity().findViewById(R.id.drawer_layout);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);//Desbloquear el menu lateral

            TextView tv_titlo_nav = Objects.requireNonNull(getActivity()).findViewById(R.id.tv_titulo_nav);
            tv_titlo_nav.setText(DatosUsuario.Nombre_Vendedor);

            TextView tv_descripcion_nav = getActivity().findViewById(R.id.tv_descripcion_nav);
            tv_descripcion_nav.setText(DatosUsuario.Celular_Vendedor + "\n" + DatosUsuario.email_Vendedor);

        }catch (Exception e){
            Log.d(TAG,"onPostExecute "+e.getMessage());
        }

        if(redDisponible.serverAvailable(getActivity(), datosConexiones.getIP_Publica(),datosConexiones.getPuertoImagenes())) {
            DetectarVariacionPesoCarpeta();
        }
        CargarImagenesMenuPrincipal();
        CodigosGenerales.isInicio=true;
        return view;
    }





    @Override
    public void onClick(View view) {
        Fragment fragment;
        CodigosGenerales.isInicio=false;
        switch (view.getId()) {
            case (R.id.iv_articulos):
                CodigosGenerales.TipoArray = "Articulos";
//                fragment = new FragArticulos();
                fragment = new FragArticulosCardView();
                CambiarFragment(fragment);
                break;
            case (R.id.iv_familia):
                CodigosGenerales.TipoArray = "Familia";
                fragment = new FragList();
                CambiarFragment(fragment);
                break;
            case (R.id.iv_subfamilia):
                CodigosGenerales.TipoArray = "SubFamilia";
                fragment = new FragList();
                CambiarFragment(fragment);
                break;
            case (R.id.iv_concepto1):
                CodigosGenerales.ID_Concepto = 1;
                CodigosGenerales.TipoArray = "Concepto";
                fragment = new FragList();
                CambiarFragment(fragment);
                break;
            case (R.id.iv_concepto2):
                CodigosGenerales.ID_Concepto = 2;
                CodigosGenerales.TipoArray = "Concepto";
                fragment = new FragList();
                CambiarFragment(fragment);
                break;
            case (R.id.iv_concepto3):
                CodigosGenerales.ID_Concepto = 3;
                CodigosGenerales.TipoArray = "Concepto";
                fragment = new FragList();
                CambiarFragment(fragment);
                break;
            case (R.id.iv_concepto4):
                CodigosGenerales.ID_Concepto = 4;
                CodigosGenerales.TipoArray = "Concepto";
                fragment = new FragList();
                CambiarFragment(fragment);
                break;
            case (R.id.iv_concepto5):
                CodigosGenerales.ID_Concepto = 5;
                CodigosGenerales.TipoArray = "Concepto";
                fragment = new FragList();
                CambiarFragment(fragment);
                break;
            case (R.id.iv_concepto6):
                CodigosGenerales.ID_Concepto = 6;
                CodigosGenerales.TipoArray = "Concepto";
                fragment = new FragList();
                CambiarFragment(fragment);
                break;
            case (R.id.iv_concepto7):
                CodigosGenerales.ID_Concepto = 7;
                CodigosGenerales.TipoArray = "Concepto";
                fragment = new FragList();
                CambiarFragment(fragment);
                break;
        }
    }

    public void CambiarFragment(Fragment fragment) {

        CodigosGenerales.CambiarFragment2(fragment,getFragmentManager().beginTransaction());
    }

    public void DetectarVariacionPesoCarpeta() {
        BDDescargarImagenes bdDescargarImagenes= new BDDescargarImagenes();
        if(bdDescargarImagenes.EsNecesarioActualizar())
            Snackbar.make(view, "Se ha detectado un cambio en las imagenes, por favor actualizar las imagenes", Snackbar.LENGTH_LONG).setAction("No action", null).show();
    }

    public void CargarImagenesMenuPrincipal(){

        try {

            iv_articulos.setImageBitmap(bdDescargarImagenes.getImageFromDirectory(  "articulos.jpg"));

            iv_familia.setImageBitmap(bdDescargarImagenes.getImageFromDirectory( "familia.jpg"));

            iv_subfamilia.setImageBitmap(bdDescargarImagenes.getImageFromDirectory( "subfamilia.jpg"));

            iv_concepto1.setImageBitmap(bdDescargarImagenes.getImageFromDirectory( "concepto1.jpg"));

            iv_concepto2.setImageBitmap(bdDescargarImagenes.getImageFromDirectory( "concepto2.jpg"));

            iv_concepto3.setImageBitmap(bdDescargarImagenes.getImageFromDirectory( "concepto3.jpg"));

            iv_concepto4.setImageBitmap(bdDescargarImagenes.getImageFromDirectory( "concepto4.jpg"));

            iv_concepto5.setImageBitmap(bdDescargarImagenes.getImageFromDirectory( "concepto5.jpg"));

            iv_concepto6.setImageBitmap(bdDescargarImagenes.getImageFromDirectory( "concepto6.jpg"));

            iv_concepto7.setImageBitmap(bdDescargarImagenes.getImageFromDirectory( "concepto7.jpg"));

        } catch (Exception e) {
            Log.d(TAG, "onCreateView " + e.getMessage());
        }
    }

    @Override
    public void onPause() {

        try {
            ImageView iv_logo = getActivity().findViewById(R.id.iv_logo);
            iv_logo.setEnabled(true);
        }catch (Exception e){
            Log.d(TAG,"onPause "+e.getMessage());
        }
        super.onPause();
    }
}