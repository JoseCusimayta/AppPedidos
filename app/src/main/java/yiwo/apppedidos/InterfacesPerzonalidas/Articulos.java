package yiwo.apppedidos.InterfacesPerzonalidas;

public class Articulos {

    String codigo_articulo;
    String nombre_articulo;
    String cantidad_articulo;
    String unidad_articulo;
    String precio_articulo;
    String moneda_articulo;

    public Articulos(String codigo_articulo, String nombre_articulo, String cantidad_articulo, String unidad_articulo, String precio_articulo, String moneda_articulo) {
        this.codigo_articulo = codigo_articulo;
        this.nombre_articulo = nombre_articulo;
        this.cantidad_articulo = cantidad_articulo;
        this.unidad_articulo = unidad_articulo;
        this.precio_articulo = precio_articulo;
        this.moneda_articulo = moneda_articulo;
    }

    public String getCodigo_articulo() {
        return codigo_articulo;
    }

    public String getNombre_articulo() {
        return nombre_articulo;
    }

    public String getCantidad_articulo() {
        return cantidad_articulo;
    }

    public String getUnidad_articulo() {
        return unidad_articulo;
    }

    public String getPrecio_articulo() {
        return precio_articulo;
    }

    public String getMoneda_articulo() {
        return moneda_articulo;
    }
}
