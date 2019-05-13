package be.ac.ulb.infof307.g04.model;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;
import io.jsondb.annotation.Secret;

import java.io.Serializable;

/**
 * Class DatabaseSource where all the sources are stored
 *
 * @see SourceManager
 */
@Document(collection = "sources", schemaVersion = "1.0")
public class DatabaseSource implements Serializable {
    public static final int ARTICLES_TO_DOWNLOAD = 1;
    public static final int LIFE_SPAN_DEFAULT = 7;
    @Id
    private String url;
    @Secret
    private String sourceName;
    private boolean enabled;
    @Secret
    private String tag;
    private int lifeSpan;
    private int articlesToDownload;

    public DatabaseSource() {
    }

    /**
     * Constructor of a source
     *
     * @param _sourceName name of the source
     * @param _url        url of the source
     * @param _tag        tag of the source
     */
    public DatabaseSource(String _sourceName, String _url, String _tag) {
        sourceName = _sourceName;
        url = _url;
        enabled = true;
        tag = _tag;
        lifeSpan = LIFE_SPAN_DEFAULT;
        articlesToDownload = ARTICLES_TO_DOWNLOAD;
    }

    /**
     * Same constructor as above, except if there's no precise tag: set as "Default"
     */
    public DatabaseSource(String _sourceName, String _url) {
        this(_sourceName, _url, "Default");
    }

    public DatabaseSource(DatabaseSource _item) {
        this.url = _item.url;
        this.sourceName = _item.sourceName;
        this.enabled = _item.enabled;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String _url) {
        this.url = _url;
    }

    public String getSourceName() {
        return sourceName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean _enabled) {
        this.enabled = _enabled;
    }

    public int getLifeSpan() {
        return lifeSpan;
    }

    public void setLifeSpan(int _lifeSpan) {
        this.lifeSpan = _lifeSpan;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String _tag) {
        this.tag = _tag;
    }

    public int getArticlesToDownload() {
        return articlesToDownload;
    }

    public void setArticlesToDownload(int _articlesToDownload) {
        this.articlesToDownload = _articlesToDownload;
    }
}
