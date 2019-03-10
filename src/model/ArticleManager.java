package model;

import io.jsondb.InvalidJsonDbApiUsageException;
import io.jsondb.JsonDBOperations;
import io.jsondb.JsonDBTemplate;

public class ArticleManager{

    private JsonDBTemplate jsonDBTemplate;

    public ArticleManager(String database_path){
        String baseScanPackage = "model";
        this.jsonDBTemplate = new JsonDBTemplate(database_path, baseScanPackage);
        if (!this.jsonDBTemplate.collectionExists(DatabaseArticle.class)){
            create_collection();
        }
    }

    private void create_collection() {
        jsonDBTemplate.createCollection(DatabaseArticle.class);
    }

    public boolean delete_article(DatabaseArticle article) {
        try {
            this.jsonDBTemplate.remove(article, DatabaseArticle.class);
            return true;
        } catch (InvalidJsonDbApiUsageException e){
            return false;
        }
    }

    public boolean add_article(DatabaseArticle article) {
        try {
            jsonDBTemplate.insert(article);
            return true;
        } catch (InvalidJsonDbApiUsageException e) {
            return false;
        }
    }

    public DatabaseArticle findArticle(String link){
        return jsonDBTemplate.findById(link, DatabaseArticle.class);
    }
}