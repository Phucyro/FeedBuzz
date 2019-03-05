package controller;

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


    public ParserRss() {
    }

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

    private void check_atom() {
        if (document.getElementsByTagName("feed").getLength() == 1) {
            atom = true;
        }
    }

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

    private NodeList get_node_list(String feed_name, String entry_name) {
        NodeList nodes = document.getElementsByTagName(feed_name);
        Element element = (Element) nodes.item(0);
        return element.getElementsByTagName(entry_name);
    }


    private Article parse_article(Element entry) {
        Article article = new Article();
        article.set_title(get_string(entry, "title"));
        article.set_author(get_string(entry, "author"));
        article.set_category(get_string(entry, "category"));
        if (atom) {
            parse_article_atom(entry, article);
        } else {
            parse_article_rss(entry, article);
        }
        System.out.println(article);
        return article;
    }

    private void parse_article_atom(Element entry, Article article) {
        article.set_link(get_link_atom(entry));
        article.set_description(get_string(entry, "content"));
        article.set_published_date(get_date(get_string(entry, "published")));
        article.set_updated_date(get_date(get_string(entry, "updated")));
    }

    private void parse_article_rss(Element item, Article article) {
        article.set_link(get_string(item, "link"));
        article.set_description(get_string(item, "description"));
        article.set_published_date(get_date(get_string(item, "pubDate")));
        article.set_updated_date(get_date(get_string(item, "lastBuildDate")));
    }

    private Date get_date(String date) {
        Date res = null;
        if (date != null) {
            try {
                if (atom) {
                    res = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(date);
                } else {
                    res = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z", new Locale("en")).parse(date);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    private String get_link_atom(Element entry) {
        NodeList tag_node = entry.getElementsByTagName("link");
        if (tag_node.getLength() > 0) {
            return tag_node.item(0).getAttributes().getNamedItem("href").getTextContent().trim();
        } else {
            return null;
        }
    }

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
