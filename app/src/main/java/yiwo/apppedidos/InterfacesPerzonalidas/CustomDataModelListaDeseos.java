package yiwo.apppedidos.InterfacesPerzonalidas;

public class CustomDataModelListaDeseos {
    String Numero_Orden;
    String Codigo_Producto;
    String Nombre_Producto;
    String Tipo_moneda;
    String Precio_Unitario;
    String Cantidad;
    String Tipo_Unidad;
    String Descuento_1;
    String Descuento_2;
    String Descuento_3;
    String Descuento_4;
    String Base_Imponible;
    String Base_Calculada;
    String IGV;
    String Importe;
    String MontoDescontado;

    public CustomDataModelListaDeseos(String numero_Orden, String codigo_Producto, String nombre_Producto, String tipo_moneda, String precio_Unitario, String cantidad, String tipo_Unidad, String descuento_1, String descuento_2, String descuento_3, String descuento_4, String base_Imponible, String base_Calculada, String IGV, String importe, String montoDescontado) {
        Numero_Orden = numero_Orden;
        Codigo_Producto = codigo_Producto;
        Nombre_Producto = nombre_Producto;
        Tipo_moneda = tipo_moneda;
        Precio_Unitario = precio_Unitario;
        Cantidad = cantidad;
        Tipo_Unidad = tipo_Unidad;
        Descuento_1 = descuento_1;
        Descuento_2 = descuento_2;
        Descuento_3 = descuento_3;
        Descuento_4 = descuento_4;
        Base_Imponible = base_Imponible;
        Base_Calculada = base_Calculada;
        this.IGV = IGV;
        Importe = importe;
        MontoDescontado = montoDescontado;
    }

    public String getNumero_Orden() {
        return Numero_Orden;
    }

    public String getCodigo_Producto() {
        return Codigo_Producto;
    }

    public String getNombre_Producto() {
        return Nombre_Producto;
    }

    public String getTipo_moneda() {
        return Tipo_moneda;
    }

    public String getPrecio_Unitario() {
        return Precio_Unitario;
    }

    public String getCantidad() {
        return Cantidad;
    }

    public String getTipo_Unidad() {
        return Tipo_Unidad;
    }

    public String getDescuento_1() {
        return Descuento_1;
    }

    public String getDescuento_2() {
        return Descuento_2;
    }

    public String getDescuento_3() {
        return Descuento_3;
    }

    public String getDescuento_4() {
        return Descuento_4;
    }

    public String getBase_Imponible() {
        return Base_Imponible;
    }

    public String getBase_Calculada() {
        return Base_Calculada;
    }

    public String getIGV() {
        return IGV;
    }

    public String getImporte() {
        return Importe;
    }

    public String getMontoDescontado() {
        return MontoDescontado;
    }
}
