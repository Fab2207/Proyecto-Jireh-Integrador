package Controladores;

import Vistas.Principal;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SettingsControllers implements MouseListener {

    private Principal view;

    public SettingsControllers(Principal view) {
        this.view = view;

        this.view.jLabel3.addMouseListener(this); //Productos
        this.view.jLabel4.addMouseListener(this); //Compras
        this.view.jLabel60.addMouseListener(this); //Ventas
        this.view.jLabel5.addMouseListener(this); //Clientes
        this.view.jLabel6.addMouseListener(this); //Empleados
        this.view.jLabel7.addMouseListener(this); //Proveedores
        this.view.jLabel8.addMouseListener(this); //Categorias
        this.view.jLabel9.addMouseListener(this); //Reportes
        this.view.jLabel10.addMouseListener(this); //Ajustes
        
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

}
