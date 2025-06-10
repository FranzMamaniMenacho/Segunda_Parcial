package FirstParcial.sis414.FirstParcial.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="pago")

public class Pago {
    public String getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private Double monto;
    private LocalDate date;
    private String metodo;
    private String estado;

    public Pago(String id, Double monto, LocalDate date, String metodo, String estado) {
        this.id = id;
        this.monto = monto;
        this.date = date;
        this.metodo = metodo;
        this.estado = estado;
    }


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
