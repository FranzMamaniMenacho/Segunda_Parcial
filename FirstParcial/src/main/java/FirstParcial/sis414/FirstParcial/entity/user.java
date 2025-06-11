package FirstParcial.sis414.FirstParcial.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class user {

    @Id
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    public user() {}

    public user(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
