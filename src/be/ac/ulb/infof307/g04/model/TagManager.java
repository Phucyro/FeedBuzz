package be.ac.ulb.infof307.g04.model;

import io.jsondb.InvalidJsonDbApiUsageException;
import io.jsondb.JsonDBTemplate;
import io.jsondb.crypto.CryptoUtil;
import io.jsondb.crypto.DefaultAESCBCCipher;
import io.jsondb.crypto.ICipher;
import io.jsondb.query.Update;

import java.util.ArrayList;

/**
 * Class TagManger to handle all the tags stored in the database (or even create new ones)
 * @see DatabaseTag
 */
public class TagManager {
    private JsonDBTemplate jsonDBTemplate;


    public TagManager(String database_path, String password) {
        /**
         * @param database_path
         *                  path to the database
         * @param password
         *                  password of the database
         */
        String baseScanPackage = "be.ac.ulb.infof307.g04.model";
        this.jsonDBTemplate = new JsonDBTemplate(database_path, baseScanPackage);

        try {
            String base64EncodedKey = CryptoUtil.generate128BitKey(password, password);
            ICipher newCipher = new DefaultAESCBCCipher(base64EncodedKey);
            this.jsonDBTemplate = new JsonDBTemplate(database_path, baseScanPackage, newCipher);
        } catch (Exception e){
            this.jsonDBTemplate = new JsonDBTemplate(database_path, baseScanPackage);
        }

        if (!this.jsonDBTemplate.collectionExists(DatabaseTag.class)) {
            create_collection();
        }
    }

    private void create_collection() {
        jsonDBTemplate.createCollection(DatabaseTag.class);
    }


    public boolean add_tag(DatabaseTag tag){
        /**
         * add a tag to the database
         * @param tag
         *          tag that will be added to the database
         * @return boolean to inform if the tag has been added
         */
        try {
            jsonDBTemplate.insert(tag);
            return true;
        } catch (InvalidJsonDbApiUsageException e){
            return false;
        }
    }


    public boolean delete_tag(DatabaseTag tag){
        /**
         * delete a tag from the database
         * @param tag
         *          tag that will be removed from the database
         * @return boolean to inform if the tag has been deleted
         */
        try {
            jsonDBTemplate.remove(tag, DatabaseTag.class);
            update("tag", tag.getName(), "Default", DatabaseSource.class);
            update("category", tag.getName(), "Default", DatabaseArticle.class);
            return true;
        } catch (InvalidJsonDbApiUsageException e){
            return false;
        }
    }

    public boolean modify_tag(DatabaseTag tag, DatabaseTag newTag){
        /**
         * modify a tag from the database with another
         * @param tag
         *          the tag that will be modified
         * @param newTag
         *          the tag that will replace the original tag
         * @return boolean to inform if the tag has well been deleted
         */
        try {
            update("name", tag.getName(), newTag.getName(), DatabaseTag.class);
            update("tag", tag.getName(), newTag.getName(), DatabaseSource.class);
            update("category", tag.getName(), newTag.getName(), DatabaseArticle.class);
            delete_tag(tag);
            return true;
        } catch(InvalidJsonDbApiUsageException e){
            delete_tag(tag);
            return false;
        }
    }

    private void update(String key, String oldValue, String newValue, Class entityClass){
        /**
         * update a value in a database
         * @param key
         *          the key of the field in the db
         * @param oldValue
         *          the old value that we want to change
         * @param newValue
         *          the new value
         * @param entityClass
         *          the database in which we work
         */
        Update update = Update.update(key, newValue);
        String jxQuery = String.format("/.[%s='%s']", key, oldValue);
        jsonDBTemplate.findAllAndModify(jxQuery, update, entityClass);
    }

    public ArrayList<DatabaseTag> get_all() {
        /**
         * @return a list that contained all the tags
         */
        return (ArrayList<DatabaseTag>) jsonDBTemplate.findAll(DatabaseTag.class);
    }

    public void delete_all(){
        /*
         * remove all the tags in the database
         */
        ArrayList<DatabaseTag> tags = get_all();
        tags.forEach(tag -> delete_tag(tag));
    }
}
