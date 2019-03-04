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

class ParserRss {
    private Document document;
    private boolean atom;


    ParserRss() {
        atom = false;
    }

    ArrayList<Article> parse(String url_name) throws MalformedURLException {
        URL url = new URL(url_name);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        try {
            docBuilder = dbf.newDocumentBuilder();
            InputStream stream = url.openStream();
            document = docBuilder.parse(stream);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

        check_atom();


        return parse_articles();
    }

    private void check_atom(){
        if (document.getElementsByTagName("feed").getLength() == 1){
            atom = true;
        }
    }

    private Article parse_article_atom(Element entry){
        Article article = new Article();
        article.set_title(get_string(entry, "title"));
        article.set_author(get_string(entry, "author"));
        article.set_category(get_string(entry, "category"));
        article.set_link(get_link_atom(entry));
        article.set_description(get_string(entry, "content"));
        article.set_published_date(get_date_atom(get_string(entry, "published")));
        article.set_updated_date(get_date_atom(get_string(entry, "updated")));
        System.out.println(article);
        return article;
    }

    private Article parse_article_rss(Element item){
        Article article = new Article();
        article.set_title(get_string(item, "title"));
        article.set_author(get_string(item, "author"));
        article.set_category(get_string(item, "category"));
        article.set_link(get_string(item, "link"));
        article.set_description(get_string(item, "description"));
        article.set_published_date(get_date_rss(get_string(item, "pubDate")));
        article.set_updated_date(get_date_rss(get_string(item, "lastBuildDate")));
        System.out.println(article);
        return article;
    }

    private Date get_date_rss(String date){
        Date res = null;
        if (date != null){
            try {
                res =  new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z", new Locale("en")).parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    private Date get_date_atom(String date){
        Date res = null;
        try {
            //return new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss XXX").parse(date);
            res =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    private String get_link_atom(Element entry){
        NodeList title = entry.getElementsByTagName("link");
        try {
            return title.item(0).getAttributes().getNamedItem("href").getTextContent().trim();
        } catch (NullPointerException e){
        }
        return null;

    }

    private String get_string(Element entry, String tag){
        NodeList title = entry.getElementsByTagName(tag);
        try {
            return title.item(0).getTextContent().trim();
        } catch (NullPointerException e){

        }
        return null;
    }

    private ArrayList<Article> parse_articles(){
        NodeList entryList;
        ArrayList<Article> articles = new ArrayList<>();

        if (atom) {
            entryList = get_node_list("feed", "entry");
            for (int i = 0; i < entryList.getLength(); i++) {
                Element entry = (Element) entryList.item(i);
                articles.add(parse_article_atom(entry));
            }
        }
        else{
            entryList = get_node_list("channel", "item");
            for (int i = 0; i < entryList.getLength(); i++) {
                Element entry = (Element) entryList.item(i);
                articles.add(parse_article_rss(entry));
            }
        }
        System.out.println(entryList.getLength());


        return articles;
    }

    private NodeList get_node_list(String feed_name, String entry_name){
        NodeList nodes = document.getElementsByTagName(feed_name);
        Element element = (Element) nodes.item(0);
        return element.getElementsByTagName(entry_name);
    }
}
