package yiwo.apppedidos.Fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.AspectosGenerales.ConfiguracionEmpresa;
import yiwo.apppedidos.AspectosGenerales.DatosCliente;
import yiwo.apppedidos.AspectosGenerales.DatosUsuario;
import yiwo.apppedidos.Control.BDFormaPago;
import yiwo.apppedidos.Control.BDListDeseo;
import yiwo.apppedidos.Control.BDMotivo;
import yiwo.apppedidos.Control.BDPedidos;
import yiwo.apppedidos.Data.DataPedidos;
import yiwo.apppedidos.InterfacesPerzonalidas.ClientesDialog;
import yiwo.apppedidos.InterfacesPerzonalidas.CustomAdapterListaDeseos;
import yiwo.apppedidos.InterfacesPerzonalidas.CustomDataModelListaDeseos;
import yiwo.apppedidos.InterfacesPerzonalidas.CustomDialog2Datos;
import yiwo.apppedidos.InterfacesPerzonalidas.CustomDialogEnviarPedido;
import yiwo.apppedidos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragListDeseo extends Fragment implements View.OnClickListener, CustomDialog2Datos.FinalizoCuadroDialogo2Datos, CustomDialogEnviarPedido.FinalizoCuadroDialogPedido, ClientesDialog.FinalizarClientesDialog {

    //region Declaración de variables
    View view;

    LinearLayout ly_datosCliente;
    Activity activity;
    ProgressBar progressBar;
    Button b_enviar_pedido, b_carrito;
    EditText et_cod_cliente, et_Nombre, et_Moneda, et_formaPago, et_comentario;
    FloatingActionButton flb_datosCliente;
    Switch simpleSwitch;

    ArrayList<CustomDataModelListaDeseos> dataModels_listaDeseos;
    CustomAdapterListaDeseos adapter_listaDeseos;
    ListView lv_items;

    Boolean switchState;
    TextView tv_origen_popup;
    BDListDeseo bdListDeseo = new BDListDeseo();
    DataPedidos dataPedidos = new DataPedidos();
    BDFormaPago bdFormaPago = new BDFormaPago();

    String Moneda_ListaDeseo;
    String TAG="FragListDeseo";
    String Dialog_ID = ""; //Variable para saber que Dialog se ha activado y saber los valores que retornará
    Double Monto_SubTotal_Pedido=0.00, Monto_Descontado_Pedido=0.00, Monto_IGV_Pedido=0.00, Monto_Importe_Pedido=0.00;
    //endregion

    public FragListDeseo() {
        // Required empty public constructor
    }

    //region onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_list_deseo, container, false);

        //region Poniendo valores a las variables
        lv_items = view.findViewById(R.id.lv_items);
        ly_datosCliente = view.findViewById(R.id.ly_datosCliente);
        b_enviar_pedido = view.findViewById(R.id.b_enviar_pedido);
        et_cod_cliente = view.findViewById(R.id.et_cod_cliente);
        et_Nombre = view.findViewById(R.id.et_Nombre);
        et_Moneda = view.findViewById(R.id.et_Moneda);
        et_comentario = view.findViewById(R.id.et_comentario);
        flb_datosCliente = view.findViewById(R.id.flb_datosCliente);
        tv_origen_popup = view.findViewById(R.id.tv_origen_popup);
        et_formaPago = view.findViewById(R.id.et_formaPago);
        b_carrito = getActivity().findViewById(R.id.b_carrito);
        progressBar = view.findViewById(R.id.progressBar);
        simpleSwitch = view.findViewById(R.id.simpleSwitch);
        activity = getActivity();
        switchState = simpleSwitch.isChecked();
        CodigosGenerales.Precio_TotalPedido = 0;
        Moneda_ListaDeseo = ConfiguracionEmpresa.Moneda_Trabajo;
        et_Moneda.setText(Moneda_ListaDeseo);
        //endregion

        //region Habilitar acciones
        flb_datosCliente.setOnClickListener(this);
        b_enviar_pedido.setOnClickListener(this);
        et_cod_cliente.setOnClickListener(this);
        et_Moneda.setOnClickListener(this);
        et_formaPago.setOnClickListener(this);
        simpleSwitch.setOnClickListener(this);
        //endregion

        //region Deshabilitar acciones
        b_carrito.setEnabled(false);
        et_formaPago.setEnabled(false);
        //endregion

        try {

            //region Hacer Visible la barra de herramientas (ToolBar)
            getActivity().findViewById(R.id.app_barLayout).setVisibility(View.VISIBLE);
            //endregion

            //region Ejecutar Tarea en Segundo Plano para cargar el ListView
            BackGroundTask task1 = new BackGroundTask();
            task1.execute("");
            //endregion

            if(DatosCliente.Codigo_Cliente!=null){
                et_cod_cliente.setText(DatosCliente.Codigo_Cliente);
                et_Nombre.setText(DatosCliente.Nombre_Cliente);
            }
            if(DatosCliente.Codigo_FormaPago!=null){
                et_formaPago.setText(DatosCliente.Codigo_FormaPago);
            }
        } catch (Exception e) {
            Log.d("FragListDeseo", "onCreateView: " + e.getMessage());
        }

        return view;
    }
    //endregion


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.b_enviar_pedido):
//                try {

                    if(Validar()){
                        new CustomDialogEnviarPedido(getContext(), this, Monto_SubTotal_Pedido, Monto_Descontado_Pedido, Monto_IGV_Pedido, Monto_Importe_Pedido); //Mostrar el dialog de Enviar Pedido
                    }

//
//                    Log.d(TAG, DatosUsuario.Codigo_Almacen+"");
//
//                    if (ConfiguracionEmpresa.ValorTipoCambio > 0 &&  DatosCliente.RUC_Cliente != null) {
//                        CodigosGenerales.CantidadDatosDialog = 3;
//                        CodigosGenerales.TipoArray = Dialog_ID = "EnviarPedido";
//                        Log.d(TAG,"Monto_Importe_Pedido "+Monto_Importe_Pedido);
//                        new CustomDialogEnviarPedido(getContext(), this, Monto_SubTotal_Pedido, Monto_Descontado_Pedido, Monto_IGV_Pedido, Monto_Importe_Pedido); //Mostrar el dialog de Enviar Pedido
//                    } else {
//                        Snackbar.make(view, "No existe valor del tipo de cambio para la fecha actual", Snackbar.LENGTH_LONG).setAction("No action", null).show();
//                    }
//                } catch (Exception e) {
//                    Log.d("onClic", e.getMessage());
//                    Snackbar.make(view, "No hay tipo de cambio en el sistema", Snackbar.LENGTH_LONG).setAction("No action", null).show();
//                }
                break;
            case (R.id.et_cod_cliente):

                new ClientesDialog(getActivity(),this);

//
//                CodigosGenerales.CantidadDatosDialog = 6;
//                CodigosGenerales.TipoArray = Dialog_ID = "Cliente";
//                new CustomDialog2Datos(getActivity(), this);
                break;
            case (R.id.et_Moneda):
                CodigosGenerales.CantidadDatosDialog = 3;
                CodigosGenerales.TipoArray = Dialog_ID = "Moneda";
                new CustomDialog2Datos(getActivity(), this);
                break;
            case (R.id.et_formaPago):
                CodigosGenerales.CantidadDatosDialog = 3;
                CodigosGenerales.TipoArray = Dialog_ID = "FormaPago";
                new CustomDialog2Datos(getActivity(), this);
                break;
            case (R.id.flb_datosCliente):

                if (ly_datosCliente.getVisibility() == View.GONE)
                    ly_datosCliente.setVisibility(View.VISIBLE);
                else
                    ly_datosCliente.setVisibility(View.GONE);

                break;
            case (R.id.simpleSwitch):
                String statusSwitch1;
                String Mensaje;
                if (simpleSwitch.isChecked()) {
                    statusSwitch1 = simpleSwitch.getTextOn().toString();
                    Mensaje = "Lista de precios Soles ";
                } else {
                    statusSwitch1 = simpleSwitch.getTextOff().toString();
                    Mensaje = "Lista de precios Dolares ";
                }
                simpleSwitch.setText(Mensaje);
                Toast.makeText(getContext(), Mensaje + " activada", Toast.LENGTH_SHORT).show(); // display the current state for switch's
                break;
        }
    }

    private boolean Validar() {

        if(ConfiguracionEmpresa.ValorTipoCambio<=0) {
            Toast.makeText(getActivity(), "No hay tipo de cambio en el sistema", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(DatosCliente.Codigo_Cliente==null || DatosCliente.Codigo_Cliente.isEmpty()){
            Toast.makeText(getActivity(), "No se ha elegido un cliente", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void ResultadoCuadroDialogo2Datos(String cod, String name, String ruc, String direccion, String listaPrecios, String dni, String dato_invisible) {
        switch (Dialog_ID) {
            case "Cliente":
                et_cod_cliente.setText(cod);

                et_Nombre.setText(name);
                DatosCliente.RUC_Cliente = ruc;
                et_formaPago.setEnabled(true);
                DatosCliente.Nombre_Cliente = name;
                DatosCliente.Direccion_Cliente = direccion;
                DatosCliente.Codigo_Cliente = cod;
                DatosCliente.Codigo_ListaPrecios = listaPrecios;
                List<String> FormPago = bdFormaPago.getPredeterminado();
                if(FormPago!=null){
                    DatosCliente.Codigo_FormaPago= FormPago.get(0);
                    et_formaPago.setText(FormPago.get(1));
                    DatosCliente.Dias_FormaPago=CodigosGenerales.tryParseInteger(FormPago.get(0));
                }


                break;
            case "Moneda":
                /*try {
                    if (ConfiguracionEmpresa.Moneda_Trabajo.equals("S/")) {
                        if (cod.equals("S/ ")) {
                            TipCambio = 1.0;
                            Moneda_ListaDeseo = "S/ ";
                            BackGroundTask task = new BackGroundTask();
                            task.execute("");
                        } else {
                            TipCambio = ConfiguracionEmpresa.ValorTipoCambio;
                            TipCambio = Math.pow(TipCambio, -1);

                            Moneda_ListaDeseo = "$  ";
                            BackGroundTask task = new BackGroundTask();
                            task.execute("");
                        }
//                    Log.d("TipCambio2", TipCambio + "");
                    } else {
                        if (cod.equals("$  ")) {
                            TipCambio = 1.0;
                            Moneda_ListaDeseo = "$  ";

                            BackGroundTask task = new BackGroundTask();
                            task.execute("");
                        } else {
                            TipCambio = ConfiguracionEmpresa.ValorTipoCambio;
//                        TipCambio = Math.pow(TipCambio, -1);

                            Moneda_ListaDeseo = "S/ ";
                            BackGroundTask task = new BackGroundTask();
                            task.execute("");
                        }
                    }
                    Log.d("cod", cod);
                    Log.d("TipCambio", TipCambio + "");
                    et_Moneda.setText(cod);
                } catch (Exception e) {
                    Snackbar.make(view, "No hay tipo de cambio en el sistema", Snackbar.LENGTH_LONG).setAction("No action", null).show();
                }*/
                break;
            case "FormaPago":
                et_formaPago.setText(name);
                DatosCliente.Codigo_FormaPago = cod;
                try {
                    DatosCliente.Dias_FormaPago = Integer.parseInt(ruc);
                } catch (Exception e) {
                    DatosCliente.Dias_FormaPago = 0;
                }
                break;
        }
    }

    @Override
    public void ResultadoCuadroDialogPedido(String Codigo, String Nombre) {
        String FechaPago = CodigosGenerales.FormatoFechas.format(CodigosGenerales.sumarRestarDiasFecha(new Date(),  DatosCliente.Dias_FormaPago));
        Log.d(TAG, "ResultadoCuadroDialogPedido ValorCambio: "+ ConfiguracionEmpresa.ValorTipoCambio);
        Log.d(TAG,"ResultadoCuadroDialogPedido Monto_Importe_Pedido"+Monto_Importe_Pedido);
        if (dataPedidos.EnviarPedido(
                String.valueOf(Monto_Importe_Pedido),String.valueOf(Monto_Descontado_Pedido),String.valueOf(Monto_SubTotal_Pedido),
                String.valueOf(Monto_IGV_Pedido),"0.00",FechaPago,et_comentario.getText().toString())) {
            Toast.makeText(getContext(), "Se ha enviado el pedido", Toast.LENGTH_SHORT).show();
            lv_items.setAdapter(null);
            if (!b_carrito.isEnabled())
                b_carrito.setEnabled(true);
            FragmentManager fm = getActivity().getSupportFragmentManager();
            for (int i = 1; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
            FragMenuPrincipal frag = new FragMenuPrincipal();
            CambiarFragment(frag);
        } else
            Toast.makeText(getContext(), "No se ha podido enviar, vuelva a intentar", Toast.LENGTH_SHORT).show();
    }


    public void CambiarFragment(Fragment fragment) {
        CodigosGenerales.isInicio=false;

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frag_contenedor, fragment)
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void ResultadoClientesDialog(String Codigo_Cliente, String Nombre_Cliente,
                                        String Direccion_Cliente, String Ruc_Cliente,
                                        String DNI_Cliente, String ListaPrecios_Cliente,
                                        String Codigo_FormaPago, String Nombre_FormaPago,
                                        String Dias_FormaPago, String Codigo_Pais) {
        DatosCliente.Codigo_Cliente =Codigo_Cliente;
        DatosCliente.Nombre_Cliente = Nombre_Cliente;
        DatosCliente.Direccion_Cliente = Direccion_Cliente;
        DatosCliente.RUC_Cliente= Ruc_Cliente;
        DatosCliente.DNI_Cliente= DNI_Cliente;
        DatosCliente.Codigo_ListaPrecios = ListaPrecios_Cliente;
        DatosCliente.Codigo_FormaPago= Codigo_FormaPago;
        DatosCliente.Nombre_FormaPago= Nombre_FormaPago;
        DatosCliente.Dias_FormaPago= CodigosGenerales.tryParseInteger(Dias_FormaPago);
        DatosCliente.Codigo_Pais= Codigo_Pais;
        et_cod_cliente.setText(DatosCliente.Codigo_Cliente);
        et_Nombre.setText(DatosCliente.Nombre_Cliente);
    }

    public class BackGroundTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            Log.d(TAG,"INICIO...");
            progressBar.setVisibility(View.VISIBLE);
            dataModels_listaDeseos = new ArrayList<>();
            lv_items.setAdapter(null);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                CodigosGenerales.Codigos_Pedido.clear();

                ArrayList<List<String>> listaDeseos = bdListDeseo.getList();

                for (int i = 0; i < listaDeseos.size(); i++) {

                    String nitem = listaDeseos.get(i).get(0);
                    String ccod_articulo = listaDeseos.get(i).get(1);
                    String nom_articulo = listaDeseos.get(i).get(2);
                    String cunidad = listaDeseos.get(i).get(3);
                    Double ncantidad = Double.parseDouble(listaDeseos.get(i).get(4));
                    Double precio_unitario = Double.parseDouble(listaDeseos.get(i).get(5));
                    Double IGV_Articulo = Double.parseDouble(listaDeseos.get(i).get(6));
                    Double descuento_1 = Double.parseDouble(listaDeseos.get(i).get(7));
                    Double descuento_2 = Double.parseDouble(listaDeseos.get(i).get(8));
                    Double descuento_3 = Double.parseDouble(listaDeseos.get(i).get(9));
                    Double descuento_4 = Double.parseDouble(listaDeseos.get(i).get(10));
                    //String LP = listaDeseos.get(i).get(11);
                    Double BaseCalculada, BaseImponible, Descuento_Unico, MontoIGV, ImporteTotal, MontoADescontar;

                    Descuento_Unico = CodigosGenerales.getDescuenetoUnico(descuento_1, descuento_2, descuento_3, descuento_4);
                    BaseImponible = precio_unitario * ncantidad;

                    Log.d(TAG,"background "+ConfiguracionEmpresa.ifIGV);
                    if (ConfiguracionEmpresa.isIncluidoIGV) {
                        BaseCalculada=BaseImponible*(100-Descuento_Unico)/100;
                        MontoIGV = BaseCalculada * IGV_Articulo / 100;
                        ImporteTotal=BaseCalculada+MontoIGV;
                        MontoADescontar = BaseImponible-BaseCalculada;
                    } else {
                        ImporteTotal=BaseImponible*(100-Descuento_Unico)/100;
                        BaseCalculada=ImporteTotal/1.18;
                        MontoIGV = BaseCalculada * IGV_Articulo / 100;
                        MontoADescontar = (BaseImponible/(1+IGV_Articulo/100)) -BaseCalculada;

                    }

                    Log.d(TAG,"BaseImponible "+BaseImponible);
                    Log.d(TAG,"Descuento_Unico "+Descuento_Unico);
                    Log.d(TAG,"BaseCalculada "+BaseCalculada);
                    Log.d(TAG,"MontoIGV "+MontoIGV);
                    Log.d(TAG,"ImporteTotal "+ImporteTotal);
                    Log.d(TAG,"MontoADescontar "+MontoADescontar);
                    //region Sumar los valores para el pedido
                    Monto_SubTotal_Pedido+=BaseImponible;
                    Monto_IGV_Pedido+=MontoIGV;
                    Monto_Descontado_Pedido+=MontoADescontar;
                    Monto_Importe_Pedido+=ImporteTotal;

                    dataModels_listaDeseos.add(
                            new CustomDataModelListaDeseos(
                                    nitem,
                                    ccod_articulo,
                                    nom_articulo,
                                    ConfiguracionEmpresa.Moneda_Trabajo,
                                    precio_unitario.toString(),
                                    ncantidad.toString(),
                                    cunidad,
                                    descuento_1.toString(),
                                    descuento_2.toString(),
                                    descuento_3.toString(),
                                    descuento_4.toString(),
                                    CodigosGenerales.RedondearDecimales(BaseImponible,2),
                                    CodigosGenerales.RedondearDecimales(BaseCalculada,2),
                                    CodigosGenerales.RedondearDecimales(MontoIGV,2),
                                    CodigosGenerales.RedondearDecimales(ImporteTotal,2)
                            ));
                }
            } catch (Exception e) {
                Log.d(TAG, "doInBackground " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);
            try {
                adapter_listaDeseos= new CustomAdapterListaDeseos(dataModels_listaDeseos,getContext());
                lv_items.setAdapter(adapter_listaDeseos);

                lv_items.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                        final String Codigo = dataModels_listaDeseos.get(i).getCodigo_Producto();
                        adb.setTitle("¿Borrar?");
                        adb.setMessage("¿Está seguro de borrar este elemento " + dataModels_listaDeseos.get(i).getNumero_Orden() + "?");
                        final int positionToRemove = i;
                        adb.setNegativeButton("Cancel", null);
                        adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dataModels_listaDeseos.remove(positionToRemove);
                                if (bdListDeseo.EliminarArticulo(Codigo)) {
                                    Toast.makeText(getContext(), "Elemento eliminado", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "No se puede eliminar", Toast.LENGTH_SHORT).show();
                                }
                                adapter_listaDeseos.notifyDataSetChanged();
                            }
                        });
                        adb.show();
                        return false;
                    }
                });

                Log.d(TAG,"FINAL...");
            } catch (Exception e) {
                Log.d(TAG, "BackGroundTask" + e.getMessage());
            }
            super.onPostExecute(s);
        }
    }

}