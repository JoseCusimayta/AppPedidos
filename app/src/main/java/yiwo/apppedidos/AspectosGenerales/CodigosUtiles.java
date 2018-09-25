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


                                select top(50)
                                ccod_articulo,
                                cnom_articulo,
                                cfamilia,
                                ccod_subfamilia,
                                codmarca,
                                modelo,
                                color,
                                tratamiento,
                                fuelle,
                                azas,
                                solapa,
                                cmoneda_precio,
                                erp_monto,
                                cunidad,
                                Isnull(( SUM(ERP_STOART) - SUM(ERP_STOCOM)),0) as stock,
                                Harticul.ccod_almacen
                                from Harticul
                                inner join Hstock
                                on
                                Harticul.ccod_articulo=HSTOCK.ERP_CODART and
                                Harticul.ccod_empresa=HSTOCK.ERP_CODEMP and
                                Harticul.ccod_almacen=HSTOCK.ERP_CODALM
                                inner join Erp_Lista_Precio_Cliente
                                on
                                Harticul.ccod_articulo=Erp_Lista_Precio_Cliente.ERP_CODART and
                                Harticul.ccod_empresa=Erp_Lista_Precio_Cliente.ERP_CODEMP and
                                Harticul.cunidad=Erp_Lista_Precio_Cliente.erp_unidad
                                where
                                ccod_empresa = ?
                                and ERP_CODPTV = ?
                                and ERP_CODALM = ?
                                and erp_tipo = '12 '
                                and erp_codigo_concepto = ?
                                and (ccod_articulo like ? or cnom_articulo like ? )
                                group by
                                ccod_articulo,
                                cnom_articulo,
                                cfamilia,
                                ccod_subfamilia,
                                codmarca,
                                modelo,
                                color,
                                tratamiento,
                                fuelle,
                                azas,
                                solapa,
                                cmoneda_precio,
                                erp_monto,
                                cunidad,
                                ccod_alma


     */



}
