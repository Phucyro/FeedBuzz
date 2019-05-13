package be.ac.ulb.infof307.g04.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SourceManagerTest {

    @BeforeEach
    void setUp() {
        //Ajout de la source et de ses paramètres
        DatabaseSource databaseSource = new DatabaseSource();
        databaseSource.setUrl("http://static.userland.com/gems/backend/rssMarkPilgrimExample.xml");
        databaseSource.setEnabled(true);
        databaseSource.setArticlesToDownload(3);
        //Ajout de cette source dans la base de donnée
        SourceManager source = new SourceManager("./article_test_db", "password");
        source.addSource(databaseSource);
        //source.download();


    }

    @Test
    void download() throws IOException, ParserConfigurationException, SAXException, ParseException {
        SourceManager sourceManager = new SourceManager("./article_test_db", "password");
        ArticleManager articleManager = new ArticleManager("./article_test_db", "password");
        sourceManager.download(articleManager);
        assertNotNull(articleManager.findArticle("http://diveintomark.org/archives/2002/09/29.html#dooce"));
        assertNotNull(articleManager.findArticle("http://diveintomark.org/archives/2002/09/27.html#advanced_css_lists"));
        assertNotNull(articleManager.findArticle("http://diveintomark.org/archives/2002/09/27.html#pingback_vs_trackback"));
    }

    private void deleteDir(File file) {
        //Supprime le dossier de la base de donnees de test apres que les tests soient termines
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (! Files.isSymbolicLink(f.toPath())) {
                    deleteDir(f);
                }
            }
        }
        file.delete();
    }
}