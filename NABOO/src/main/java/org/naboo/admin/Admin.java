package org.naboo.admin;

import jakarta.persistence.*;

//Classe Admin contenente i campi della tabella admin nel database
@Entity
@Table(name = "admin")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_admin;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "login")
    private boolean login;

    public Admin() {

    }

    public int getId_admin() {
        return this.id_admin;
    }

    public void setId_admin(int id_admin) {
        this.id_admin = id_admin;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getLogin() {
        return this.login;
    }

    public void setLogin(boolean password) {
        this.login = login;
    }
}
