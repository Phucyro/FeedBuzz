package be.ac.ulb.infof307.g04.model;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;
import java.util.Date;

import java.io.Serializable;

/**
 * Class DatabaseTag where all tags are stored
 * @see TagManager
 */

@Document(collection = "tags", schemaVersion= "1.0")
public class DatabaseTag implements Serializable {
    @Id
    private String name;
    private float score;
    private Date lastActualisationDate;

    public DatabaseTag(){
        score = 0;
        //actualisationDate = new Date();
    }


    public String getName() { return name; }
    public void setName(String _name) { this.name = _name; }
    public float getScore() { return score; }
    public void setScore(float _score) { this.score = _score; }
    public Date getLastActualisationDate() {return lastActualisationDate; }
    public void setLastActualisationDate(Date _newDate) { this.lastActualisationDate = _newDate; }
}
