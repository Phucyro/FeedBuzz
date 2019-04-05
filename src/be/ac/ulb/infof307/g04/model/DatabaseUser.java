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
    private int hashed_password;

    public DatabaseUser() {
    }

    public DatabaseUser(String _username, String password) {
        this.username = _username;
        this.hashed_password = password.hashCode();
    }


    public DatabaseUser(DatabaseUser user) {
        this.setUsername(user.getUsername());
        this.setPassword(user.getPassword());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String _username) {
        this.username = _username;
    }

    public int getPassword() {
        return hashed_password;
    }

    public void setPassword(String _password) {
        this.hashed_password = _password.hashCode();
    }
    public void setPassword(int _password) {
        this.hashed_password = _password;
    }
}