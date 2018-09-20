package yiwo.apppedidos.InterfacesPerzonalidas;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class frag_dialog_galeria {

    ImageView iv_producto, iv_producto1, iv_producto2, iv_producto3, iv_producto4;
    Bitmap bitmap, bitmap1, bitmap2, bitmap3, bitmap4;
    Context context;
    String TAG = "frag_dialog_galeria";

    public interface FinalizoCuadroDialogGaleria {
        void ResultadoCuadroDialogGaleria(Bitmap bitmap);
    }

    private frag_dialog_galeria.FinalizoCuadroDialogGaleria interfaz;

    public frag_dialog_galeria(Context context, frag_dialog_galeria.FinalizoCuadroDialogGaleria actividad) {

        this.context = context;
        interfaz = actividad;
        final Dialog dialogo = new Dialog(context);
        //dialogo.setCancelable(false);
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogo.setContentView(R.layout.frag_dialog_galeria);
        dialogo.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        iv_producto = dialogo.findViewById(R.id.iv_producto);
        iv_producto1 = dialogo.findViewById(R.id.iv_producto1);
        iv_producto2 = dialogo.findViewById(R.id.iv_producto2);
        iv_producto3 = dialogo.findViewById(R.id.iv_producto3);
        iv_producto4 = dialogo.findViewById(R.id.iv_producto4);

        iv_producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaz.ResultadoCuadroDialogGaleria(bitmap);
                dialogo.dismiss();
            }
        });
        iv_producto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bitmap = bitmap1;
                iv_producto.setImageBitmap(bitmap);
            }
        });
        iv_producto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bitmap = bitmap2;
                iv_producto.setImageBitmap(bitmap);
            }
        });
        iv_producto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bitmap = bitmap3;
                iv_producto.setImageBitmap(bitmap);
            }
        });
        iv_producto4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bitmap = bitmap4;
                iv_producto.setImageBitmap(bitmap);
            }
        });
        try {
            File file = new File(CodigosGenerales.myDirectorio, CodigosGenerales.Codigo_Empresa + "_" + CodigosGenerales.Codigo_Articulo + "_1.jpg"); //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"
            if (file.exists()) {
                bitmap1 = BitmapFactory.decodeStream(new FileInputStream(file));
                bitmap=bitmap1;
                iv_producto.setImageBitmap(bitmap1);
                iv_producto1.setImageBitmap(bitmap1);
            } else {
                Log.d(TAG + "-A", "Poniendo Logo ");
                iv_producto1.setVisibility(View.INVISIBLE);
                dialogo.dismiss();
            }
            file = new File(CodigosGenerales.myDirectorio, CodigosGenerales.Codigo_Empresa + "_" + CodigosGenerales.Codigo_Articulo + "_2.jpg"); //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"
            if (file.exists()) {
                bitmap2 = BitmapFactory.decodeStream(new FileInputStream(file));
                iv_producto2.setImageBitmap(bitmap2);
            } else {
                Log.d(TAG + "-A", "Poniendo Logo ");
                iv_producto2.setVisibility(View.INVISIBLE);
            }
            file = new File(CodigosGenerales.myDirectorio, CodigosGenerales.Codigo_Empresa + "_" + CodigosGenerales.Codigo_Articulo + "_3.jpg"); //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"
            if (file.exists()) {
                bitmap3 = BitmapFactory.decodeStream(new FileInputStream(file));
                iv_producto3.setImageBitmap(bitmap3);
            } else {
                Log.d(TAG + "-A", "Poniendo Logo ");
                iv_producto3.setVisibility(View.INVISIBLE);
            }
            file = new File(CodigosGenerales.myDirectorio, CodigosGenerales.Codigo_Empresa + "_" + CodigosGenerales.Codigo_Articulo + "_4.jpg"); //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"
            if (file.exists()) {
                bitmap4 = BitmapFactory.decodeStream(new FileInputStream(file));
                iv_producto4.setImageBitmap(bitmap4);
            } else {
                Log.d(TAG + "-A", "Poniendo Logo ");
                iv_producto4.setVisibility(View.INVISIBLE);
            }
        } catch (FileNotFoundException e) {
            Log.d(TAG, e.getMessage());
        }
        dialogo.show();
    }
}