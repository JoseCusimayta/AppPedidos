package yiwo.apppedidos.InterfacesPerzonalidas;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.AspectosGenerales.ConfiguracionEmpresa;
import yiwo.apppedidos.AspectosGenerales.DatosConexiones;
import yiwo.apppedidos.Fragment.FragDescripcion;
import yiwo.apppedidos.R;

public class CustomAdapterArticulos extends ArrayAdapter<CustomDataModelArticulos> implements View.OnClickListener {

    //region Declaración de variables a usar
    private String TAG = "CustomAdapterArticulos";
    private ArrayList<CustomDataModelArticulos> dataSet;
    private Context mContext;
    private Activity mActivity;
    private FragmentManager fragmentManager;
    private CustomAdapterArticulos.ViewHolder viewHolder;    // se arma el viewHolder para cada fila
    CustomDataModelArticulos dataModel;
    //region Variables para guardar y recibir imagenes
    private Integer imagenescargadas = 0;
    private String setTag = "articulos";
    //endregion
    //    //endregion

    // View lookup cache
    private static class ViewHolder {
        ImageView iv_urlA;
        TextView txtnombreA;
        TextView txtprecioA;
        TextView tv_centeredA;
        ImageView iv_urlB;
        TextView txtnombreB;
        TextView txtprecioB;
        TextView tv_centeredB;
    }


    public CustomAdapterArticulos(ArrayList<CustomDataModelArticulos> data, Context context, Activity activity, FragmentManager fragmentManager) {
        super(context, R.layout.custom_row_item_articulos, data);
        this.dataSet = data;
        this.mContext = context;
        this.mActivity= activity;
        this.fragmentManager = fragmentManager;
    }


    @Override
    public void onClick(View v) {
        //region Obtener el DataModel para esta posición
        int position = (Integer) v.getTag();
        Object object = getItem(position);
        CustomDataModelArticulos dataModel = (CustomDataModelArticulos) object;
        //endregion

        Fragment fragment;

        switch (v.getId()) {

            //region si se presiona la primera imagen de la fila
            case R.id.iv_imagenA:
                try {
                    Log.d(TAG, "getCodA " + dataModel.getCodA());
                    //region se verifica que la cantidad sea mayor a 0
                    if (Double.parseDouble(dataModel.getCantidadA()) > 0.00) {
                        //region si es mayor a 0 se actualiza el codigo de articulo general y se cambia a la descripción
                        CodigosGenerales.Codigo_Articulo = dataModel.getCodA();
                        fragment = new FragDescripcion();
                        CambiarFragment(fragment);
                        //endregion
                    } else {
                        Toast.makeText(mContext, "No hay suficientes productos", Toast.LENGTH_SHORT).show();
                    }
                    //endregion
                } catch (Exception e) {
                    Log.d(TAG, "onClick A  " + e.getMessage());
                }
                break;
            //endregion

            //region si se presiona la segunda imagen de la fila
            case R.id.iv_imagenB:
                Log.d(TAG, "getCodB " + dataModel.getCodB());
                try {
                    //region se verifica que la cantidad sea mayor a 0
                    if (Double.parseDouble(dataModel.getCantidadB()) > 0.00) {
                        //region si es mayor a 0 se actualiza el codigo de articulo general y se cambia a la descripción
                        CodigosGenerales.Codigo_Articulo = dataModel.getCodB();
                        fragment = new FragDescripcion();
                        CambiarFragment(fragment);
                        //endregion
                    } else {
                        Toast.makeText(mContext, "No hay suficientes productos", Toast.LENGTH_SHORT).show();
                    }
                    //endregion
                } catch (Exception e) {
                    Log.d(TAG, "onClick B  " + e.getMessage());
                }
                break;
            //endregion
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        dataModel = getItem(position);//Obtener el DataModel para esta posición Get the data item for this position


        viewHolder = new CustomAdapterArticulos.ViewHolder(); // se inicializa el viewHolder para llenarlo con información

        //region Verificar si existe la vista, si existe se reusa
        if (convertView == null) {

            //region declarando la el inflater para crear una fila personalizada
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.custom_row_item_articulos, parent, false);

            //region declarando las variables del viewHolder
            viewHolder.iv_urlA = convertView.findViewById(R.id.iv_imagenA);
            viewHolder.txtnombreA = convertView.findViewById(R.id.tv_nombreA);
            viewHolder.txtprecioA = convertView.findViewById(R.id.tv_precioA);
            viewHolder.tv_centeredA = convertView.findViewById(R.id.tv_centeredA);
            viewHolder.iv_urlB = convertView.findViewById(R.id.iv_imagenB);
            viewHolder.txtnombreB = convertView.findViewById(R.id.tv_nombreB);
            viewHolder.txtprecioB = convertView.findViewById(R.id.tv_precioB);
            viewHolder.tv_centeredB = convertView.findViewById(R.id.tv_centeredB);
            //endregion

            convertView.setTag(viewHolder);
            //endregion

        } else {
            viewHolder = (CustomAdapterArticulos.ViewHolder) convertView.getTag();
        }

        //region poniendo información del articulo A (sin imagen)
        viewHolder.iv_urlA.setTag(position);
        viewHolder.iv_urlA.setOnClickListener(this);
        viewHolder.txtnombreA.setText(dataModel.getCodA());
        viewHolder.txtprecioA.setText(dataModel.getNombreA());
        viewHolder.tv_centeredA.setText(ConfiguracionEmpresa.Moneda_Trabajo + " " + dataModel.getPrecioA() + "\n" + dataModel.getCantidadA() + " " + dataModel.getUrlA());

        try {
            File file = new File(DatosConexiones.myDirectorio, ConfiguracionEmpresa.Codigo_Empresa+"_"+dataModel.getCodA() + "_1.jpg"); //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                viewHolder.iv_urlA.setImageBitmap(bitmap);
            }else{
                Log.d(TAG+"-A","Poniendo Logo ");
                viewHolder.iv_urlA.setImageResource(R.drawable.logo);
            }
        } catch (FileNotFoundException e) {
            Log.d(TAG,e.getMessage());
//            e.printStackTrace();
        }
        //endregion

        //region poniendo información del articulo B (sin imagen)
        try {
            if (dataModel.getCodB() != null) {
                viewHolder.iv_urlB.setVisibility(View.VISIBLE);
                viewHolder.txtnombreB.setVisibility(View.VISIBLE);
                viewHolder.txtprecioB.setVisibility(View.VISIBLE);
                viewHolder.iv_urlB.setTag(position);
                viewHolder.iv_urlB.setOnClickListener(this);
                viewHolder.txtnombreB.setText(dataModel.getCodB());
                viewHolder.txtprecioB.setText(dataModel.getNombreB());
                viewHolder.tv_centeredB.setText(ConfiguracionEmpresa.Moneda_Trabajo + " " + dataModel.getPrecioB() + "\n" + dataModel.getCantidadB() + " " + dataModel.getUrlB());


                try {
                    File file = new File(DatosConexiones.myDirectorio, ConfiguracionEmpresa.Codigo_Empresa+"_"+ dataModel.getCodB() + "_1.jpg"); //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"
                    if (file.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                        viewHolder.iv_urlB.setImageBitmap(bitmap);
                    }else{
                        Log.d(TAG+"-B","Poniendo Logo ");
                        viewHolder.iv_urlB.setImageResource(R.drawable.logo);
                    }
                } catch (FileNotFoundException e) {
//                     e.printStackTrace();
                     Log.d(TAG,e.getMessage());
                }
            } else {
                viewHolder.iv_urlB.setVisibility(View.GONE);
                viewHolder.txtnombreB.setVisibility(View.GONE);
                viewHolder.txtprecioB.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //endregion
        return convertView;
    }

    public void CambiarFragment(Fragment fragment) {
        CodigosGenerales.hideSoftKeyboard(mActivity);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frag_contenedor, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}