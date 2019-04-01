package be.ac.ulb.infof307.g04.controller;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ParserRss {
    private Document document;
    private boolean atom;
    private String updated;

    public ParserRss() {
    }

    /**
     * parse an rss feed
     * @param url_name url of the rss feed
     * @return a list of articles
     */
    public ArrayList<Article> parse(String url_name) {
        atom = false;

        URL url = null;
        try {
            url = new URL(url_name);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (url != null) {
            get_xml_file(url);
            check_atom();
            return parse_articles();
        }

        return new ArrayList<>();
    }

    /**
     * get the updated field of an entry
     * @param element the entry
     */
    private void get_updated(Element element) {
        if(atom) {
            updated = element.getElementsByTagName("updated").item(0).getTextContent().trim();
        }
        else {
            updated = element.getElementsByTagName("lastBuildDate").item(0).getTextContent().trim();
        }
    }

    /**
     * download the xml file of the rss feed
     * @param url url of the feed
     */
    private void get_xml_file(URL url){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;

        try {
            docBuilder = dbf.newDocumentBuilder();
            InputStream stream = url.openStream();
            document = docBuilder.parse(stream);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

    }

    /**
     * check if the feed is an atom feed or a rss2.0 feed
     */
    private void check_atom() {
        if (document.getElementsByTagName("feed").getLength() == 1) {
            atom = true;
        }
    }

    /**
     * @return list of the articles
     */
    private ArrayList<Article> parse_articles() {
        NodeList entryList;
        ArrayList<Article> articles = new ArrayList<>();

        if (atom) {
            entryList = get_node_list("feed", "entry");
        } else {
            entryList = get_node_list("channel", "item");
        }
        for (int i = 0; i < entryList.getLength(); i++) {
            Element entry = (Element) entryList.item(i);
            articles.add(parse_article(entry));
        }
        return articles;
    }

    /**
     * get all the entries of the feed
     * @param feed_name name of the principal entry (feed of channel)
     * @param entry_name name of the articles entries (entry of item)
     * @return xml nodes
     */
    private NodeList get_node_list(String feed_name, String entry_name) {
        NodeList nodes = document.getElementsByTagName(feed_name);
        Element element = (Element) nodes.item(0);
        get_updated(element);
        return element.getElementsByTagName(entry_name);
    }

    /**
     * parse a specific article
     * @param entry xml article entry
     */
    private Article parse_article(Element entry) {
        Article article = new Article();
        article.setTitle(get_string(entry, "title"));
        article.setAuthor(get_string(entry, "author"));
        article.setCategory(get_string(entry, "category"));
        if (atom) {
            parse_article_atom(entry, article);
        } else {
            parse_article_rss(entry, article);
        }
        return article;
    }

    /**
     * specific parse for atom feeds
     * @param entry xml article entry
     * @param article article that is being built
     */
    private void parse_article_atom(Element entry, Article article) {
        article.setLink(get_link_atom(entry));
        article.setDescription(get_string(entry, "content"));
        article.setPublished_date(get_date(get_string(entry, "published")));
        article.setUpdated_date(get_date(get_string(entry, "updated")));
    }

    /**
     * specific parse for rss2.0 feeds
     * @param item xml article item
     * @param article article that is being built
     */
    private void parse_article_rss(Element item, Article article) {
        article.setLink(get_string(item, "link"));
        article.setDescription(get_string(item, "description"));
        article.setPublished_date(get_date(get_string(item, "pubDate")));
        article.setUpdated_date(get_date(get_string(item, "lastBuildDate")));
    }

    /**
     * get a date from a string
     * @param date string containing the date
     * @return date object
     */
    private Date get_date(String date) {
        Date res = null;
        if (date == null){
            date = updated;
        }
        try {
            if (atom) {
                res = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(date);
            } else {
                res = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z", new Locale("en")).parse(date);
            }
            if (res == null){
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * get the link of an atom feed
     * @param entry xml article entry
     * @return
     */
    private String get_link_atom(Element entry) {
        NodeList tag_node = entry.getElementsByTagName("link");
        if (tag_node.getLength() > 0) {
            return tag_node.item(0).getAttributes().getNamedItem("href").getTextContent().trim();
        } else {
            return null;
        }
    }

    /**
     * get the string of a tag of an entry
     * @param entry xml articl entry
     * @param tag tag of the xml entry
     * @return text of the specific tag
     */
    private String get_string(Element entry, String tag) {
        NodeList tag_node = entry.getElementsByTagName(tag);
        if(tag_node.getLength() > 0) {
            return tag_node.item(0).getTextContent().trim();
        }
        else{
            return null;
        }
    }

}
