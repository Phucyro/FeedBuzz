package be.ac.ulb.infof307.g04.model;

import be.ac.ulb.infof307.g04.controller.HTMLArticleDownloader;
import be.ac.ulb.infof307.g04.controller.ParserRss;
import io.jsondb.InvalidJsonDbApiUsageException;
import io.jsondb.JsonDBTemplate;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class SourceManager where we can handle all the sources
 * @see DatabaseSource
 */

public class SourceManager {
    private JsonDBTemplate jsonDBTemplate;

    /**
     * @param _databasePath
     *                  path to the database
     */
    public SourceManager(String _databasePath) {

        String baseScanPackage = "be.ac.ulb.infof307.g04.model";
        this.jsonDBTemplate = new JsonDBTemplate(_databasePath, baseScanPackage);

        if (!this.jsonDBTemplate.collectionExists(DatabaseSource.class)) {
            createCollection();
        }
    }

    private void createCollection() {
        jsonDBTemplate.createCollection(DatabaseSource.class);
    }

    /**
     * @return a list that contained all the sources
     */
    public ArrayList<DatabaseSource> loadSources() {
        return (ArrayList<DatabaseSource>) jsonDBTemplate.findAll(DatabaseSource.class);
    }

    /**
     * @param _source
     *          _source that will be added
     * @return boolean to inform if the _source has been added
     */
    public boolean addSource(DatabaseSource _source) {
        try {
            jsonDBTemplate.insert(_source);
            return true;
        } catch (InvalidJsonDbApiUsageException e) {
            return false;
        }
    }

    /**
     * @param _source
     *             _source to update
     * @return boolean to inform if the sources has been updated
     */
    public boolean updateSource(DatabaseSource _source){
        try{
            jsonDBTemplate.upsert(_source);
            return true;
        } catch (InvalidJsonDbApiUsageException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Download the articles
     * @see ArticleManager
     * @see ParserRss
     * @see DatabaseArticle
     * @param _articleManager
     *                  article manager to see what articles can we load
     */
    public void download(ArticleManager _articleManager) {
        ParserRss source_parser = new ParserRss();
        ArrayList<DatabaseSource> sources = loadSources();
        for (DatabaseSource source : sources) {
            if (source.isEnabled()) {
                try {
                    int counter = source.getNumberToDownload();
                    ArrayList<DatabaseArticle> articles = source_parser.parse(source.getUrl());
                    for (DatabaseArticle articleToSave : articles) {
                        if (counter-- > 0) {

                            if (_articleManager.findArticle(articleToSave.getLink()) == null) {
                                articleToSave.setDaysToSave(source.getLifeSpanDefault());
                                articleToSave.setCategory(source.getTag());
                                articleToSave.setDownloadDate(new Date());
                                articleToSave.setSourceUrl(source.getUrl());
                                articleToSave.setTags(source.getTag());
                                try {
                                    System.out.println("Downloading article");
                                    articleToSave.setHtmlContent(HTMLArticleDownloader.ArticleLocalifier(articleToSave.getLink()));
                                    System.out.println("Downloaded");
                                } catch (IOException e) {
                                    System.out.println("Erreur");
                                    articleToSave.setHtmlContent("");
                                }
                                _articleManager.addArticle(articleToSave);
                            } else {
                                System.out.println("Existing article");
                            }
                        } else {
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //TODO cas offline
                }

            }
        }
    }
}
