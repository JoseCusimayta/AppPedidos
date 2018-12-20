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
import java.io.FileNotFoundException;
import java.util.ArrayList;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.AspectosGenerales.ConfiguracionEmpresa;
import yiwo.apppedidos.AspectosGenerales.DatosConexiones;
import yiwo.apppedidos.R;

public class CustomAdapterNumerado  extends ArrayAdapter<CustomDataModel> {

    ArrayList<CustomDataModel> data;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtnro;
        ImageView iv_url;
        TextView tv_info1;
        TextView tv_info2;
    }


    public CustomAdapterNumerado(ArrayList<CustomDataModel> data, Context context) {
        super(context, R.layout.custom_row_item_numerado, data);
        this.data = data;
        this.mContext = context;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        CustomDataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        CustomAdapterNumerado.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new CustomAdapterNumerado.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.custom_row_item_numerado, parent, false);
            viewHolder.txtnro = convertView.findViewById(R.id.tv_numero);
            viewHolder.iv_url = convertView.findViewById(R.id.iv_imagen);
            viewHolder.tv_info1 = convertView.findViewById(R.id.tv_info1);
            viewHolder.tv_info2 = convertView.findViewById(R.id.tv_info2);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CustomAdapterNumerado.ViewHolder) convertView.getTag();
            result = convertView;
        }

        // Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        // result.startAnimation(animation);
        lastPosition = position;
        Log.d("TAG","DatoInvisible"+dataModel.getDato_invisible());
        if(dataModel.getDato_invisible()=="1")
            viewHolder.tv_info2.setVisibility(View.GONE);
        else
            viewHolder.tv_info2.setVisibility(View.VISIBLE);


        viewHolder.txtnro.setText(dataModel.getCod());
        if (dataModel.getName()==null)
            viewHolder.iv_url.setVisibility(View.GONE);
        else {
            viewHolder.iv_url.setVisibility(View.VISIBLE);
            viewHolder.iv_url.setImageResource(mContext.getResources().getIdentifier(dataModel.getName(), "drawable", mContext.getPackageName()));
            try {
                File file = new File(DatosConexiones.myDirectorio, ConfiguracionEmpresa.Codigo_Empresa+"_"+ dataModel.getDni() + "_1.jpg"); //guardar el archivo con el nombre de la imagen en el directorio "myDirectorio"
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                viewHolder.iv_url.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        viewHolder.iv_url.setTag(position);
        viewHolder.tv_info1.setText(dataModel.getRuc());
        viewHolder.tv_info2.setText(dataModel.getDireccion());
        try {
            CodigosGenerales.Precio_TotalPedido += Double.parseDouble(dataModel.getDato_invisible());
        }catch (Exception e){
            Log.d("CustomAdapterNumerado",e.getMessage());
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
