
package FirstParcial.sis414.FirstParcial.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reserv")
public class Reserv {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente")
    private Cliente clienteID;

    @ManyToOne
    @JoinColumn(name = "habitacionId")
    private Habitacion habitacionID;


    @OneToOne
    @JoinColumn(name = "pagoId")
    private Pago pagoID;

    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;

    public Reserv(Cliente clienteID, Habitacion habitacionID, LocalDate fechaEntrada, LocalDate fechaSalida, Pago pagoID) {
        this.clienteID = clienteID;
        this.habitacionID = habitacionID;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.pagoID = pagoID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Cliente getCliente() {
        return clienteID;
    }

    public void setCliente(Cliente cliente) {
        this.clienteID = cliente;
    }

    public Habitacion getHabitacion() {
        return habitacionID;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacionID = habitacion;
    }

    public Pago getPago() {
        return pagoID;
    }

    public void setPago(Pago pago) {
        this.pagoID = pago;
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

}