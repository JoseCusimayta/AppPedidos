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
        CodigosGenerales.getActivity=this;
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

        //Variable usada para cancelar cualquier Tarea en segundo plano que se esté realizando
        CodigosGenerales.CancelarTask=true;

        //Variable para guardar en que número del fragmento nos encontramos,
        // si estamos en el 1, quiere decir que estamos en el login,
        // si es 2, quiere decir Menu Principal,
        // con la cual activamos la funcion para evitar cerrar el programa y poner un aviso de Cerrar sesion
        int count = getSupportFragmentManager().getBackStackEntryCount();


        //Forzamos la activación del boton de carrito,
        // si a veces se bloquea (por entrar a la lista del pedido),
        // entonces, con esto podemos reactivarlo  forzosamente
        if (!b_carrito.isEnabled())
            b_carrito.setEnabled(true);

        //AL presionar el botón atrás, primero se verifican 2 cosas
        //1: que el usuario haya ingresado al sistema
        //2: que esté en el menú principal
        //Adicional a eso, se hace una corrección por si hay algún error inesperado
        //En el cual se fuerza la salida del usuario que ha ingresado
        if(CodigosGenerales.Login)
            Log.d(TAG,"¡El usuario ha ingresado? - "+CodigosGenerales.Login);
        else
            Log.d(TAG,"¡El usuario no ha ingresado? - "+CodigosGenerales.Login);


        //region Verificar si el usuario ha ingresado previamente
        if (CodigosGenerales.Login) {

            //region Configuración para cuando ya se ha iniciado sesión previamente
            super.onBackPressed();
            Log.d("MainActivity", "onBackPressed count: " + count);
            //endregion

        } else {
            //region Configuración para cuando no se ha iniciado sesión previamente
            if (count == 1) {
                CerrarSesion();
            } else if (count < 1) {
                CodigosGenerales.Login = false;
                super.onBackPressed();
            } else if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
            //endregion
        }
        //endregion
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment;
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case (R.id.nav_catalogo):
                try {
                    FragmentManager fm = getSupportFragmentManager();
                    for (int i = 1; i < fm.getBackStackEntryCount(); ++i) {
                        fm.popBackStack();
                    }
                    CodigosGenerales.TipoArray = "Articulos";
                    fragment = new FragList();
                    CambiarFragmentDrawer(fragment);
                } catch (Exception e) {
                    Log.d("LateralPedidos", e.getMessage());
                }
                break;
            case (R.id.nav_clientes):
                try {
                    FragmentManager fm = getSupportFragmentManager();
                    for (int i = 1; i < fm.getBackStackEntryCount(); ++i) {
                        fm.popBackStack();
                    }
                    fragment = new LateralClientes();
                    CambiarFragmentDrawer(fragment);
                } catch (Exception e) {
                    Log.d("LateralPedidos", e.getMessage());
                }
                break;
            case (R.id.nav_pedidos):
                try {
                    FragmentManager fm = getSupportFragmentManager();
                    for (int i = 1; i < fm.getBackStackEntryCount(); ++i) {
                        fm.popBackStack();
                    }
                    fragment = new LateralPedidos();
                    CambiarFragmentDrawer(fragment);
                } catch (Exception e) {
                    Log.d("LateralPedidos", e.getMessage());
                }
                break;
            case (R.id.nav_actualizar):
                fragment = new LateralActualizar();
                CambiarFragmentDrawer(fragment);
                break;
            case (R.id.nav_cerrarSesion):
                CerrarSesion();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void CambiarFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frag_contenedor, fragment)
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .commit();
    }

    public void CambiarFragmentDrawer(Fragment fragment) {
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
                CambiarFragmentDrawer(fragment);
                break;
            case (R.id.iv_logo):
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