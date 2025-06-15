package Models;

public class Ventas {

    private int id;
    private String fecha_venta;
    private double total;
    private int cliente_id;
    private String cliente_nombre;
    private int empleado_id;
    private String empleado_nombre;

    public Ventas() {
    }

    public Ventas(int id, String fecha_venta, double total, int cliente_id, String cliente_nombre, int empleado_id, String empleado_nombre) {
        this.id = id;
        this.fecha_venta = fecha_venta;
        this.total = total;
        this.cliente_id = cliente_id;
        this.cliente_nombre = cliente_nombre;
        this.empleado_id = empleado_id;
        this.empleado_nombre = empleado_nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha_venta() {
        return fecha_venta;
    }

    public void setFecha_venta(String fecha_venta) {
        this.fecha_venta = fecha_venta;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(int cliente_id) {
        this.cliente_id = cliente_id;
    }

    public String getCliente_nombre() {
        return cliente_nombre;
    }

    public void setCliente_nombre(String cliente_nombre) {
        this.cliente_nombre = cliente_nombre;
    }

    public int getEmpleado_id() {
        return empleado_id;
    }

    public void setEmpleado_id(int empleado_id) {
        this.empleado_id = empleado_id;
    }

    public String getEmpleado_nombre() {
        return empleado_nombre;
    }

    public void setEmpleado_nombre(String empleado_nombre) {
        this.empleado_nombre = empleado_nombre;
    }

}
