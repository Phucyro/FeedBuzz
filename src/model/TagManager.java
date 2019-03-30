package model;

import io.jsondb.InvalidJsonDbApiUsageException;
import io.jsondb.JsonDBTemplate;
import io.jsondb.crypto.CryptoUtil;
import io.jsondb.crypto.DefaultAESCBCCipher;
import io.jsondb.crypto.ICipher;
import io.jsondb.query.Update;

import java.util.ArrayList;

public class TagManager {
    private JsonDBTemplate jsonDBTemplate;

    public TagManager(String database_path, String password) {
        String baseScanPackage = "model";
        this.jsonDBTemplate = new JsonDBTemplate(database_path, baseScanPackage);

        try {
            String base64EncodedKey = CryptoUtil.generate128BitKey(password, password);
            ICipher newCipher = new DefaultAESCBCCipher(base64EncodedKey);
            this.jsonDBTemplate = new JsonDBTemplate(database_path, baseScanPackage, newCipher);
        } catch (Exception e){
            this.jsonDBTemplate = new JsonDBTemplate(database_path, baseScanPackage);
        }

        if (!this.jsonDBTemplate.collectionExists(DatabaseTag.class)) {
            create_collection();
        }
    }

    private void create_collection() {
        jsonDBTemplate.createCollection(DatabaseTag.class);
    }

    public boolean add_tag(DatabaseTag tag){
        try {
            jsonDBTemplate.insert(tag);
            return true;
        } catch (InvalidJsonDbApiUsageException e){
            return false;
        }
    }

    public boolean delete_tag(DatabaseTag tag){
        try {
            jsonDBTemplate.remove(tag, DatabaseTag.class);
            update("tag", tag.getName(), "Default", DatabaseSource.class);
            update("category", tag.getName(), "Default", DatabaseArticle.class);
            return true;
        } catch (InvalidJsonDbApiUsageException e){
            return false;
        }
    }

    public boolean modify_tag(DatabaseTag tag, DatabaseTag newTag){
        try {
            update("name", tag.getName(), newTag.getName(), DatabaseTag.class);
            update("tag", tag.getName(), newTag.getName(), DatabaseSource.class);
            update("category", tag.getName(), newTag.getName(), DatabaseArticle.class);
            return true;
        } catch(InvalidJsonDbApiUsageException e){
            return false;
        }
    }
    public boolean update(String key, String oldValue, String newValue, Class entityClass){
        Update update = Update.update(key, newValue);
        String jxQuery = String.format("/.[%s='%s']", key, oldValue);
        System.out.println(jsonDBTemplate.findAllAndModify(jxQuery, update, entityClass).size());
        return true;
    }

    public ArrayList<DatabaseTag> get_all() {
        return (ArrayList<DatabaseTag>) jsonDBTemplate.findAll(DatabaseTag.class);
    }
}
