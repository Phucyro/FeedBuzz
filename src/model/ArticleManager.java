package model;

import controller.Article;
import io.jsondb.InvalidJsonDbApiUsageException;
import io.jsondb.JsonDBTemplate;
import io.jsondb.crypto.CryptoUtil;
import io.jsondb.crypto.DefaultAESCBCCipher;
import io.jsondb.crypto.ICipher;

import java.util.ArrayList;
import java.util.List;

public class ArticleManager{

    private JsonDBTemplate jsonDBTemplate;

    public ArticleManager(String database_path, String password) {
        String baseScanPackage = "model";
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

    public boolean delete_article(Article article) {
        /* Pour chaque article supprimé on garde uniquement son url qui sert de clé primaire.
         * Ainsi, les articles supprimés ne seront pas retéléchargés dans la DB*/
        try {
            DatabaseArticle to_replace = new DatabaseArticle();
            to_replace.setLink(article.getLink());
            to_replace.setDeleted(true);
            this.jsonDBTemplate.remove(article, DatabaseArticle.class);
            this.jsonDBTemplate.insert(to_replace);
            return true;
        } catch (InvalidJsonDbApiUsageException e){
            return false;
        }
    }


    public boolean add_article(Article article) {
        try {
            jsonDBTemplate.insert(article);
            return true;
        } catch (InvalidJsonDbApiUsageException e) {
            return false;
        }
    }


    public DatabaseArticle findArticle(String link){
        try {
            return jsonDBTemplate.findById(link, DatabaseArticle.class);
        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<Article> load_articles() {
        return this.load_articles("");
    }

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