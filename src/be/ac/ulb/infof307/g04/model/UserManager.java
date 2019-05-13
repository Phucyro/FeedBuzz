package be.ac.ulb.infof307.g04.model;

import io.jsondb.InvalidJsonDbApiUsageException;
import io.jsondb.JsonDBTemplate;

public class UserManager {

    private final JsonDBTemplate jsonDBTemplate;

    /**
     * @param database_path path to the database
     * @param _password password of the database
     */
    public UserManager(String database_path, String _password) {
        String baseScanPackage = "be.ac.ulb.infof307.g04.model";
        this.jsonDBTemplate = new JsonDBTemplate(database_path, baseScanPackage);

        if (!this.jsonDBTemplate.collectionExists(DatabaseUser.class)) {
            createCollection();
        }

    }

    public UserManager(String _databasePath) {
        this(_databasePath, "password");

    }

    private void createCollection() {
        jsonDBTemplate.createCollection(DatabaseUser.class);
    }



    /**
     * @param _username username to add
     */
    public void addUser(String _username, String _password) throws InvalidJsonDbApiUsageException {

        DatabaseUser user = new DatabaseUser();
        user.setPassword(_password);
        user.setUsername(_username);

        jsonDBTemplate.insert(user);
    }

    /**
     * Check if an username is already in the database
     * @param _username username to test
     * @return return true if the username is already used
     */
    public boolean existUsername(String _username){
        try {
            DatabaseUser user = jsonDBTemplate.findById(_username, DatabaseUser.class);
            return user != null;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * check if an user is already in the database
     * @param _username name of the user
     * @param _password password of the user
     * @return return true if the user is already in the database
     */
    public boolean existUser(String _username, String _password){
        try {
            DatabaseUser user = jsonDBTemplate.findById(_username, DatabaseUser.class);
            return user.getPassword() == _password.hashCode();
        } catch (Exception e) {
            return false;
        }
    }



}
