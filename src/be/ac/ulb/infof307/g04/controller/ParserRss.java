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

/**
 * Class Parser Rss, used to parse an RSS link
 */

public class ParserRss {
    private Document document;
    private boolean atom;
    private String updated;

    public ParserRss() {
    }

    public ArrayList<Article> parse(String url_name) throws IOException, SAXException, ParserConfigurationException {
        /**
         * parse an rss feed
         * @param url_name
         *              url of the rss feed
         * @see Article
         * @throws MalformedURLException : if the url is not valid
         * @return a list of articles
         */
        atom = false;

        URL url = null;

        url = new URL(url_name);


        if (url != null) {
            get_xml_file(url);
            check_atom();
            return parse_articles();
        }

        return new ArrayList<>();
    }

    private void get_updated(Element element) {
        /**
         * Get the updated field of an entry
         * @see parse()
         * @param element
         *              the entry
         */
        if(atom) {
            updated = element.getElementsByTagName("updated").item(0).getTextContent().trim();
        }
        else {
            updated = element.getElementsByTagName("lastBuildDate").item(0).getTextContent().trim();
        }
    }


    private void get_xml_file(URL url) throws ParserConfigurationException, IOException, SAXException {
        /**
         * download the xml file of the rss feed
         * @param url
         *          url of the feed$
         * @throws ParserConfigurationException
         * @throws IOException
         * @throws SAXException
         *
         */
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;

        docBuilder = dbf.newDocumentBuilder();
        InputStream stream = url.openStream();
        document = docBuilder.parse(stream);


    }

    private void check_atom() {
        /*
         * Check if the feed is an atom feed or a rss2.0 feed
         */
        if (document.getElementsByTagName("feed").getLength() == 1) {
            atom = true;
        }
    }


    private ArrayList<Article> parse_articles() {
        /**
         * Parse a load of articles
         * @see Article
         * @return list of the articles
         */
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


    private NodeList get_node_list(String feed_name, String entry_name) {
        /**
         * get all the entries of the feed
         * @param feed_name
         *                 name of the principal entry (feed of channel)
         * @param entry_name
         *                  name of the articles entries (entry of item)
         * @return xml nodes
         */
        NodeList nodes = document.getElementsByTagName(feed_name);
        Element element = (Element) nodes.item(0);
        get_updated(element);
        return element.getElementsByTagName(entry_name);
    }


    private Article parse_article(Element entry) {
        /**
         * parse a specific article
         * @see Article
         * @param entry
         *            xml article entry
         * @return an article
         */
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

    private void parse_article_atom(Element entry, Article article) {
        /**
         * specific parse for atom feeds
         * @param entry
         *             xml article entry
         * @param article
         *              article that is being built
         */
        article.setLink(get_link_atom(entry));
        article.setDescription(get_string(entry, "content"));
        article.setPublished_date(get_date(get_string(entry, "published")));
        article.setUpdated_date(get_date(get_string(entry, "updated")));
    }

    private void parse_article_rss(Element item, Article article) {
        /**
         * specific parse for rss2.0 feeds
         * @param item
         *           xml article item
         * @param article
         *           article that is being built
         */
        article.setLink(get_string(item, "link"));
        article.setDescription(get_string(item, "description"));
        article.setPublished_date(get_date(get_string(item, "pubDate")));
        article.setUpdated_date(get_date(get_string(item, "lastBuildDate")));
    }

    private Date get_date(String date) {
        /**
         * get a date from a string
         * @param date
         *          string containing the date
         * @return date object
         */
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


    private String get_link_atom(Element entry) {
        /**
         * get the link of an atom feed
         * @param entry
         *          xml article entry
         * @throws IOException
         * @return link of the atom
         */
        NodeList tag_node = entry.getElementsByTagName("link");
        if (tag_node.getLength() > 0) {
            return tag_node.item(0).getAttributes().getNamedItem("href").getTextContent().trim();
        } else {
            return null;
        }
    }

    private String get_string(Element entry, String tag) {
        /**
         * get the string of a tag of an entry
         * @param entry
         *          xml articl entry
         * @param tag
         *          tag of the xml entry
         * @return text of the specific tag
         */
        NodeList tag_node = entry.getElementsByTagName(tag);
        if(tag_node.getLength() > 0) {
            return tag_node.item(0).getTextContent().trim();
        }
        else{
            return null;
        }
    }

}
