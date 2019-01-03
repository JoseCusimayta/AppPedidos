package yiwo.apppedidos.InterfacesPerzonalidas;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import yiwo.apppedidos.AspectosGenerales.DatosCliente;
import yiwo.apppedidos.Control.BDClientes;
import yiwo.apppedidos.Control.BDMotivo;
import yiwo.apppedidos.R;

public class ClienteCorreoTelefonoDialog {
    private EditText et_telefono, et_correo;
    private Button b_registrar, b_cancelar;
    Context context;
    BDClientes bdClientes= new BDClientes();
    private String TAG="ClienteCorreoTelefonoDialog";

    public interface FianlizoClienteCorreoTelefonoDialog {
        void ResultadoClienteCorreoTelefonoDialog(Boolean resultado);
    }

    private FianlizoClienteCorreoTelefonoDialog interfaz;

    public ClienteCorreoTelefonoDialog(Context context,
                                       FianlizoClienteCorreoTelefonoDialog actividad
    ) {
        this.context = context;
        interfaz = actividad;
        final Dialog dialogo = new Dialog(context);
        //dialogo.setCancelable(false);
        //dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogo.setContentView(R.layout.cliente_correo_telefono);
        dialogo.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        et_correo= dialogo.findViewById(R.id.et_correo);
        et_telefono= dialogo.findViewById(R.id.et_telefono);
        b_registrar= dialogo.findViewById(R.id.b_registrar);
        b_cancelar= dialogo.findViewById(R.id.b_cancelar);

        et_correo.setText(DatosCliente.Cliene_Correo);
        et_telefono.setText(DatosCliente.Cliente_Telefono);

        b_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                interfaz.ResultadoClienteCorreoTelefonoDialog(bdClientes.actualizarCorreoTelefono(et_correo.getText().toString(),et_telefono.getText().toString()));
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