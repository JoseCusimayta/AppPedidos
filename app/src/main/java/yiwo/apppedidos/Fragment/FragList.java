package yiwo.apppedidos.Fragment;


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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.InterfacesPerzonalidas.CustomAdapterCodNom;
import yiwo.apppedidos.InterfacesPerzonalidas.CustomDataModel;
import yiwo.apppedidos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragList extends Fragment {

    View view;
    String TAG = "FragList";
    EditText et_bucar;
    ListView lv_items;
    ProgressBar progressBar;
    AppBarLayout app_barLayout;

    ArrayList<CustomDataModel> dataModels;
    CustomAdapterCodNom adapter;
    ArrayList<List<String>> arrayList;


    public FragList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_list, container, false);
        lv_items = view.findViewById(R.id.lv_items);
        et_bucar = view.findViewById(R.id.et_buscar);
        progressBar = view.findViewById(R.id.progressBar);

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
                    Log.d(TAG, "et_buscar: " + e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        try {
            lv_items.setAdapter(null);
            app_barLayout = getActivity().findViewById(R.id.app_barLayout);
            app_barLayout.setVisibility(View.VISIBLE);
            BackGroundTask task1 = new BackGroundTask();
            task1.execute("");
        } catch (Exception e) {
            Log.d(TAG, "onCreateView: " + e.getMessage());
        }


        return view;
    }


    public class BackGroundTask extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            dataModels = new ArrayList<>();
            arrayList = new ArrayList<>();
            lv_items.setAdapter(null);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                dataModels = new ArrayList<>();
                arrayList = CodigosGenerales.getArrayList(et_bucar.getText().toString());
                for (int i = 0; i < arrayList.size(); i++) {
                    dataModels.add(new CustomDataModel(arrayList.get(i).get(0), arrayList.get(i).get(1), null, null, null, null, null));
                }
                CodigosGenerales.DataModelsList = dataModels;
            } catch (Exception e) {
                Log.d(TAG, "BackGroundTask: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                lv_items.setAdapter(null);
                progressBar.setVisibility(View.GONE);

                adapter = new CustomAdapterCodNom(CodigosGenerales.DataModelsList, getContext());
                lv_items.setAdapter(adapter);

                lv_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Fragment fragment;
                        CodigosGenerales.Filtro = false;
                        if (CodigosGenerales.TipoArray.equals("Articulos")) {
                            try {
                                if (Double.parseDouble(arrayList.get(position).get(3)) > 0) {
                                    CodigosGenerales.Nombre_Categoria = "";
                                    CodigosGenerales.Codigo_Articulo = arrayList.get(position).get(0);
                                    fragment = new FragDescripcion();
                                    CambiarFragment(fragment);
                                } else {
                                    Toast.makeText(getActivity(), "Cantidad insuficiente", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Log.d("FragList", "onItemClick " + e.getMessage());
                            }
                        } else {
                            CodigosGenerales.Nombre_Categoria = arrayList.get(position).get(1) + " - ";
                            CodigosGenerales.Codigo_Categoria = arrayList.get(position).get(0);
                            fragment = new FragArticulos();
                            CambiarFragment(fragment);
                        }
                    }
                });
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());

            }
            super.onPostExecute(s);
        }
    }

    public void CambiarFragment(Fragment fragment) {
        CodigosGenerales.hideSoftKeyboard(getActivity());
        assert getFragmentManager() != null;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        transaction.replace(R.id.frag_contenedor, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}