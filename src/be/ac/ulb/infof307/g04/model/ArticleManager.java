package be.ac.ulb.infof307.g04.model;


import io.jsondb.InvalidJsonDbApiUsageException;
import io.jsondb.JsonDBTemplate;
import io.jsondb.crypto.CryptoUtil;
import io.jsondb.crypto.DefaultAESCBCCipher;
import io.jsondb.crypto.ICipher;
import java.util.ArrayList;

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
    private final SourceManager sourceManager;
    /**
     * Constructor with the path to the database and the _password
     * @param _databasePath path to the database
     * @param _password _password of the database
     */
    public ArticleManager(String _databasePath, String _password) {
        tagManager = new TagManager(_databasePath, _password);
        sourceManager = new SourceManager(_databasePath, _password);
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
        return replaceArticle(_article,to_replace); // TODO replace by upsert
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

    public void upsertArticle(DatabaseArticle _article){
        try {
            this.jsonDBTemplate.upsert(_article);
        } catch (InvalidJsonDbApiUsageException e){
        }
    }

    /**
     * Add an _article from the database
     * @param _article
     *              _article the will be added
     * @return inform if the _article has been added
     */
    public void addArticle(DatabaseArticle _article) {
        DatabaseArticle dbArticle = _article;
        try {
            jsonDBTemplate.insert(dbArticle);
        } catch (InvalidJsonDbApiUsageException e) {
        }
    }

    /**
     * search an article in the database
     * @param _link
     *          the _link of the article
     * @return found article
     */
    public DatabaseArticle findArticle(String _link){
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

    public DatabaseSource getArticleSource(DatabaseArticle article) {
        return sourceManager.findSource(article.getSourceUrl());
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

    public void dislikeArticle(DatabaseArticle _article){
        if (_article.getLikeState() == LIKED) {
            tagManager.removeLike(_article.getTags());
        }
        tagManager.addDislike(_article.getTags());
        _article.setLikeState(DISLIKED);
        this.upsertArticle(_article);
    }

    public void likeArticle(DatabaseArticle _article){
        if (_article.getLikeState() == DISLIKED) {
            tagManager.removeDislike(_article.getTags());
        }
        tagManager.addLike(_article.getTags());
        _article.setLikeState(LIKED);
        this.upsertArticle(_article);
    }

    public void setNeutralLike(DatabaseArticle _article){
        if (_article.getLikeState() == LIKED) {
            tagManager.removeLike(_article.getTags());
        } else if (_article.getLikeState() == DISLIKED) {
            tagManager.removeDislike(_article.getTags());
        }
        _article.setLikeState(NEUTRAL);
        this.upsertArticle(_article);
    }

    public ArrayList<DatabaseArticle> getSuggestion(String _tag){
        ArrayList<DatabaseArticle> suggestedArticles = new ArrayList<>();
        for (DatabaseArticle article: loadArticles()) {
            if(article.getTags().equals(_tag) && !article.getViewed()){
                suggestedArticles.add(article);
            }
        }
        return suggestedArticles;
    }

    public void addTimeWatched(DatabaseArticle _article, int _sec){
        tagManager.addTime(_article.getTags(), _sec);
    }

    public void openArticle(DatabaseArticle _article) {
        tagManager.addView(_article.getTags());
        _article.setViewed(true);
        upsertArticle(_article);

    }
}