package be.ac.ulb.infof307.g04.model;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

import java.io.Serializable;

@Document(collection = "users", schemaVersion = "1.0")
public class DatabaseUser implements Serializable {
    @Id
    private String username;
    private int hashedPassword;

    public DatabaseUser() {
    }

    /**
     * Constructor of Database User
     *
     * @param _username username of the user
     * @param _password password of the user
     */
    public DatabaseUser(String _username, String _password) {
        this.username = _username;
        this.hashedPassword = _password.hashCode();
    }

    /**
     * @param _user user in the database
     */
    public DatabaseUser(DatabaseUser _user) {
        this.setUsername(_user.getUsername());
        this.setPassword(_user.getPassword());
    }

    /**
     * @return name of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * When a user determine his username
     *
     * @param _username name that the user will use
     */
    public void setUsername(String _username) {
        this.username = _username;
    }

    /**
     * @return password of the user
     */
    public int getPassword() {
        return hashedPassword;
    }

    /**
     * @param _password password that the user will choose (only numbers)
     */
    public void setPassword(int _password) {
        this.hashedPassword = _password;
    }

    /**
     * @param _password password that the user will choose
     */
    public void setPassword(String _password) {
        this.hashedPassword = _password.hashCode();
    }
}