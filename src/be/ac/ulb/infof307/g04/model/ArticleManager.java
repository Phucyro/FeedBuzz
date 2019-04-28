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

/**
 * Class DatabaseArticle Manager, used to handle the display of all the articles
 * @see DatabaseArticle
 * @see DatabaseSource
 * @see DatabaseTag
 */

public class ArticleManager{

    private JsonDBTemplate jsonDBTemplate;

    public ArticleManager(String _databasePath, String _password) {
        /**
         * Constructor with the path to the database and the _password
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
            System.out.println(e);
            this.jsonDBTemplate = new JsonDBTemplate(_databasePath, baseScanPackage);
        }

        if (!this.jsonDBTemplate.collectionExists(DatabaseArticle.class)) {
            createCollection();
        }
        deleteExpired();

    }

    public ArticleManager(String _database_path) {
        /**
         * Constructor with only the path to the database
         * @param _database_path
         *                   path to the database
         */
        this(_database_path, "password");

    }

    private void createCollection() {
        jsonDBTemplate.createCollection(DatabaseArticle.class);
    }


    public boolean deleteArticle(DatabaseArticle _article) {
        /**
         * Delete an _article. For every deleted _article, we keep the url (will be used as a primary key)
         * so they won't be loaded again by the database
         * @param _article
         *              _article to delete
         * @return inform if the _article has been deleted
         */
            DatabaseArticle to_replace = new DatabaseArticle();
            to_replace.setLink(_article.getLink());
            to_replace.setDeleted(true);
            return replaceArticle(_article,to_replace);
    }


    private boolean replaceArticle(DatabaseArticle _article, DatabaseArticle _article2) {
        /**
         * replace an _article by another one
         * @param _article
         *              _article that will be removed
         * @param _article2
         *              _article that will replaced the first
         * @return inform if the _article has been replaced
         */

        try {
            this.jsonDBTemplate.remove(_article, DatabaseArticle.class);
            this.jsonDBTemplate.insert(_article2);
            return true;
        } catch (InvalidJsonDbApiUsageException e){
            return false;
        }
    }


    public void verifyArticles() throws IOException, ParserConfigurationException, SAXException, ParseException {
        /*
         * check the integrity of all articles. If not valid (because it was modified) -> try to correct it
         * replace it if possible or delete it
         */
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

    public boolean addArticle(DatabaseArticle _article) {
        /**
         * Add an _article from the database
         * @param _article
         *              _article the will be added
         * @return inform if the _article has been added
         */
        DatabaseArticle dbArticle = _article;
        try {
            jsonDBTemplate.insert(dbArticle);
            //System.out.println(dbArticle.getDescription());
            //System.out.println(dbArticle.getHtmlContent());
            return true;
        } catch (InvalidJsonDbApiUsageException e) {
            System.out.println("Erreur 2");
            return false;
        }
    }


    public DatabaseArticle findArticle(String _link){
        /**
         * search an article in the database
         * @param _link
         *          the _link of the article
         * @return found article
         */
        try {
            return jsonDBTemplate.findById(_link, DatabaseArticle.class);
        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<DatabaseArticle> loadArticles() {
        /**
         * @return list containing all the articles
         */
        return this.loadArticles("");
    }

    public ArrayList<DatabaseArticle> loadArticles(String _titleContains) {
        /**
         * search an article by its title
         * @param _titleContains
         *                  title of the article
         * @return list containing all the articles with a specific title
         */
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


    private void deleteExpired() {
        /**
         * delete the expired articles
         */
        if (jsonDBTemplate.collectionExists(DatabaseArticle.class)) {
            ArrayList<DatabaseArticle> articles = loadArticles();
            for (DatabaseArticle article : articles) {
                if (article.needToBeDeleted()) {
                    deleteArticle(article);
                }
            }
        }
    }


}