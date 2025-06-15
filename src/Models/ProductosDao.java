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

public class ProductosDao {

    //Instancia Conexion
    ConeccionMySQL cn = new ConeccionMySQL();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    //Registrar Productos
    public boolean registrarProductoQuery(Productos product) {
        String query = "INSERT INTO productos(codigo, nombre, descripcion,"
                + "precio_unitario, crear_producto, actualizar_producto, id_categoria"
                + "VALUES(?,?,?,?,?,?)";

        Timestamp dateTime = new Timestamp(new Date().getTime());
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);

            pst.setInt(1, product.getCodigo());
            pst.setString(2, product.getNombre());
            pst.setString(3, product.getDescripcion());
            pst.setDouble(4, product.getPrecio_unitario());
            pst.setTimestamp(5, dateTime);
            pst.setTimestamp(6, dateTime);
            pst.setInt(7, product.getCategorias_id());

            pst.execute();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar el producto" + e);
            return false;
        }
    }

    //Listar Productos
    public List listarProductoQuery(String value) {
        List<Productos> list_productos = new ArrayList();
        String query = "select pro.*, ca.nombre AS nombre_categoria FROM "
                + "productos pro, categorias ca WHERE pro.categorias_id = ca.id;";
        String query_buscar_producto = "select pro.*, ca.nombre AS nombre_categoria "
                + "FROM productos pro inner join categorias ca on "
                + "pro.categorias_id = ca.id; WHERE pro.nombre like '%" + value + '%';

        try {
            conn = cn.getConnection();
            if (value.equalsIgnoreCase("")) {
                pst = conn.prepareStatement(query);
                rs = pst.executeQuery();
            } else {
                pst = conn.prepareStatement(query_buscar_producto);
                rs = pst.executeQuery();
            }
            while (rs.next()) {
                Productos product = new Productos();
                product.setId(rs.getInt("id"));
                product.setCodigo(rs.getInt("codigo"));
                product.setNombre(rs.getString("nombre"));
                product.setDescripcion(rs.getString("descripcion"));
                product.setPrecio_unitario(rs.getDouble("precio_unitario"));
                product.setCantidad_producto(rs.getInt("cantidad_producto"));
                product.setNombre_categoria(rs.getString("nombre_categoria"));
                list_productos.add(product);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
        }
        return list_productos;
    }

    //Modificar Productos
    public boolean actualizarProductoQuery(Productos product) {
        String query = "UPDATE productos SET codigo=?, nombre=?, descripcion=?,"
                + "precio_unitario=?, actualizar_producto=?, categorias_id=? "
                + "WHERE id=?";

        Timestamp dateTime = new Timestamp(new Date().getTime());
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);

            pst.setInt(1, product.getCodigo());
            pst.setString(2, product.getNombre());
            pst.setString(3, product.getDescripcion());
            pst.setDouble(4, product.getPrecio_unitario());
            pst.setTimestamp(5, dateTime);
            pst.setInt(6, product.getCategorias_id());
            pst.setInt(7, product.getId());

            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar el producto:"
                    + e);
            return false;
        }
    }

    //Eliminar Productos
    public boolean eliminarProductoQuery(int id) {
        String query = "DELETE FROM productos WHERE id =" + id;
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);

            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se puede eliminar el "
                    + "producto si tiene relacion con otra tabla" + e);
            return false;
        }
    }

    //Buscar Productos
    public Productos buscarProductoQuery(int id) {
        String query = "select pro.*, ca.nombre as nombre_categoria from productos"
                + "pro inner join categorias ca on pro.categorias_id where pro.id=?";
        Productos product = new Productos();

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);

            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                product.setId(rs.getInt("id"));
                product.setCodigo(rs.getInt("codigo"));
                product.setNombre(rs.getString("nombre"));
                product.setDescripcion(rs.getString("descripcion"));
                product.setPrecio_unitario(rs.getDouble("precio_unitario"));
                product.setCategorias_id(rs.getInt("categorias_id"));
                product.setNombre_categoria(rs.getString("nombre_categoria"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return product;
    }

    //Buscar Productos por codigo
    public Productos buscarCodigo(int codigo) {
        String query = "select pro.id, pro.nombre from productos pro where "
                + "pro.codigo = ?";
        Productos product = new Productos();
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);

            pst.setInt(1, codigo);
            rs = pst.executeQuery();
            if (rs.next()) {
                product.setId(rs.getInt("id"));
                product.setCodigo(rs.getInt("codigo"));
                product.setNombre(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return product;
    }

    //Verificar la cantidad de productos
    public Productos buscarId(int id) {
        String query = "SELECT pro.cantidad_producto from productos pro where "
                + "pro.id=?";
        Productos product = new Productos();
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                product.setCantidad_producto(rs.getInt("cantidad_producto"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return product;
    }

    //Actualizar Stock
    public boolean actualizarStockQuery(int monto, int producto_id) {
        String query = "udpate productos set cantidad_producto =? where id=?";
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, monto);
            pst.setInt(2, producto_id);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return false;
        }
    }
}
