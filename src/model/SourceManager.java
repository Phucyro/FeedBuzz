package model;

import controller.Article;
import io.jsondb.InvalidJsonDbApiUsageException;
import io.jsondb.JsonDBTemplate;

import java.util.ArrayList;
import java.util.List;

public class SourceManager {
    private JsonDBTemplate jsonDBTemplate;

    public SourceManager(String database_path) {
        String baseScanPackage = "model";
        this.jsonDBTemplate = new JsonDBTemplate(database_path, baseScanPackage);

        if (!this.jsonDBTemplate.collectionExists(DatabaseSource.class)) {
            create_collection();
        }
    }

    private void create_collection() {
        jsonDBTemplate.createCollection(DatabaseSource.class);
    }

    public ArrayList<DatabaseSource> load_sources () {
        return (ArrayList<DatabaseSource>) jsonDBTemplate.findAll(DatabaseSource.class);
    }

    public boolean add_source(DatabaseSource source) {
        try {
            jsonDBTemplate.insert(source);
            return true;
        } catch (InvalidJsonDbApiUsageException e) {
            return false;
        }
    }

    public void download(int number,int lifespan){
        /*ParserRss source = new ParserRss();
        //ArticleManager articleManager = new ArticleManager("./test.db", "test");
        ArrayList<Article> articles = source.parse(this.getUrl());
        Article article_temp;
        for (int i = 0; i < number; i++) {
            article_temp = articles.get(i);
            //articleManager.add_article(article_temp);
        }*/

    }

}
