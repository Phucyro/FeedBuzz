package be.ac.ulb.infof307.g04.model;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class InitDatabase {
    public static void InitTagAndSourceDatabase(String _dbPath, String _password) {
        initSources(_dbPath, _password);
        initTags(_dbPath, _password);
    }

    /**
     * Initialize all the tags
     */
    private static void initTags(String _dbPath, String _password) {
        TagManager tagManager = new TagManager(_dbPath, _password);
        JSONParser parser = new JSONParser();
        try {
            Object objectParser = parser.parse(new FileReader("src/be/ac/ulb/infof307/g04/model/wordlists.json")); // parse the json, each entry has the label as the key and an array of words as value
            JSONObject object = (JSONObject) objectParser;

            DatabaseTag tag;
            // iterate through the keys of the JSONObject
            for (Object o : object.keySet()) {
                tag = new DatabaseTag();
                String label = (String) o;
                tag.setName(label);
                tagManager.addTag(tag);
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize all the sources
     */
    private static void initSources(String _dbPath, String _password) {
        SourceManager sourceManager = new SourceManager(_dbPath, _password);
        ArrayList<DatabaseSource> sources = new ArrayList<>();
        sources.add(new DatabaseSource("The Verge", "https://www.theverge.com/rss/index.xml", "Technology"));
        sources.add(new DatabaseSource("BBC world news", "http://feeds.bbci.co.uk/news/world/rss.xml"));
        sources.add(new DatabaseSource("Polygon", "https://www.polygon.com/rss/index.xml", "Technology"));
        sources.add(new DatabaseSource("Vox", "https://www.vox.com/rss/world/index.xml"));
        sources.add(new DatabaseSource("CNN Money", "http://rss.cnn.com/rss/money_topstories.rss", "Business"));
        sources.forEach(sourceManager::addSource);
    }
}