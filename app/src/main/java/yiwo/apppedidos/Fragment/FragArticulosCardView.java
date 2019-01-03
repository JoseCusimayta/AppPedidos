package yiwo.apppedidos.Fragment;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
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
import yiwo.apppedidos.AspectosGenerales.ConfiguracionEmpresa;
import yiwo.apppedidos.Control.BDArticulos;
import yiwo.apppedidos.Control.BDConceptos;
import yiwo.apppedidos.Control.BDFamilia;
import yiwo.apppedidos.Control.BDSubfamilia;
import yiwo.apppedidos.Data.DataArticulos;
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
    Button b_filtro;
    TextView tv_origenFiltro;
    AppBarLayout app_barLayout;
    DataArticulos dataArticulos = new DataArticulos();

    Boolean isFilter = false;
    String CriterioFiltro = "";

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

        isFilter = false;

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        articulosList = new ArrayList<>();
        adapter = new ArticulosAdapter(getActivity(), articulosList, getFragmentManager(), getActivity());

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        try {
            app_barLayout = getActivity().findViewById(R.id.app_barLayout);
            tv_origenFiltro = getActivity().findViewById(R.id.tv_origenFiltro);
            toolbar = getActivity().findViewById(R.id.toolbar);
            app_barLayout.setVisibility(View.VISIBLE);
            if (task != null)
                task.cancel(true);
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
                    if (task != null)
                        task.cancel(true);
                    task = new BackGroundTask();
                    task.execute("");

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
                    if (task != null)
                        task.cancel(true);
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
                if (task != null)
                    task.cancel(true);
                isFilter = true;
                Filtros();
                b_filtro.setEnabled(false);
                CodigosGenerales.hideSoftKeyboard(getActivity());
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        if (task != null)
            task.cancel(true);
        isFilter = false;
        super.onPause();
    }

    //region Tarea en Segundo Plano con ListView
    public class BackGroundTask extends AsyncTask<String, String, String> {

        ArrayList<List<String>> arrayList;

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "BackGroundTask iniciando");
            progressBar.setVisibility(View.VISIBLE);
            articulosList.clear();
            adapter.notifyDataSetChanged();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            Log.d(TAG, "BackGroundTask ejecutando");
            if (isFilter)
                arrayList = BuscarConFiltros();
            else
                arrayList = BuscarSinFiltros();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d(TAG, "BackGroundTask terminando");
            try {
                progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
                if (adapter.getItemCount() < 1) {
                    Snackbar.make(view, "No se encontraron los datos", Snackbar.LENGTH_LONG).setAction("No action", null).show();
                }
            } catch (Exception e) {
                Log.d(TAG, "onPostExecute: " + e.getMessage());
            }

            super.onPostExecute(s);
        }

        @Override
        protected void onCancelled() {

            Log.d(TAG, "BackGroundTask Cancelado");
            super.onCancelled();
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

    public ArrayList<List<String>> BuscarSinFiltros() {


        ArrayList<List<String>> arrayList = new ArrayList<>();
        try {
            if (task.isCancelled())
                return arrayList;
            arrayList = dataArticulos.getList(et_buscar.getText().toString());
            articulosList.clear();
            Log.d(TAG, "ArticulosCardView " + arrayList.get(0).size());
            for (int i = 0; i < arrayList.size(); i++) {
                if (task.isCancelled())
                    break;
                if (arrayList.get(i).size() > 7) {

                    Double precio_productos = CodigosGenerales.tryParseDouble(arrayList.get(i).get(4));
                    Double porcentaje_igv = CodigosGenerales.tryParseDouble(arrayList.get(i).get(6));
                    String moneda = arrayList.get(i).get(5);

//                    Log.d(TAG, "Nombre:" + arrayList.get(i).get(1));
//                    Log.d(TAG, "Stock:" + arrayList.get(i).get(2));
                    Articulos a = new Articulos(
                            //region registrar los Datos del producto A
                            arrayList.get(i).get(0),   // codigo
                            arrayList.get(i).get(1),   // Nombre
                            arrayList.get(i).get(2),   // stock
                            arrayList.get(i).get(3),   // cunidad
                            CodigosGenerales.RedondearDecimalesFormateado(precio_productos),   // precio
                            ConfiguracionEmpresa.Moneda_Empresa,   // moneda
                            CodigosGenerales.RedondearDecimales(porcentaje_igv)   // nigv

                    );
                    articulosList.add(a);
                }

            }
        } catch (Exception e) {
            Log.d(TAG, "BuscarSinFiltros: " + e.getMessage());
        }
        return arrayList;
    }


    public ArrayList<List<String>> BuscarConFiltros() {
        ArrayList<List<String>> arrayList = new ArrayList<>();
        try {
            if (task.isCancelled())
                return arrayList;
            arrayList = dataArticulos.getArticulosFiltrados(et_buscar.getText().toString(), CriterioFiltro);
            articulosList.clear();
          //  Log.d(TAG, "ArticulosCardView " + arrayList.get(0).size());
//                if(arrayList.get(0).size()>5)
            for (int i = 0; i < arrayList.size(); i++) {
                if (task.isCancelled())
                    break;
                Double cantidad_productos = CodigosGenerales.tryParseDouble(arrayList.get(i).get(2));
                Double precio_productos = CodigosGenerales.tryParseDouble(arrayList.get(i).get(4));
                Double porcentaje_igv = CodigosGenerales.tryParseDouble(arrayList.get(i).get(6));
//                    Log.d(TAG,"Nombre:"+arrayList.get(i).get(1));
                Articulos a = new Articulos(
                        //region registrar los Datos del producto A
                        arrayList.get(i).get(0),   // codigo
                        arrayList.get(i).get(1),   // Nombre
                        CodigosGenerales.RedondearDecimales(cantidad_productos),   // stock
                        arrayList.get(i).get(3),   // cunidad
                        CodigosGenerales.RedondearDecimales(precio_productos),   // precio
                        arrayList.get(i).get(5),   // moneda
                        CodigosGenerales.RedondearDecimales(porcentaje_igv)   // nigv

                );
                articulosList.add(a);
            }
        } catch (Exception e) {
            Log.d(TAG, "doInBackground: " + e.getMessage());
        }
        return arrayList;
    }


    //region preparar datos para Filtrar
    public void Filtros() {

        try {

            listDataHeader = dataArticulos.getTituloCategoria();
            listDataChild = dataArticulos.getNombresCategorias(listDataHeader);
            Log.d(TAG, "listDataChild: " + listDataChild.get(listDataHeader.get(0)));
            Log.d(TAG, "listDataChild: " + listDataChild.get(listDataHeader.get(0)).get(0));

            toolbar.setVisibility(View.GONE);
            layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            popupView = layoutInflater.inflate((R.layout.popup_filtros), null);
            popupWindow = new PopupWindow(popupView, RadioGroup.LayoutParams.MATCH_PARENT,
                    RadioGroup.LayoutParams.MATCH_PARENT);
            // popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            expListView = popupView.findViewById(R.id.lvExp);


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

                    ArrayList<List<Integer>> CheckedList = listAdapterCheckbox.getCheckedList();
                    toolbar.setVisibility(View.VISIBLE);
                    et_buscar.setText("");
                    try {
                        String cfamilia = " cnom_familia in ( ";
                        String csubfamilia = " cnom_subfamilia in ( ";
                        String concepto1 = " erp_concepto1.erp_nomcon in ( ";
                        String concepto2 = " erp_concepto2.erp_nomcon in ( ";
                        String concepto3 = " erp_concepto3.erp_nomcon in ( ";
                        String concepto4 = " erp_concepto4.erp_nomcon in ( ";
                        String concepto5 = " erp_concepto5.erp_nomcon in ( ";
                        String concepto6 = " erp_concepto6.erp_nomcon in ( ";
                        String concepto7 = " erp_concepto7.erp_nomcon in ( ";
                        Log.d(TAG, "CheckedList " + CheckedList);
                        Log.d(TAG, "CheckedList " + CheckedList.get(0).get(0));

                        Log.d(TAG, "listDataChild: " + listDataChild.get(listDataHeader.get(CheckedList.get(0).get(0))));
                        Log.d(TAG, "listDataChild: " + listDataChild.get(listDataHeader.get(CheckedList.get(0).get(0))).get(CheckedList.get(0).get(1)));

                        for (int i = 0; i < CheckedList.size(); i++) {
                            if (CheckedList.get(i).get(0) == 0) {
                                cfamilia += "'" + listDataChild.get(listDataHeader.get(CheckedList.get(i).get(0))).get(CheckedList.get(i).get(1)) + "', ";
                            }
                            if (CheckedList.get(i).get(0) == 1) {
                                csubfamilia += "'" + listDataChild.get(listDataHeader.get(CheckedList.get(i).get(0))).get(CheckedList.get(i).get(1)) + "', ";
                            }
                        }

                        if (cfamilia.equals(" cnom_familia in ( "))
                            cfamilia = "";
                        else
                            cfamilia = getQueryCorregido(cfamilia);

                        if (csubfamilia.equals(" cnom_subfamilia in ( "))
                            csubfamilia = "";
                        else
                            csubfamilia = getQueryCorregido(csubfamilia);

                        if (concepto1.equals(" erp_concepto1.erp_nomcon in ( "))
                            concepto1 = "";
                        else
                            concepto1 = getQueryCorregido(concepto1);

                        if (concepto2.equals(" erp_concepto2.erp_nomcon in ( "))
                            concepto2 = "";
                        else
                            concepto2 = getQueryCorregido(concepto2);

                        if (concepto3.equals(" erp_concepto3.erp_nomcon in ( "))
                            concepto3 = "";
                        else
                            concepto3 = getQueryCorregido(concepto3);

                        if (concepto4.equals(" erp_concepto4.erp_nomcon in ( "))
                            concepto4 = "";
                        else
                            concepto4 = getQueryCorregido(concepto4);

                        if (concepto5.equals(" erp_concepto5.erp_nomcon in ( "))
                            concepto5 = "";
                        else
                            concepto5 = getQueryCorregido(concepto5);

                        if (concepto6.equals(" erp_concepto6.erp_nomcon in ( "))
                            concepto6 = "";
                        else
                            concepto6 = getQueryCorregido(concepto6);

                        if (concepto7.equals(" erp_concepto7.erp_nomcon in ( "))
                            concepto7 = "";
                        else
                            concepto7 = getQueryCorregido(concepto7);

                        CriterioFiltro = cfamilia + csubfamilia + concepto1 + concepto2 + concepto3 + concepto4 + concepto5 + concepto6 + concepto7;
                        Log.d(TAG, "CriterioFiltro: " + CriterioFiltro);
                        task = new BackGroundTask();
                        task.execute("");
                    } catch (Exception e) {
                        Log.d(TAG, "Filtros: " + e.getMessage());
                    }
                    popupWindow.dismiss();
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

    //endregion

    public String getQueryCorregido(String query) {
        query = query.substring(0, query.length() - 2);
        query += " )";
        return query;
    }
}