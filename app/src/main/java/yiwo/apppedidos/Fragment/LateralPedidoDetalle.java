package yiwo.apppedidos.Fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.AspectosGenerales.ConfiguracionEmpresa;
import yiwo.apppedidos.Control.BDEmpresa;
import yiwo.apppedidos.Control.BDPedidos;
import yiwo.apppedidos.Data.DataPedidos;
import yiwo.apppedidos.InterfacesPerzonalidas.CustomAdapterListaDeseos;
import yiwo.apppedidos.InterfacesPerzonalidas.CustomDataModelListaDeseos;
import yiwo.apppedidos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LateralPedidoDetalle extends Fragment {


    DataPedidos dataPedidos = new DataPedidos();
    ListView lv_items;
    ProgressBar progressBar;

    AppBarLayout app_barLayout;

    ArrayList<CustomDataModelListaDeseos> dataModels_listaDeseos;
    CustomAdapterListaDeseos adapter_listaDeseos;
    String TAG="";
    BackGroundTask task;
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
            if (task != null)
                task.cancel(true);
            task= new BackGroundTask();
            task.execute("");
            if (app_barLayout.getVisibility() == View.GONE)
                app_barLayout.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Log.d("FragListDeseo", "onCreateView: " + e.getMessage());
        }
        return view;
    }

    @Override
    public void onPause() {
        task.cancel(true);
        super.onPause();
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
                ArrayList<List<String>> listaDeseos = dataPedidos.getDetalle(CodigosGenerales.Codigo_Pedido,ConfiguracionEmpresa.Codigo_Motivo);

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

                    dataModels_listaDeseos.add(
                            new CustomDataModelListaDeseos(
                                    nitem,
                                    ccod_articulo,
                                    nom_articulo,
                                    ConfiguracionEmpresa.Moneda_Trabajo,
                                    precio_unitario.toString(),
                                    ncantidad.toString(),
                                    cunidad,
                                    descuento_1.toString(),
                                    descuento_2.toString(),
                                    descuento_3.toString(),
                                    descuento_4.toString(),
                                    CodigosGenerales.RedondearDecimales(BaseImponible),
                                    CodigosGenerales.RedondearDecimales(BaseCalculada),
                                    CodigosGenerales.RedondearDecimales(MontoIGV),
                                    CodigosGenerales.RedondearDecimales(ImporteTotal),
                                    CodigosGenerales.RedondearDecimales(MontoADescontar)

                            ));
                }
            } catch (Exception e) {
                Log.d(TAG, "doInBackground " + e.getMessage());
            }
            return null;
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
        }
    }
}