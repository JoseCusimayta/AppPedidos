package yiwo.apppedidos.InterfacesPerzonalidas;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import yiwo.apppedidos.Data.DataEmpresa;
import yiwo.apppedidos.R;

public class EmpresaDialog {

    private ProgressBar progressBar;

    private ArrayList<Empresa> dataModels;
    private ListView listView;
    private EmpresaAdapter adapter;

    private Activity context;
    private Dialog dialogo;
    private EditText et_buscar;
    private DataEmpresa dataEmpresa = new DataEmpresa();

    private String TAG = "EmpresaDialog", Nombre = "";

    private BackGroundTask task;

    public interface FinalizarEmpresaDialog {
        void ResultadoEmpresaDialog(
                String cod
                , String name
                , String ruc);

    }

    private EmpresaDialog.FinalizarEmpresaDialog interfaz;


    public EmpresaDialog(Activity context, EmpresaDialog.FinalizarEmpresaDialog actividad) {

        this.context = context;
        dialogo = new Dialog(context);
        dialogo.setContentView(R.layout.custom_dialog);
        dialogo.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        interfaz = actividad;
        listView = dialogo.findViewById(R.id.list);
        et_buscar = dialogo.findViewById(R.id.et_buscar);
        progressBar = dialogo.findViewById(R.id.progressBar);


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
                    task = new BackGroundTask();
                    task.execute("");
                } catch (Exception e) {
                    Log.d(TAG, "onTextChanged " + e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        task = new BackGroundTask();
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

            ArrayList<List<String>> arrayListDialog = dataEmpresa.getList(Nombre);

            for (int i = 0; i < arrayListDialog.size(); i++) {
                if (isCancelled())
                    break;
                dataModels.add(new Empresa(
                        arrayListDialog.get(i).get(0),
                        arrayListDialog.get(i).get(1),
                        arrayListDialog.get(i).get(2)
                ));
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);
            if (!isCancelled()) {
                try {
                    listView.setAdapter(null);
                    adapter = new EmpresaAdapter(dataModels, context);

                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            CodigosGenerales.hideSoftKeyboard(context);
                            Empresa dataModel = dataModels.get(position);
                            interfaz.ResultadoEmpresaDialog(
                                    dataModel.getCodigo_Empresa(),
                                    dataModel.getNombre_Empresa(),
                                    dataModel.getRUC_Empresa()
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