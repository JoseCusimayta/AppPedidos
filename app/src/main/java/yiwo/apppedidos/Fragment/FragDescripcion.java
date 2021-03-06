package yiwo.apppedidos.Fragment;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.github.chrisbanes.photoview.PhotoView;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.AspectosGenerales.ConfiguracionEmpresa;
import yiwo.apppedidos.AspectosGenerales.DatosCliente;
import yiwo.apppedidos.ConexionBD.BDDescargarImagenes;
import yiwo.apppedidos.Control.BDArticulos;
import yiwo.apppedidos.Control.BDListDeseo;
import yiwo.apppedidos.Data.DataArticulos;
import yiwo.apppedidos.Data.DataListaDeseo;
import yiwo.apppedidos.InterfacesPerzonalidas.frag_dialog_galeria;
import yiwo.apppedidos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragDescripcion extends Fragment implements View.OnClickListener,  frag_dialog_galeria.FinalizoCuadroDialogGaleria {


    //region Declaración de variables a usar
    String TAG = "FragDescripcion";
    View view;
    List<String> informacion = new ArrayList<>();
    Button b_cerrar_popup;
    View popupView;
    PopupWindow popupWindow;

    LayoutInflater layoutInflater;
    FloatingActionButton fab_info;
    ImageButton ib_carritoPedidos, ib_fichatecnica, ib_galeria;

    TextView et_nombre, tv_cantidad, tv_precio;
    PhotoView iv_imagen;
    String Codigo_Articulo, Nombre_Articulo, Unidad_Articulo;

    EditText et_cantidad;
    TextView tv_origen_popup;
    AppBarLayout app_barLayout;
    FrameLayout id_lyContent;
    Double Precio_Articulo, Stock, PorcentajeIGV;

    ArrayList<List<String>> FichaTecnica = null;

    BDDescargarImagenes bdDescargarImagenes = new BDDescargarImagenes();
    Boolean isGalleryReady=false;
    DataArticulos dataArticulos= new DataArticulos();
    DataListaDeseo dataListaDeseo= new DataListaDeseo();
    TextView tv_cabecera_total, tv_cabecera_cantidad;
    //endregion

    public FragDescripcion() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_descripcion, container, false);

        //region Poniendo valores a las variables
        et_nombre = view.findViewById(R.id.et_nombre);
        tv_precio = view.findViewById(R.id.tv_precio);
        tv_cantidad = view.findViewById(R.id.tv_cantidad);
        iv_imagen = view.findViewById(R.id.iv_imagen);
        fab_info = view.findViewById(R.id.fab_info);
        id_lyContent = view.findViewById(R.id.id_lyContent);
        ib_fichatecnica = view.findViewById(R.id.ib_fichatecnica);
        ib_galeria = view.findViewById(R.id.ib_galeria);
        et_cantidad = view.findViewById(R.id.et_cantidad);
        ib_carritoPedidos = view.findViewById(R.id.ib_carritoPedidos);
        tv_origen_popup = view.findViewById(R.id.tv_origen_popup);
        //endregion


        try {
            informacion = dataArticulos.getList(CodigosGenerales.Codigo_Articulo).get(0);
            Codigo_Articulo = informacion.get(0);
            Nombre_Articulo = informacion.get(1);
            Stock = Double.parseDouble(informacion.get(2));
            Unidad_Articulo = informacion.get(3);
            Precio_Articulo = CodigosGenerales.tryParseDouble(informacion.get(4));
            Log.d(TAG,"Precio: "+Precio_Articulo);
            PorcentajeIGV= CodigosGenerales.tryParseDouble(informacion.get(7));
            tv_cantidad.setText( CodigosGenerales.RedondearDecimalesFormateado(Stock )+ " " +Unidad_Articulo);
            tv_precio.setText(ConfiguracionEmpresa.Moneda_Trabajo + " " + CodigosGenerales.RedondearDecimalesFormateado( Precio_Articulo));
            et_nombre.setText(Nombre_Articulo);

            tv_cabecera_total = getActivity().findViewById(R.id.tv_total);
            tv_cabecera_cantidad = getActivity().findViewById(R.id.tv_cantidad);

            app_barLayout = getActivity().findViewById(R.id.app_barLayout);
            app_barLayout.setVisibility(View.VISIBLE);

            BackGroundTask task = new BackGroundTask();
            task.execute("");
        } catch (Exception e) {
            Log.d(TAG, "onCreateView: " + e.getMessage());
        }

        iv_imagen.setOnClickListener(this);
        fab_info.setOnClickListener(this);
        ib_carritoPedidos.setOnClickListener(this);
        ib_fichatecnica.setOnClickListener(this);
        ib_galeria.setOnClickListener(this);
        et_cantidad.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    AgregarProductosListaDeseo(CodigosGenerales.tryParseDouble(et_cantidad.getText().toString()));
                    InputMethodManager imm = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        CodigosGenerales.ImagenGaleria1 = bdDescargarImagenes.getImageFromDirectory(CodigosGenerales.Codigo_Articulo + "_1.jpg");
        iv_imagen.setImageBitmap(CodigosGenerales.ImagenGaleria1);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.fab_info):
                ShowFichaTecnica();
                break;
            case (R.id.ib_carritoPedidos):
                Double Cantidad = CodigosGenerales.tryParseDouble(et_cantidad.getText().toString());
                if (Cantidad == 0)
                    Cantidad = 1.0;
                AgregarProductosListaDeseo(Cantidad);
                break;
            case (R.id.ib_fichatecnica):
                ShowFichaTecnica();
                break;
            case (R.id.ib_galeria):
                try {
                    if (isGalleryReady)
                        new frag_dialog_galeria(getContext(), this);
                    else
                        Toast.makeText(getActivity(), "Cargando imagenes... Intente de nuevo por favor", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
                break;
        }
    }

    public void ShowFichaTecnica() {
        layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate((R.layout.custom_popup_info), null);
        popupWindow = new PopupWindow(popupView, id_lyContent.getWidth(), id_lyContent.getHeight());

        TextView tv_nombre = popupView.findViewById(R.id.tv_nombre);
        TextView tv_detalle = popupView.findViewById(R.id.tv_detalle);
        if (FichaTecnica == null) {
            FichaTecnica = dataArticulos.getFichaTecnica(Codigo_Articulo);
            //tv_nombre.setText("Ficha Técnica");

            if (FichaTecnica != null) {
                String Detalle = "";
                for (int i = 0; i < FichaTecnica.size(); i++) {
                    Detalle += FichaTecnica.get(i).get(0) + "\n\t " + FichaTecnica.get(i).get(1) + "\n\n";
                }
                tv_detalle.setText(Detalle);
            }
        }
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        b_cerrar_popup = popupView.findViewById(R.id.id_cerrar);
        b_cerrar_popup.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.showAsDropDown(tv_origen_popup, 0, 0);
    }


    @Override
    public void ResultadoCuadroDialogGaleria(Bitmap bimap) {
        iv_imagen.setImageBitmap(bimap);
    }


    public class BackGroundTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            CodigosGenerales.ImagenGaleria2 = bdDescargarImagenes.getImageFromDirectory(CodigosGenerales.Codigo_Articulo + "_2.jpg");
            CodigosGenerales.ImagenGaleria3 = bdDescargarImagenes.getImageFromDirectory(CodigosGenerales.Codigo_Articulo + "_3.jpg");
            CodigosGenerales.ImagenGaleria4 = bdDescargarImagenes.getImageFromDirectory(CodigosGenerales.Codigo_Articulo + "_4.jpg");
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                isGalleryReady=true;
            } catch (Exception e) {
                Log.d("FragList", e.getMessage());

            }
            super.onPostExecute(s);
        }
    }

    private void AgregarProductosListaDeseo(Double Cantidad) {
        if (Cantidad > 0) {
            if (Cantidad <= Stock) {

                Log.d(TAG,"Precio: "+Precio_Articulo);
                if (dataListaDeseo.GuardarListaDeseo(Codigo_Articulo, Nombre_Articulo, Cantidad.toString(), Unidad_Articulo, Precio_Articulo.toString(), DatosCliente.Codigo_ListaPrecios,PorcentajeIGV.toString())) {
                    Toast.makeText(getActivity(), "Se ha(n) agregado " + Cantidad + " elemento(s)", Toast.LENGTH_SHORT).show();
                    CodigosGenerales.ImporteTotal+=Precio_Articulo;
                    CodigosGenerales.CantidadItems+=Cantidad;

                    dataListaDeseo.setResumen(tv_cabecera_cantidad,tv_cabecera_total);
                    et_cantidad.setText("");
                } else
                    Toast.makeText(getActivity(), "No se ha podido realizar la operación", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "No hay suficientes productos.", Toast.LENGTH_SHORT).show();
            }
        }
        CodigosGenerales.hideSoftKeyboard(getActivity());
    }

}