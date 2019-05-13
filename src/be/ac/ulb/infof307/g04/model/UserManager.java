package be.ac.ulb.infof307.g04.model;

import io.jsondb.InvalidJsonDbApiUsageException;
import io.jsondb.JsonDBTemplate;
import io.jsondb.crypto.CryptoUtil;
import io.jsondb.crypto.DefaultAESCBCCipher;
import io.jsondb.crypto.ICipher;

public class UserManager {

    private JsonDBTemplate jsonDBTemplate;

    /**
     * @param _databasePath path to the database
     * @param _password     password of the database
     */
    public UserManager(String _databasePath, String _password) {
        String baseScanPackage = "be.ac.ulb.infof307.g04.model";
        this.jsonDBTemplate = new JsonDBTemplate(_databasePath, baseScanPackage);

        try {
            String base64EncodedKey = CryptoUtil.generate128BitKey(_password, _password);
            ICipher newCipher = new DefaultAESCBCCipher(base64EncodedKey);
            this.jsonDBTemplate = new JsonDBTemplate(_databasePath, baseScanPackage, newCipher);
        } catch (Exception e) {
            this.jsonDBTemplate = new JsonDBTemplate(_databasePath, baseScanPackage);
        }

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
     * @param _username username of the user that will be deleted
     * @return inform if the user has been deleted
     */
    public boolean deleteUser(String _username) {
        DatabaseUser user = findUserByUsername(_username);

        try {
            this.jsonDBTemplate.remove(user, DatabaseUser.class);
            return true;
        } catch (InvalidJsonDbApiUsageException e) {
            return false;
        }
    }


    /**
     * @param _username username to add
     */
    public void addUser(String _username, String _password) {
        try {

            DatabaseUser user = new DatabaseUser();
            user.setPassword(_password);
            user.setUsername(_username);

            jsonDBTemplate.insert(user);
        } catch (InvalidJsonDbApiUsageException e) {
        }
    }

    /**
     * add an user in the db
     *
     * @param _user user to add
     * @return return true if the insert worked well
     */
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
     *
     * @param _username name of the user that we are looking for
     * @return found user
     */
    private DatabaseUser findUserByUsername(String _username) {
        try {
            return jsonDBTemplate.findById(_username, DatabaseUser.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * search an user in the database
     *
     * @param _username name of the user that we are looking for
     * @return found user
     */
    public DatabaseUser findUser(String _username, String _password) {
        try {
            DatabaseUser user = jsonDBTemplate.findById(_username, DatabaseUser.class);
            if (user.getPassword() == _password.hashCode()) {
                return user;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Check if an username is already in the database
     *
     * @param _username username to test
     * @return return true if the username is already used
     */
    public boolean existUsername(String _username) {
        try {
            DatabaseUser user = jsonDBTemplate.findById(_username, DatabaseUser.class);
            return user != null;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * check if an user is already in the database
     *
     * @param _username name of the user
     * @param _password password of the user
     * @return return true if the user is already in the database
     */
    public boolean existUser(String _username, String _password) {
        try {
            DatabaseUser user = jsonDBTemplate.findById(_username, DatabaseUser.class);
            return user.getPassword() == _password.hashCode();
        } catch (Exception e) {
            return false;
        }
    }

}
