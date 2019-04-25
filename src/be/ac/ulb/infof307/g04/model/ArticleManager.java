package be.ac.ulb.infof307.g04.model;

import be.ac.ulb.infof307.g04.controller.Article;
import be.ac.ulb.infof307.g04.controller.ArticleVerification;
import io.jsondb.InvalidJsonDbApiUsageException;
import io.jsondb.JsonDBTemplate;
import io.jsondb.crypto.CryptoUtil;
import io.jsondb.crypto.DefaultAESCBCCipher;
import io.jsondb.crypto.ICipher;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * Class Article Manager, used to handle the display of all the articles
 * @see DatabaseArticle
 * @see DatabaseSource
 * @see DatabaseTag
 */

public class ArticleManager{

    private JsonDBTemplate jsonDBTemplate;

    public ArticleManager(String database_path, String password) {
        /**
         * Constructor with the path to the database and the password
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
            System.out.println(e);
            this.jsonDBTemplate = new JsonDBTemplate(database_path, baseScanPackage);
        }

        if (!this.jsonDBTemplate.collectionExists(DatabaseArticle.class)) {
            create_collection();
        }
        deleteExpired();

    }

    public ArticleManager(String database_path) {
        /**
         * Constructor with only the path to the database
         * @param database_path
         *                   path to the database
         */
        this(database_path, "password");

    }

    private void create_collection() {
        jsonDBTemplate.createCollection(DatabaseArticle.class);
    }


    public boolean delete_article(Article article) {
        /**
         * Delete an article. For every deleted article, we keep the url (will be used as a primary key)
         * so they won't be loaded again by the database
         * @param article
         *              article to delete
         * @return inform if the article has been deleted
         */
            DatabaseArticle to_replace = new DatabaseArticle();
            to_replace.setLink(article.getLink());
            to_replace.setDeleted(true);
            return replace_article(article,to_replace);
    }


    public boolean replace_article(Article article, DatabaseArticle article2) {
        /**
         * replace an article by another one
         * @param article
         *              article that will be removed
         * @param article2
         *              article that will replaced the first
         * @return inform if the article has been replaced
         */

        try {
            this.jsonDBTemplate.remove(article, DatabaseArticle.class);
            this.jsonDBTemplate.insert(article2);
            return true;
        } catch (InvalidJsonDbApiUsageException e){
            return false;
        }
    }


    public void verify_articles() throws IOException, ParserConfigurationException, SAXException {
        /*
         * check the integrity of all articles. If not valid (because it was modified) -> try to correct it
         * replace it if possible or delete it
         */
        ArrayList<Article> articles = load_articles();
        for(Article article : articles){
            ArticleVerification article_verification = new ArticleVerification(article,article.getSource_url());
            if(!article_verification.is_valid()){
                if(article_verification.is_correctable()){
                    article_verification.correct_article();
                    replace_article(article, article_verification.get_article());
                }
                else{
                    delete_article(article);
                }
            }

        }
    }

    public boolean add_article(Article article) {
        /**
         * Add an article from the database
         * @param article
         *              article the will be added
         * @return inform if the article has been added
         */
        DatabaseArticle dbArticle = article;
        try {
            jsonDBTemplate.insert(dbArticle);
            return true;
        } catch (InvalidJsonDbApiUsageException e) {
            return false;
        }
    }


    public DatabaseArticle findArticle(String link){
        /**
         * search an article in the database
         * @param link
         *          the link of the article
         * @return found article
         */
        try {
            return jsonDBTemplate.findById(link, DatabaseArticle.class);
        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<Article> load_articles() {
        /**
         * @return list containing all the articles
         */
        return this.load_articles("");
    }

    public ArrayList<Article> load_articles(String title_contains) {
        /**
         * search an article by its title
         * @param title_contains
         *                  title of the article
         * @return list containing all the articles with a specific title
         */
        ArrayList<Article> result = new ArrayList<Article>();
        for (DatabaseArticle item : jsonDBTemplate.findAll(DatabaseArticle.class)) {
            if (!item.getDeleted()) {
                if (item.getTitle().toLowerCase().contains(title_contains.toLowerCase())) {
                    result.add(new Article(item));
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
            ArrayList<Article> articles = load_articles();
            for (Article article : articles) {
                if (article.need_to_be_deleted()) {
                    delete_article(article);
                }
            }
        }
    }


}