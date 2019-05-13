package be.ac.ulb.infof307.g04.model;

import io.jsondb.InvalidJsonDbApiUsageException;
import io.jsondb.JsonDBTemplate;
import io.jsondb.crypto.CryptoUtil;
import io.jsondb.crypto.DefaultAESCBCCipher;
import io.jsondb.crypto.ICipher;
import io.jsondb.query.Update;

import java.util.ArrayList;
import java.util.Date;

/**
 * Class TagManger to handle all the tags stored in the database (or even create new ones)
 *
 * @see DatabaseTag
 */
public class TagManager {
    private static final int DISLIKE_WEIGHT = -100;
    private static final int LIKE_WEIGHT = 100;
    private static final int SEC_WEIGHT = 1;
    private static final int VIEW_WEIGHT = 20;
    private static final int DAY_WEIGHT = 5;
    private JsonDBTemplate jsonDBTemplate;


    /**
     * @param _databasePath path to the database
     * @param _password     _password of the database
     */
    public TagManager(String _databasePath, String _password) {
        String baseScanPackage = "be.ac.ulb.infof307.g04.model";
        this.jsonDBTemplate = new JsonDBTemplate(_databasePath, baseScanPackage);
        try {
            String base64EncodedKey = CryptoUtil.generate128BitKey(_password, _password);
            ICipher newCipher = new DefaultAESCBCCipher(base64EncodedKey);
            this.jsonDBTemplate = new JsonDBTemplate(_databasePath, baseScanPackage, newCipher);
        } catch (Exception e) {
            this.jsonDBTemplate = new JsonDBTemplate(_databasePath, baseScanPackage);
        }
        if (!this.jsonDBTemplate.collectionExists(DatabaseTag.class)) {
            createCollection();
        }
        actualizeScore();
    }

    private void createCollection() {
        jsonDBTemplate.createCollection(DatabaseTag.class);
    }

    /**
     * Add a _tag to the database
     *
     * @param _tag _tag that will be added to the database
     */
    void addTag(DatabaseTag _tag) {
        try {
            _tag.setLastActualisationDate(new Date());
            jsonDBTemplate.insert(_tag);
        } catch (InvalidJsonDbApiUsageException ignored) {
        }
    }

    /**
     * Delete a _tag from the database
     *
     * @param _tag _tag that will be removed from the database
     */
    void deleteTag(DatabaseTag _tag) throws InvalidJsonDbApiUsageException {
        jsonDBTemplate.remove(_tag, DatabaseTag.class);
        update("_tag", _tag.getName(), "Default", DatabaseSource.class);
        update("category", _tag.getName(), "Default", DatabaseArticle.class);
    }


    /**
     * Modify a _tag from the database with an other
     *
     * @param _newTag the _tag that will replace the original _tag
     */
    public void modifyTag(Object _newTag) {
        try {
            this.jsonDBTemplate.upsert(_newTag);
        } catch (InvalidJsonDbApiUsageException ignored) {
        }

    }

    /**
     * Update a value in a database
     *
     * @param _key         the _key of the field in the db
     * @param _oldValue    the old value that we want to change
     * @param _newValue    the new value
     * @param _entityClass the database in which we work
     */
    private void update(String _key, String _oldValue, String _newValue, Class _entityClass) {
        Update update = Update.update(_key, _newValue);
        String jxQuery = String.format("/.[%s='%s']", _key, _oldValue);
        jsonDBTemplate.findAllAndModify(jxQuery, update, _entityClass);
    }

    /**
     * Edit the score of a specific tag
     *
     * @param _tagName tag that will be edited
     * @param _score   new score of the tag
     */
    private void editScore(String _tagName, int _score) {
        DatabaseTag toEdit = getTag(_tagName);
        if (toEdit != null) {
            toEdit.setScore(toEdit.getScore() + _score);
            jsonDBTemplate.upsert(toEdit);
        }
    }

    /**
     * Edit score if the last actualisation date was longer than 1 day
     */
    private void actualizeScore() {
        Date current_date = new Date();
        ArrayList<DatabaseTag> tags_list = getAll();
        for (DatabaseTag checkedTag : tags_list) {
            long diff = current_date.getTime() - checkedTag.getLastActualisationDate().getTime();
            int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
            if (diffDays >= 1) {
                float current_score = checkedTag.getScore();
                current_score = Math.max(current_score - ((current_score / 100) * (DAY_WEIGHT * diffDays)), 0);
                checkedTag.setScore(current_score);
                checkedTag.setLastActualisationDate(current_date);
                jsonDBTemplate.upsert(checkedTag);
            }
        }
    }


    /**
     * get a tag of the database from his name
     *
     * @param _tagManager name of the tag
     * @return tag we are looking for
     */
    private DatabaseTag getTag(String _tagManager) {
        try {
            return jsonDBTemplate.findById(_tagManager, DatabaseTag.class);
        } catch (InvalidJsonDbApiUsageException e) {
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
    void deleteAll() {
        ArrayList<DatabaseTag> tags = getAll();
        tags.forEach(this::deleteTag);
    }

    /**
     * get the tag with the highest score
     *
     * @return the best tag
     */
    public String getBest() {
        String best = "";
        float maxValue = -1;
        for (DatabaseTag tag : getAll()) {
            //The score of the tag is multiplied by the user preference to promote subjects that fits to user preferences more
            if (tag.getScore()*tag.getUserPreference() > maxValue) {
                maxValue = tag.getScore();
                best = tag.getName();
            }
        }
        return best;
    }

    /**
     * remove a dislike (add point)
     *
     * @param _tag tag to apply the action to
     */
    void removeDislike(String _tag) {
        editScore(_tag, -DISLIKE_WEIGHT);
    }

    /**
     * add a dislike (remove point)
     *
     * @param _tag tag to apply the action to
     */
    void addDislike(String _tag) {
        editScore(_tag, DISLIKE_WEIGHT);
    }

    /**
     * remove a like (remove point)
     *
     * @param _tag tag to apply the action to
     */
    void removeLike(String _tag) {
        editScore(_tag, -LIKE_WEIGHT);
    }

    /**
     * add a like (add point)
     *
     * @param _tag tag to apply the action to
     */
    void addLike(String _tag) {
        editScore(_tag, LIKE_WEIGHT);
    }

    /**
     * add point from the time passed on an article of a specific tag
     *
     * @param _tag tag to apply the action to
     */
    void addTime(String _tag, int sec) {
        editScore(_tag, sec * SEC_WEIGHT);
    }

    /**
     * add point from the opening of an article
     *
     * @param _tag tag to apply the action to
     */
    void addView(String _tag) {
        editScore(_tag, VIEW_WEIGHT);
    }
}
