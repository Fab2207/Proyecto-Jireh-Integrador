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

public class CategoriasDao {

    //Instancia Conexion
    ConeccionMySQL cn = new ConeccionMySQL();
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    //Registrar Categoria
    public boolean registrarCategoriaQuery(Categorias category) {
        String query = "INSERT INTO categorias(nombre, fecha_registro, "
                + "actualizar_categoria) VALUES(?,?,?)";

        Timestamp dateTime = new Timestamp(new Date().getTime());

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setString(1, category.getNombre());
            pst.setTimestamp(2, dateTime);
            pst.setTimestamp(3, dateTime);

            pst.execute();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar la categoria:" + e);
            return false;
        }
    }

    //Listar Categoria
    public List listarCategoriaQuery(String value) {
        List<Categorias> list_categorias = new ArrayList();

        String query = "SELECT *FROM categorias";
        String query_buscar_categoria = "SELECT *FROM categorias WHERE nombre "
                + "LIKE'%" + value + "%";
        try {
            conn = cn.getConnection();
            if (value.equalsIgnoreCase("")) {
                pst = conn.prepareStatement(query);
                rs = pst.executeQuery();
            } else {
                pst = conn.prepareStatement(query_buscar_categoria);
                rs = pst.executeQuery();
            }
            while (rs.next()) {
                Categorias categoria = new Categorias();
                categoria.setId(rs.getInt("id"));
                categoria.setNombre(rs.getString("nombre"));
                list_categorias.add(categoria);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
        }
        return list_categorias;
    }

    //Modificar Categoria
    public boolean actualizarCategoriaQuery(Categorias category) {
        String query = "UPDATE categorias SET nombre = ?, actualizar_categoria = ? "
                + "WHERE id = ?";
        Timestamp dateTime = new Timestamp(new Date().getTime());
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setString(1, category.getNombre());
            pst.setTimestamp(2, dateTime);
            pst.setInt(3, category.getId());

            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar la categoria:"
                    + e);
            return false;
        }
    }

    //Eliminar Categoria
    public boolean eliminarCategoriaQuery(int id) {
        String query = "DELETE FROM categorias WHERE id =" + id;
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.executeUpdate();

            reordenarIdsCategorias(); 
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se puede eliminar la categoria: " + e);
            return false;
        }
    }

    // Reordenar IDs después de una eliminación
    public void reordenarIdsCategorias() {
        String resetContador = "SET @count = 0";
        String reordenar = "UPDATE categorias SET id = (@count := @count + 1)";
        String reiniciarAI = "ALTER TABLE categorias AUTO_INCREMENT = 1";

        try {
            conn = cn.getConnection();

            pst = conn.prepareStatement(resetContador);
            pst.executeUpdate();

            pst = conn.prepareStatement(reordenar);
            pst.executeUpdate();

            pst = conn.prepareStatement(reiniciarAI);
            pst.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al reordenar IDs: " + e);
        }
    }
}
