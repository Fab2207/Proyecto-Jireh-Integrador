package Controladores;

import Models.ComboBoxDinamico;
import static Models.EmpleadosDao.rol_user;
import Models.Productos;
import Models.ProductosDao;
import Vistas.Principal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ProductosController implements ActionListener, MouseListener, KeyListener {

    private Productos producto;
    private ProductosDao producto_dao;
    private Principal vista;
    String rol = rol_user;
    DefaultTableModel model = new DefaultTableModel();

    public ProductosController(Productos producto, ProductosDao producto_dao, Principal vista) {
        this.producto = producto;
        this.producto_dao = producto_dao;
        this.vista = vista;
        //Boton Registrar Producto
        this.vista.btn_Registrar_Producto.addActionListener(this);
        //Vincular la tabla
        this.vista.Tabla_Productos.addMouseListener(this);
        //txt Buscar Producto 
        this.vista.txt_Buscar_Producto.addKeyListener(this);
        //Boton modificar Producto
        this.vista.btn_Actualizar_Producto.addActionListener(this);
        //Boton eliminar Producto
        this.vista.btn_Eliminar_Producto.addActionListener(this);
        //Boton cancelar Producto
        this.vista.btn_Cancelar_Producto.addActionListener(this);
        //Vincular labels a las pestañas del sistema
        this.vista.jLabel3.addMouseListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btn_Registrar_Producto) {
            if (vista.txt_Producto_Codigo.getText().equals("")
                    || vista.txt_Producto_Nombre.getText().equals("")
                    || vista.txt_Producto_Descripcion.getText().equals("")
                    || vista.txt_Producto_Precio.getText().equals("")
                    || vista.cmb_Producto_Categoria.getSelectedItem().toString().equals("")) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            } else {
                producto.setCodigo(Integer.parseInt(vista.txt_Producto_Codigo.getText()));
                producto.setNombre(vista.txt_Producto_Nombre.getText().trim());
                producto.setDescripcion(vista.txt_Producto_Descripcion.getText().trim());
                producto.setPrecio_unitario(Double.parseDouble(vista.txt_Producto_Precio.getText().trim()));
                ComboBoxDinamico categoria_id = (ComboBoxDinamico) vista.cmb_Producto_Categoria.getSelectedItem();
                producto.setCategorias_id(categoria_id.getId());
                if (producto_dao.registrarProductoQuery(producto)) {
                    JOptionPane.showMessageDialog(null, "Producto registardo con éxito");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al registrar producto");
                }
            }

        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
