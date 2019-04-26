package be.ac.ulb.infof307.g04.controller;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import be.ac.ulb.infof307.g04.model.DatabaseArticle;
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

    public ArrayList<DatabaseArticle> parse(String _urlName) throws IOException, SAXException, ParserConfigurationException, ParseException {
        /**
         * parse an rss feed
         * @param _url_name
         *              url of the rss feed
         * @see databaseArticle
         * @throws MalformedURLException : if the url is not valid
         * @return a list of articles
         */
        atom = false;

        URL url = null;

        url = new URL(_urlName);


        if (url != null) {
            getXmlFile(url);
            checkAtom();
            return parseArticles();
        }

        return new ArrayList<>();
    }

    private void getUpdated(Element _element) {
        /**
         * Get the updated field of an entry
         * @see parse()
         * @param _element
         *              the entry
         */
        NodeList elementNodes;
        if(atom) {
            elementNodes = _element.getElementsByTagName("updated");
        }
        else {
            elementNodes = _element.getElementsByTagName("lastBuildDate");
        }
        if (elementNodes.getLength() > 0) {
            this.updated = elementNodes.item(0).getTextContent().trim();
        }
        else{
            this.updated = null;
        }
    }


    private void getXmlFile(URL _url) throws ParserConfigurationException, IOException, SAXException {
        /**
         * download the xml file of the rss feed
         * @see <a href=" https://www.programcreek.com/java-api-examples/?class=javax.xml.parsers.DocumentBuilder&method=parse">source</a>
         *
         * @param _url
         *          _url of the feed$
         * @throws ParserConfigurationException
         * @throws IOException
         * @throws SAXException
         */
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;

        docBuilder = dbf.newDocumentBuilder();
        InputStream stream = _url.openStream();
        document = docBuilder.parse(stream);

    }

    private void checkAtom() {
        /*
         * Check if the feed is an atom feed or a rss2.0 feed
         */
        if (document.getElementsByTagName("feed").getLength() == 1) {
            atom = true;
        }
    }


    private ArrayList<DatabaseArticle> parseArticles() throws ParseException {
        /**
         * Parse a load of articles
         * @see databaseArticle
         * @return list of the articles
         */
        NodeList entryList;
        ArrayList<DatabaseArticle> articles = new ArrayList<>();

        if (atom) {
            entryList = getNodeList("feed", "entry");
        } else {
            entryList = getNodeList("channel", "item");
        }
        for (int i = 0; i < entryList.getLength(); i++) {
            Element entry = (Element) entryList.item(i);
            articles.add(parseArticle(entry));
        }
        return articles;
    }


    private NodeList getNodeList(String _feedName, String _entryName) {
        /**
         * get all the entries of the feed
         * @param _feedName
         *                 name of the principal entry (feed of channel)
         * @param _entryName
         *                  name of the articles entries (entry of item)
         * @return xml nodes
         */
        NodeList nodes = document.getElementsByTagName(_feedName);
        Element element = (Element) nodes.item(0);
        getUpdated(element);
        return element.getElementsByTagName(_entryName);
    }


    private DatabaseArticle parseArticle(Element _entry) throws ParseException {
        /**
         * parse a specific article
         * @see databaseArticle
         * @param _entry
         *            xml article _entry
         * @return an article
         */
        DatabaseArticle article = new DatabaseArticle();
        article.setTitle(getString(_entry, "title"));
        article.setAuthor(getString(_entry, "author"));
        article.setCategory(getString(_entry, "category"));
        if (atom) {
            parseArticleAtom(_entry, article);
        } else {
            parseArticleRss(_entry, article);
        }
        return article;
    }

    private void parseArticleAtom(Element _entry, DatabaseArticle _article) throws ParseException {
        /**
         * specific parse for atom feeds
         * @param _entry
         *             xml _article _entry
         * @param _article
         *              _article that is being built
         */
        _article.setLink(getLinkAtom(_entry));
        _article.setDescription(getString(_entry, "content"));
        _article.setPublishedDate(getDate(getString(_entry, "published")));
        _article.setUpdatedDate(getDate(getString(_entry, "updated")));
    }

    private void parseArticleRss(Element _item, DatabaseArticle _article) throws ParseException {
        /**
         * specific parse for rss2.0 feeds
         * @param _item
         *           xml _article _item
         * @param _article
         *           _article that is being built
         */
        _article.setLink(getString(_item, "link"));
        _article.setDescription(getString(_item, "description"));
        _article.setPublishedDate(getDate(getString(_item, "pubDate")));
        _article.setUpdatedDate(getDate(getString(_item, "lastBuildDate")));
    }

    private Date getDate(String date) throws ParseException {
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
        else {
            if (atom) {
                res = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(date);
            } else {
                res = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z", new Locale("en")).parse(date);
            }
        }

        return res;
    }


    private String getLinkAtom(Element _entry) {
        /**
         * get the link of an atom feed
         * @param _entry
         *          xml article _entry
         * @throws IOException
         * @return link of the atom
         */
        NodeList tag_node = _entry.getElementsByTagName("link");
        if (tag_node.getLength() > 0) {
            return tag_node.item(0).getAttributes().getNamedItem("href").getTextContent().trim();
        } else {
            return null;
        }
    }

    private String getString(Element _entry, String _tag) {
        /**
         * get the string of a _tag of an _entry
         * @param _entry
         *          xml articl _entry
         * @param _tag
         *          _tag of the xml _entry
         * @return text of the specific _tag
         */
        NodeList tagNode = _entry.getElementsByTagName(_tag);
        if(tagNode.getLength() > 0) {
            return tagNode.item(0).getTextContent().trim();
        }
        else{
            return null;
        }
    }

}
