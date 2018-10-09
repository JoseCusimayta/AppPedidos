package yiwo.apppedidos.Data;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import yiwo.apppedidos.AspectosGenerales.DatosCliente;
import yiwo.apppedidos.ConexionBD.BDConexionSQL;
import yiwo.apppedidos.Control.BDListDeseo;
import yiwo.apppedidos.Control.BDMotivo;
import yiwo.apppedidos.Control.BDPedidos;

public class DataPedidos {
    BDPedidos bdPedidos = new BDPedidos();
    BDListDeseo bdListDeseo = new BDListDeseo();
    BDMotivo bdMotivo = new BDMotivo();
    BDConexionSQL bdata = new BDConexionSQL();

    public Boolean EnviarPedido(String Importe,
                                String Descuento, String Subtotal, String IGV,
                                String Percepcion, String Fecha_Entrega, String Comentario) {
        try {
            Connection connection = bdata.getConnection();
            String CodPedido = bdMotivo.getNuevoCodigoPedido(connection);
            if (DatosCliente.Codigo_Cliente.isEmpty() || CodPedido.isEmpty()) {
                connection.close();
                return false;
            }
            if (bdPedidos.GuardarPedido(connection, CodPedido, Importe, Descuento, Subtotal, IGV, Percepcion, Fecha_Entrega, Comentario)
                    && bdMotivo.ActualizarCorrelativo(connection)
                    && bdPedidos.LlenarDetalle(connection, CodPedido)) {
                bdListDeseo.LimpiarListaDeseo(connection);
                connection.close();
                return true;
            } else {
                connection.close();
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public ArrayList<List<String>> getDetalle(String Cod_Pedido, String Cod_Motivo) {
        return bdPedidos.getDetalle(Cod_Pedido, Cod_Motivo);
    }

    public ArrayList<List<String>> getList(String Nombre) {
        return bdPedidos.getList(Nombre);
    }
}