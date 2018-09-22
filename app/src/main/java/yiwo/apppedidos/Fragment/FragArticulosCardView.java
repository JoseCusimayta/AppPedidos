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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.InterfacesPerzonalidas.Articulos;
import yiwo.apppedidos.InterfacesPerzonalidas.ArticulosAdapter;
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
                arrayList = CodigosGenerales.getArrayListArticulos(et_buscar.getText().toString());
                articulosList.clear();
                for (int i = 0; i < arrayList.size(); i = i + 2) {
                    if(isCancelled())
                        break;
                    Double cantidad_productos = CodigosGenerales.tryParseDouble(arrayList.get(i).get(2));
                    Double precio_productos = CodigosGenerales.tryParseDouble(arrayList.get(i).get(4));
//                    Log.d(TAG,"Nombre:"+arrayList.get(i).get(1));
                    Articulos a = new Articulos(
                            //region registrar los Datos del producto A
                            arrayList.get(i).get(0),   // codigo
                            arrayList.get(i).get(1),   // Nombre
                            CodigosGenerales.RedondearDecimales(cantidad_productos, 2),   // stock
                            arrayList.get(i).get(3),   // cunidad
                            CodigosGenerales.RedondearDecimales(precio_productos, 2),   // precio
                            arrayList.get(i).get(5)   // moneda
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
}