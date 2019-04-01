package be.ac.ulb.infof307.g04.model;

import be.ac.ulb.infof307.g04.controller.Article;
import be.ac.ulb.infof307.g04.controller.ArticleVerification;
import io.jsondb.InvalidJsonDbApiUsageException;
import io.jsondb.JsonDBTemplate;
import io.jsondb.crypto.CryptoUtil;
import io.jsondb.crypto.DefaultAESCBCCipher;
import io.jsondb.crypto.ICipher;

import java.util.ArrayList;

public class ArticleManager{

    private JsonDBTemplate jsonDBTemplate;

    /**
     * @param database_path path to the database
     * @param password password of the database
     */
    public ArticleManager(String database_path, String password) {
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
        this(database_path, "password");

    }

    private void create_collection() {
        jsonDBTemplate.createCollection(DatabaseArticle.class);
    }

    /**
     * @param article article to delete
     * @return inform if the article has been deleted
     */
    public boolean delete_article(Article article) {
        /* Pour chaque article supprimé on garde uniquement son url qui sert de clé primaire.
         * Ainsi, les articles supprimés ne seront pas retéléchargés dans la DB*/
            DatabaseArticle to_replace = new DatabaseArticle();
            to_replace.setLink(article.getLink());
            to_replace.setDeleted(true);
            return replace_article(article,to_replace);
    }


    /**
     * replace an article by another
     * @param article article that will be removed
     * @param article2 article that will replaced the first
     * @return inform if the article has been replaced
     */
    public boolean replace_article(Article article, DatabaseArticle article2) {
        /* On remplace un article dans la database par un second article*/
        try {
            this.jsonDBTemplate.remove(article, DatabaseArticle.class);
            this.jsonDBTemplate.insert(article2);
            return true;
        } catch (InvalidJsonDbApiUsageException e){
            return false;
        }
    }


    /**
     * check the integrity of all articles
     */
    public void verify_articles() {
        /* procede à la verification d'un article, s'il n'est pas valide (car modifié) on tente de le corriger, si cela est possible on remplace l'article corrigé, sinon on supprime l'article*/
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

    /**
     * @param article article the will be added
     * @return informt if the article has been added
     */
    public boolean add_article(Article article) {
        DatabaseArticle dbArticle = article;
        try {
            jsonDBTemplate.insert(dbArticle);
            return true;
        } catch (InvalidJsonDbApiUsageException e) {
            return false;
        }
    }


    /**
     * search an article in the database
     * @param link the link of the article
     * @return found article
     */
    public DatabaseArticle findArticle(String link){
        try {
            return jsonDBTemplate.findById(link, DatabaseArticle.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @return list containing all the articles
     */
    public ArrayList<Article> load_articles() {
        return this.load_articles("");
    }

    /**
     * search an article by with the title
     * @param title_contains title of the article
     * @return list containing all the articles with a specific title
     */
    public ArrayList<Article> load_articles(String title_contains) {
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

    /**
     * delete the expired articles
     */
    private void deleteExpired() {
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