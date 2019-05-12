package be.ac.ulb.infof307.g04.model;

import be.ac.ulb.infof307.g04.controller.HTMLArticleDownloader;
import be.ac.ulb.infof307.g04.controller.ParserRss;
import io.jsondb.InvalidJsonDbApiUsageException;
import io.jsondb.JsonDBTemplate;
import io.jsondb.crypto.CryptoUtil;
import io.jsondb.crypto.DefaultAESCBCCipher;
import io.jsondb.crypto.ICipher;
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

    /**
     * @param _databasePath
     *                  path to the database
     */
    public SourceManager(String _databasePath, String _password) {
        String baseScanPackage = "be.ac.ulb.infof307.g04.model";
        this.jsonDBTemplate = new JsonDBTemplate(_databasePath, baseScanPackage);

        this.jsonDBTemplate = new JsonDBTemplate(_databasePath, baseScanPackage);

        try {
            String base64EncodedKey = CryptoUtil.generate128BitKey(_password, _password);
            ICipher newCipher = new DefaultAESCBCCipher(base64EncodedKey);
            this.jsonDBTemplate = new JsonDBTemplate(_databasePath, baseScanPackage, newCipher);
        } catch (Exception e){
            this.jsonDBTemplate = new JsonDBTemplate(_databasePath, baseScanPackage);
        }

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
     */
    public void addSource(DatabaseSource _source) {
        try {
            jsonDBTemplate.insert(_source);
        } catch (InvalidJsonDbApiUsageException e) {
        }
    }

    /**
     * @param _source
     *             _source to update
     */
    public void updateSource(DatabaseSource _source){
        try{
            jsonDBTemplate.upsert(_source);
        } catch (InvalidJsonDbApiUsageException e) {
            e.printStackTrace();
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
    public void download(ArticleManager _articleManager) throws SAXException, ParserConfigurationException, ParseException, IOException {
        ParserRss source_parser = new ParserRss();
        ArrayList<DatabaseSource> sources = loadSources();
        for (DatabaseSource source : sources) {
            if (source.isEnabled()) {
                System.out.println(source.getSourceName());
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
                                System.out.println("Downloading article");
                                articleToSave.setHtmlContent(HTMLArticleDownloader.ArticleLocalifier(articleToSave.getLink(), articleToSave.getDescription()));
                                System.out.println("Downloaded");
                                _articleManager.addArticle(articleToSave);
                            } else {
                                System.out.println("Existing article");
                            }
                        }
                    }

            }
        }
    }
}
