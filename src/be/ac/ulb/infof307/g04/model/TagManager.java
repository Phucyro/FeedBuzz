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


    public TagManager(String _databasePath, String _password) {
        /**
         * @param _databasePath
         *                  path to the database
         * @param _password
         *                  _password of the database
         */
        String baseScanPackage = "be.ac.ulb.infof307.g04.model";
        this.jsonDBTemplate = new JsonDBTemplate(_databasePath, baseScanPackage);

        try {
            String base64EncodedKey = CryptoUtil.generate128BitKey(_password, _password);
            ICipher newCipher = new DefaultAESCBCCipher(base64EncodedKey);
            this.jsonDBTemplate = new JsonDBTemplate(_databasePath, baseScanPackage, newCipher);
        } catch (Exception e){
            this.jsonDBTemplate = new JsonDBTemplate(_databasePath, baseScanPackage);
        }

        if (!this.jsonDBTemplate.collectionExists(DatabaseTag.class)) {
            createCollection();
        }
    }

    private void createCollection() {
        jsonDBTemplate.createCollection(DatabaseTag.class);
    }


    public boolean addTag(DatabaseTag _tag){
        /**
         * add a _tag to the database
         * @param _tag
         *          _tag that will be added to the database
         * @return boolean to inform if the _tag has been added
         */
        try {
            jsonDBTemplate.insert(_tag);
            return true;
        } catch (InvalidJsonDbApiUsageException e){
            return false;
        }
    }


    public boolean deleteTag(DatabaseTag _tag){
        /**
         * delete a _tag from the database
         * @param _tag
         *          _tag that will be removed from the database
         * @return boolean to inform if the _tag has been deleted
         */
        try {
            jsonDBTemplate.remove(_tag, DatabaseTag.class);
            update("_tag", _tag.getName(), "Default", DatabaseSource.class);
            update("category", _tag.getName(), "Default", DatabaseArticle.class);
            return true;
        } catch (InvalidJsonDbApiUsageException e){
            return false;
        }
    }

    public boolean modifyTag(DatabaseTag _tag, DatabaseTag _newTag){
        /**
         * modify a _tag from the database with another
         * @param _tag
         *          the _tag that will be modified
         * @param _newTag
         *          the _tag that will replace the original _tag
         * @return boolean to inform if the _tag has well been deleted
         */
        try {
            update("name", _tag.getName(), _newTag.getName(), DatabaseTag.class);
            update("_tag", _tag.getName(), _newTag.getName(), DatabaseSource.class);
            update("category", _tag.getName(), _newTag.getName(), DatabaseArticle.class);
            deleteTag(_tag);
            return true;
        } catch(InvalidJsonDbApiUsageException e){
            deleteTag(_tag);
            return false;
        }
    }

    private void update(String _key, String _oldValue, String _newValue, Class _entityClass){
        /**
         * update a value in a database
         * @param _key
         *          the _key of the field in the db
         * @param _oldValue
         *          the old value that we want to change
         * @param _newValue
         *          the new value
         * @param _entityClass
         *          the database in which we work
         */
        Update update = Update.update(_key, _newValue);
        String jxQuery = String.format("/.[%s='%s']", _key, _oldValue);
        jsonDBTemplate.findAllAndModify(jxQuery, update, _entityClass);
    }

    public ArrayList<DatabaseTag> getAll() {
        /**
         * @return a list that contained all the tags
         */
        return (ArrayList<DatabaseTag>) jsonDBTemplate.findAll(DatabaseTag.class);
    }

    public void deleteAll(){
        /*
         * remove all the tags in the database
         */
        ArrayList<DatabaseTag> tags = getAll();
        tags.forEach(tag -> deleteTag(tag));
    }
}
