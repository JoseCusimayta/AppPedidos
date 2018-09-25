package yiwo.apppedidos.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.AspectosGenerales.ConfiguracionEmpresa;
import yiwo.apppedidos.Data.BDEmpresa;
import yiwo.apppedidos.Data.BDListDeseo;
import yiwo.apppedidos.Data.BDPedidos;
import yiwo.apppedidos.InterfacesPerzonalidas.CustomAdapterListaDeseos;
import yiwo.apppedidos.InterfacesPerzonalidas.CustomAdapterNumerado;
import yiwo.apppedidos.InterfacesPerzonalidas.CustomDataModel;
import yiwo.apppedidos.InterfacesPerzonalidas.CustomDataModelListaDeseos;
import yiwo.apppedidos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LateralPedidoDetalle extends Fragment {


    BDPedidos bdPedidos = new BDPedidos();
    ListView lv_items;
    ProgressBar progressBar;

    AppBarLayout app_barLayout;

    ArrayList<CustomDataModelListaDeseos> dataModels_listaDeseos;
    CustomAdapterListaDeseos adapter_listaDeseos;
    String TAG="";
    BDEmpresa bdEmpresa = new BDEmpresa();
    public LateralPedidoDetalle() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_lateral_pedido_detalle, container, false);

        lv_items = view.findViewById(R.id.list);
        progressBar = view.findViewById(R.id.progressBar);
        try {
            app_barLayout = getActivity().findViewById(R.id.app_barLayout);
            app_barLayout.setVisibility(View.VISIBLE);
            BackGroundTask task= new BackGroundTask();
            task.execute("");
            if (app_barLayout.getVisibility() == View.GONE)
                app_barLayout.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Log.d("FragListDeseo", "onCreateView: " + e.getMessage());
        }
        return view;
    }


    public class BackGroundTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            dataModels_listaDeseos = new ArrayList<>();
            lv_items.setAdapter(null);

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                ArrayList<List<String>> listaDeseos = bdPedidos.getDetalle(CodigosGenerales.Codigo_Pedido,ConfiguracionEmpresa.Codigo_Motivo);

                for (int i = 0; i < listaDeseos.size(); i++) {

                    String nitem = listaDeseos.get(i).get(0);
                    String ccod_articulo = listaDeseos.get(i).get(1);
                    String nom_articulo = listaDeseos.get(i).get(2);
                    String cunidad = listaDeseos.get(i).get(3);
                    Double ncantidad = Double.parseDouble(listaDeseos.get(i).get(4));
                    Double precio_unitario = Double.parseDouble(listaDeseos.get(i).get(5));
                    Double IGV_Articulo = Double.parseDouble(listaDeseos.get(i).get(6));
                    Double descuento_1 = Double.parseDouble(listaDeseos.get(i).get(7));
                    Double descuento_2 = Double.parseDouble(listaDeseos.get(i).get(8));
                    Double descuento_3 = Double.parseDouble(listaDeseos.get(i).get(9));
                    Double descuento_4 = Double.parseDouble(listaDeseos.get(i).get(10));
                    //String LP = listaDeseos.get(i).get(11);
                    Double BaseCalculada, BaseImponible, Descuento_Unico, MontoIGV, ImporteTotal, MontoADescontar;


                    Descuento_Unico = CodigosGenerales.getDescuenetoUnico(descuento_1, descuento_2, descuento_3, descuento_4);
                    BaseImponible = precio_unitario * ncantidad;

                    if (ConfiguracionEmpresa.isIncluidoIGV) {
                        MontoADescontar = (BaseImponible / (1 + (IGV_Articulo / 100))) * (Descuento_Unico) / 100;
                        BaseCalculada = (BaseImponible / (1 + (IGV_Articulo / 100))) * (100 - Descuento_Unico) / 100;
                    } else {
                        MontoADescontar = BaseImponible * (Descuento_Unico) / 100;
                        BaseCalculada = BaseImponible * (100 - Descuento_Unico) / 100;
                    }
                    MontoIGV = BaseCalculada * IGV_Articulo / 100;
                    ImporteTotal = BaseCalculada + MontoIGV;

                    //endregion

//                    Log.d(TAG,"nitem "+nitem);
//                    Log.d(TAG,"ccod_articulo "+ ccod_articulo);
//                    Log.d(TAG,"nom_articulo "+ nom_articulo);
//                    Log.d(TAG,"cunida "+ cunidad);
//                    Log.d(TAG,"ncantidad "+ ncantidad);
//                    Log.d(TAG,"precio_unitario "+ precio_unitario);
//                    Log.d(TAG,"IGV_Articulo "+ IGV_Articulo);
//                    Log.d(TAG,"descuento_1 "+ descuento_1);
//                    Log.d(TAG,"descuento_2 "+ descuento_2);
//                    Log.d(TAG,"descuento_3 "+ descuento_3);
//                    Log.d(TAG,"descuento_4 "+ descuento_4);
//                    Log.d(TAG,"BaseCalculada "+ BaseCalculada);
//                    Log.d(TAG,"BaseImponible "+ BaseImponible);
//                    Log.d(TAG,"Descuento_Unico "+ Descuento_Unico);
//                    Log.d(TAG,"MontoIGV "+ MontoIGV);
//                    Log.d(TAG,"ImporteTotal "+ ImporteTotal);
//                    Log.d(TAG,"CodigosGenerales.Moneda_Empresa "+ CodigosGenerales.Moneda_Empresa);

                    dataModels_listaDeseos.add(
                            new CustomDataModelListaDeseos(
                                    nitem,
                                    ccod_articulo,
                                    nom_articulo,
                                    CodigosGenerales.Moneda_Empresa,
                                    precio_unitario.toString(),
                                    ncantidad.toString(),
                                    cunidad,
                                    descuento_1.toString(),
                                    descuento_2.toString(),
                                    descuento_3.toString(),
                                    descuento_4.toString(),
                                    CodigosGenerales.RedondearDecimales(BaseImponible,2),
                                    CodigosGenerales.RedondearDecimales(BaseCalculada,2),
                                    CodigosGenerales.RedondearDecimales(MontoIGV,2),
                                    CodigosGenerales.RedondearDecimales(ImporteTotal,2)
                            ));
                }
            } catch (Exception e) {
                Log.d(TAG, "doInBackground " + e.getMessage());
            }
            return null;

//
//
//            dataModels = new ArrayList<>();
//            arrayList = bdPedidos.getDetalle(CodigosGenerales.Codigo_Pedido,CodigosGenerales.Codigo_Motivo);
//            for (int i = 0; i < arrayList.size(); i++) {
//                dataModels.add(new CustomDataModel(
//                        String.valueOf(i + 1),
//                        "logo",
//                        arrayList.get(i).get(2),
//                        CodigosGenerales.Moneda_Empresa+" "+arrayList.get(i).get(5)+"\n" + arrayList.get(i).get(4) + " " + arrayList.get(i).get(3),
//                        null,
//                        arrayList.get(i).get(1),
//                        ""
//                ));
//            }
//            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            progressBar.setVisibility(View.GONE);
            try {
                adapter_listaDeseos= new CustomAdapterListaDeseos(dataModels_listaDeseos,getContext());
                lv_items.setAdapter(adapter_listaDeseos);
            } catch (Exception e) {
                Log.d(TAG, "BackGroundTask" + e.getMessage());
            }
            super.onPostExecute(s);
//
//            progressBar.setVisibility(View.GONE);
//
//            try {
//                adapter = new CustomAdapterNumerado(dataModels, getContext());
//                lv_items.setAdapter(adapter);
//            } catch (Exception e) {
//                Log.d("FragListDeseo", "BackGroundTask" + e.getMessage());
//            }
//            super.onPostExecute(s);
        }
    }
}