package yiwo.apppedidos.Data;

import android.util.Log;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import yiwo.apppedidos.ConexionBD.BDConexionSQL;
import yiwo.apppedidos.Control.BDArticulos;
import yiwo.apppedidos.Control.BDConceptos;
import yiwo.apppedidos.Control.BDFamilia;
import yiwo.apppedidos.Control.BDSubfamilia;

public class DataArticulos {
    private BDConexionSQL data = new BDConexionSQL();
    private BDArticulos bdArticulos= new BDArticulos();
    private BDConceptos bdConceptos = new BDConceptos();
    private BDFamilia bdFamilia = new BDFamilia();
    private BDSubfamilia bdSubfamilia = new BDSubfamilia();
    private String TAG="DataArticulos";

    public List<String> getTituloCategoria() {

        List<String> listDataHeader = new ArrayList<String>();
        listDataHeader.add("Familia");
        listDataHeader.add("Sub Familia");
        List<String> Conceptos = bdConceptos.getNombresConceptos();
        Integer conceptos = Conceptos.size();
        for (int i = 0; i < conceptos; i++) {
            listDataHeader.add(Conceptos.get(i));
        }
        return listDataHeader;
    }

    public HashMap<String, List<String>> getNombresCategorias(List<String> listDataHeader) {

        HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        try {
            Connection connection = data.getConnection();
            List<String> list_familia = bdFamilia.getNombres(connection);
            if (listDataHeader.size() > 0)
                listDataChild.put(listDataHeader.get(0), list_familia);

            List<String> list_subFamilia = bdSubfamilia.getNombres(connection);
            if (listDataHeader.size() > 1)
                listDataChild.put(listDataHeader.get(1), list_subFamilia);

            List<String> list_concepto1 = bdConceptos.getNombres(connection, 1);
            if (listDataHeader.size() > 2)
                listDataChild.put(listDataHeader.get(2), list_concepto1);

            List<String> list_concepto2 = bdConceptos.getNombres(connection, 2);
            if (listDataHeader.size() > 3)
                listDataChild.put(listDataHeader.get(3), list_concepto2);

            List<String> list_concepto3 = bdConceptos.getNombres(connection, 3);
            if (listDataHeader.size() > 4)
                listDataChild.put(listDataHeader.get(4), list_concepto3);

            List<String> list_concepto4 = bdConceptos.getNombres(connection, 4);
            if (listDataHeader.size() > 5)
                listDataChild.put(listDataHeader.get(5), list_concepto4);

            List<String> list_concepto5 = bdConceptos.getNombres(connection, 5);
            if (listDataHeader.size() > 6)
                listDataChild.put(listDataHeader.get(6), list_concepto5);

            List<String> list_concepto6 = bdConceptos.getNombres(connection, 6);
            if (listDataHeader.size() > 7)
                listDataChild.put(listDataHeader.get(7),list_concepto6);

            List<String> list_concepto7 = bdConceptos.getNombres(connection, 7);
            if (listDataHeader.size() > 8)
                listDataChild.put(listDataHeader.get(8), list_concepto7);
            connection.close();
        } catch (Exception e) {
            Log.d(TAG, "Concentps " + e.getMessage());
        }
        return listDataChild;
    }

    public  ArrayList<List<String>>  getArticulosFiltrados(String Nombre, String BusquedaCategoria){
        return bdArticulos.getArticulosFiltrado(Nombre,BusquedaCategoria);
    }

    public ArrayList<List<String>> getList(String Nombre) {
        return bdArticulos.getList(Nombre);
    }

    public ArrayList<List<String>> getFichaTecnica(String Codigo_Articulo) {
        return bdArticulos.getFichaTecnica(Codigo_Articulo);

    }
}
