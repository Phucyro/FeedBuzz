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
    private int score;

    public DatabaseTag(){
        score = 0;
    }


    public String getName() { return name; }
    public void setName(String _name) { this.name = _name; }
    public int getScore() { return score; }
    public void setScore(int _score) { this.score = _score; }
}
