package yiwo.apppedidos.InterfacesPerzonalidas;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.ConexionBD.BDDescargarImagenes;
import yiwo.apppedidos.Fragment.FragDescripcion;
import yiwo.apppedidos.R;

public class ArticulosAdapter  extends RecyclerView.Adapter<ArticulosAdapter.MyViewHolder> {

    private Context mContext;
    private List<Articulos> ArticulosList;
    private String TAG = "ArticulosAdapter";
    private FragmentManager fragmentManager;
    private BDDescargarImagenes bdDescargarImagenes = new BDDescargarImagenes();
    private Activity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView codigo_articulo;
        public TextView nombre_articulo;
        public TextView cantidad_articulo;
        public TextView unidad_articulo;
        public TextView precio_articulo;
        public TextView moneda_articulo;
        public ImageView iv_articulos;

        public MyViewHolder(View view) {
            super(view);
//            codigo_articulo=view.findViewById(R.id.tv_codigo_articulo);
            nombre_articulo = view.findViewById(R.id.tv_nombre_articulo);
            cantidad_articulo = view.findViewById(R.id.tv_cantidad_articulo);
//            unidad_articulo=view.findViewById(R.id.tv_unidad_articulo);
            precio_articulo = view.findViewById(R.id.tv_precio_articulo);
//            moneda_articulo=view.findViewById(R.id.tv_moneda_articulo);
            iv_articulos = view.findViewById(R.id.iv_articulos);
        }
    }


    public ArticulosAdapter(Context mContext, List<Articulos> ArticulosList, FragmentManager fragmentManager, Activity activity) {
        this.mContext = mContext;
        this.ArticulosList = ArticulosList;
        this.fragmentManager = fragmentManager;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.articulos_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try{
        final Articulos Articulos = ArticulosList.get(position);
        holder.nombre_articulo.setText(Articulos.getNombre_articulo());

        holder.cantidad_articulo.setText(Articulos.getCantidad_articulo() + " " + Articulos.getUnidad_articulo());
        holder.precio_articulo.setText(Articulos.getMoneda_articulo() + " " + Articulos.getPrecio_articulo());

        Log.d(TAG, "Codigo Producto: " + Articulos.getCodigo_articulo());
        try {
            Bitmap bitmap = bdDescargarImagenes.getImageFromDirectory(Articulos.getCodigo_articulo() + "_1.jpg");
            if (bitmap != null)
                holder.iv_articulos.setImageBitmap(bitmap);
            else
                holder.iv_articulos.setImageResource(R.drawable.logo);
        } catch (Exception e) {
            Log.d(TAG, "onBindViewHolder " + e.getMessage());
        }
        holder.iv_articulos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CodigosGenerales.hideSoftKeyboard(activity);
                CodigosGenerales.Codigo_Articulo = Articulos.getCodigo_articulo();

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                Fragment fragment = new FragDescripcion();
                transaction.replace(R.id.frag_contenedor, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });}catch (Exception e){
            Log.d(TAG,"onBindViewHolder "+e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return ArticulosList.size();
    }
}