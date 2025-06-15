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

public class ComprasDao {

    ConeccionMySQL cn = new ConeccionMySQL();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    //Registrar Compra
    public boolean registrarCompraQuery(int proveedor_id, int empleados_id,
            double total) {
        String query = "INSERT INTO compras (proveedor_id, empleados_id, total, "
                + "crear_compra) VALUES (?,?,?,?)";
        Timestamp dateTime = new Timestamp(new Date().getTime());
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, proveedor_id);
            pst.setInt(2, empleados_id);
            pst.setDouble(3, total);
            pst.setTimestamp(4, dateTime);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar la compra:" + e);
            return false;
        }
    }

    //Registrar Detalles de la Compra
    public boolean registrarCompraDetallesQuery(int compras_id, double compras_precio,
            int compras_cantidad, double compras_subtotal, int productos_id) {
        String query = "INSERT INTO compras_detalles (compras_id, compras_precio, "
                + "compras_cantidad, compras_subtotal, crear_compra, compras_id) "
                + "VALUES (?,?,?,?,?,?) ";
        Timestamp dateTime = new Timestamp(new Date().getTime());
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, compras_id);
            pst.setDouble(2, compras_precio);
            pst.setInt(3, compras_cantidad);
            pst.setDouble(4, compras_subtotal);
            pst.setTimestamp(5, dateTime);
            pst.setInt(6, productos_id);

            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar detalles de "
                    + "la compra:" + e);
            return false;
        }
    }

    //Obtener ID de la Compra
    public int CompraId() {
        int id = 0;
        String query = "Select MAX(id) AS id FROM compras";
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

    //Listar Compras realizadas
    public List listaAllComprasQuery() {
        List<Compras> list_compras = new ArrayList();
        String query = "select co.*, prov.nombre as nombre_proveedor from "
                + "compras co, proveedores prov where co.proveedor_id = prov.id "
                + "order by co.id asc";
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();

            while (rs.next()) {
                Compras compra = new Compras();
                compra.setId(rs.getInt("id"));
                compra.setNombre_proveedor_producto(rs.getString("nombre_proveedor"));
                compra.setTotal(rs.getDouble("total"));
                compra.setCrear_compra(rs.getString("crear_compra"));
                list_compras.add(compra);

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return list_compras;
    }
    
    

}
