package FirstParcial.sis414.FirstParcial.entity;

public class Personal {
    private Long id;
    private String nombre;
    private String rol;
    private String CI;

    public Personal(String nombre, String rol, String CI) {
        this.nombre = nombre;
        this.rol = rol;
        this.CI = CI;
    }
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getCI() {return CI;}
    public void setCI(String CI) {this.CI = CI;}


}