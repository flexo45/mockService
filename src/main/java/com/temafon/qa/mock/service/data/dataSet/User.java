package com.temafon.qa.mock.service.data.dataSet;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = -8706689714326132798L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "login", unique = true, updatable = false)
    private String login;

    @Column(name = "password", updatable = false)
    private String password;

    @Column(name = "email")
    private String email;

    public User() {
    }

    public User(long id, String login, String password, String email) {
        this.setId(id);
        this.setLogin(login);
        this.setPassword(password);
        this.setEmail(email);
    }

    public User(String login, String password, String email) {
        this.setId(-1);
        this.setLogin(login);
        this.setPassword(password);
        this.setEmail(email);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return this.email; }

    public void setEmail(String email) { this.email = email; }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserDataSet{" +
                "id=" + id +
                ", login='" + login + '\'' +
                '}';
    }
}
