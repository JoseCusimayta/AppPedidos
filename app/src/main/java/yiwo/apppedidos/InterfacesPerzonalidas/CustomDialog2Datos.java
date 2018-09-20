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
import yiwo.apppedidos.R;

public class CustomDialog2Datos {

    ProgressBar progressBar;

    private EditText et_buscar;
    private ArrayList<CustomDataModel> dataModels;
    private ListView listView;
    private CustomAdapterCodNom adapter;

    private Activity context;
    private Dialog dialogo;

    private String Nombre;
    public interface FinalizoCuadroDialogo2Datos {
        void ResultadoCuadroDialogo2Datos(String cod
                , String name
                , String ruc
                , String direccion
                , String listaPrecios
                , String dni
                , String dato_invisible);

    }

    private CustomDialog2Datos.FinalizoCuadroDialogo2Datos interfaz;


    public CustomDialog2Datos(Activity context, CustomDialog2Datos.FinalizoCuadroDialogo2Datos actividad) {

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
                RellenarData(et_buscar.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        RellenarData("");
        dialogo.show();
    }

    public void RellenarData(String Nombre) {

        try {
            this.Nombre=Nombre;
            BackGroundTask task= new BackGroundTask();
            task.execute("");
        } catch (Exception e) {
            Log.d("CustomDialog2Datos", "RellenarData: " + e.getMessage());
        }
    }




    public class BackGroundTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            dataModels = new ArrayList<>();
            ArrayList<List<String>> arrayListDialog = CodigosGenerales.getArrayList(Nombre);
            switch (CodigosGenerales.CantidadDatosDialog) {
                case 3:
                    for (int i = 0; i < arrayListDialog.size(); i++) {
                        dataModels.add(new CustomDataModel(
                                arrayListDialog.get(i).get(0),
                                arrayListDialog.get(i).get(1),
                                arrayListDialog.get(i).get(2),
                                null,
                                null,
                                null,
                                null
                        ));
                    }
                    break;
                case 4:
                    for (int i = 0; i < arrayListDialog.size(); i++) {
                        dataModels.add(new CustomDataModel(
                                arrayListDialog.get(i).get(0),
                                arrayListDialog.get(i).get(1),
                                arrayListDialog.get(i).get(2),
                                arrayListDialog.get(i).get(3),
                                null,
                                null,
                                null
                        ));
                    }
                    break;
                case 6:
                    for (int i = 0; i < arrayListDialog.size(); i++) {
                        dataModels.add(new CustomDataModel(
                                arrayListDialog.get(i).get(0),
                                arrayListDialog.get(i).get(1),
                                arrayListDialog.get(i).get(2),
                                arrayListDialog.get(i).get(3),
                                arrayListDialog.get(i).get(4),
                                arrayListDialog.get(i).get(5),
                                null
                        ));
                    }
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);
            try{
                listView.setAdapter(null);
                adapter = new CustomAdapterCodNom(dataModels, context);


                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        CustomDataModel dataModel = dataModels.get(position);
                        interfaz.ResultadoCuadroDialogo2Datos(
                                dataModel.getCod(),
                                dataModel.getName(),
                                dataModel.getRuc(),
                                dataModel.getDireccion(),
                                dataModel.getListaPrecios(),
                                dataModel.getDni(),
                                dataModel.getDato_invisible());
                        dialogo.dismiss();
                    }
                });
            }catch (Exception e){
                Log.d("CustomDialog2Datos",e.getMessage());
            }
            super.onPostExecute(s);
        }
    }

}