package Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

public class VentasDao {

    ConeccionMySQL cn = new ConeccionMySQL();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    //Registrar Venta
    public boolean registrarVentaQuery(int cliente_id, int empleado_id, double total) {
        String query = "INSERT INTO ventas (cliente_id, empleado_id, total, fecha_venta"
                + "VALUES(?,?,?,?)";
        Timestamp dateTime = new Timestamp(new Date().getTime());
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, cliente_id);
            pst.setInt(2, empleado_id);
            pst.setDouble(3, total);
            pst.setTimestamp(4, dateTime);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return false;
        }
    }

    //Registrar Detalles de Venta
    public boolean registrarVentaDetallesQuery(int producto_id, int venta_id,
            int venta_cantidad, double venta_precio, double venta_subtotal) {
        String query = "INSERT INTO ventas_detalles (producto_id, venta_id, "
                + "venta_cantidad, venta_precio, venta_subtotal) VALUES(?,?;?,?,?)";
        Timestamp dateTime = new Timestamp(new Date().getTime());
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, producto_id);
            pst.setInt(2, venta_id);
            pst.setInt(3, venta_cantidad);
            pst.setDouble(4, venta_precio);
            pst.setDouble(5, venta_subtotal);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
            return false;
        }
    }

    //Obtener ID de la Venta
    public int VentaId() {
        int id = 0;
        String query = "Select MAX(id) AS id FROM ventas";
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.print(e.getMessage());
        }
        return id;
    }

    //Listar ventas realizadas
    public List listaAllVentasQuery() {
        List<Ventas> list_ventas = new ArrayList();
        String query = "select ve.id AS invoice, cli.nombre_completo AS clientes,"
                + "em.nombre_completo AS empleados, ve.total, ve.fecha_venta from "
                + "ventas ve inner join clientes cli on ve.cliente_id = cli.id "
                + "inner join empleados em on ve.empleado_id = em.id order by "
                + "ve.id ASC";
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();

            while (rs.next()) {
                Ventas venta = new Ventas();
                venta.setId(rs.getInt("invoice"));
                venta.setCliente_nombre(rs.getString("clientes"));
                venta.setEmpleado_nombre(rs.getString("empleados"));
                venta.setTotal(rs.getDouble("total"));
                venta.setFecha_venta(rs.getString("fecha_venta"));
                list_ventas.add(venta);

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
        }
        return list_ventas;
    }

}
