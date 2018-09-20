package yiwo.apppedidos.InterfacesPerzonalidas;

public class CustomDataModelArticulos {

    String codA;
    String urlA;
    String nombreA;
    String cantidadA;
    String precioA;

    String codB;
    String urlB;
    String nombreB;
    String cantidadB;
    String precioB;

    public CustomDataModelArticulos(String codA, String urlA, String nombreA, String cantidadA, String precioA, String codB, String urlB, String nombreB, String cantidadB, String precioB) {
        this.codA = codA;
        this.urlA = urlA;
        this.nombreA = nombreA;
        this.cantidadA = cantidadA;
        this.precioA = precioA;
        this.codB = codB;
        this.urlB = urlB;
        this.nombreB = nombreB;
        this.cantidadB = cantidadB;
        this.precioB = precioB;
    }

    public String getCodA() {
        return codA;
    }

    public String getUrlA() {
        return urlA;
    }

    public String getNombreA() {
        return nombreA;
    }

    public String getCantidadA() {
        return cantidadA;
    }

    public String getPrecioA() {
        return precioA;
    }

    public String getCodB() {
        return codB;
    }

    public String getUrlB() {
        return urlB;
    }

    public String getNombreB() {
        return nombreB;
    }

    public String getCantidadB() {
        return cantidadB;
    }

    public String getPrecioB() {
        return precioB;
    }
}
