package model;

import controller.Article;
import controller.ParserRss;
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

    public boolean update_source(DatabaseSource source){
        try{
            jsonDBTemplate.upsert(source);
            return true;
        } catch (InvalidJsonDbApiUsageException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void download(ArticleManager articleManager) {
        ParserRss source_parser = new ParserRss();
        ArrayList<DatabaseSource> sources = load_sources();
        for (DatabaseSource source : sources) {
            if (source.isEnabled()) {
                ArrayList<Article> articles = source_parser.parse(source.getUrl());
                for (int i = 0; i < source.getNumber_to_download(); i++) {
                    Article article_to_save = articles.get(i);
                    article_to_save.setDays_to_save(source.getLifeSpan_default());
                    articleManager.add_article(article_to_save);
                }

            }
        }
    }
}
