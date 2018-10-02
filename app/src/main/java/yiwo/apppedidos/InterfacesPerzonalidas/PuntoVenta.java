package yiwo.apppedidos.InterfacesPerzonalidas;

public class PuntoVenta {

    String ccod_almacen;
    String cnom_almacen;
    String erp_codalmacen_ptovta;
    String cdireccion;

    public PuntoVenta(String ccod_almacen, String cnom_almacen, String erp_codalmacen_ptovta, String cdireccion) {
        this.ccod_almacen = ccod_almacen;
        this.cnom_almacen = cnom_almacen;
        this.erp_codalmacen_ptovta = erp_codalmacen_ptovta;
        this.cdireccion = cdireccion;
    }

    public String getCcod_almacen() {
        return ccod_almacen;
    }

    public String getCnom_almacen() {
        return cnom_almacen;
    }

    public String getErp_codalmacen_ptovta() {
        return erp_codalmacen_ptovta;
    }

    public String getCdireccion() {
        return cdireccion;
    }
}