package be.ac.ulb.infof307.g04.model;

import be.ac.ulb.infof307.g04.controller.Article;
import be.ac.ulb.infof307.g04.controller.ParserRss;
import io.jsondb.InvalidJsonDbApiUsageException;
import io.jsondb.JsonDBTemplate;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class SourceManager where we can handle all the sources
 * @see DatabaseSource
 */

public class SourceManager {
    private JsonDBTemplate jsonDBTemplate;


    public SourceManager(String database_path) {
        /**
         * @param database_path
         *                  path to the database
         */
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
        /**
         * @return a list that contained all the sources
         */
        return (ArrayList<DatabaseSource>) jsonDBTemplate.findAll(DatabaseSource.class);
    }


    public boolean add_source(DatabaseSource source) {
        /**
         * @param source
         *          source that will be added
         * @return boolean to inform if the source has been added
         */
        try {
            jsonDBTemplate.insert(source);
            return true;
        } catch (InvalidJsonDbApiUsageException e) {
            return false;
        }
    }


    public boolean update_source(DatabaseSource source){
        /**
         * @param source
         *             source to update
         * @return boolean to inform if the sources has been updated
         * @throws InvalidJsonDbApiUsageException
         */
        try{
            jsonDBTemplate.upsert(source);
            return true;
        } catch (InvalidJsonDbApiUsageException e) {
            e.printStackTrace();
            return false;
        }
    }


    public void download(ArticleManager articleManager) throws IOException, ParserConfigurationException, SAXException, ParseException {
        /**
         * Download the articles
         * @see ArticleManager
         * @see Article
         * @see ParserRss
         * @param articleManager
         *                  article manager to see what articles can we load
         */
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
                    article_to_save.setTags(source.getTag());
                    articleManager.add_article(article_to_save);
                }

            }
        }
    }
}
