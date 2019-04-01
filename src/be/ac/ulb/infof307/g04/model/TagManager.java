package be.ac.ulb.infof307.g04.model;

import io.jsondb.InvalidJsonDbApiUsageException;
import io.jsondb.JsonDBTemplate;
import io.jsondb.crypto.CryptoUtil;
import io.jsondb.crypto.DefaultAESCBCCipher;
import io.jsondb.crypto.ICipher;
import io.jsondb.query.Update;

import java.util.ArrayList;

/**
 *
 */
public class TagManager {
    private JsonDBTemplate jsonDBTemplate;

    /**
     * @param database_path path to the database
     * @param password password of the database
     */
    public TagManager(String database_path, String password) {
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

    /**
     * add a tag to the database
     * @param tag tag that will be added to the database
     * @return boolean to inform if the tag has been added
     */
    public boolean add_tag(DatabaseTag tag){
        try {
            jsonDBTemplate.insert(tag);
            return true;
        } catch (InvalidJsonDbApiUsageException e){
            return false;
        }
    }

    /**
     * delete a tag from the database
     * @param tag tag that will be removed from the database
     * @return boolean to inform if the tag has been deleted
     */
    public boolean delete_tag(DatabaseTag tag){
        try {
            jsonDBTemplate.remove(tag, DatabaseTag.class);
            update("tag", tag.getName(), "Default", DatabaseSource.class);
            update("category", tag.getName(), "Default", DatabaseArticle.class);
            return true;
        } catch (InvalidJsonDbApiUsageException e){
            return false;
        }
    }

    /**
     * modify a tag from the database with another
     * @param tag the tag that will be modified
     * @param newTag the tag that will replace the original tag
     * @return boolean to inform if the tag has well been deleted
     */
    public boolean modify_tag(DatabaseTag tag, DatabaseTag newTag){
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

    /**
     * update a value in a database
     * @param key the key of the field in the db
     * @param oldValue the old value that we want to change
     * @param newValue the new value
     * @param entityClass the database in which we work
     */
    private void update(String key, String oldValue, String newValue, Class entityClass){
        Update update = Update.update(key, newValue);
        String jxQuery = String.format("/.[%s='%s']", key, oldValue);
        jsonDBTemplate.findAllAndModify(jxQuery, update, entityClass);
    }

    /**
     * @return a list that contained all the tags
     */
    public ArrayList<DatabaseTag> get_all() {
        return (ArrayList<DatabaseTag>) jsonDBTemplate.findAll(DatabaseTag.class);
    }

    /**
     * remove all the tags in the database
     */
    public void delete_all(){
        ArrayList<DatabaseTag> tags = get_all();
        tags.forEach(tag -> delete_tag(tag));
    }
}
