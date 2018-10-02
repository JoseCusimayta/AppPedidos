package yiwo.apppedidos.Fragment;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.AspectosGenerales.ConfiguracionEmpresa;
import yiwo.apppedidos.Control.BDPedidos;
import yiwo.apppedidos.InterfacesPerzonalidas.CustomAdapterNumerado;
import yiwo.apppedidos.InterfacesPerzonalidas.CustomDataModel;
import yiwo.apppedidos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LateralPedidos extends Fragment {

    ArrayList<List<String>> ListPedidos = new ArrayList<>();
    Activity activity;
    BDPedidos bdPedidos = new BDPedidos();

    Button b_carrito;
    EditText et_bucar;
    ArrayList<CustomDataModel> dataModels;
    CustomAdapterNumerado adapter;
    ListView listView;

    ProgressBar progressBar;
    AppBarLayout app_barLayout;


    public LateralPedidos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_lateral_pedidos, container, false);

        listView = view.findViewById(R.id.list);
        progressBar = view.findViewById(R.id.progressBar);
        activity = getActivity();
        app_barLayout = activity.findViewById(R.id.app_barLayout);
        b_carrito = activity.findViewById(R.id.b_carrito);
        et_bucar = view.findViewById(R.id.et_buscar);


        et_bucar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    BackGroundTask task = new BackGroundTask();
                    task.execute("");
                } catch (Exception e) {
                    Log.d("FragList", "et_buscar: " + e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        if (!b_carrito.isEnabled())
            b_carrito.setEnabled(true);

        try {
            if (app_barLayout.getVisibility() == View.GONE)
                app_barLayout.setVisibility(View.VISIBLE);

            BackGroundTask task = new BackGroundTask();
            task.execute("");
        } catch (Exception e) {
            Log.d("AsyncTaskPedidos", e.getMessage());
        }
        return view;
    }

    public void CambiarFragment(Fragment fragment) {
        CodigosGenerales.hideSoftKeyboard(getActivity());
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frag_contenedor, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public class BackGroundTask extends AsyncTask<String, String, String> {
        String Mensaje;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                dataModels = new ArrayList<>();
                ListPedidos = bdPedidos.getList(et_bucar.getText().toString());
                dataModels = new ArrayList<>();
                for (int i = 0; i < ListPedidos.size(); i++) {
                    dataModels.add(new CustomDataModel(
                            String.valueOf(i + 1),
                            null,
                            "Fecha del Pedido:\t" + ListPedidos.get(i).get(12)+"\n"+
                                    "Codigo de Pedido:\t" + ListPedidos.get(i).get(3)+"\n"+
                                    "Codigo de Motivo:\t" + ListPedidos.get(i).get(1) + "-" + ListPedidos.get(i).get(2) + "\n"+
                                    "Cliente:\t" + ListPedidos.get(i).get(4) + "-" + ListPedidos.get(i).get(5) + "\n"+
                                    "Forma de Pago:\t" + ListPedidos.get(i).get(7) + "-" + ListPedidos.get(i).get(8) + "\n"+
                                    "Importe Total:\t" + ListPedidos.get(i).get(9) + "\n"+
                                    "IGV:\t" + ListPedidos.get(i).get(10)  + "\n"+
                                    "Neto:\t" + ListPedidos.get(i).get(11)  + "\n"+
                                    "Estado:\t" + ListPedidos.get(i).get(13)  + "\n"+
                                    "Observación:\t" + ListPedidos.get(i).get(14) + "\n",
                            "2",
                            ListPedidos.get(i).get(3),
                            ListPedidos.get(i).get(1),
                            "1"
                    ));
                }
            } catch (Exception e) {
                Mensaje = "Error en las credenciales";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                progressBar.setVisibility(View.GONE);


                adapter = new CustomAdapterNumerado(dataModels, getContext());

                listView.setAdapter(adapter);


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        CustomDataModel dataModel = dataModels.get(position);
                        CodigosGenerales.Codigo_Pedido = dataModel.getListaPrecios();
                        ConfiguracionEmpresa.Codigo_Motivo = dataModel.getDni();
//                    CodigosGenerales.Cod_Articulo = ListPedidos.get(position).get(0);
                        Fragment fragment = new LateralPedidoDetalle();
                        CambiarFragment(fragment);
                        // Toast.makeText(getContext(), "Elemento seleccionado: " + position, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Log.d("LateralPedidos", "Async " + e.getMessage());
            }
            super.onPostExecute(s);
        }
    }
}