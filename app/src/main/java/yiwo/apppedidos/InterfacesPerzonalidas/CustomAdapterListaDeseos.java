package yiwo.apppedidos.InterfacesPerzonalidas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.R;

public class CustomAdapterListaDeseos extends ArrayAdapter<CustomDataModelListaDeseos> {

    ArrayList<CustomDataModelListaDeseos> data;
    Context context;


    // View lookup cache
        private static class ViewHolder {

        TextView Numero_Orden;
       ImageView Imagen_Producto_url;
        TextView Codigo_Producto;
        TextView Nombre_Producto;
        TextView Tipo_moneda;
        TextView Precio_Unitario;
        TextView Cantidad;
        TextView Tipo_Unidad;
        TextView Descuento_1;
        TextView Descuento_2;
        TextView Descuento_3;
        TextView Descuento_4;
        TextView Base_Imponible;
        TextView Base_Calculada;
        TextView IGV;
        TextView Importe;
    }

    public CustomAdapterListaDeseos(ArrayList<CustomDataModelListaDeseos> data, Context context) {
        super(context, R.layout.custom_list_view_lista_deseo, data);
        this.data = data;
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        CustomDataModelListaDeseos dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        CustomAdapterListaDeseos.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new CustomAdapterListaDeseos.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.custom_list_view_lista_deseo, parent, false);
            viewHolder.Numero_Orden = convertView.findViewById(R.id.tv_numero);
            viewHolder.Imagen_Producto_url = convertView.findViewById(R.id.iv_imagen);
            viewHolder.Codigo_Producto = convertView.findViewById(R.id.tv_codigo_producto);
            viewHolder.Nombre_Producto = convertView.findViewById(R.id.tv_nombre_producto);
            viewHolder.Tipo_moneda = convertView.findViewById(R.id.tv_moneda_producto);
            viewHolder.Precio_Unitario = convertView.findViewById(R.id.tv_precio_producto);
            viewHolder.Cantidad = convertView.findViewById(R.id.tv_cantidad_producto);
            viewHolder.Tipo_Unidad = convertView.findViewById(R.id.tv_unidad_producto);
            viewHolder.Descuento_1 = convertView.findViewById(R.id.tv_descuento1);
            viewHolder.Descuento_2 = convertView.findViewById(R.id.tv_descuento2);
            viewHolder.Descuento_3 = convertView.findViewById(R.id.tv_descuento3);
            viewHolder.Descuento_4 = convertView.findViewById(R.id.tv_descuento4);
            viewHolder.Base_Imponible = convertView.findViewById(R.id.tv_base_imponible);
            viewHolder.Base_Calculada = convertView.findViewById(R.id.tv_base_calculada);
            viewHolder.IGV = convertView.findViewById(R.id.tv_igv);
            viewHolder.Importe = convertView.findViewById(R.id.tv_importe);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CustomAdapterListaDeseos.ViewHolder) convertView.getTag();
        }

        // Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        // result.startAnimation(animation);


        viewHolder.Numero_Orden.setText(dataModel.getNumero_Orden());
        try {
            File file = new File(CodigosGenerales.myDirectorio, CodigosGenerales.Codigo_Empresa + "_" + dataModel.getCodigo_Producto() + "_1.jpg"); //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            viewHolder.Imagen_Producto_url.setImageBitmap(bitmap);
        } catch (Exception e) {
            //  viewHolder.Imagen_Producto_url.setImageResource(context.getResources().getIdentifier("logo", "drawable", Context.getPackageName()));
            Log.d("CustomAdapter", "CustomAdapterListaDeseos - " + " - No se pudo isertar imagen: " + e.getMessage());
        }

        viewHolder.Codigo_Producto.setText(dataModel.getCodigo_Producto());
        viewHolder.Nombre_Producto.setText(dataModel.getNombre_Producto());
        viewHolder.Tipo_moneda.setText(dataModel.getTipo_moneda());
        viewHolder.Precio_Unitario.setText(dataModel.getPrecio_Unitario());
        viewHolder.Cantidad.setText(dataModel.getCantidad());
        viewHolder.Tipo_Unidad.setText(dataModel.getTipo_Unidad());
        viewHolder.Descuento_1.setText(dataModel.getDescuento_1());
        viewHolder.Descuento_2.setText(dataModel.getDescuento_2());
        viewHolder.Descuento_3.setText(dataModel.getDescuento_3());
        viewHolder.Descuento_4.setText(dataModel.getDescuento_4());
        viewHolder.Base_Imponible.setText(dataModel.getBase_Imponible());
        viewHolder.Base_Calculada.setText(dataModel.getBase_Calculada());
        viewHolder.IGV.setText(dataModel.getIGV());
        viewHolder.Importe.setText(dataModel.getImporte());

        // Return the completed view to render on screen
        return convertView;
    }
}
