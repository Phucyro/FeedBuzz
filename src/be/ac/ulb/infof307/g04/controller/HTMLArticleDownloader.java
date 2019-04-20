package be.ac.ulb.infof307.g04.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Random;


public class HTMLArticleDownloader {
    //Recois une URL
    //Extrait tout les liens et les trie par type
    private Article article;
    public HTMLArticleDownloader() {
    }

    public static void main(String[] args) {
        try{
            ArticleLocalifier("https://www.bbc.co.uk/news/world-asia-india-47114401");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getIconUrl(String html) {
        String imageUrl = null;
        Document doc = Jsoup.parse(html);
        Elements imgs = doc.getElementsByTag("img");

        boolean found = false;

        for (Element elem: imgs)
        {
            if (!found)
            {
                imageUrl = elem.absUrl("src");

                found = true;
            }
        }

        return imageUrl;
    }

    public static String ArticleLocalifier(String url) throws IOException {
        String foldername = sanitize_string(url);
        new File("./media/"+ foldername).mkdir();
        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");

        replaceLinksHref(links,"#");
        replaceLinksSrc(media, foldername);
        replaceImportsHref(imports, foldername);
        //System.out.println(doc);
        return ("Ninja");
    }

    private static String sanitize_string(String url){
        url = url.replace("/","");
        url = url.replace("<","");
        url = url.replace(">","");
        url = url.replace(":","");
        url = url.replace("\"","");
        url = url.replace("/","");
        url = url.replace("\\","");
        url = url.replace("|","");
        url = url.replace("?","");
        url = url.replace("*","");
        return url;
    }

    private static void replaceLinksHref(Elements links,String toReplace) {
        for (Element link : links) {
            //link.attr("abs:href", "#");
            link.attr("href", toReplace);
        }
    }

    private static void replaceLinksSrc(Elements medias, String foldername){
        for (Element media: medias){
            try {
                media.attr("src", downloader(media.attr("src"), foldername));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void replaceImportsHref(Elements imports, String foldername){
        for (Element _import: imports){
            try {
                _import.attr("href", downloader(_import.attr("href"), foldername));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static String downloader(String url, String foldername) throws IOException {
        //System.out.println(url);
        InputStream in = new URL(url).openStream();
        String filename = getFilename(in, url);
        String complete_path = "./media/" + foldername + "/" + filename;
        Files.copy(in, Paths.get(complete_path), StandardCopyOption.REPLACE_EXISTING);)
        //System.out.println(url);
        //System.out.println(Paths.get(filename).toString());
        System.out.println(complete_path);
        return Paths.get(complete_path).toString();
    }

    private static String getFilename(InputStream in, String url){
        String[] url_splitted = url.split("/");
        url_splitted = url_splitted[url_splitted.length-1].split("\\.");
        String filename = Integer.toString(in.hashCode());
        if (url_splitted.length > 1) {
            filename = filename + "." + url_splitted[url_splitted.length-1];
        }
        return filename;
    }

    public String ToPlainText(String html) {
        return Jsoup.parse(html).text();
    }
}
