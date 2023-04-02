package hu.me.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Felhasznalok")
public class User {

    @Id
    private Long id;
    private String fullName;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Embedded
    private Credentials credentials;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public User(long id, String name, Role role, Credentials credentials) {
        this.id = id;
        this.fullName = name;
        this.role = role;
        this.credentials = credentials;
    }

}
