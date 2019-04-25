package be.ac.ulb.infof307.g04.model;

import be.ac.ulb.infof307.g04.controller.ParserRss;
import io.jsondb.InvalidJsonDbApiUsageException;
import io.jsondb.JsonDBTemplate;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class SourceManager where we can handle all the sources
 * @see DatabaseSource
 */

public class SourceManager {
    private JsonDBTemplate jsonDBTemplate;


    public SourceManager(String _databasePath) {
        /**
         * @param _databasePath
         *                  path to the database
         */
        String baseScanPackage = "be.ac.ulb.infof307.g04.model";
        this.jsonDBTemplate = new JsonDBTemplate(_databasePath, baseScanPackage);

        if (!this.jsonDBTemplate.collectionExists(DatabaseSource.class)) {
            createCollection();
        }
    }

    private void createCollection() {
        jsonDBTemplate.createCollection(DatabaseSource.class);
    }


    public ArrayList<DatabaseSource> loadSources() {
        /**
         * @return a list that contained all the sources
         */
        return (ArrayList<DatabaseSource>) jsonDBTemplate.findAll(DatabaseSource.class);
    }


    public boolean addSource(DatabaseSource _source) {
        /**
         * @param _source
         *          _source that will be added
         * @return boolean to inform if the _source has been added
         */
        try {
            jsonDBTemplate.insert(_source);
            return true;
        } catch (InvalidJsonDbApiUsageException e) {
            return false;
        }
    }


    public boolean updateSource(DatabaseSource _source){
        /**
         * @param _source
         *             _source to update
         * @return boolean to inform if the sources has been updated
         * @throws InvalidJsonDbApiUsageException
         */
        try{
            jsonDBTemplate.upsert(_source);
            return true;
        } catch (InvalidJsonDbApiUsageException e) {
            e.printStackTrace();
            return false;
        }
    }


    public void download(ArticleManager _articleManager) throws IOException, ParserConfigurationException, SAXException, ParseException {
        /**
         * Download the articles
         * @see ArticleManager
         * @see databaseArticle
         * @see ParserRss
         * @param _articleManager
         *                  article manager to see what articles can we load
         */
        ParserRss source_parser = new ParserRss();
        ArrayList<DatabaseSource> sources = loadSources();
        for (DatabaseSource source : sources) {
            if (source.isEnabled()) {
                ArrayList<DatabaseArticle> articles = source_parser.parse(source.getUrl());
                for (int i = 0; i < source.getNumber_to_download(); i++) {
                    DatabaseArticle article_to_save = articles.get(i);
                    article_to_save.setDaysToSave(source.getLifeSpan_default());
                    article_to_save.setCategory(source.getTag());
                    article_to_save.setDownloadDate(new Date());
                    article_to_save.setSourceUrl(source.getUrl());
                    article_to_save.setTags(source.getTag());
                    _articleManager.addArticle(article_to_save);
                }

            }
        }
    }
}
