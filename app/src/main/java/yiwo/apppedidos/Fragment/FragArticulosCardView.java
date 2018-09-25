package yiwo.apppedidos.Fragment;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.Data.BDArticulos;
import yiwo.apppedidos.Data.BDConceptos;
import yiwo.apppedidos.Data.BDFamilia;
import yiwo.apppedidos.Data.BDSubfamilia;
import yiwo.apppedidos.InterfacesPerzonalidas.Articulos;
import yiwo.apppedidos.InterfacesPerzonalidas.ArticulosAdapter;
import yiwo.apppedidos.InterfacesPerzonalidas.ExpListViewAdapterWithCheckbox;
import yiwo.apppedidos.InterfacesPerzonalidas.ExpandableListAdapter;
import yiwo.apppedidos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragArticulosCardView extends Fragment {


    private ArticulosAdapter adapter;
    private List<Articulos> articulosList;
    EditText et_buscar;
    View view;
    String TAG = "FragArticulosCardView";
    ProgressBar progressBar;
    BackGroundTask task;
    BDArticulos bdArticulos= new BDArticulos();

    //region Elementos para el filtro
    Toolbar toolbar;
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
    Button b_filtro;
    TextView tv_origenFiltro;

    //endregion
    public FragArticulosCardView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_articulos_card_view, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        et_buscar = view.findViewById(R.id.et_buscar);
        b_filtro = view.findViewById(R.id.b_filtro);


        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        articulosList = new ArrayList<>();
        adapter = new ArticulosAdapter(getActivity(), articulosList, getFragmentManager(), getActivity());

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        try {
            task = new BackGroundTask();
            task.execute("");
        } catch (Exception e) {
            task.cancel(true);
            Log.d(TAG, "onCreateView " + e.getMessage());
        }

        et_buscar.setImeOptions(EditorInfo.IME_ACTION_DONE);

        et_buscar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    try {
                        task = new BackGroundTask();
                        task.execute("");
                    } catch (Exception e) {
                        task.cancel(true);
                        Log.d(TAG, "et_buscar: " + e.getMessage());
                    }
                    InputMethodManager imm = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        et_buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    task = new BackGroundTask();
                    task.execute("");
                } catch (Exception e) {
                    task.cancel(true);
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
//        prepareAlbums();
        return view;
    }

    //region Tarea en Segundo Plano con ListView
    public class BackGroundTask extends AsyncTask<String, String, String> {

        ArrayList<List<String>> arrayList;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
//            articulosList = new ArrayList<>();
            articulosList.clear();
            adapter.notifyDataSetChanged();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                arrayList = bdArticulos.getList(et_buscar.getText().toString());
                articulosList.clear();

                Log.d(TAG,"ArticulosCardView "+arrayList.get(0).size());
//                if(arrayList.get(0).size()>5)
                for (int i = 0; i < arrayList.size(); i = i + 2) {
                    if(isCancelled())
                        break;
                    Double cantidad_productos = CodigosGenerales.tryParseDouble(arrayList.get(i).get(2));
                    Double precio_productos = CodigosGenerales.tryParseDouble(arrayList.get(i).get(4));
                    Double porcentaje_igv = CodigosGenerales.tryParseDouble(arrayList.get(i).get(6));
//                    Log.d(TAG,"Nombre:"+arrayList.get(i).get(1));
                    Articulos a = new Articulos(
                            //region registrar los Datos del producto A
                            arrayList.get(i).get(0),   // codigo
                            arrayList.get(i).get(1),   // Nombre
                            CodigosGenerales.RedondearDecimales(cantidad_productos, 2),   // stock
                            arrayList.get(i).get(3),   // cunidad
                            CodigosGenerales.RedondearDecimales(precio_productos, 2),   // precio
                            arrayList.get(i).get(5),   // moneda
                            CodigosGenerales.RedondearDecimales(porcentaje_igv, 2)   // nigv

                    );
                    articulosList.add(a);
                }
            } catch (Exception e) {
                Log.d(TAG, "doInBackground: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                progressBar.setVisibility(View.GONE);

                adapter.notifyDataSetChanged();
                if (adapter.getItemCount() < 1) {
                    Snackbar.make(view, "No se encontraron los datos", Snackbar.LENGTH_LONG).setAction("No action", null).show();
//                    Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                }
//                recyclerView.setAdapter(adapter);
            } catch (Exception e) {
                Log.d(TAG, "onPostExecute: " + e.getMessage());
            }

            super.onPostExecute(s);
        }
    }

    //endregion

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onResume() {

        et_buscar.setText("");
        super.onResume();
    }









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