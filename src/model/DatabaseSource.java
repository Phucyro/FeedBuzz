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

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getSource_name() { return source_name; }
    public void setSource_name(String source_name) { this.source_name = source_name; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public DatabaseSource() { }
}
