package be.ac.ulb.infof307.g04.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HTMLArticleDownloader {
    private static final String HREF_TAG = "href";
    private static final String SRC_TAG = "src";
    private static final String MEDIA_FOLDER = "media/";

    /**
     * Extract all the links and sort them by type
     * @param _articleLink link of the article
     * @param _descriptionHtml description of the article
     * @return url of the image
     * @throws IOException exception due
     */
    static String getIconFromDescription(String _articleLink, String _descriptionHtml) throws IOException {
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


    /**
     * <a href>https://stackoverflow.com/questions/4852531/find-files-in-a-folder-using-java</a>
     * @param _articleLink link of the article to get icon from
     * @return uri of the file that contains the icon
     */
    public static String getIconUrlFromArticleUrl(String _articleLink) throws FileNotFoundException {
        String articleLinkCurated = sanitizeString(_articleLink);
        articleLinkCurated = "media/"+articleLinkCurated;
        File folder = new File(articleLinkCurated);

        File[] matchingFiles = folder.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.startsWith("icon.");
            }
        });

        if (matchingFiles.length == 0 || matchingFiles[0].getName().equals("icon.")) {
            throw new FileNotFoundException();
        }
        else {
            return matchingFiles[0].getAbsolutePath();
        }
    }

    /**
     * @param _url url of the article
     * @return return the modified html to stock
     */
    public static String ArticleLocalifier(String _url) throws IOException {
        String folder_name = getFolderName(_url);
        new File(MEDIA_FOLDER).mkdir(); //TODO delete this
        new File(MEDIA_FOLDER + folder_name).mkdir();

        Document doc = Jsoup.connect(_url).get();
        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");
        doc.getElementsByClass("m-privacy-consent").remove();

        replaceLinksHref(links, "#");
        downloadReplaceElementsFromTag(media, folder_name, SRC_TAG);
        downloadReplaceElementsFromTag(imports, folder_name, HREF_TAG);

        return downloadReplaceRemainingLinks(doc.toString(), folder_name);
    }

    /**
     * @param _url url of the article
     * @return name of the folder
     */
    private static String getFolderName(String _url) {
        return sanitizeString(_url);
    }

    /**
     * replace the remaining links
     * @param _htmlContent html content
     * @param _folderName folder name
     * @return new html content
     */
    static String downloadReplaceRemainingLinks(String _htmlContent, String _folderName) throws IOException {
        Pattern pat = Pattern.compile("(http(s?):)([/|.|\\w|\\s|-])*\\.(?:css|jpg|gif|png|js)");

        Matcher mat = pat.matcher(_htmlContent);

        while (mat.find()) {
            _htmlContent = _htmlContent.replaceAll(mat.group(), downloader(mat.group(), _folderName));
        }
        return _htmlContent;
    }

    /**
     * remove special characters from the url
     * @param _url url to sanitize
     * @return sanitized url
     */
    protected static String sanitizeString(String _url) {
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

    /**
     * Replace the href reference in the links
     */
    protected static void replaceLinksHref(Elements _links, String _toReplace) {
        for (Element link : _links) {
            link.attr(HREF_TAG, _toReplace);
        }
    }


    /**
     * download and replace elements from a specific tag
     * @param _elements elements to download and replace
     * @param _folderName name of the folder
     */
    protected static void downloadReplaceElementsFromTag(Elements _elements, String _folderName, String _tag) {
        for (Element element : _elements) {
            try {
                element.attr(SRC_TAG, downloader(element.attr(SRC_TAG), _folderName));
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * @param _url url of the article
     * @param _folderName name of the folder of the article
     * @param _filename name of the file
     * @return html content
     * @throws IOException exception to handle the opening of files
     */
    private static String downloader(String _url, String _folderName, String _filename) throws IOException {
        String filename = _filename;
        if (filename == null) {
            filename = getHashedFilename(_url);
        }
        String complete_path = MEDIA_FOLDER + _folderName + "/" + filename;
        InputStream in = new URL(_url).openStream();
        if (!new File(complete_path).exists()) {
            Files.copy(in, Paths.get(complete_path), StandardCopyOption.REPLACE_EXISTING);
        }

        return new File(complete_path).toURI().toString();
    }

    /**
     * overload of downloader
     */
    private static String downloader(String _url, String _folderName) throws IOException {
        return downloader(_url, _folderName, null);
    }

    /**
     * @param _url url of the article
     * @return return hashed filename
     */
    private static String getHashedFilename(String _url) {
        String filename = Integer.toString(_url.hashCode());
        filename = filename + "." + getFileExtension(_url);
        return filename;
    }

    /**
     * @param _url url of the article
     * @return return the extension of the file
     */
    private static String getFileExtension(String _url) {
        String[] urlSplitted = _url.split("/");
        urlSplitted = urlSplitted[urlSplitted.length-1].split("\\.");
        if (urlSplitted.length > 1) {
            return urlSplitted[urlSplitted.length - 1];
        }
        return "";
    }
}