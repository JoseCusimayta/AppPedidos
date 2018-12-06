package yiwo.apppedidos.InterfacesPerzonalidas;

public class Clientes {
    private String Codigo_Cliente;
    private String Nombre_Cliente;
    private String Direccion_Cliente;
    private String Ruc_Cliente;
    private String DNI_Cliente;
    private String ListaPrecios_Cliente;
    private String Codigo_FormaPago;
    private String Nombre_FormaPago;
    private String Dias_FormaPago;
    private String Codigo_Pais;
    private String linea_disponible;
    private String nombre_comercial;

    public Clientes(String codigo_Cliente, String nombre_Cliente, String direccion_Cliente, String ruc_Cliente, String DNI_Cliente, String listaPrecios_Cliente, String codigo_FormaPago, String nombre_FormaPago, String dias_FormaPago, String codigo_Pais, String linea_disponible, String nombre_comercial) {
        Codigo_Cliente = codigo_Cliente;
        Nombre_Cliente = nombre_Cliente;
        Direccion_Cliente = direccion_Cliente;
        Ruc_Cliente = ruc_Cliente;
        this.DNI_Cliente = DNI_Cliente;
        ListaPrecios_Cliente = listaPrecios_Cliente;
        Codigo_FormaPago = codigo_FormaPago;
        Nombre_FormaPago = nombre_FormaPago;
        Dias_FormaPago = dias_FormaPago;
        Codigo_Pais = codigo_Pais;
        this.linea_disponible = linea_disponible;
        this.nombre_comercial = nombre_comercial;
    }

    public String getCodigo_Cliente() {
        return Codigo_Cliente;
    }

    public String getNombre_Cliente() {
        return Nombre_Cliente;
    }

    public String getDireccion_Cliente() {
        return Direccion_Cliente;
    }

    public String getRuc_Cliente() {
        return Ruc_Cliente;
    }

    public String getDNI_Cliente() {
        return DNI_Cliente;
    }

    public String getListaPrecios_Cliente() {
        return ListaPrecios_Cliente;
    }

    public String getCodigo_FormaPago() {
        return Codigo_FormaPago;
    }

    public String getNombre_FormaPago() {
        return Nombre_FormaPago;
    }

    public String getDias_FormaPago() {
        return Dias_FormaPago;
    }

    public String getCodigo_Pais() {
        return Codigo_Pais;
    }

    public String getLinea_disponible() {
        return linea_disponible;
    }

    public String getNombre_comercial() {
        return nombre_comercial;
    }
}