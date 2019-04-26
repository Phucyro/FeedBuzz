package be.ac.ulb.infof307.g04.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SourceManagerTest {

    @BeforeEach
    void setUp() {
        //Ajout de la source et de ses paramètres
        DatabaseSource databaseSource = new DatabaseSource();
        databaseSource.setUrl("http://static.userland.com/gems/backend/rssMarkPilgrimExample.xml");
        databaseSource.setEnabled(true);
        databaseSource.setNumberToDownload(3);
        //Ajout de cette source dans la base de donnée
        SourceManager source = new SourceManager("./article_test_db");
        source.addSource(databaseSource);
        //source.download();


    }

  /*  @AfterEach
    void tearDown(){
        try {
            FileUtils.deleteDirectory(new File("./article_test_db"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    @Test
    void download() throws IOException, ParserConfigurationException, SAXException, ParseException {
        SourceManager sourceManager = new SourceManager("./article_test_db");
        ArticleManager articleManager = new ArticleManager("./article_test_db", "password");
        sourceManager.download(articleManager);
        assertNotNull(articleManager.findArticle("http://diveintomark.org/archives/2002/09/29.html#dooce"));
        assertNotNull(articleManager.findArticle("http://diveintomark.org/archives/2002/09/27.html#advanced_css_lists"));
        assertNotNull(articleManager.findArticle("http://diveintomark.org/archives/2002/09/27.html#pingback_vs_trackback"));
    }
}