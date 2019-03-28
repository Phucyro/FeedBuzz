package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SourceManagerTest {

    @BeforeEach
    void setUp() {
        //Ajout de la source et de ses paramètres
        DatabaseSource database_source = new DatabaseSource();
        database_source.setUrl("http://static.userland.com/gems/backend/rssMarkPilgrimExample.xml");
        database_source.setEnabled(true);
        database_source.setNumber_to_download(3);
        //Ajout de cette source dans la base de donnée
        SourceManager source = new SourceManager("./article_test_db");
        source.add_source(database_source);
        //source.download();


    }

    @AfterEach
    void tearDown(){
        try {
            FileUtils.deleteDirectory(new File("./article_test_db"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void download() {
        SourceManager source_manager = new SourceManager("./article_test_db");
        ArticleManager article_manager = new ArticleManager("./article_test_db", "password");
        source_manager.download(article_manager);
        assertNotNull(article_manager.findArticle("http://diveintomark.org/archives/2002/09/29.html#dooce"));
        assertNotNull(article_manager.findArticle("http://diveintomark.org/archives/2002/09/27.html#advanced_css_lists"));
        assertNotNull(article_manager.findArticle("http://diveintomark.org/archives/2002/09/27.html#pingback_vs_trackback"));
    }

}