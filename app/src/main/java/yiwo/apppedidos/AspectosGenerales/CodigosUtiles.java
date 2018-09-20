package yiwo.apppedidos.AspectosGenerales;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import yiwo.apppedidos.R;

public class CodigosUtiles {
    public void CompartirEnRedes(Activity activity){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBodyText = CodigosGenerales.Nombre_Categoria +" - "+ "Nombre_Articulo";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Titulo");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        activity.startActivity(Intent.createChooser(sharingIntent, "Compartir este producto"));
    }

    public void MostrarImagenPopUpZoom(Activity activity){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        View mView = activity.getLayoutInflater().inflate(R.layout.photoview_zoom, null);
        PhotoView photoView = mView.findViewById(R.id.imageView);
//                photoView.setImageResource(R.drawable.logo);
        try {
            File file = new File(CodigosGenerales.myDirectorio, CodigosGenerales.Codigo_Articulo + "_1.jpg"); //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            photoView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBuilder.setView(mView);
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    /*
    Para crear un EditText de tipo password con un icono para poder ver la clave
                    <android.support.design.widget.TextInputLayout
                        android:id="@+id/inputLayout"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        app:passwordToggleEnabled="true">

                        <EditText
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:inputType="textPassword"
                            android:hint="Password" />

                    </android.support.design.widget.TextInputLayout>
     */
}
