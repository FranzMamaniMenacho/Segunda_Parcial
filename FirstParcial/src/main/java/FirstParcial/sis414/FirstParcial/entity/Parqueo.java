package FirstParcial.sis414.FirstParcial.entity;

public class Parqueo {
    private Long id;
    private String estado;
    private double precioPorNoche;
    private String marca;
    private String color;

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    private String placa;

    public Parqueo(Long id,String estado, double precioPorNoche, String marca, String color, String placa) {
        this.id = id;
        this.estado = estado;
        this.precioPorNoche = precioPorNoche;
        this.marca = marca;
        this.color = color;
        this.placa = placa;
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

    public double getPrecioPorNoche() {
        return precioPorNoche;
    }

    public void setPrecioPorNoche(double precioPorNoche) {
        this.precioPorNoche = precioPorNoche;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
