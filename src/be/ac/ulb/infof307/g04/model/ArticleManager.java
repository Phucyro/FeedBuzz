package be.ac.ulb.infof307.g04.model;

import be.ac.ulb.infof307.g04.controller.ArticleVerification;
import io.jsondb.InvalidJsonDbApiUsageException;
import io.jsondb.JsonDBTemplate;
import io.jsondb.crypto.CryptoUtil;
import io.jsondb.crypto.DefaultAESCBCCipher;
import io.jsondb.crypto.ICipher;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class DatabaseArticle Manager, used to handle the display of all the articles
 * @see DatabaseArticle
 * @see DatabaseSource
 * @see DatabaseTag
 */

public class ArticleManager{

    public static final int DISLIKED = -1;
    public static final int LIKED = 1;
    public static final int NEUTRAL = 0;
    private JsonDBTemplate jsonDBTemplate;
    private final TagManager tagManager;
    /**
     * Constructor with the path to the database and the _password
     * @param _databasePath path to the database
     * @param _password _password of the database
     */
    public ArticleManager(String _databasePath, String _password) {
        tagManager = new TagManager(_databasePath, _password);
        String baseScanPackage = "be.ac.ulb.infof307.g04.model";
        this.jsonDBTemplate = new JsonDBTemplate(_databasePath, baseScanPackage);

        try {
            String base64EncodedKey = CryptoUtil.generate128BitKey(_password, _password);
            ICipher newCipher = new DefaultAESCBCCipher(base64EncodedKey);
            this.jsonDBTemplate = new JsonDBTemplate(_databasePath, baseScanPackage, newCipher);
        } catch (Exception e){
            this.jsonDBTemplate = new JsonDBTemplate(_databasePath, baseScanPackage);
        }

        if (!this.jsonDBTemplate.collectionExists(DatabaseArticle.class)) {
            createCollection();
        }
        deleteExpired();

    }

    /**
     * Constructor with only the path to the database
     * @param _database_path path to the database
     */
    public ArticleManager(String _database_path) {
        this(_database_path, "password");

    }

    /**
     * create a new collection
     */
    private void createCollection() {
        jsonDBTemplate.createCollection(DatabaseArticle.class);
    }

    /**
     * Delete an _article. For every deleted _article, we keep the url (will be used as a primary key)
     * so they won't be loaded again by the database
     * @param _article _article to delete
     * @return inform if the _article has been deleted
     */
    public boolean deleteArticle(DatabaseArticle _article) {
        DatabaseArticle to_replace = new DatabaseArticle();
        to_replace.setLink(_article.getLink());
        to_replace.setDeleted(true);
        return replaceArticle(_article,to_replace);
    }

    /**
     * replace an _article by another one
     * @param _article _article that will be removed
     * @param _article2 _article that will replaced the first
     * @return inform if the _article has been replaced
     */
    private boolean replaceArticle(DatabaseArticle _article, DatabaseArticle _article2) {
        try {
            this.jsonDBTemplate.remove(_article, DatabaseArticle.class);
            this.jsonDBTemplate.insert(_article2);
            return true;
        } catch (InvalidJsonDbApiUsageException e){
            return false;
        }
    }

    /**
     * upsert an article in the database
     * @param _article article to upsert
     */
    private void upsertArticle(DatabaseArticle _article){
        this.jsonDBTemplate.upsert(_article);

    }

    /**
     * check the integrity of all articles. If not valid (because it was modified): try to correct it
     * replace it if possible or delete it
     */
    public void verifyArticles() throws IOException, ParserConfigurationException, SAXException, ParseException {

        ArrayList<DatabaseArticle> articles = loadArticles();
        for(DatabaseArticle article : articles){
            ArticleVerification articleVerification = new ArticleVerification(article,article.getSourceUrl());
            if(!articleVerification.isValid()){
                if(articleVerification.isCorrectable()){
                    articleVerification.correctArticle();
                    replaceArticle(article, articleVerification.getArticle());
                }
                else{
                    deleteArticle(article);
                }
            }

        }
    }

    /**
     * Add an _article from the database
     * @param _article
     *              _article the will be added
     */
    void addArticle(DatabaseArticle _article) throws InvalidJsonDbApiUsageException {
        jsonDBTemplate.insert(_article);
    }

    /**
     * search an article in the database
     * @param _link
     *          the _link of the article
     * @return found article
     */
    DatabaseArticle findArticle(String _link){
        try {
            return jsonDBTemplate.findById(_link, DatabaseArticle.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @return list containing all the articles
     */
    public ArrayList<DatabaseArticle> loadArticles() {
        return this.loadArticles("");
    }

    /**
     * search an article by its title
     * @param _titleContains title of the article
     * @return list containing all the articles with a specific title
     */
    public ArrayList<DatabaseArticle> loadArticles(String _titleContains) {
        ArrayList<DatabaseArticle> result = new ArrayList<>();
        for (DatabaseArticle item : jsonDBTemplate.findAll(DatabaseArticle.class)) {
            if (!item.getDeleted()) {
                if (item.getTitle().toLowerCase().contains(_titleContains.toLowerCase())) {
                    result.add(new DatabaseArticle(item));
                }
            }
        }
        return (result);
    }

    /**
     * delete the expired articles
     */
    private void deleteExpired() {
        if (jsonDBTemplate.collectionExists(DatabaseArticle.class)) {
            ArrayList<DatabaseArticle> articles = loadArticles();
            for (DatabaseArticle article : articles) {
                if (article.needToBeDeleted()) {
                    deleteArticle(article);
                }
            }
        }
    }

    /**
     * called when the dislike button is clicked
     * @param _article article to dislike
     */
    public void dislikeArticle(DatabaseArticle _article){
        if (_article.getLikeState() == LIKED) {
            tagManager.removeLike(_article.getTags());
        }
        tagManager.addDislike(_article.getTags());
        _article.setLikeState(DISLIKED);
        this.upsertArticle(_article);
    }

    /**
     * called when the like button is clicked
     * @param _article article to like
     */
    public void likeArticle(DatabaseArticle _article){
        if (_article.getLikeState() == DISLIKED) {
            tagManager.removeDislike(_article.getTags());
        }
        tagManager.addLike(_article.getTags());
        _article.setLikeState(LIKED);
        this.upsertArticle(_article);
    }

    /**
     * called when an article is neither liked and disliked
     * @param _article article to set to neutral
     */
    public void setNeutralLike(DatabaseArticle _article){
        if (_article.getLikeState() == LIKED) {
            tagManager.removeLike(_article.getTags());
        } else if (_article.getLikeState() == DISLIKED) {
            tagManager.removeDislike(_article.getTags());
        }
        _article.setLikeState(NEUTRAL);
        this.upsertArticle(_article);
    }

    /**
     * get all the suggested articles
     * @param _tag tag to get the suggestion from
     * @return array containing all the suggested articles
     */
    public ArrayList<DatabaseArticle> getSuggestion(String _tag){
        ArrayList<DatabaseArticle> suggestedArticles = new ArrayList<>();
        for (DatabaseArticle article: loadArticles()) {
            if(article.getTags().equals(_tag) && !article.getViewed()){
                suggestedArticles.add(article);
            }
        }
        return suggestedArticles;
    }

    /**
     * set the number of time passed on the article
     * @param _article article the user was looking at
     * @param _sec time passed on the article
     */
    public void addTimeWatched(DatabaseArticle _article, int _sec){
        tagManager.addTime(_article.getTags(), _sec);
    }

    /**
     * open a specific article
     * @param _article article to open
     */
    public void openArticle(DatabaseArticle _article) {
        tagManager.addView(_article.getTags());
        _article.setViewed(true);
        upsertArticle(_article);

    }
}