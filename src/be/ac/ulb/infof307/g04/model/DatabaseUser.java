package be.ac.ulb.infof307.g04.model;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;
import io.jsondb.annotation.Secret;

import java.io.Serializable;

@Document(collection = "tags", schemaVersion = "1.0")
class DatabaseUser implements Serializable {
    @Id
    @Secret
    private String username;
    @Secret
    private String password;

    public DatabaseUser() {
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