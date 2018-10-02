package yiwo.apppedidos.InterfacesPerzonalidas;

public class CodNom {
    String Codigo, Nombre;

    public CodNom(String codigo, String nombre) {
        Codigo = codigo;
        Nombre = nombre;
    }

    public String getCodigo() {
        return Codigo;
    }

    public String getNombre() {
        return Nombre;
    }
}
