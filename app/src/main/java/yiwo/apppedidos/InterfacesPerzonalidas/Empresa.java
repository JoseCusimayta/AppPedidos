package yiwo.apppedidos.InterfacesPerzonalidas;

public class Empresa {
    String Codigo_Empresa;
    String Nombre_Empresa;
    String RUC_Empresa;
    public Empresa(String codigo_Empresa, String nombre_Empresa, String RUC_Empresa) {
        Codigo_Empresa = codigo_Empresa;
        Nombre_Empresa = nombre_Empresa;
        this.RUC_Empresa = RUC_Empresa;
    }

    public String getCodigo_Empresa() {
        return Codigo_Empresa;
    }

    public String getNombre_Empresa() {
        return Nombre_Empresa;
    }

    public String getRUC_Empresa() {
        return RUC_Empresa;
    }
}
