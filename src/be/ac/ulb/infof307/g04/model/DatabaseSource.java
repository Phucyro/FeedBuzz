package be.ac.ulb.infof307.g04.model;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

import java.io.Serializable;

/**
 * Class DatabaseSource where all the sources are stored
 * @see SourceManager
 */
@Document(collection = "sources", schemaVersion= "1.0")
public class DatabaseSource implements Serializable {
    @Id
    private String url;
    private String sourceName;
    private boolean enabled;
    private int numberToDownload;
    private int lifeSpanDefault;
    private String tag;

    public DatabaseSource() { }

    public DatabaseSource(String _sourceName, String _url, String _tag){
        /**
         * Constructor of a source
         * @param _sourceName
         *                  name of the source
         * @param _url
         *                  url of the source
         * @param _tag
         *                  tag of the source
         */
        sourceName = _sourceName;
        url = _url;
        enabled = true;
        numberToDownload = 2;
        lifeSpanDefault = 7;
        tag = _tag;
    }

    public DatabaseSource(String _sourceName, String _url){
        /*
        Same constructor as above, except if there's no precise tag -> set as "Default"
         */
        this(_sourceName, _url, "Default");
    }

    public DatabaseSource(DatabaseSource _item) {
        this.url = _item.url;
        this.sourceName = _item.sourceName;
        this.enabled = _item.enabled;
    }

    /*
    Methods to get/set infos from sources
     */

    public String getUrl() { return url; }
    public void setUrl(String _url) { this.url = _url; }
    public String getSourceName() { return sourceName; }
    public void setSourceName(String _sourceName) { this.sourceName = _sourceName;}
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public int getLifeSpanDefault() {return lifeSpanDefault; }
    public void setLifeSpanDefault(int lifeSpanDefault) { this.lifeSpanDefault = lifeSpanDefault; }
    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }
    public int getNumberToDownload() { return numberToDownload; }
    public void setNumberToDownload(int numberToDownload) { this.numberToDownload = numberToDownload; }
}
