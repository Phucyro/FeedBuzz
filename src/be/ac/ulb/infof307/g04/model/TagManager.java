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
    private static final int DISLIKEWEIGHT = -1;
    private static final int LIKEWEIGHT = 1;
    private static final int SECWEIGHT = 1;
    private static final int VIEWWEIGHT = 1;
    private JsonDBTemplate jsonDBTemplate;

    /**
     * @param _databasePath
     *                  path to the database
     * @param _password
     *                  _password of the database
     */
    public TagManager(String _databasePath, String _password) {
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

    /**
     * add a _tag to the database
     * @param _tag
     *          _tag that will be added to the database
     * @return boolean to inform if the _tag has been added
     */
    public boolean addTag(DatabaseTag _tag){
        try {
            jsonDBTemplate.insert(_tag);
            return true;
        } catch (InvalidJsonDbApiUsageException e){
            return false;
        }
    }

    /**
     * delete a _tag from the database
     * @param _tag
     *          _tag that will be removed from the database
     * @return boolean to inform if the _tag has been deleted
     */
    public boolean deleteTag(DatabaseTag _tag){
        try {
            jsonDBTemplate.remove(_tag, DatabaseTag.class);
            update("_tag", _tag.getName(), "Default", DatabaseSource.class);
            update("category", _tag.getName(), "Default", DatabaseArticle.class);
            return true;
        } catch (InvalidJsonDbApiUsageException e){
            return false;
        }
    }

    /**
     * modify a _tag from the database with an other
     * @param _tag
     *          the _tag that will be modified
     * @param _newTag
     *          the _tag that will replace the original _tag
     * @return boolean to inform if the _tag has well been deleted
     */
    public boolean modifyTag(DatabaseTag _tag, DatabaseTag _newTag){
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
    private void update(String _key, String _oldValue, String _newValue, Class _entityClass){
        Update update = Update.update(_key, _newValue);
        String jxQuery = String.format("/.[%s='%s']", _key, _oldValue);
        jsonDBTemplate.findAllAndModify(jxQuery, update, _entityClass);
    }

    private void editScore(String _tagName, int _score){
        DatabaseTag toEdit = getTag(_tagName);
        if (toEdit != null){
            toEdit.setScore(toEdit.getScore() + _score);
            jsonDBTemplate.upsert(toEdit);
        }
    }

    private DatabaseTag getTag(String tagName) {
        try {
            return jsonDBTemplate.findById(tagName, DatabaseTag.class);
        } catch(InvalidJsonDbApiUsageException e){
            return null;
        }
    }

    /**
     * @return a list that contained all the tags
     */
    public ArrayList<DatabaseTag> getAll() {
        return (ArrayList<DatabaseTag>) jsonDBTemplate.findAll(DatabaseTag.class);
    }

    /**
     * delete all tags
     */
    public void deleteAll(){
        /*
         * remove all the tags in the database
         */
        ArrayList<DatabaseTag> tags = getAll();
        tags.forEach(tag -> deleteTag(tag));
    }

    public void removeDislike(String _tag) {
        editScore(_tag, -DISLIKEWEIGHT);
    }

    public void addDislike(String _tag) {
        editScore(_tag, DISLIKEWEIGHT);
    }

    public void removeLike(String _tag) {
        editScore(_tag, -LIKEWEIGHT);
    }

    public void addLike(String _tag) {
        editScore(_tag, LIKEWEIGHT);
    }

    public void addTime(String _tag, int sec) {
        editScore(_tag, sec* SECWEIGHT);
    }

    public void addView(String _tag) {
        editScore(_tag, VIEWWEIGHT);
    }
}
