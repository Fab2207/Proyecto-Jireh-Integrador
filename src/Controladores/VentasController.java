package Controladores;

import Models.Clientes;
import Models.ClientesDao;
import Models.EmpleadosDao;
import static Models.EmpleadosDao.rol_user;
import Models.Productos;
import Models.ProductosDao;
import Models.Ventas;
import Models.VentasDao;
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

public class VentasController implements ActionListener, MouseListener, KeyListener {

    private Ventas venta;
    private VentasDao venta_Dao;
    private Principal vista;
    Productos producto = new Productos();
    ProductosDao producto_dao = new ProductosDao();
    Clientes cliente = new Clientes();
    ClientesDao cliente_Dao = new ClientesDao();

    private int item = 0;
    String rol = rol_user;
    DefaultTableModel model = new DefaultTableModel();
    DefaultTableModel temp;

    public VentasController(Ventas venta, VentasDao venta_Dao, Principal vista) {
        this.venta = venta;
        this.venta_Dao = venta_Dao;
        this.vista = vista;

        //Botones Venta
        this.vista.btn_agregar_venta_producto.addActionListener(this);
        this.vista.btn_confirmar_venta.addActionListener(this);
        this.vista.btn_eliminar_venta.addActionListener(this);
        this.vista.btn_nueva_venta.addActionListener(this);
        this.vista.Tabla_ventas.addMouseListener(this);
        this.vista.txt_venta_producto_code.addKeyListener(this);
        this.vista.txt_venta_precio_producto.addKeyListener(this);
        this.vista.txt_venta_cedula_cliente.addKeyListener(this);
        this.vista.txt_venta_cantidad_producto.addKeyListener(this);
        this.vista.jLabel60.addMouseListener(this); //Ventas
        this.vista.jLabel9.addMouseListener(this);  //Reportes
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btn_confirmar_venta) {
            if (vista.Tabla_ventas.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "Debe agregar al menos un producto antes de confirmar la venta");
                return;
            }
            insertarVenta();

        } else if (e.getSource() == vista.btn_nueva_venta) {
            if (vista.Tabla_ventas.getRowCount() == 0
                    && vista.txt_venta_producto_name.getText().trim().isEmpty()
                    && vista.txt_venta_cantidad_producto.getText().trim().isEmpty()
                    && vista.txt_venta_precio_producto.getText().trim().isEmpty()
                    && vista.txt_venta_producto_id.getText().trim().isEmpty()
                    && vista.txt_venta_stock_producto.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Primero debes realizar o terminar una venta");
                return;
            }
            limpiarAllVentas();
            limpiarTablaTemp();

        } else if (e.getSource() == vista.btn_eliminar_venta) {
            model = (DefaultTableModel) vista.Tabla_ventas.getModel();

            if (vista.Tabla_ventas.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "Primero debes generar una venta");
                return;
            }

            int selectedRow = vista.Tabla_ventas.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Selecciona una fila para eliminar");
                return;
            }

            model.removeRow(selectedRow);
            calcularVenta();
            vista.txt_venta_producto_code.requestFocus();

        } else if (e.getSource() == vista.btn_agregar_venta_producto) {
            if (vista.txt_venta_producto_name.getText().trim().isEmpty()
                    || vista.txt_venta_cantidad_producto.getText().trim().isEmpty()
                    || vista.txt_venta_precio_producto.getText().trim().isEmpty()
                    || vista.txt_venta_producto_id.getText().trim().isEmpty()
                    || vista.txt_venta_stock_producto.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe agregar un producto para continuar con la venta");
                return;
            }

            int monto = Integer.parseInt(vista.txt_venta_cantidad_producto.getText());
            String producto_nombre = vista.txt_venta_producto_name.getText();
            double precio = Double.parseDouble(vista.txt_venta_precio_producto.getText());
            int venta_id = Integer.parseInt(vista.txt_venta_producto_id.getText());
            double subtotal = monto * precio;
            int stock = Integer.parseInt(vista.txt_venta_stock_producto.getText());
            String nombre_completo = vista.txt_venta_cliente_name.getText();

            if (stock >= monto) {
                item = item + 1;
                temp = (DefaultTableModel) vista.Tabla_ventas.getModel();

                for (int i = 0; i < vista.Tabla_ventas.getRowCount(); i++) {
                    if (vista.Tabla_ventas.getValueAt(i, 1).equals(producto_nombre)) {
                        JOptionPane.showMessageDialog(null, "El producto ya está registrado en la tabla de ventas");
                        return;
                    }
                }

                ArrayList list = new ArrayList();
                list.add(item);
                list.add(venta_id);
                list.add(producto_nombre);
                list.add(monto);
                list.add(precio);
                list.add(subtotal);
                list.add(nombre_completo);

                Object[] obj = new Object[6];
                obj[0] = list.get(1);
                obj[1] = list.get(2);
                obj[2] = list.get(3);
                obj[3] = list.get(4);
                obj[4] = list.get(5);
                obj[5] = list.get(6);

                temp.addRow(obj);
                calcularVenta();
                limpiarCamposVentas();
                vista.txt_venta_producto_code.requestFocus();
            } else {
                JOptionPane.showMessageDialog(null, "Stock no disponible");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Ingrese una cantidad");
        }
    }

    public void listAllVentas() {
        if (rol.equals("Administrador")) {
            List<Ventas> list = venta_Dao.listaAllVentasQuery();
            model = (DefaultTableModel) vista.Tabla_Reporte_Ventas.getModel();
            Object[] row = new Object[5];
            for (int i = 0; i < list.size(); i++) {
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getCliente_nombre();
                row[2] = list.get(i).getEmpleado_nombre();
                row[3] = list.get(i).getTotal();
                row[4] = list.get(i).getFecha_venta();
                model.addRow(row);
            }
            vista.Tabla_Reporte_Ventas.setModel(model);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == vista.jLabel60) {
            vista.jTabbedPane2.setSelectedIndex(2);
        } else if (e.getSource() == vista.jLabel9) {
            if (rol.equals("Administrador")) {
                vista.jTabbedPane2.setSelectedIndex(7);
                listAllVentas();
            } else {
                vista.jTabbedPane2.setEnabledAt(7, false);
                JOptionPane.showMessageDialog(null, "No tienes privilegios de administrador");
            }
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
        if (e.getSource() == vista.txt_venta_producto_code) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (!"".equals(vista.txt_venta_producto_code.getText())) {
                    int code = Integer.parseInt(vista.txt_venta_producto_code.getText());
                    producto = producto_dao.buscarCodigo(code);
                    if (producto.getNombre() != null) {
                        vista.txt_venta_producto_name.setText(producto.getNombre());
                        vista.txt_venta_producto_id.setText("" + producto.getId());
                        vista.txt_venta_stock_producto.setText("" + producto.getCantidad_producto());
                        vista.txt_venta_precio_producto.setText("" + producto.getPrecio_unitario());
                        vista.txt_venta_cantidad_producto.requestFocus();
                    } else {
                        JOptionPane.showMessageDialog(null, "No existe ningun producto con ese codigo");
                        limpiarCamposVentas();
                        vista.txt_venta_producto_code.requestFocus();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Ingrese el codigo del producto a vender");
                }
            }
        } else if (e.getSource() == vista.txt_venta_cedula_cliente) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (!"".equals(vista.txt_venta_cedula_cliente.getText())) {
                    int cliente_id = Integer.parseInt(vista.txt_venta_cedula_cliente.getText());
                    cliente = cliente_Dao.buscarClientes(cliente_id);
                    if (cliente.getNombre_completo() != null) {
                        vista.txt_venta_cliente_name.setText("" + cliente.getNombre_completo());
                    } else {
                        vista.txt_venta_cedula_cliente.setText("");
                        JOptionPane.showMessageDialog(null, "El cliente no existe");
                    }
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == vista.txt_venta_cantidad_producto) {
            int cantidad;
            double precio = Double.parseDouble(vista.txt_venta_precio_producto.getText());
            if (vista.txt_venta_cantidad_producto.getText().equals("")) {
                cantidad = 1;
                vista.txt_venta_precio_producto.setText("" + precio);
            } else {
                cantidad = Integer.parseInt(vista.txt_venta_cantidad_producto.getText());
                precio = Double.parseDouble(vista.txt_venta_precio_producto.getText());
                vista.txt_venta_subtotal_producto.setText("" + (cantidad * precio));
            }
        }
    }

    private void insertarVenta() {
        int cliente_id = Integer.parseInt(vista.txt_venta_cedula_cliente.getText());
        int empleado_id = EmpleadosDao.id_user;
        double total = Double.parseDouble(vista.txt_venta_monto_total.getText());
        if (venta_Dao.registrarVentaQuery(cliente_id, empleado_id, total)) {
            int venta_id = venta_Dao.VentaId();
            for (int i = 0; i < vista.Tabla_ventas.getRowCount(); i++) {
                int producto_id = Integer.parseInt(vista.Tabla_ventas.getValueAt(i, 0).toString());
                int venta_cantidad = Integer.parseInt(vista.Tabla_ventas.getValueAt(i, 2).toString());
                double venta_precio = Double.parseDouble(vista.Tabla_ventas.getValueAt(i, 3).toString());
                double venta_subtotal = venta_cantidad * venta_precio;
                venta_Dao.registrarVentaDetallesQuery(producto_id, venta_id, venta_cantidad, venta_precio, venta_subtotal);
                producto = producto_dao.buscarId(producto_id);
                int monto = producto.getCantidad_producto() - venta_cantidad;
                producto_dao.actualizarStockQuery(monto, producto_id);
            }
            JOptionPane.showMessageDialog(null, "Venta Generada");
            limpiarTablaTemp();
            limpiarAllVentas();
        }
    }

    private void calcularVenta() {
        double total = 0.0;
        int numRow = vista.Tabla_ventas.getRowCount();
        for (int i = 0; i < numRow; i++) {
            total = total + Double.parseDouble(String.valueOf(vista.Tabla_ventas.getValueAt(i, 4)));
        }
        vista.txt_venta_monto_total.setText("" + total);
    }

    public void limpiarTabla() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }

    public void limpiarTablaTemp() {
        for (int i = 0; i < temp.getRowCount(); i++) {
            temp.removeRow(i);
            i = i - 1;
        }
    }

    public void limpiarCamposVentas() {
        vista.txt_venta_producto_code.setText("");
        vista.txt_venta_producto_name.setText("");
        vista.txt_venta_cantidad_producto.setText("");
        vista.txt_venta_producto_id.setText("");
        vista.txt_venta_precio_producto.setText("");
        vista.txt_venta_subtotal_producto.setText("");
        vista.txt_venta_stock_producto.setText("");
    }

    public void limpiarAllVentas() {
        vista.txt_venta_producto_code.setText("");
        vista.txt_venta_producto_name.setText("");
        vista.txt_venta_cantidad_producto.setText("");
        vista.txt_venta_producto_id.setText("");
        vista.txt_venta_precio_producto.setText("");
        vista.txt_venta_subtotal_producto.setText("");
        vista.txt_venta_stock_producto.setText("");
        vista.txt_venta_cedula_cliente.setText("");
        vista.txt_venta_cliente_name.setText("");
        vista.txt_venta_monto_total.setText("");
    }

}
