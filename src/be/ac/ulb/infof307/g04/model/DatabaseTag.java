package be.ac.ulb.infof307.g04.model;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

import java.io.Serializable;

/**
 * Class DatabaseTag where all tags are stored
 * @see TagManager
 */

@Document(collection = "tags", schemaVersion= "1.0")
public class DatabaseTag implements Serializable {
    @Id
    private String name;

    public DatabaseTag(){}


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
