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

import yiwo.apppedidos.Data.DataEmpresa;
import yiwo.apppedidos.Data.DataPuntoVenta;
import yiwo.apppedidos.R;

public class PuntoVentaDialog {

    private ProgressBar progressBar;

    private ArrayList<PuntoVenta> dataModels;
    private ListView listView;
    private PuntoVentaAdapter adapter;

    private Activity context;
    private Dialog dialogo;
    private EditText et_buscar;
    private DataPuntoVenta dataPuntoVenta = new DataPuntoVenta();

    private String TAG = "PuntoVentaDialog", Nombre = "";

    private PuntoVentaDialog.BackGroundTask task;

    public interface FinalizarPuntoVentaDialog {
        void ResultadoPuntoVentaDialog(
                String ccod_almacen,
                String cnom_almacen,
                String erp_codalmacen_ptovta,
                String cdireccion
        );

    }

    private PuntoVentaDialog.FinalizarPuntoVentaDialog interfaz;


    public PuntoVentaDialog(Activity context, PuntoVentaDialog.FinalizarPuntoVentaDialog actividad) {

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
                    task = new PuntoVentaDialog.BackGroundTask();
                    task.execute("");
                } catch (Exception e) {
                    Log.d(TAG, "onTextChanged " + e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        task = new PuntoVentaDialog.BackGroundTask();
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

            ArrayList<List<String>> arrayListDialog = dataPuntoVenta.getList(Nombre);

            for (int i = 0; i < arrayListDialog.size(); i++) {
                if (isCancelled())
                    break;
                dataModels.add(new PuntoVenta(
                        arrayListDialog.get(i).get(0),
                        arrayListDialog.get(i).get(1),
                        arrayListDialog.get(i).get(2),
                        arrayListDialog.get(i).get(3)
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
                    adapter = new PuntoVentaAdapter(dataModels, context);

                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            PuntoVenta dataModel = dataModels.get(position);
                            interfaz.ResultadoPuntoVentaDialog(
                                    dataModel.getCcod_almacen(),
                                    dataModel.getCnom_almacen(),
                                    dataModel.getErp_codalmacen_ptovta(),
                                    dataModel.getCdireccion()
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