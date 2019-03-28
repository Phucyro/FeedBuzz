package model;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

import java.io.Serializable;

@Document(collection = "sources", schemaVersion= "1.0")
public class DatabaseSource implements Serializable {
    @Id
    private String url;
    private String source_name ;
    private boolean enabled;
    private int number_to_download;
    private int lifeSpan_default;
    private String tag;

    public DatabaseSource() { }

    public DatabaseSource(String _source_name, String _url, String _tag){
        source_name = _source_name;
        url = _url;
        enabled = true;
        number_to_download = 10;
        lifeSpan_default = 7;
        tag = _tag;
    }

    public DatabaseSource(String source_name, String url){
        this(source_name, url, "Default");
    }

    public DatabaseSource(DatabaseSource item) {
        this.url = item.url;
        this.source_name = item.source_name;
        this.enabled = item.enabled;
    }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getSource_name() { return source_name; }
    public void setSource_name(String source_name) { this.source_name = source_name; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public int getLifeSpan_default() {return lifeSpan_default; }
    public void setLifeSpan_default(int lifeSpan_default) { this.lifeSpan_default = lifeSpan_default; }
    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }
    public int getNumber_to_download() { return number_to_download; }
    public void setNumber_to_download(int number_to_download) { this.number_to_download = number_to_download; }
}
