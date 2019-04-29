package be.ac.ulb.infof307.g04.controller;

import be.ac.ulb.infof307.g04.model.DataForTests;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import be.ac.ulb.infof307.g04.controller.HTMLArticleDownloader;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class HTMLArticleDownloaderTest {
    public static final String SRC_TAG = "src";
    public static final String HREF_TAG = "href";
    public static final String MEDIA_FOLDER = "media/";
    public static final String FOLDER_NAME = "articleTest";
    public String resultantHtml; //String containing the resulting html from the fetched article
    DataForTests dataForTests = new DataForTests();
    Document doc; //Document containing the fetched article

    @BeforeEach
    void setUp() {
        String url = "https://www.theverge.com/2012/12/5/3729722/amazon-kindle-freetime-unlimited-subscription-kids";
        String folderName = FOLDER_NAME;
        new File(MEDIA_FOLDER).mkdir();
        new File(MEDIA_FOLDER + folderName).mkdir();

        try{
            doc = Jsoup.connect(url).get();
        } catch (IOException ignore){
        }
        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");

        HTMLArticleDownloader.replaceLinksHref(links, "#");
        HTMLArticleDownloader.downloadReplaceElementsFromTag(media, folderName, SRC_TAG);
        HTMLArticleDownloader.downloadReplaceElementsFromTag(imports, folderName, HREF_TAG);
        resultantHtml = HTMLArticleDownloader.downloadReplaceRemainingLinks(doc.toString(), folderName);
    }


    /**
     * Test if the html of the article is correct
     */
    @Test
    void articleLocalifier() {
        assertTrue(similarity(dataForTests.getHtmlForDownloaderTests(), resultantHtml) > 0.80);
        //If the strings are more than 80% different, they are considered the same because the html of the article changes a bit from time to time
    }

    /**
     * Test if the number of articles downloaded is correct
     */
    @Test
    void testNumberOfArticlesDownloaded(){
        System.out.println(MEDIA_FOLDER+FOLDER_NAME);
        int expectedNumberOfImages = new File(MEDIA_FOLDER+FOLDER_NAME).listFiles().length;
        assertEquals(17,expectedNumberOfImages);
    }


    /**
     * @param s1 First string to compare
     * @param s2 Second string to compare
     * @return a double equals to the similarity of two strings. 1.0 means the strings are the same and 0.0 means the strings are completely different
     */
    public static double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2; shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) { return 1.0; /* both strings are zero length */ }
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
    }

    /**
     * @param s1 is the first string to compare
     * @param s2 is the second string to compare
     * @return the comparison between the two strings
     */
    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }



    /**
     * Delete the folders that were created for the tests
     */
    @AfterAll
    public static void tearDown() {
        System.out.println(MEDIA_FOLDER + FOLDER_NAME);
        File directory = new File(MEDIA_FOLDER + FOLDER_NAME);
        String[]entries = directory.list();
        for(String s: entries){
            File currentFile = new File(directory.getPath(),s);
            currentFile.delete();
        }
        directory.delete();
    }
}