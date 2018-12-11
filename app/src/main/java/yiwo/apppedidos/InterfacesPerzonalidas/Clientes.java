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
    private String cliente_telefonos;
    private String contacto_codigo;
    private String contacto_nombre;
    private String contacto_telefono;
    private String cliente_correo;
    private String vendedor_codigo;
    private String vendedor_nombre;
    private String observacion;

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

    public String getCliente_telefonos() {
        return cliente_telefonos;
    }

    public String getContacto_codigo() {
        return contacto_codigo;
    }

    public String getContacto_nombre() {
        return contacto_nombre;
    }

    public String getContacto_telefono() {
        return contacto_telefono;
    }

    public String getCliente_correo() {
        return cliente_correo;
    }

    public String getVendedor_codigo() {
        return vendedor_codigo;
    }

    public String getVendedor_nombre() {
        return vendedor_nombre;
    }

    public String getObservacion() {
        return observacion;
    }

    public Clientes(String codigo_Cliente, String nombre_Cliente, String direccion_Cliente, String ruc_Cliente, String DNI_Cliente, String listaPrecios_Cliente, String codigo_FormaPago, String nombre_FormaPago, String dias_FormaPago, String codigo_Pais, String linea_disponible, String nombre_comercial, String cliente_telefonos, String contacto_codigo, String contacto_nombre, String contacto_telefono, String cliente_correo, String vendedor_codigo, String vendedor_nombre, String observacion) {

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
        this.cliente_telefonos = cliente_telefonos;
        this.contacto_codigo = contacto_codigo;
        this.contacto_nombre = contacto_nombre;
        this.contacto_telefono = contacto_telefono;
        this.cliente_correo = cliente_correo;
        this.vendedor_codigo = vendedor_codigo;
        this.vendedor_nombre = vendedor_nombre;
        this.observacion = observacion;
    }
}