package yiwo.apppedidos.InterfacesPerzonalidas;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.SQLException;

import yiwo.apppedidos.AspectosGenerales.CodigosGenerales;
import yiwo.apppedidos.ConexionBD.BDConexionSQL;
import yiwo.apppedidos.Data.BDMotivo;
import yiwo.apppedidos.R;

public class CustomDialogEnviarPedido {
    private TextView tv_titulo, tv_motivo, tv_cliente, tv_subtotal, tv_descuento, tv_igv, tv_importe;
    private Button b_aceptar, b_cancelar;
    Context context;
    private BDMotivo bdMotivo= new BDMotivo();
    private String Correlativo;
    private String TAG="CustomDialogEnviarPedido";
    public interface FinalizoCuadroDialogPedido {
        void ResultadoCuadroDialogPedido(String Codigo, String Nombre);
    }

    private CustomDialogEnviarPedido.FinalizoCuadroDialogPedido interfaz;

    public CustomDialogEnviarPedido(Context context,
                                    CustomDialogEnviarPedido.FinalizoCuadroDialogPedido actividad,
                                    Double Monto_SubTotal_Pedido,
                                    Double Monto_Descontado_Pedido,
                                    Double Monto_IGV_Pedido,
                                    Double Monto_Importe_Pedido
                                    ) {
        this.context = context;
        interfaz = actividad;
        final Dialog dialogo = new Dialog(context);
        //dialogo.setCancelable(false);
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogo.setContentView(R.layout.custom_dialog_enviar_pedido);
        dialogo.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        tv_titulo = dialogo.findViewById(R.id.tv_titulo);
        tv_motivo = dialogo.findViewById(R.id.tv_motivo);
        tv_cliente = dialogo.findViewById(R.id.tv_cliente);
        tv_subtotal = dialogo.findViewById(R.id.tv_subtotal);
        b_aceptar = dialogo.findViewById(R.id.b_aceptar);
        b_cancelar = dialogo.findViewById(R.id.b_cerrar);
        tv_descuento = dialogo.findViewById(R.id.tv_descuento);
        tv_igv = dialogo.findViewById(R.id.tv_igv);
        tv_importe = dialogo.findViewById(R.id.tv_importe);
        try {
            BDConexionSQL bdConexionSQL= new BDConexionSQL();
            Connection connection=bdConexionSQL.getConnection();
            Correlativo = bdMotivo.getNuevoCodigoPedido(connection);
            connection.close();
        } catch (SQLException e) {
            Log.d(TAG,"CustomDialogEnviarPedido "+e.getMessage());
        }


        tv_motivo.setText(CodigosGenerales.Nombre_Motivo + " - " + Correlativo);

        //tv_cliente.setText("Cliente: "+CodigosGenerales.Codigo_Cliente+" - "+CodigosGenerales.Nombre_Cliente);

        tv_cliente.setText("Cliente: " + CodigosGenerales.Nombre_Cliente);
        tv_subtotal.setText("SubTotal: " + CodigosGenerales.Moneda_Empresa + " " + CodigosGenerales.RedondearDecimales(Monto_SubTotal_Pedido,2));
        tv_descuento.setText("Descuento:  "+ CodigosGenerales.Moneda_Empresa + " " + CodigosGenerales.RedondearDecimales(Monto_Descontado_Pedido,2));
        tv_igv.setText("IGV:  "+ CodigosGenerales.Moneda_Empresa + " " + CodigosGenerales.RedondearDecimales(Monto_IGV_Pedido,2));
        tv_importe.setText("Importe:  "+ CodigosGenerales.Moneda_Empresa + " " + CodigosGenerales.RedondearDecimales(Monto_Importe_Pedido,2));

        tv_cliente.setText(CodigosGenerales.Nombre_Cliente);

        b_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaz.ResultadoCuadroDialogPedido(Correlativo, tv_cliente.getText().toString());
                dialogo.dismiss();
            }
        });
        b_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogo.dismiss();
            }
        });

        dialogo.show();
    }

}