package yiwo.apppedidos.AspectosGenerales;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import yiwo.apppedidos.R;

public class CodigosUtiles {
     static String  TAG="CodigosUtiles";
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
            File file = new File(DatosConexiones.myDirectorio, CodigosGenerales.Codigo_Articulo + "_1.jpg"); //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"
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
cnom_familia,
cnom_subfamilia,
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
Harticul.ccod_almacen,
nigv
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
and (
(ccod_articulo like ? or cnom_articulo like ? )
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
ccod_almacen,
nigv




                            SELECT
                            ccod_cliente, cnom_cliente, cdireccion, cnum_ruc, cnum_dni
                            lista_precios, Hforpag_provee.ccod_forpago as ccod_forpago,cnom_forpago, nro_dias
                            From Hcliente
                            inner join Hforpag_provee on
                            ccod_proveedor=ccod_cliente and
                            Hforpag_provee.ccod_empresa=Hcliente.ccod_empresa and
                            Hforpag_provee.tipo=Hcliente.cgrupo_cliente
                            inner join Hfor_pag on
                            hforpag_provee.ccod_empresa = Hfor_pag.ccod_empresa And
                            hforpag_provee.ccod_forpago = Hfor_pag.ccod_forpago
                            Where
                            Hcliente.ccod_empresa = ?
                            And Hcliente.cgrupo_cliente = '12'
                            and (ccod_cliente like ? or cnom_cliente like ? )
                            and selec='S'

     */



    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }


    public File crearDirectorioPublico(String nombreDirectorio) {
        //Crear directorio público en la carpeta Pictures.
        File directorio = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), nombreDirectorio);
        //Muestro un mensaje en el logcat si no se creo la carpeta por algun motivo
        Log.e(TAG, ""+directorio.mkdir());
        directorio.mkdir();
      //  if (!directorio.mkdir())
      //      Log.e(TAG, "Error: No se creo el directorio público");

        return directorio;
    }

    public static File crearDirectorioPrivado(Context context, String nombreDirectorio) {

        //String root = Environment.getExternalStorageDirectory().toString(); //Obetner el directorio padre
        //File myDirectorio = new File(root + "/pedidos"); // Crear una carpeta para guardar las imagenes
        //Crear directorio privado en la carpeta Pictures.
        File directorio =new File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                nombreDirectorio);
        //Muestro un mensaje en el logcat si no se creo la carpeta por algun motivo
        if (!directorio.mkdirs())
            Log.e(TAG, "Error: No se creo el directorio privado");

        return directorio;
    }

    public static File crearCarpetaAlmInterno(){

        File directorio = DatosConexiones.myDirectorio;
        Log.e(TAG, ""+directorio.mkdir());
       // if(!directorio.mkdirs()){
       //     Log.e(TAG, "Error: No se creo el directorio privado");
       // }
        directorio.mkdir();
        return directorio;
    }
}
