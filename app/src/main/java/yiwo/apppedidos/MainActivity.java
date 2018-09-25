package yiwo.apppedidos;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.AspectosGenerales.ConfiguracionEmpresa;
import yiwo.apppedidos.Fragment.FragList;
import yiwo.apppedidos.Fragment.FragListDeseo;
import yiwo.apppedidos.Fragment.FragLogin;
import yiwo.apppedidos.Fragment.FragMenuPrincipal;
import yiwo.apppedidos.Fragment.FragSplashScreen;
import yiwo.apppedidos.Fragment.LateralActualizar;
import yiwo.apppedidos.Fragment.LateralClientes;
import yiwo.apppedidos.Fragment.LateralPedidos;
import yiwo.apppedidos.ConexionBD.BDConexionSQLite;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    //region Declaración de variables a usar
    DrawerLayout drawer;
    Button b_carrito;
    ImageView iv_logo;
    Toolbar toolbar;
    AppBarLayout app_barLayout;
    TextView tv_origenFiltro;
    FrameLayout frameLayout;
    NavigationView navigationView;
    BDConexionSQLite myDb;
    String TAG="MainActivity";
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //region Declarando variables del activity
        toolbar = findViewById(R.id.toolbar);
        app_barLayout = findViewById(R.id.app_barLayout);
        iv_logo = findViewById(R.id.iv_logo);
        b_carrito = findViewById(R.id.b_carrito);
        drawer = findViewById(R.id.drawer_layout);
        tv_origenFiltro = findViewById(R.id.tv_origenFiltro);
        frameLayout = findViewById(R.id.frag_contenedor);
        navigationView = findViewById(R.id.nav_view);
        //endregion
        findViewById(R.id.frag_contenedor).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return false;
            }
        });
        //region Cambiar de Toolbar
        setSupportActionBar(toolbar);
        //endregion

        //region Activar Menú Lateral
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);//Bloquear el menu lateral
        //endregion

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //region Ocultar Toolbar para que no aparezca en el Login
        app_barLayout.setVisibility(View.GONE);
        tv_origenFiltro.setVisibility(View.GONE);
        //endregion

        //region GenerarAccion de "Click"
        navigationView.setNavigationItemSelectedListener(this);
        b_carrito.setOnClickListener(this);
        iv_logo.setOnClickListener(this);
        //endregion


        myDb = new BDConexionSQLite(this); //Cargar base de datos SQLite

        Fragment fragment = new FragSplashScreen();
        CambiarFragment(fragment);
    }

    @Override
    public void onBackPressed() {
        b_carrito.setEnabled(true);
        if(CodigosGenerales.isInicio){
            new AlertDialog.Builder(this)
                    .setTitle("Salir")
                    .setMessage("¿Esstá seguro de salir?")
                    .setNegativeButton(android.R.string.cancel, null) // dismisses by default
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .create()
                    .show();
        }else
            super.onBackPressed();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        if (!b_carrito.isEnabled())
            b_carrito.setEnabled(true);
        Fragment fragment;
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case (R.id.nav_catalogo):
                CodigosGenerales.TipoArray = "Articulos";
                fragment = new FragList();
                CambiarFragment(fragment);
                break;
            case (R.id.nav_clientes):
                fragment = new LateralClientes();
                CambiarFragment(fragment);
                break;
            case (R.id.nav_pedidos):
                fragment = new LateralPedidos();
                CambiarFragment(fragment);
                break;
            case (R.id.nav_actualizar):
                fragment = new LateralActualizar();
                CambiarFragment(fragment);
                break;
            case (R.id.nav_cerrarSesion):
                CerrarSesion();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void CambiarFragment(Fragment fragment) {

        CodigosGenerales.isInicio=false;

        CodigosGenerales.hideSoftKeyboard(this);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frag_contenedor, fragment)
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View view) {
        Fragment fragment;
        switch (view.getId()) {
            case (R.id.b_carrito):
                fragment = new FragListDeseo();
                CambiarFragment(fragment);
                break;
            case (R.id.iv_logo):
                iv_logo.setEnabled(false);
                if (!b_carrito.isEnabled())
                    b_carrito.setEnabled(true);
                FragmentManager fm = getSupportFragmentManager();
                for (int i = 1; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                FragMenuPrincipal frag = new FragMenuPrincipal();
                CambiarFragment(frag);

                break;
        }
    }

    public void CerrarSesion(){
        new AlertDialog.Builder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Esstá seguro de cerrar sesión?")
                .setNegativeButton(android.R.string.cancel, null) // dismisses by default
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            app_barLayout.setVisibility(View.GONE); //Ocultar el Toolbar
                            myDb.deleteAllDataLogin();   //Borrar toda la información del SQLITE
                            CodigosGenerales.Login = false;//Guardar el estado de Login en falso para que el sistema sepa que nadie ha iniciado sesión


                            FragmentManager fm = getSupportFragmentManager();
                            for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
                                fm.popBackStack();
                            }
                            frameLayout.removeAllViews();


                            FragLogin fragLogin = new FragLogin();
                            CambiarFragment(fragLogin);
                        } catch (Exception e) {
                            Log.d("LateralPedidos", e.getMessage());
                        }
                    }
                })
                .create()
                .show();
    }
}