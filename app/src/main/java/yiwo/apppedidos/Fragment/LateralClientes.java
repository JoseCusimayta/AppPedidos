package yiwo.apppedidos.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.AspectosGenerales.DatosCliente;
import yiwo.apppedidos.Control.BDClientes;
import yiwo.apppedidos.Control.BDFormaPago;
import yiwo.apppedidos.InterfacesPerzonalidas.Clientes;
import yiwo.apppedidos.InterfacesPerzonalidas.ClientesAdapter;
import yiwo.apppedidos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LateralClientes extends Fragment {

    EditText et_bucar;
    ListView lv_items;
    ProgressBar progressBar;
    AppBarLayout app_barLayout;

    ArrayList<Clientes> dataModels;
    ClientesAdapter adapter;
    ArrayList<List<String>> arrayList;
    BDClientes bdClientes = new BDClientes();
    String TAG="LateralClientes";
    View view;
    public LateralClientes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_lateral_clientes, container, false);
        lv_items = view.findViewById(R.id.lv_items);
        et_bucar = view.findViewById(R.id.et_buscar);
        progressBar = view.findViewById(R.id.progressBar);

//        CodigosGenerales.Filtro=false;

        try {
            app_barLayout = getActivity().findViewById(R.id.app_barLayout);
            app_barLayout.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Log.d("FragList", "app_barLayout: " + e.getMessage());
        }

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


        BackGroundTask task1 = new BackGroundTask();
        task1.execute("");
        return view;
    }

    public class BackGroundTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            dataModels = new ArrayList<>();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                dataModels = new ArrayList<>();
                arrayList = bdClientes.getList(et_bucar.getText().toString());
                for (int i = 0; i < arrayList.size(); i++) {
                    dataModels.add(
                            new Clientes(
                                    arrayList.get(i).get(0),
                                    arrayList.get(i).get(1),
                                    arrayList.get(i).get(2),
                                    arrayList.get(i).get(3),
                                    arrayList.get(i).get(4),
                                    arrayList.get(i).get(5),
                                    arrayList.get(i).get(6),
                                    arrayList.get(i).get(7),
                                    arrayList.get(i).get(8),
                                    arrayList.get(i).get(9)
                            ));
                }
            } catch (Exception e) {
                Log.d("FragList", "BackGroundTask: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                progressBar.setVisibility(View.GONE);
                adapter = new ClientesAdapter(dataModels, getContext());
                lv_items.setAdapter(adapter);
                final View _view= view;


                lv_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                        final String Codigo = dataModels.get(i).getCodigo_Cliente();
                        adb.setTitle("¿Elegir?");
                        adb.setMessage("¿Está seguro de elegir el cliente " + dataModels.get(i).getNombre_Cliente() + "?");
                        final int positionToRemove = i;
                        adb.setNegativeButton("Cancel", null);
                        adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                DatosCliente.Nombre_Cliente = dataModels.get(positionToRemove).getNombre_Cliente();
                                DatosCliente.Direccion_Cliente = dataModels.get(positionToRemove).getDireccion_Cliente();
                                DatosCliente.Codigo_Cliente = dataModels.get(positionToRemove).getCodigo_Cliente();
                                DatosCliente.Codigo_ListaPrecios = dataModels.get(positionToRemove).getListaPrecios_Cliente();
                                DatosCliente.RUC_Cliente= dataModels.get(positionToRemove).getRuc_Cliente();
                                DatosCliente.DNI_Cliente= dataModels.get(positionToRemove).getDNI_Cliente();
                                DatosCliente.Codigo_FormaPago= dataModels.get(positionToRemove).getCodigo_FormaPago();
                                DatosCliente.Nombre_FormaPago= dataModels.get(positionToRemove).getNombre_FormaPago();
                                DatosCliente.Dias_FormaPago= CodigosGenerales.tryParseInteger(dataModels.get(positionToRemove).getDias_FormaPago());
                                DatosCliente.Codigo_Pais= dataModels.get(positionToRemove).getCodigo_Pais();

                                Log.d(TAG, "Nombre_Cliente " + DatosCliente.Nombre_Cliente+" ...");
                                Log.d(TAG, "Direccion_Cliente " + DatosCliente.Direccion_Cliente+" ...");
                                Log.d(TAG, "Codigo_Cliente " + DatosCliente.Codigo_Cliente+" ...");
                                Log.d(TAG, "Codigo_ListaPrecios " + DatosCliente.Codigo_ListaPrecios+" ...");
                                Log.d(TAG, "Nombre_Cliente " + DatosCliente.Nombre_Cliente+" ...");
                                Log.d(TAG, "RUC_Cliente " + DatosCliente.RUC_Cliente+" ...");
                                Log.d(TAG, "Codigo_FormaPago " + DatosCliente.Codigo_FormaPago+" ...");
                                Log.d(TAG, "Nombre_FormaPago " + DatosCliente.Nombre_FormaPago+" ...");
                                Log.d(TAG, "Dias_FormaPago " + DatosCliente.Dias_FormaPago+" ...");
                                Log.d(TAG, "Codigo_Pais " + DatosCliente.Codigo_Pais+" ...");
                                Snackbar.make(_view, DatosCliente.Nombre_Cliente + " ha sido elegido", Snackbar.LENGTH_LONG).setAction("No action", null).show();


                            }
                        });
                        adb.show();
                    }
                });

            } catch (Exception e) {
                Log.d("FragList", "BackGroundTask: " + e.getMessage());
            }
            super.onPostExecute(s);
        }
    }
}