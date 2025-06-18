package Controladores;

import Models.Empleados;
import Models.EmpleadosDao;
import Vistas.Login;
import Vistas.Principal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class LoginController implements ActionListener {

    //Encapsular variables
    private Empleados empleado;
    private EmpleadosDao empleado_dao;
    private Login login;

    public LoginController(Empleados empleado, EmpleadosDao empleado_dao, Login login) {
        this.empleado = empleado;
        this.empleado_dao = empleado_dao;
        this.login = login;
        this.login.btnLogin.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Obtener datos de la vista Login
        String user = login.txtUsername.getText().trim();
        String pass = String.valueOf(login.txtPassword.getPassword());
        if (e.getSource() == login.btnLogin) {
            if (!user.equals("") || !pass.equals("")) {
                empleado = empleado_dao.loginQuery(user, pass);
                JOptionPane.showMessageDialog(null, "Campos llenos, accediendo al sistema");
                if (empleado.getUsuario() != null) {
                    if (empleado.getRol().equals("Administrador")) {
                        Principal admin = new Principal();
                        admin.setVisible(true);
                    } else {
                        Principal aux = new Principal();
                        aux.setVisible(true);
                    }
                    this.login.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrecto");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Los campos o uno de los campos esta vacío");
            }
        }
    }

}
