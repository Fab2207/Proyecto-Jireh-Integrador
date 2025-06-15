package Models;

public class Empleados {

    private int id;
    private String nombre;
    private String usuario;
    private String direccion;
    private String celular;
    private String correo_electronico;
    private String contraseña;
    private String rol;
    private String crear_usuario;
    private String actualizar_usuario;

    public Empleados() {
    }

    public Empleados(int id, String nombre, String usuario, String direccion, String celular, String correo_electronico, String contraseña, String rol, String crear_usuario, String actualizar_usuario) {
        this.id = id;
        this.nombre = nombre;
        this.usuario = usuario;
        this.direccion = direccion;
        this.celular = celular;
        this.correo_electronico = correo_electronico;
        this.contraseña = contraseña;
        this.rol = rol;
        this.crear_usuario = crear_usuario;
        this.actualizar_usuario = actualizar_usuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCorreo_electronico() {
        return correo_electronico;
    }

    public void setCorreo_electronico(String correo_electronico) {
        this.correo_electronico = correo_electronico;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getCrear_usuario() {
        return crear_usuario;
    }

    public void setCrear_usuario(String crear_usuario) {
        this.crear_usuario = crear_usuario;
    }

    public String getActualizar_usuario() {
        return actualizar_usuario;
    }

    public void setActualizar_usuario(String actualizar_usuario) {
        this.actualizar_usuario = actualizar_usuario;
    }

    
}
