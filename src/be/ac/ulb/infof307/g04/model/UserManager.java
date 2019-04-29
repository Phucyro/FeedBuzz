package be.ac.ulb.infof307.g04.model;

import io.jsondb.InvalidJsonDbApiUsageException;
import io.jsondb.JsonDBTemplate;

public class UserManager {

    private JsonDBTemplate jsonDBTemplate;

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
     * @param _username
     * @return inform if the user has been deleted
     */
    public boolean deleteUser(String _username) {
        DatabaseUser user = findUserByUsername(_username);

        try {
            this.jsonDBTemplate.remove(user, DatabaseUser.class);
            return true;
        } catch (InvalidJsonDbApiUsageException e){
            return false;
        }
    }


    /**
     * @param _username username to add
     * @return inform if the user has been added
     */
    public boolean addUser(String _username, String _password) {
        try {

            DatabaseUser user = new DatabaseUser();
            user.setPassword(_password);
            user.setUsername(_username);

            jsonDBTemplate.insert(user);
            return true;
        } catch (InvalidJsonDbApiUsageException e) {
            return false;
        }
    }

    public boolean addUser(DatabaseUser _user) {
        try {
            jsonDBTemplate.insert(_user);
            return true;
        } catch (InvalidJsonDbApiUsageException e) {
            return false;
        }
    }

    /**
     * search an user in the database
     * @param _username
     * @return found user
     */
    public DatabaseUser findUserByUsername(String _username){
        try {
            DatabaseUser user = jsonDBTemplate.findById(_username, DatabaseUser.class);
            return user;
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * search an user in the database
     * @param _username
     * @return found user
     */
    public DatabaseUser findUser(String _username, String _password){
        try {
            DatabaseUser user = jsonDBTemplate.findById(_username, DatabaseUser.class);
            if(user.getPassword() == _password.hashCode()){
                return user;
            }
            else{
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }




    public boolean existUsername(String _username){
        try {
            DatabaseUser user = jsonDBTemplate.findById(_username, DatabaseUser.class);
            if(user != null){
                return true;
            }
            else{
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }



    public boolean existUser(String _username, String _password){
        try {
            DatabaseUser user = jsonDBTemplate.findById(_username, DatabaseUser.class);
            if(user.getPassword() == _password.hashCode()){
                return true;
            }
            else{
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }



}
