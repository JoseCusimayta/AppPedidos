package yiwo.apppedidos.Fragment;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.Data.BDConceptos;
import yiwo.apppedidos.Data.BDFamilia;
import yiwo.apppedidos.Data.BDSubfamilia;
import yiwo.apppedidos.InterfacesPerzonalidas.CustomAdapterArticulos;
import yiwo.apppedidos.InterfacesPerzonalidas.CustomDataModelArticulos;
import yiwo.apppedidos.InterfacesPerzonalidas.ExpandableListAdapter;
import yiwo.apppedidos.InterfacesPerzonalidas.ExpListViewAdapterWithCheckbox;
import yiwo.apppedidos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragArticulos extends Fragment {

    //region Declaración de variables a usar
    String TAG = "FragArticulos";
    View view;
    ProgressBar progressBar;
    Button b_filtro;
    EditText et_buscar;
    Toolbar toolbar;
    TextView tv_origenFiltro;
    AppBarLayout app_barLayout;

    ListView listView;


    ArrayList<CustomDataModelArticulos> dataModels;
    CustomAdapterArticulos adapter;
    ArrayList<List<String>> arrayList;


    LayoutInflater layoutInflater;
    View popupView;
    PopupWindow popupWindow;
    ExpandableListAdapter listAdapter;
    ExpListViewAdapterWithCheckbox listAdapterCheckbox;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    Button b_borrar, b_listo;
    BDFamilia bdFamilia = new BDFamilia();
    BDSubfamilia bdSubFamilia = new BDSubfamilia();
    BDConceptos bdConcepto = new BDConceptos();


    //endregion
    public FragArticulos() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_articulos, container, false);


        b_filtro = view.findViewById(R.id.b_filtro);
        et_buscar = view.findViewById(R.id.et_buscar);
        progressBar = view.findViewById(R.id.progressBar);
        listView = view.findViewById(R.id.list);
//        recyclerView = view.findViewById(R.id.recyclerView);


        et_buscar.addTextChangedListener(new TextWatcher() {
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
        b_filtro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Filtros();
                b_filtro.setEnabled(false);
            }
        });
        try {
            app_barLayout = getActivity().findViewById(R.id.app_barLayout);
            tv_origenFiltro = getActivity().findViewById(R.id.tv_origenFiltro);
            toolbar = getActivity().findViewById(R.id.toolbar);
            app_barLayout.setVisibility(View.VISIBLE);

            BackGroundTask task = new BackGroundTask();
            task.execute("");
        } catch (Exception e) {
            Log.d(TAG, "onCreateView: " + e.getMessage());
        }

        return view;
    }


    //region Tarea en Segundo Plano con ListView
    public class BackGroundTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            listView.setAdapter(null);
            progressBar.setVisibility(View.VISIBLE);
            dataModels = new ArrayList<>();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {

                dataModels = new ArrayList<>();
                arrayList = CodigosGenerales.getArrayListArticulos(et_buscar.getText().toString());
                for (int i = 0; i < arrayList.size(); i = i + 2) {
                    List<String> ListaA = new ArrayList(), ListaB = new ArrayList();
                    String CodigoA, CodigoB = null;

                    CodigoA = arrayList.get(i).get(0);//codigo
                    ListaA.add(arrayList.get(i).get(1));//Nombre
                    ListaA.add(arrayList.get(i).get(2));//Tipo de Unidad
                    ListaA.add(arrayList.get(i).get(3));//cantidad
                    ListaA.add(arrayList.get(i).get(4));//precio

                    if (i + 1 < arrayList.size()) {
                        CodigoB = arrayList.get(i + 1).get(0);//codigo
                        ListaB.add(arrayList.get(i + 1).get(1));//Nombre
                        ListaB.add(arrayList.get(i + 1).get(2));//Tipo de Unidad
                        ListaB.add(arrayList.get(i + 1).get(3));//cantidad
                        ListaB.add(arrayList.get(i + 1).get(4));//precio

                    } else {
                        ListaB.clear();
                        ListaB.add("");
                        ListaB.add("");
                        ListaB.add("");
                        ListaB.add("");
                        ListaB.add("");
                    }
                    dataModels.add(
                            new CustomDataModelArticulos(
                                    //region registrar los Datos del producto A
                                    CodigoA,//codA
                                    ListaA.get(1),//tipo de Unidad A
                                    ListaA.get(0),//nombreA
                                    ListaA.get(2),//cantidadA
                                    ListaA.get(3),//precioA
                                    //endregion

                                    //region registrar los Datos del producto A
                                    CodigoB,//codB
                                    ListaB.get(1),//tipo de Unidad B
                                    ListaB.get(0),//nombreB
                                    ListaB.get(2),//cantidadB
                                    ListaB.get(3)//precioB
                                    //endregion
                            ));


                }
            } catch (Exception e) {
                Log.d(TAG, "doInBackground: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                listView.setAdapter(null);

                progressBar.setVisibility(View.GONE);
                adapter = new CustomAdapterArticulos(dataModels, getContext(), getActivity(), getFragmentManager());
                if (adapter.getCount() < 1) {
                    Snackbar.make(view, "No se encontraron los datos", Snackbar.LENGTH_LONG).setAction("No action", null).show();
//                    Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                }
                listView.setAdapter(adapter);
            } catch (Exception e) {
                Log.d(TAG, "onPostExecute: " + e.getMessage());
            }

            super.onPostExecute(s);
        }
    }

    //endregion

    //region preparar datos para Filtrar
    public void Filtros() {

        try {
            toolbar.setVisibility(View.GONE);
            layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            popupView = layoutInflater.inflate((R.layout.popup_filtros), null);
            popupWindow = new PopupWindow(popupView, RadioGroup.LayoutParams.MATCH_PARENT,
                    RadioGroup.LayoutParams.MATCH_PARENT);
            // popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            expListView = popupView.findViewById(R.id.lvExp);
            prepareListData();
            listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);
            listAdapterCheckbox = new ExpListViewAdapterWithCheckbox(getContext(), listDataHeader, listDataChild);
            b_borrar = popupView.findViewById(R.id.b_borrar);
            b_listo = popupView.findViewById(R.id.b_listo);
            expListView.setAdapter(listAdapterCheckbox);
            b_borrar.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listAdapterCheckbox.ClearAllCheckBoxes();
                    for (int i = 0; i < listAdapterCheckbox.getGroupCount(); i++) {
                        expListView.collapseGroup(i);
                    }
                }
            });
            b_listo.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Boolean Filtrar = false;
                    ArrayList<List<Integer>> CheckedList = listAdapterCheckbox.getCheckedList();
                    try {
                        ArrayList<List<String>> Familia = bdFamilia.getList("");
                        ArrayList<List<String>> SubFamilia = bdSubFamilia.getList("");
                        CodigosGenerales.ID_Concepto = 1;
                        ArrayList<List<String>> Concepto1 = bdConcepto.getList("");
                        CodigosGenerales.ID_Concepto = 2;
                        ArrayList<List<String>> Concepto2 = bdConcepto.getList("");
                        CodigosGenerales.ID_Concepto = 3;
                        ArrayList<List<String>> Concepto3 = bdConcepto.getList("");
                        CodigosGenerales.ID_Concepto = 4;
                        ArrayList<List<String>> Concepto4 = bdConcepto.getList("");
                        CodigosGenerales.ID_Concepto = 5;
                        ArrayList<List<String>> Concepto5 = bdConcepto.getList("");
                        CodigosGenerales.ID_Concepto = 6;
                        ArrayList<List<String>> Concepto6 = bdConcepto.getList("");
                        CodigosGenerales.ID_Concepto = 7;
                        ArrayList<List<String>> Concepto7 = bdConcepto.getList("");
                        CodigosGenerales.list_familia.clear();
                        CodigosGenerales.list_subFamilia.clear();
                        CodigosGenerales.list_concepto1.clear();
                        CodigosGenerales.list_concepto2.clear();
                        CodigosGenerales.list_concepto3.clear();
                        CodigosGenerales.list_concepto4.clear();
                        CodigosGenerales.list_concepto5.clear();
                        CodigosGenerales.list_concepto6.clear();
                        CodigosGenerales.list_concepto7.clear();

                        for (int i = 0; i < CheckedList.size(); i++) {
                            Filtrar = true;
                            if (CheckedList.get(i).get(0) == 0) {
                                CodigosGenerales.list_familia.add(Familia.get(CheckedList.get(i).get(1)).get(0));
                            }
                            if (CheckedList.get(i).get(0) == 1) {
                                CodigosGenerales.list_subFamilia.add(SubFamilia.get(CheckedList.get(i).get(1)).get(0));
                            }
                            if (CheckedList.get(i).get(0) == 2) {
                                CodigosGenerales.list_concepto1.add(Concepto1.get(CheckedList.get(i).get(1)).get(0));
                            }
                            if (CheckedList.get(i).get(0) == 3) {
                                CodigosGenerales.list_concepto2.add(Concepto2.get(CheckedList.get(i).get(1)).get(0));
                            }
                            if (CheckedList.get(i).get(0) == 4) {
                                CodigosGenerales.list_concepto3.add(Concepto3.get(CheckedList.get(i).get(1)).get(0));
                            }
                            if (CheckedList.get(i).get(0) == 5) {
                                CodigosGenerales.list_concepto4.add(Concepto4.get(CheckedList.get(i).get(1)).get(0));
                            }
                            if (CheckedList.get(i).get(0) == 6) {
                                CodigosGenerales.list_concepto5.add(Concepto5.get(CheckedList.get(i).get(1)).get(0));
                            }
                            if (CheckedList.get(i).get(0) == 7) {
                                CodigosGenerales.list_concepto6.add(Concepto6.get(CheckedList.get(i).get(1)).get(0));
                            }
                            if (CheckedList.get(i).get(0) == 8) {
                                CodigosGenerales.list_concepto7.add(Concepto7.get(CheckedList.get(i).get(1)).get(0));
                            }
                        }
                        if (Filtrar) {
                            CodigosGenerales.Filtro = true;
                            toolbar.setVisibility(View.VISIBLE);
                            et_buscar.setText("");
                            try {
                                BackGroundTask task = new BackGroundTask();
                                task.execute("");
                            } catch (Exception e) {
                                Log.d(TAG, "Filtros: " + e.getMessage());
                            }
                            popupWindow.dismiss();
                        } else {
                            Log.d(TAG, "b_listo " + CheckedList + "");
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "b_listo " + e.getMessage());
                    }

                }
            });
            popupWindow.showAsDropDown(tv_origenFiltro, 0, 0);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    toolbar.setVisibility(View.VISIBLE);
                    b_filtro.setEnabled(true);
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "getSupportActionBar " + e.getMessage());
        }

    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding Header data
        listDataHeader.add("Familia");
        listDataHeader.add("Sub Familia");
        ArrayList<List<String>> Conceptos = bdConcepto.getNombresConceptos();
        Integer conceptos = Conceptos.size();
        for (int i = 0; i < conceptos; i++) {
            listDataHeader.add(Conceptos.get(i).get(1));
        }
        List<String> list_familia = new ArrayList<>();
        List<String> list_subFamilia = new ArrayList<>();
        List<String> list_concepto1 = new ArrayList<>();
        List<String> list_concepto2 = new ArrayList<>();
        List<String> list_concepto3 = new ArrayList<>();
        List<String> list_concepto4 = new ArrayList<>();
        List<String> list_concepto5 = new ArrayList<>();
        List<String> list_concepto6 = new ArrayList<>();
        List<String> list_concepto7 = new ArrayList<>();
        // Adding child data
        try {
            list_familia = bdFamilia.getNombres();
            if (listDataHeader.size() > 0)
                listDataChild.put(listDataHeader.get(0), list_familia);

            list_subFamilia = bdSubFamilia.getNombres();
            if (listDataHeader.size() > 1)
                listDataChild.put(listDataHeader.get(1), list_subFamilia);

            CodigosGenerales.ID_Concepto = 1;
            list_concepto1 = bdConcepto.getNombres();
            if (listDataHeader.size() > 2)
                listDataChild.put(listDataHeader.get(2), list_concepto1);

            CodigosGenerales.ID_Concepto = 2;
            list_concepto2 = bdConcepto.getNombres();
            if (listDataHeader.size() > 3)
                listDataChild.put(listDataHeader.get(3), list_concepto2);

            CodigosGenerales.ID_Concepto = 3;
            list_concepto3 = bdConcepto.getNombres();
            if (listDataHeader.size() > 4)
                listDataChild.put(listDataHeader.get(4), list_concepto3);

            CodigosGenerales.ID_Concepto = 4;
            list_concepto4 = bdConcepto.getNombres();
            if (listDataHeader.size() > 5)
                listDataChild.put(listDataHeader.get(5), list_concepto4);

            CodigosGenerales.ID_Concepto = 5;
            list_concepto5 = bdConcepto.getNombres();
            if (listDataHeader.size() > 6)
                listDataChild.put(listDataHeader.get(6), list_concepto5);

            CodigosGenerales.ID_Concepto = 6;
            list_concepto6 = bdConcepto.getNombres();
            if (listDataHeader.size() > 7)
                listDataChild.put(listDataHeader.get(7), list_concepto6);

            CodigosGenerales.ID_Concepto = 7;
            list_concepto7 = bdConcepto.getNombres();
            if (listDataHeader.size() > 8)
                listDataChild.put(listDataHeader.get(8), list_concepto7);

        } catch (Exception e) {
            Log.d(TAG, "Concentps " + e.getMessage());
        }
    }
    //endregion

}