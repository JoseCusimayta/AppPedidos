package yiwo.apppedidos.InterfacesPerzonalidas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import yiwo.apppedidos.R;

public class CodNomAdapter extends ArrayAdapter<CodNom> {

    ArrayList<CodNom> data;
    Context context;


    private static class ViewHolder {
        TextView tv_code;
        TextView tv_name;
    }

    public CodNomAdapter(ArrayList<CodNom> data, Context context) {
        super(context, R.layout.custom_row_item, data);
        this.data = data;
        this.context = context;
    }

    private int lastPosition = -1;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CodNom dataModel = getItem(position);
        CustomAdapterCodNom.ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new CustomAdapterCodNom.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.custom_row_item, parent, false);
            viewHolder.tv_code = convertView.findViewById(R.id.cod);
            viewHolder.tv_name = convertView.findViewById(R.id.name);
            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CustomAdapterCodNom.ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.tv_code.setText(dataModel.getCodigo());
        viewHolder.tv_name.setText(dataModel.getNombre());

        // Return the completed view to render on screen
        return convertView;
    }


}
