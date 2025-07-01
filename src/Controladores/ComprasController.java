package Controladores;

import Models.ComboBoxDinamico;
import Models.Compras;
import Models.ComprasDao;
import static Models.EmpleadosDao.id_user;
import static Models.EmpleadosDao.rol_user;
import Models.Productos;
import Models.ProductosDao;
import Vistas.Factura;
import Vistas.Principal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ComprasController implements ActionListener, MouseListener, KeyListener {

    private Compras compra;
    private ComprasDao compra_Dao;
    private Principal vista;
    Productos producto = new Productos();
    ProductosDao producto_dao = new ProductosDao();
    String rol = rol_user;
    private int getIdProveedor = 0;
    private int item = 0;
    DefaultTableModel model = new DefaultTableModel();
    DefaultTableModel temp = new DefaultTableModel();

    public ComprasController(Compras compra, ComprasDao compra_Dao, Principal vista) {
        this.compra = compra;
        this.compra_Dao = compra_Dao;
        this.vista = vista;

        this.vista.txt_Compras_Producto_codigo.addKeyListener(this);
        this.vista.txt_Compras_Producto_Precio.addKeyListener(this);

        //Agregar
        this.vista.btn_agregar_producto_compra.addActionListener(this);
        //Tabla Compras
        this.vista.Tabla_Compras.addMouseListener(this);
        //Boton Comprar
        this.vista.btn_confirmar_compra.addActionListener(this);
        //Boton Eliminar Compra
        this.vista.btn_eliminar_compra.addActionListener(this);
        //Nueva Compra
        this.vista.btn_nueva_compra.addActionListener(this);
        //Vincular pestaña label Compras
        this.vista.jLabel4.addMouseListener(this);
        //Vincular pestaña Reportes
        this.vista.jLabel9.addMouseListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btn_agregar_producto_compra) {
            ComboBoxDinamico proveedor_cmb = (ComboBoxDinamico) vista.cmb_compras_Proveedor.getSelectedItem();
            int proveedor_id = proveedor_cmb.getId();
            if (getIdProveedor == 0) {
                getIdProveedor = proveedor_id;
            } else if (getIdProveedor != proveedor_id) {
                JOptionPane.showMessageDialog(null, "No puede realizar una misma compra con varios proveedores");
                return;
            }

            if (!validarCamposCompra()) {
                JOptionPane.showMessageDialog(null, "Debes agregar un producto para realizar la compra");
                return;
            }

            int cantidad = Integer.parseInt(vista.txt_Compras_Producto_cantidad.getText());
            String nombre_producto = vista.txt_Compras_Producto_name.getText();
            double precio = Double.parseDouble(vista.txt_Compras_Producto_Precio.getText());
            int compra_id = Integer.parseInt(vista.txt_Compras_Producto_id.getText());
            String proveedor_nombre = vista.cmb_compras_Proveedor.getSelectedItem().toString();

            if (cantidad > 0) {
                temp = (DefaultTableModel) vista.Tabla_Compras.getModel();
                for (int i = 0; i < vista.Tabla_Compras.getRowCount(); i++) {
                    if (vista.Tabla_Compras.getValueAt(i, 1).equals(nombre_producto)) {
                        JOptionPane.showMessageDialog(null, "El producto ya está registrado en la tabla de compras");
                        return;
                    }
                }

                ArrayList<Object> list = new ArrayList<>();
                item = 1;
                list.add(item);
                list.add(compra_id);
                list.add(nombre_producto);
                list.add(cantidad);
                list.add(precio);
                list.add(cantidad * precio);
                list.add(proveedor_nombre);

                Object[] obj = new Object[6];
                obj[0] = list.get(1);
                obj[1] = list.get(2);
                obj[2] = list.get(3);
                obj[3] = list.get(4);
                obj[4] = list.get(5);
                obj[5] = list.get(6);
                temp.addRow(obj);
                vista.Tabla_Compras.setModel(temp);
                limpiarCamposCompras();
                vista.cmb_compras_Proveedor.setEditable(false);
                vista.txt_Compras_Producto_codigo.requestFocus();
                calcularCompra();
            }
        } else if (e.getSource() == vista.btn_confirmar_compra) {
            if (vista.Tabla_Compras.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "Debes agregar un producto para realizar la compra");
            } else {
                insertarCompra();
            }
        } else if (e.getSource() == vista.btn_eliminar_compra) {
            model = (DefaultTableModel) vista.Tabla_Compras.getModel();
            model.removeRow(vista.Tabla_Compras.getSelectedRow());
            calcularCompra();
            vista.txt_Compras_Producto_codigo.requestFocus();
        } else if (e.getSource() == vista.btn_nueva_compra) {
            limpiarTablaTemp();
            limpiarCamposCompras();
        }
    }

    public void listAllCompras() {
        if (rol.equals("Administrador") || rol.equals("Auxiliar")) {
            List<Compras> list = compra_Dao.listaAllComprasQuery();
            model = (DefaultTableModel) vista.Tabla_Reportes_Compras.getModel();
            Object[] row = new Object[4];
            for (int i = 0; i < list.size(); i++) {
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getNombre_proveedor_producto();
                row[2] = list.get(i).getTotal();
                row[3] = list.get(i).getCrear_compra();
                model.addRow(row);
            }
            vista.Tabla_Reportes_Compras.setModel(model);
        }
    }

    public void limpiarCamposCompras() {
        vista.txt_Compras_Producto_name.setText("");
        vista.txt_Compras_Producto_Precio.setText("");
        vista.txt_Compras_Producto_cantidad.setText("");
        vista.txt_Compras_Producto_codigo.setText("");
        vista.txt_Compras_Producto_Subtotal.setText("");
        vista.txt_Compras_Producto_id.setText("");
        vista.txt_Compras_Producto_Total.setText("");
    }

    public void calcularCompra() {
        double total = 0;
        int numRow = vista.Tabla_Compras.getRowCount();
        for (int i = 0; i < numRow; i++) {
            total = total + Double.parseDouble(String.valueOf(vista.Tabla_Compras.getValueAt(i, 4)));
        }
        vista.txt_Compras_Producto_Total.setText("" + total);
    }

    private void insertarCompra() {
        double total = Double.parseDouble(vista.txt_Compras_Producto_Total.getText());
        int empleado_id = id_user;
        if (compra_Dao.registrarCompraQuery(getIdProveedor, empleado_id, total)) {
            int compra_id = compra_Dao.CompraId();
            for (int i = 0; i < vista.Tabla_Compras.getRowCount(); i++) {
                int producto_id = Integer.parseInt(vista.Tabla_Compras.getValueAt(i, 0).toString());
                int compra_cantidad = Integer.parseInt(vista.Tabla_Compras.getValueAt(i, 2).toString());
                double compra_precio = Double.parseDouble(vista.Tabla_Compras.getValueAt(i, 3).toString());
                double compra_subtotal = compra_precio * compra_cantidad;
                compra_Dao.registrarCompraDetallesQuery(compra_id, compra_precio, compra_cantidad, compra_subtotal, producto_id);
                producto = producto_dao.buscarId(producto_id);
                int cantidad = producto.getCantidad_producto() + compra_cantidad;
                producto_dao.actualizarStockQuery(cantidad, producto_id);

            }
            limpiarTablaTemp();
            JOptionPane.showMessageDialog(null, "Compra generada con éxito");
            limpiarCamposCompras();
            Factura print = new Factura(compra_id);
            print.setVisible(true);
        }
    }

    private boolean validarCamposCompra() {
        return !vista.txt_Compras_Producto_id.getText().isEmpty()
                && !vista.txt_Compras_Producto_cantidad.getText().isEmpty()
                && !vista.txt_Compras_Producto_Precio.getText().isEmpty();
    }

    public void limpiarTablaTemp() {
        for (int i = 0; i < temp.getRowCount(); i++) {
            temp.removeRow(i);
            i = i - 1;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == vista.jLabel4) {
            if (rol.equals("Administrador")) {
                vista.jTabbedPane2.setSelectedIndex(1);
                limpiarTablaTemp();
            } else {
                vista.jTabbedPane2.setEnabledAt(0, false);
                JOptionPane.showMessageDialog(null, "No tienes privilegios de Administrador");
            }
        } else if (e.getSource() == vista.jLabel9) {
            vista.jTabbedPane2.setSelectedIndex(7);
            limpiarTabla();
            listAllCompras();
        }
    }

    public void limpiarTabla() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
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
        if (e.getSource() == vista.txt_Compras_Producto_codigo) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (vista.txt_Compras_Producto_codigo.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Ingrese el codigo del producto");
                } else {
                    int id = Integer.parseInt(vista.txt_Compras_Producto_codigo.getText());
                    producto = producto_dao.buscarCodigo(id);
                    vista.txt_Compras_Producto_name.setText(producto.getNombre());
                    vista.txt_Compras_Producto_id.setText("" + producto.getId());
                    vista.txt_Compras_Producto_cantidad.requestFocus();
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == vista.txt_Compras_Producto_Precio) {
            int cant;
            double monto = 0.0;
            if (vista.txt_Compras_Producto_cantidad.getText().equals("")) {
                cant = 1;
                vista.txt_Compras_Producto_Precio.setText("" + monto);
            } else {
                cant = Integer.parseInt(vista.txt_Compras_Producto_cantidad.getText());
                monto = Double.parseDouble(vista.txt_Compras_Producto_Precio.getText());
                vista.txt_Compras_Producto_Subtotal.setText("" + cant * monto);
            }
        }
    }

}
