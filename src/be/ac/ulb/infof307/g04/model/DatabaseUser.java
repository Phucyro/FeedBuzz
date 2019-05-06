package be.ac.ulb.infof307.g04.model;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;
import io.jsondb.annotation.Secret;

import java.io.Serializable;

@Document(collection = "users", schemaVersion = "1.0")
public class DatabaseUser implements Serializable {
    @Id
    @Secret
    private String username;
    @Secret
    private int hashedPassword;

    public DatabaseUser() {
    }

    public DatabaseUser(String _username, String _password) {
        this.username = _username;
        this.hashedPassword = _password.hashCode();
    }


    public DatabaseUser(DatabaseUser _user) {
        this.setUsername(_user.getUsername());
        this.setPassword(_user.getPassword());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String _username) {
        this.username = _username;
    }

    public int getPassword() {
        return hashedPassword;
    }

    public void setPassword(String _password) {
        this.hashedPassword = _password.hashCode();
    }
    public void setPassword(int _password) {
        this.hashedPassword = _password;
    }
}