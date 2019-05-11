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
    public void addTag(DatabaseTag _tag){
        try {
            jsonDBTemplate.insert(_tag);
        } catch (InvalidJsonDbApiUsageException e){
        }
    }

    /**
     * delete a _tag from the database
     * @param _tag
     *          _tag that will be removed from the database
     * @return boolean to inform if the _tag has been deleted
     */
    public void deleteTag(DatabaseTag _tag) throws InvalidJsonDbApiUsageException {
        jsonDBTemplate.remove(_tag, DatabaseTag.class);
        update("_tag", _tag.getName(), "Default", DatabaseSource.class);
        update("category", _tag.getName(), "Default", DatabaseArticle.class);
    }

    /**
     * modify a _tag from the database with an other
     * @param _tag
     *          the _tag that will be modified
     * @param _newTag
     *          the _tag that will replace the original _tag
     */
    public void modifyTag(DatabaseTag _tag, DatabaseTag _newTag){
        try {
            update("name", _tag.getName(), _newTag.getName(), DatabaseTag.class);
            update("_tag", _tag.getName(), _newTag.getName(), DatabaseSource.class);
            update("category", _tag.getName(), _newTag.getName(), DatabaseArticle.class);
            deleteTag(_tag);
        } catch(InvalidJsonDbApiUsageException e){
            deleteTag(_tag);
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
    void deleteAll(){
        ArrayList<DatabaseTag> tags = getAll();
        tags.forEach(this::deleteTag);
    }

    public String getBest(){
        String best = "";
        int maxValue = -1;
        for(DatabaseTag tag: getAll()){
            if (tag.getScore() > maxValue){
                maxValue = tag.getScore();
                best = tag.getName();
            }
        }
        return best;
    }

    void removeDislike(String _tag) {
        editScore(_tag, -DISLIKEWEIGHT);
    }

    void addDislike(String _tag) {
        editScore(_tag, DISLIKEWEIGHT);
    }

    void removeLike(String _tag) {
        editScore(_tag, -LIKEWEIGHT);
    }

    void addLike(String _tag) {
        editScore(_tag, LIKEWEIGHT);
    }

    void addTime(String _tag, int sec) {
        editScore(_tag, sec* SECWEIGHT);
    }

    void addView(String _tag) {
        editScore(_tag, VIEWWEIGHT);
    }
}
