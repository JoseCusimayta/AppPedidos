package yiwo.apppedidos.Fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import yiwo.apppedidos.Data.BDClientes;
import yiwo.apppedidos.InterfacesPerzonalidas.CustomAdapterCodNom;
import yiwo.apppedidos.InterfacesPerzonalidas.CustomDataModel;
import yiwo.apppedidos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LateralClientes extends Fragment {

    EditText et_bucar;
    ListView lv_items;
    ProgressBar progressBar;
    AppBarLayout app_barLayout;

    ArrayList<CustomDataModel> dataModels;
    CustomAdapterCodNom adapter;
    ArrayList<List<String>> arrayList;
    BDClientes bdClientes = new BDClientes();

    public LateralClientes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_lateral_clientes, container, false);
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
//                arrayList = CodigosGenerales.getArrayList(et_bucar.getText().toString());
                arrayList = bdClientes.getList(et_bucar.getText().toString());
                for (int i = 0; i < arrayList.size(); i++) {
                    dataModels.add(new CustomDataModel(arrayList.get(i).get(0), arrayList.get(i).get(1), null, null, null, null, null));
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

                adapter = new CustomAdapterCodNom(dataModels, getContext());

                lv_items.setAdapter(adapter);
            } catch (Exception e) {
                Log.d("FragList", "BackGroundTask: " + e.getMessage());
            }
            super.onPostExecute(s);
        }
    }
}