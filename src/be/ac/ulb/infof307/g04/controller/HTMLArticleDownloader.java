package be.ac.ulb.infof307.g04.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HTMLArticleDownloader {
    public static final String HREF_TAG = "href";
    public static final String SRC_TAG = "src";
    public static final String MEDIA_FOLDER = "media/";


    //Recois une URL
    //Extrait tout les liens et les trie par type

    public static String getIconFromDescription(String _articleLink, String _descriptionHtml) throws IOException {
        String folder_name = getFolderName(_articleLink);
        new File(MEDIA_FOLDER).mkdir(); //TODO delete this
        new File(MEDIA_FOLDER + folder_name).mkdir();
        Document doc = Jsoup.parse(_descriptionHtml);
        Elements images = doc.select("img[src]");

        String img_url = "";
        if (images.size() > 0) {
            img_url = images.get(0).attr("abs:src");

        }

        img_url = downloader(img_url, folder_name, "icon." + getFileExtension(img_url));

        return img_url;
    }

    public static String ArticleLocalifier(String _url) throws IOException {
        String folder_name = getFolderName(_url);
        new File(MEDIA_FOLDER).mkdir(); //TODO delete this
        new File(MEDIA_FOLDER + folder_name).mkdir();

        Document doc = Jsoup.connect(_url).get();
        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");

        replaceLinksHref(links, "#");
        downloadReplaceElementsFromTag(media, folder_name, SRC_TAG);
        downloadReplaceElementsFromTag(imports, folder_name, HREF_TAG);

        return downloadReplaceRemainingLinks(doc.toString(), folder_name);
    }

    private static String getFolderName(String _url) {
        return sanitize_string(_url);
    }

    private static String downloadReplaceRemainingLinks(String _input, String _folderName) {
        Pattern pat = Pattern.compile("(http(s?):)([/|.|\\w|\\s|-])*\\.(?:css|jpg|gif|png|js)");

        Matcher mat = pat.matcher(_input);

        while (mat.find()) {
            try {
                _input = _input.replaceAll(mat.group(), downloader(mat.group(), _folderName));
            } catch (IOException e) {
            }
        }
        return _input;
    }

    private static String sanitize_string(String _url) {
        _url = _url.replace("/", "");
        _url = _url.replace("<", "");
        _url = _url.replace(">", "");
        _url = _url.replace(":", "");
        _url = _url.replace("\"", "");
        _url = _url.replace("/", "");
        _url = _url.replace("\\", "");
        _url = _url.replace("|", "");
        _url = _url.replace("?", "");
        _url = _url.replace("*", "");
        return _url;
    }

    private static void replaceLinksHref(Elements _links, String _toReplace) {
        for (Element link : _links) {
            link.attr(HREF_TAG, _toReplace);
        }
    }


    private static void downloadReplaceElementsFromTag(Elements _elements, String _folderName, String _tag) {
        for (Element element : _elements) {
            try {
                element.attr(SRC_TAG, downloader(element.attr(SRC_TAG), _folderName));
            } catch (IOException e) {
            }
        }
    }

    private static String downloader(String _url, String _folderName) throws IOException {
        return downloader(_url, _folderName, null);
    }

    private static String downloader(String _url, String _folderName, String _filename) throws IOException {
        String filename = _filename;
        if (filename == null) {
            filename = getHashedFilename(_url);
        }
        String complete_path = MEDIA_FOLDER + _folderName + "/" + filename;
        try {
            InputStream in = new URL(_url).openStream();
            if (!new File(complete_path).exists()) {
                Files.copy(in, Paths.get(complete_path), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (UnknownHostException e) { //No internet
        }

        return new File(complete_path).toURI().toString();
    }

    private static String getHashedFilename(String _url) {
        String filename = Integer.toString(_url.hashCode());
        filename = filename + "." + getFileExtension(_url);
        return filename;
    }

    private static String getFileExtension(String _url) {
        String[] url_splitted = _url.split("/");
        url_splitted = url_splitted[url_splitted.length-1].split("\\.");
        if (url_splitted.length > 1) {
            return url_splitted[url_splitted.length - 1];
        }
        return "";
    }
}