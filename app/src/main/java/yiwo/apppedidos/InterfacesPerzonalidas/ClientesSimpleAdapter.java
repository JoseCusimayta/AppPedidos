package yiwo.apppedidos.InterfacesPerzonalidas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.Control.BDClientes;
import yiwo.apppedidos.R;

public class ClientesSimpleAdapter  extends ArrayAdapter<Clientes> {

    ArrayList<Clientes> data;
    Context context;

    private static class ViewHolder {
        TextView tv_code;
        TextView tv_name;
    }

    public ClientesSimpleAdapter(ArrayList<Clientes> data, Context context) {
        super(context, R.layout.custom_row_item, data);
        this.data = data;
        this.context = context;
    }

    private int lastPosition = -1;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Clientes dataModel = getItem(position);
        ClientesSimpleAdapter.ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new ClientesSimpleAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.custom_row_item, parent, false);
            viewHolder.tv_code = convertView.findViewById(R.id.cod);
            viewHolder.tv_name = convertView.findViewById(R.id.name);

            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ClientesSimpleAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;
        Double LineaTotal,LineaDisponible=0.0,LineaUsada=0.0;
        Double PorcentajeLineaTotal=0.0,PorcentajeLineaDisponible=0.0,PorcentajeLineaUsada=0.0;
        LineaTotal= CodigosGenerales.tryParseDouble(dataModel.getLinea_disponible());
        if(LineaTotal!=0){
            PorcentajeLineaTotal=100.0;
            BDClientes bdClientes=new BDClientes();
            LineaUsada=CodigosGenerales.tryParseDouble(bdClientes.getLineaUsada(dataModel.getCodigo_Cliente()));
            LineaDisponible=LineaTotal-LineaUsada;
            PorcentajeLineaUsada=(LineaUsada/LineaTotal)*100;
            PorcentajeLineaDisponible=PorcentajeLineaTotal-PorcentajeLineaUsada;
        }
        viewHolder.tv_code.setText(dataModel.getCodigo_Cliente());
        viewHolder.tv_name.setText(dataModel.getNombre_Cliente());
        // Return the completed view to render on screen
        return convertView;
    }


}
