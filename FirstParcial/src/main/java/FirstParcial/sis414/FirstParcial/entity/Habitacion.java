package FirstParcial.sis414.FirstParcial.entity;

public class Habitacion {
    private Long id;
    private String tipoHabitacion;
    private String estado;
    private double precioNoche;

    public Habitacion(Long id, String tipoHabitacion, String estado, double precioNoche) {
        this.id = id;
        this.tipoHabitacion = tipoHabitacion;
        this.estado= estado;
        this.precioNoche = precioNoche;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipoHabitacion() {
        return tipoHabitacion;
    }

    public void setTipoHabitacion(String tipoHabitacion) {
        this.tipoHabitacion = tipoHabitacion;
    }


    public double getPrecioNoche() {
        return precioNoche;
    }

    public void setPrecioNoche(double precioNoche) {
        this.precioNoche = precioNoche;
    }
}
