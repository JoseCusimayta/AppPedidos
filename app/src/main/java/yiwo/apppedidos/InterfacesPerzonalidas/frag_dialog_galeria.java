package yiwo.apppedidos.InterfacesPerzonalidas;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class frag_dialog_galeria {

    private Context context;
    private String TAG = "frag_dialog_galeria";

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

        sampleImages = new Bitmap[]{CodigosGenerales.ImagenGaleria1, CodigosGenerales.ImagenGaleria2, CodigosGenerales.ImagenGaleria3, CodigosGenerales.ImagenGaleria4};
        CarouselView carouselView = dialogo.findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);

        carouselView.setImageListener(imageListener);
        carouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                Log.d(TAG, "Posicion "+position+"");
                interfaz.ResultadoCuadroDialogGaleria(sampleImages[position]);
                dialogo.dismiss();
                //Toast.makeText(this, "Clicked item: "+ position, Toast.LENGTH_SHORT).show();
            }
        });

        dialogo.show();
    }


    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageBitmap(sampleImages[position]);
        }
    };

}