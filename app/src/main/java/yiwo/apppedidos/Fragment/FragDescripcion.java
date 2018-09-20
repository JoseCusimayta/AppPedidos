package yiwo.apppedidos.Fragment;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import com.github.chrisbanes.photoview.PhotoView;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.Data.BDArticulos;
import yiwo.apppedidos.Data.BDListDeseo;
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
    String Codigo_Articulo, Nombre_Articulo, Unidad_Articulo, Precio_Articulo;
    EditText et_cantidad;
    TextView tv_origen_popup;
    AppBarLayout app_barLayout;
    FrameLayout id_lyContent;
    Double Stock;
    BDArticulos bdArticulos = new BDArticulos();
    BDListDeseo bdListDeseo = new BDListDeseo();
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
            informacion = bdArticulos.getDescripcionArticulo(CodigosGenerales.Codigo_Articulo);
            Codigo_Articulo = informacion.get(0);
            Nombre_Articulo = informacion.get(1);
            Unidad_Articulo = informacion.get(2);
            Precio_Articulo = informacion.get(4);
            Stock = Double.parseDouble(informacion.get(3));
            tv_cantidad.setText(Stock + " " + Unidad_Articulo);
            tv_precio.setText(CodigosGenerales.Moneda_Empresa + " " + Precio_Articulo);
            et_nombre.setText(CodigosGenerales.Nombre_Categoria + Nombre_Articulo);

            app_barLayout = getActivity().findViewById(R.id.app_barLayout);
            app_barLayout.setVisibility(View.VISIBLE);

            BackGroundTask task = new BackGroundTask();
            task.execute("");
        } catch (Exception e) {
            Log.d(TAG, "FragDescripcion: " + e.getMessage());
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
                    new frag_dialog_galeria(getContext(), this);
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                }
                /*FragmentManager manager= getSupportFragmentManager();
                DialogFragmentGaleria galeria= new DialogFragmentGaleria();
                galeria.setStyle(DialogFragment.STYLE_NO_FRAME,R.style.transparente);
                galeria.show(manager,"");*/
                break;
        }
    }

    public void ShowFichaTecnica() {
        layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate((R.layout.custom_popup_info), null);
        popupWindow = new PopupWindow(popupView, id_lyContent.getWidth(), id_lyContent.getHeight());

        TextView tv_nombre = popupView.findViewById(R.id.tv_nombre);
        TextView tv_detalle = popupView.findViewById(R.id.tv_detalle);

        ArrayList<List<String>> FichaTecnica = bdArticulos.getFichaTecnica(Codigo_Articulo);
        //tv_nombre.setText("Ficha Técnica");


        if (FichaTecnica != null) {
            String Detalle = "";
            for (int i = 0; i < FichaTecnica.size(); i++) {
                Detalle += FichaTecnica.get(i).get(0) + "\n\t " + FichaTecnica.get(i).get(1) + "\n\n";
            }
            tv_detalle.setText(Detalle);
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

        Bitmap bitmap1, bitmap2, bitmap3, bitmap4;
        String Imagen_nombre1, Imagen_nombre2, Imagen_nombre3, Imagen_nombre4;
        File imgFile1, imgFile2, imgFile3, imgFile4;

        @Override
        protected void onPreExecute() {
//            progressBar.setVisibility(View.VISIBLE);

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {

                try {
                    Imagen_nombre1 = CodigosGenerales.Codigo_Empresa + "_" + CodigosGenerales.Codigo_Articulo + "_1.jpg";
                    Imagen_nombre2 = CodigosGenerales.Codigo_Empresa + "_" + CodigosGenerales.Codigo_Articulo + "_2.jpg";
                    Imagen_nombre3 = CodigosGenerales.Codigo_Empresa + "_" + CodigosGenerales.Codigo_Articulo + "_3.jpg";
                    Imagen_nombre4 = CodigosGenerales.Codigo_Empresa + "_" + CodigosGenerales.Codigo_Articulo + "_4.jpg";

                    imgFile1 = new File(CodigosGenerales.myDirectorio, Imagen_nombre1); //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"
                    bitmap1 = BitmapFactory.decodeStream(new FileInputStream(imgFile1));
                    CodigosGenerales.ImagenGaleria1 = bitmap1;


                    imgFile2 = new File(CodigosGenerales.myDirectorio, Imagen_nombre2); //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"
                    bitmap2 = BitmapFactory.decodeStream(new FileInputStream(imgFile2));
                    CodigosGenerales.ImagenGaleria2 = bitmap2;


                    imgFile3 = new File(CodigosGenerales.myDirectorio, Imagen_nombre3); //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"
                    bitmap3 = BitmapFactory.decodeStream(new FileInputStream(imgFile3));
                    CodigosGenerales.ImagenGaleria3 = bitmap3;


                    imgFile4 = new File(CodigosGenerales.myDirectorio, Imagen_nombre4); //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"
                    bitmap4 = BitmapFactory.decodeStream(new FileInputStream(imgFile4));
                    CodigosGenerales.ImagenGaleria4 = bitmap4;

                } catch (Exception e) {

                    Log.d(TAG, "doInBackground: DOWNLOAD_URL: " + e.getMessage());
                }
            } catch (Exception e) {
                Log.d("FragList", "BackGroundTask: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                iv_imagen.setImageBitmap(bitmap1);
            } catch (Exception e) {
                Log.d("FragList", e.getMessage());

            }
            super.onPostExecute(s);
        }
    }

    private void AgregarProductosListaDeseo(Double Cantidad) {
        if (Cantidad > 0) {
            if (Cantidad <= Stock) {
                if (bdListDeseo.GuardarListaDeseo(Codigo_Articulo, Nombre_Articulo, Cantidad.toString(), Unidad_Articulo, Precio_Articulo, "")) {
                    Toast.makeText(getActivity(), "Se ha(n) agregado " + Cantidad + " elemento(s)", Toast.LENGTH_SHORT).show();
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