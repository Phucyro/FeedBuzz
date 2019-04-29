package be.ac.ulb.infof307.g04.model;

import be.ac.ulb.infof307.g04.model.DatabaseUser;
import io.jsondb.InvalidJsonDbApiUsageException;
import io.jsondb.JsonDBTemplate;
import io.jsondb.crypto.CryptoUtil;
import io.jsondb.crypto.DefaultAESCBCCipher;
import io.jsondb.crypto.ICipher;

import java.util.ArrayList;

public class UserManager {

    private JsonDBTemplate jsonDBTemplate;

    /**
     * @param database_path path to the database
     * @param password password of the database
     */
    public UserManager(String database_path, String password) {
        String baseScanPackage = "be.ac.ulb.infof307.g04.model";
        this.jsonDBTemplate = new JsonDBTemplate(database_path, baseScanPackage);

        if (!this.jsonDBTemplate.collectionExists(DatabaseSource.class)) {
            create_collection();
        }

    }

    public UserManager(String database_path) {
        this(database_path, "password");

    }

    private void create_collection() {
        jsonDBTemplate.createCollection(DatabaseUser.class);
    }

    /**
     * @param username
     * @return inform if the user has been deleted
     */
    public boolean delete_user(String username) {
        DatabaseUser user = findUser_by_username(username);

        try {
            this.jsonDBTemplate.remove(user, DatabaseUser.class);
            return true;
        } catch (InvalidJsonDbApiUsageException e){
            return false;
        }
    }


    /**
     * @param username username to add
     * @return inform if the user has been added
     */
    public boolean add_user(String username, String password) {
        try {

            DatabaseUser user = new DatabaseUser();
            user.setPassword(password);
            user.setUsername(username);

            System.out.println(username);
            jsonDBTemplate.insert(user);
            return true;
        } catch (InvalidJsonDbApiUsageException e) {
            System.out.println(e);
            return false;
        }
    }

    public boolean add_user(DatabaseUser user) {
        try {
            jsonDBTemplate.insert(user);
            return true;
        } catch (InvalidJsonDbApiUsageException e) {
            System.out.println(e);
            return false;
        }
    }








    /**
     * search an user in the database
     * @param username
     * @return found user
     */
    public DatabaseUser findUser_by_username(String username){
        try {
            DatabaseUser user = jsonDBTemplate.findById(username, DatabaseUser.class);
            return user;
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * search an user in the database
     * @param username
     * @return found user
     */
    public DatabaseUser findUser(String username, String password){
        try {
            DatabaseUser user = jsonDBTemplate.findById(username, DatabaseUser.class);
            if(user.getPassword() == password.hashCode()){
                return user;
            }
            else{
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }




    public boolean existUsername(String username){
        try {
            DatabaseUser user = jsonDBTemplate.findById(username, DatabaseUser.class);
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



    public boolean existUser(String username, String password){
        try {
            DatabaseUser user = jsonDBTemplate.findById(username, DatabaseUser.class);
            if(user.getPassword() == password.hashCode()){
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
