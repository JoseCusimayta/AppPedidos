package yiwo.apppedidos.InterfacesPerzonalidas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.util.Log;
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
import yiwo.apppedidos.AspectosGenerales.CodigosUtiles;
import yiwo.apppedidos.AspectosGenerales.DatosCliente;
import yiwo.apppedidos.Control.BDClientes;
import yiwo.apppedidos.R;

public class ClientesAdapter extends ArrayAdapter<Clientes> {

    ArrayList<Clientes> data;
    Context context;

    private static class ViewHolder {
        TextView tv_code;
        TextView tv_name;
        TextView tv_razon_social;
        TextView tv_nombre_comercial;
        TextView tv_direccion;
        TextView tv_codigo_nombre_forma_pago;
        TextView tv_linea_soles;
        TextView tv_linea_porcentaje;
        LinearLayout ly_row;
        LinearLayout ly_detalle;
    }

    public ClientesAdapter(ArrayList<Clientes> data, Context context) {
        super(context, R.layout.custom_row_item_clientes, data);
        this.data = data;
        this.context = context;
    }

    private int lastPosition = -1;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Clientes dataModel = getItem(position);
        ClientesAdapter.ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new ClientesAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.custom_row_item_clientes, parent, false);
            viewHolder.tv_code = convertView.findViewById(R.id.cod);
            viewHolder.tv_name = convertView.findViewById(R.id.name);

            viewHolder.tv_razon_social=convertView.findViewById(R.id.tv_razon_social);
            viewHolder.tv_nombre_comercial=convertView.findViewById(R.id.tv_nombre_comercial);
            viewHolder.tv_direccion=convertView.findViewById(R.id.tv_direccion);
            viewHolder.tv_codigo_nombre_forma_pago=convertView.findViewById(R.id.tv_codigo_nombre_forma_pago);
            viewHolder.tv_linea_soles=convertView.findViewById(R.id.tv_linea_soles);
            viewHolder.tv_linea_porcentaje=convertView.findViewById(R.id.tv_linea_porcentaje);

            viewHolder.ly_row = convertView.findViewById(R.id.ly_row);
            viewHolder.ly_detalle = convertView.findViewById(R.id.ly_detalle);
            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ClientesAdapter.ViewHolder) convertView.getTag();
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
        viewHolder.tv_razon_social.setText("Razon Social :\n \t"+dataModel.getNombre_Cliente());
        viewHolder.tv_nombre_comercial.setText("Nombre Comercial :\n \t"+dataModel.getNombre_comercial());
        viewHolder.tv_direccion.setText("Direccion :\n \t"+dataModel.getDireccion_Cliente());
        viewHolder.tv_codigo_nombre_forma_pago.setText("Forma de pago :\n \t Codigo:"+dataModel.getCodigo_FormaPago()+" \n \t Nombre: "+dataModel.getNombre_FormaPago());
        viewHolder.tv_linea_soles.setText("Linea S/ : \n \t Linea Total: "+CodigosGenerales.RedondearDecimalesFormateado(LineaTotal)+" \n \t Linea Disponible: "+CodigosGenerales.RedondearDecimalesFormateado(LineaDisponible)+" \n \t Linea Usada: "+CodigosGenerales.RedondearDecimalesFormateado(LineaUsada));
        viewHolder.tv_linea_porcentaje.setText("Linea % : \n \t Linea Total: "+PorcentajeLineaTotal+" % \n \t Linea Disponible: "+CodigosGenerales.RedondearDecimalesFormateado(PorcentajeLineaDisponible)+" % \n \t Linea Usada: "+CodigosGenerales.RedondearDecimalesFormateado(PorcentajeLineaUsada)+" %");
        final LinearLayout ly=viewHolder.ly_detalle;
        viewHolder.ly_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ly.getVisibility()==View.GONE){
                    ly.setVisibility(View.VISIBLE);
                }else {
                    ly.setVisibility(View.GONE);
                }
            }
        });
        viewHolder.ly_row.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                return false;
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }


}
