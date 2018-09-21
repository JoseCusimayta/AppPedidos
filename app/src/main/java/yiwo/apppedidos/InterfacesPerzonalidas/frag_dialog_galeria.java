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
import android.widget.Toast;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class frag_dialog_galeria {

    private Bitmap bitmap, bitmap1, bitmap2, bitmap3, bitmap4;
    private Context context;
    private String TAG = "frag_dialog_galeria";

    private CarouselView carouselView;

    private Bitmap[] sampleImages;
    public interface FinalizoCuadroDialogGaleria {
        void ResultadoCuadroDialogGaleria(Bitmap bitmap);
    }

    private frag_dialog_galeria.FinalizoCuadroDialogGaleria interfaz;

    public frag_dialog_galeria(Context context, frag_dialog_galeria.FinalizoCuadroDialogGaleria actividad) {

        this.context = context;
        interfaz = actividad;
        final Dialog dialogo = new Dialog(context);
        dialogo.setCancelable(true);
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogo.setContentView(R.layout.frag_dialog_galeria);
        dialogo.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        try {
            File file = new File(CodigosGenerales.myDirectorio, CodigosGenerales.Codigo_Empresa + "_" + CodigosGenerales.Codigo_Articulo + "_1.jpg"); //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"
            if (file.exists()) {
                bitmap1 = BitmapFactory.decodeStream(new FileInputStream(file));
                bitmap=bitmap1;
            } else {
                Log.d(TAG + "-A", "Poniendo Logo ");
                dialogo.dismiss();
            }
            file = new File(CodigosGenerales.myDirectorio, CodigosGenerales.Codigo_Empresa + "_" + CodigosGenerales.Codigo_Articulo + "_2.jpg"); //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"
            if (file.exists()) {
                bitmap2 = BitmapFactory.decodeStream(new FileInputStream(file));
            } else {
                Log.d(TAG + "-A", "Poniendo Logo ");
            }
            file = new File(CodigosGenerales.myDirectorio, CodigosGenerales.Codigo_Empresa + "_" + CodigosGenerales.Codigo_Articulo + "_3.jpg"); //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"
            if (file.exists()) {
                bitmap3 = BitmapFactory.decodeStream(new FileInputStream(file));
            } else {
                Log.d(TAG + "-A", "Poniendo Logo ");
            }
            file = new File(CodigosGenerales.myDirectorio, CodigosGenerales.Codigo_Empresa + "_" + CodigosGenerales.Codigo_Articulo + "_4.jpg"); //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"
            if (file.exists()) {
                bitmap4 = BitmapFactory.decodeStream(new FileInputStream(file));
            } else {
                Log.d(TAG + "-A", "Poniendo Logo ");
            }

            sampleImages = new Bitmap[]{bitmap1,bitmap2,bitmap3,bitmap4};
            carouselView = dialogo.findViewById(R.id.carouselView);
            carouselView.setPageCount(sampleImages.length);

            carouselView.setImageListener(imageListener);
            carouselView.setImageClickListener(new ImageClickListener() {
                @Override
                public void onClick(int position) {
                    switch (position){
                        case 0:
                            bitmap=bitmap1;
                        case 1:
                            bitmap=bitmap2;
                        case 2:
                            bitmap=bitmap3;
                        case 3:
                            bitmap=bitmap4;
                    }
                    interfaz.ResultadoCuadroDialogGaleria(bitmap);
                    dialogo.dismiss();
                    //Toast.makeText(this, "Clicked item: "+ position, Toast.LENGTH_SHORT).show();
                }
            });

        } catch (FileNotFoundException e) {
            Log.d(TAG, e.getMessage());
        }





        dialogo.show();
    }


    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageBitmap(sampleImages[position]);
        }
    };

}