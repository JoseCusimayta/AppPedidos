package yiwo.apppedidos.InterfacesPerzonalidas;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import yiwo.apppedidos.R;

public class CustomDialogConfiguraciones implements View.OnClickListener {
    private EditText et_lan, et_publica, et_ruc;
    Button b_aceptar;
    Activity activity;
    private Dialog dialogo;

    @Override
    public void onClick(View view) {
        interfaz.ResultadoConfiguraciones(et_lan.getText().toString(),et_publica.getText().toString(),et_ruc.getText().toString());
        dialogo.dismiss();
    }

    public interface FinalizoConfiguraciones{
        void ResultadoConfiguraciones(String ip_lan, String ip_publica, String ruc_empresa);
    }

    private CustomDialogConfiguraciones.FinalizoConfiguraciones interfaz;

    public CustomDialogConfiguraciones (Activity activity, CustomDialogConfiguraciones.FinalizoConfiguraciones interfaz){
        this.activity=activity;
        this.interfaz=interfaz;

        dialogo=new Dialog(activity);
        dialogo.setContentView(R.layout.custom_dialog_configuraciones);
        dialogo.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        et_lan=dialogo.findViewById(R.id.et_lan);
        et_publica=dialogo.findViewById(R.id.et_publica);
        et_ruc=dialogo.findViewById(R.id.et_ruc);


        b_aceptar=dialogo.findViewById(R.id.b_aceptar);
        b_aceptar.setOnClickListener(this);

        dialogo.show();
    }
}
