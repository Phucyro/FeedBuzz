package be.ac.ulb.infof307.g04.model;

import be.ac.ulb.infof307.g04.controller.Article;
import be.ac.ulb.infof307.g04.controller.ParserRss;
import io.jsondb.InvalidJsonDbApiUsageException;
import io.jsondb.JsonDBTemplate;

import java.util.ArrayList;
import java.util.Date;

public class SourceManager {
    private JsonDBTemplate jsonDBTemplate;

    public SourceManager(String database_path) {
        String baseScanPackage = "be.ac.ulb.infof307.g04.model";
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
                    article_to_save.setCategory(source.getTag());
                    article_to_save.setDownload_date(new Date());
                    article_to_save.setSource_url(source.getUrl());
                    articleManager.add_article(article_to_save);
                }

            }
        }
    }
}
