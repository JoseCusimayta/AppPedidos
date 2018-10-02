package yiwo.apppedidos.InterfacesPerzonalidas;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.Control.BDCentroCostos;
import yiwo.apppedidos.Control.BDUnidNegocios;
import yiwo.apppedidos.R;

public class CodNomDialog {

    private ProgressBar progressBar;

    private ArrayList<CodNom> dataModels;
    private ListView listView;
    private CodNomAdapter adapter;

    private Activity context;
    private Dialog dialogo;
    private EditText et_buscar;

    private String TAG = "CodNomDialog", Nombre = "";

    private CodNomDialog.BackGroundTask task;

    String TipoArray;
    public interface FinalizarCodNomDialog {
        void ResultadoCodNomDialog(String cod,String name);

    }

    private CodNomDialog.FinalizarCodNomDialog interfaz;


    public CodNomDialog(Activity context, CodNomDialog.FinalizarCodNomDialog actividad, String TipoArray) {

        this.context = context;
        dialogo = new Dialog(context);
        dialogo.setContentView(R.layout.custom_dialog);
        dialogo.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        interfaz = actividad;
        listView = dialogo.findViewById(R.id.list);
        et_buscar = dialogo.findViewById(R.id.et_buscar);
        progressBar = dialogo.findViewById(R.id.progressBar);
        this.TipoArray=TipoArray;

        et_buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    Nombre = et_buscar.getText().toString();
                    task.cancel(true);
                    task.onPostExecute("");
                    task = new CodNomDialog.BackGroundTask();
                    task.execute("");
                } catch (Exception e) {
                    Log.d(TAG, "onTextChanged " + e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        task = new CodNomDialog.BackGroundTask();
        task.execute("");
        dialogo.show();
    }


    public class BackGroundTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            if (isCancelled())
                onPostExecute("");

            dataModels = new ArrayList<>();

            ArrayList<List<String>> arrayListDialog= new ArrayList<>();
            switch (TipoArray){
                case "CentroCostos":
                    BDCentroCostos bdCentroCostos= new BDCentroCostos();
                    arrayListDialog=bdCentroCostos.getList(Nombre);
                    break;
                case "UnidadNegocio":
                    BDUnidNegocios bdUnidNegocios= new BDUnidNegocios();
                    arrayListDialog=bdUnidNegocios.getList(Nombre);
                    break;
            }

            if(arrayListDialog!=null) {
                for (int i = 0; i < arrayListDialog.size(); i++) {
                    if (isCancelled())
                        break;
                    dataModels.add(new CodNom(
                            arrayListDialog.get(i).get(0),
                            arrayListDialog.get(i).get(1)
                    ));
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);
            if (!isCancelled()) {
                try {
                    listView.setAdapter(null);
                    adapter = new CodNomAdapter(dataModels, context);

                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            CodigosGenerales.hideSoftKeyboard(context);
                            CodNom dataModel = dataModels.get(position);
                            interfaz.ResultadoCodNomDialog(
                                    dataModel.getCodigo(),
                                    dataModel.getNombre()
                            );
                            dialogo.dismiss();
                        }
                    });
                } catch (Exception e) {
                    Log.d(TAG, "onPostExecute "+e.getMessage());
                }
            }
            super.onPostExecute(s);
        }
    }
}
