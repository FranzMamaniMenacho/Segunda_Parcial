package FirstParcial.sis414.FirstParcial.entity;

import java.time.LocalDate;

public class Pago {
    private String id;
    private double monto;
    private LocalDate date;
    private String metodo;
    private String estado;

    public Pago() {
    }

    public Pago(String id, double monto, LocalDate date, String metodo, String estado) {
        this.id = id;
        this.monto = monto;
        this.date = date;
        this.metodo = metodo;
        this.estado = estado;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
