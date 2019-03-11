package model;

import io.jsondb.InvalidJsonDbApiUsageException;
import io.jsondb.JsonDBOperations;
import io.jsondb.JsonDBTemplate;
import io.jsondb.crypto.CryptoUtil;
import io.jsondb.crypto.DefaultAESCBCCipher;
import io.jsondb.crypto.ICipher;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class ArticleManager{

    private JsonDBTemplate jsonDBTemplate;

    public ArticleManager(String database_path, String password) {
        String baseScanPackage = "model";

        try {
            String base64EncodedKey = CryptoUtil.generate128BitKey(password, password);
            ICipher newCipher = newCipher = new DefaultAESCBCCipher(base64EncodedKey);
            this.jsonDBTemplate = new JsonDBTemplate(database_path, baseScanPackage, newCipher);
        } catch (Exception e){
            this.jsonDBTemplate = new JsonDBTemplate(database_path, baseScanPackage);
        }


        if (!this.jsonDBTemplate.collectionExists(DatabaseArticle.class)) {
            create_collection();
        }

    }

    private void create_collection() {
        jsonDBTemplate.createCollection(DatabaseArticle.class);
    }

    public boolean delete_article(DatabaseArticle article) {
        try {
            this.jsonDBTemplate.remove(article, DatabaseArticle.class);
            return true;
        } catch (InvalidJsonDbApiUsageException e){
            return false;
        }
    }

    public boolean add_article(DatabaseArticle article) {
        try {
            jsonDBTemplate.insert(article);
            return true;
        } catch (InvalidJsonDbApiUsageException e) {
            return false;
        }
    }

    public DatabaseArticle findArticle(String link){
        try {
            return jsonDBTemplate.findById(link, DatabaseArticle.class);
        } catch (Exception e) {
            return null;
        }
    }
}