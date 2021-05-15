package hu.db.techshop.model;

import javax.validation.constraints.*;
import java.sql.Timestamp;

public class User {

    private int id;

    @NotEmpty(message = "A mező kitöltése kötelező!")
    private String firstname;

    @NotEmpty(message = "A mező kitöltése kötelező!")
    private String lastname;

    @NotEmpty(message = "A mező kitöltése kötelező!")
    @Email(message = "A megadott e-mail cím hibás!")
    private String email;

    @Size(min = 6, message = "A megadott jelszó túl rövid! (min. 6 karakter)")
    private String password;

    @Size(min = 6, message = "A megadott jelszó túl rövid! (min. 6 karakter)")
    private String passwordConfirmation;

    @AssertTrue(message = "Nem fogadta el az adatvédelmi tájékoztatót!")
    private boolean terms;

    private boolean admin;
    private Timestamp createdAt;

    public User() {
    }

    public User(int id, String firstname, String lastname) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public User(String firstname, String lastname, String email, String password, boolean admin) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.admin = admin;
    }

    public User(int id, String firstname, String lastname, String email, String password, boolean admin) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.admin = admin;
    }

    public User(int id, String firstname, String lastname, String email, String password, boolean admin, Timestamp createdAt) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.admin = admin;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public boolean isTerms() {
        return terms;
    }

    public void setTerms(boolean terms) {
        this.terms = terms;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
