package yiwo.apppedidos.InterfacesPerzonalidas;

public class CustomDataModel {

    private String cod,name,ruc,direccion,listaPrecios,dni,dato_invisible;


    public String getCod() {
        return cod;
    }

    public String getName() {
        return name;
    }

    public String getRuc() {
        return ruc;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getListaPrecios() {
        return listaPrecios;
    }

    public String getDni() {
        return dni;
    }

    public String getDato_invisible() {
        return dato_invisible;
    }

    public CustomDataModel(String cod, String name, String ruc, String direccion, String listaPrecios, String dni, String dato_invisible) {

        this.cod = cod;
        this.name = name;
        this.ruc = ruc;
        this.direccion = direccion;
        this.listaPrecios = listaPrecios;
        this.dni = dni;
        this.dato_invisible = dato_invisible;
    }
}