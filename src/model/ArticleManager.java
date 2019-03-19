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

    }

    public ArticleManager(String database_path) {
        this(database_path, "password");

    }

    private void create_collection() {
        jsonDBTemplate.createCollection(DatabaseArticle.class);
    }

    public boolean delete_article(Article article) {
        DatabaseArticle dbarticle = article;
        try {
            this.jsonDBTemplate.remove(dbarticle, DatabaseArticle.class);
            return true;
        } catch (InvalidJsonDbApiUsageException e){
            return false;
        }
    }

    public boolean add_article(Article article) {
        DatabaseArticle dbArticle = article;
        try {
            jsonDBTemplate.insert(dbArticle);
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
            if (item.getTitle().toLowerCase().contains(title_contains.toLowerCase())) {
                result.add(new Article(item));
            }
        }
        return (result);
    }
}