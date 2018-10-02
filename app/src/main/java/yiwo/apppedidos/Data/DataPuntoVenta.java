package yiwo.apppedidos.Data;

import java.util.ArrayList;
import java.util.List;

import yiwo.apppedidos.Control.BDPuntoVenta;

public class DataPuntoVenta {
    BDPuntoVenta bdPuntoVenta = new BDPuntoVenta();

    public ArrayList<List<String>> getList(String Nombre) {
        return bdPuntoVenta.getList(Nombre);
    }
}