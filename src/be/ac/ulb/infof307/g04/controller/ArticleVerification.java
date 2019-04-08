package be.ac.ulb.infof307.g04.controller;
import java.util.ArrayList;

/**
 * Class ArticleVerification, where articles are analyzed on different to see their integrity or their equilaty
 *
 * @version 2.0
 *
 */

public class ArticleVerification {
    private Article article_to_verify;
    private Article article_from_source;
    private String source;

    public ArticleVerification(Article article, String source_url){
        /**
         * Constructor of the Article verification
         *
         * @param article
         *              get the article that needs to be verified
         * @param source_url
         *              get the url of the article
         */
        article_to_verify = article;
        source = source_url;
    }

    /**
     * Get all the articles from a source
     * @return An ArrayList of articles
     */
    public ArrayList<Article> get_articles_from_source(){
        ParserRss parser = new ParserRss();
        ArrayList<Article> articles;
        articles = parser.parse(source);
        return articles;
    }


    public static int hashCode(Object o) {
        /*
        Method used to generate a hash
         */
        return o != null ? o.hashCode() : 0;
    }

    public boolean is_equal(){
        /**
         * Test if an article is equal to another
         * @return boolean
         * @throws
         */

        Article a1 = article_to_verify;
        Article a2 = article_from_source;

        // condition de la forme, soit les deux champs sont nuls, soit aucun des deux n'est nul et les hash correspondent
        if((((a1.getDescription() == null && a2.getDescription() == null ) || (a1.getDescription() != null && a2.getDescription() != null )) && hashCode(a1.getDescription()) == hashCode(a2.getDescription())) &&
                /*(((a1.getCategory() == null && a2.getCategory() == null ) || (a1.getCategory() != null && a2.getCategory() != null )) && hashCode(a1.getCategory()) == hashCode(a2.getCategory())) &&*/
                    (((a1.getLink() == null && a2.getLink() == null ) || (a1.getLink() != null && a2.getLink() != null )) && hashCode(a1.getLink()) == hashCode(a2.getLink())) &&
                        (((a1.getPublished_date() == null && a2.getPublished_date() == null ) || (a1.getPublished_date() != null && a2.getPublished_date() != null )) && hashCode(a1.getPublished_date()) == hashCode(a2.getPublished_date())) &&
                            (((a1.getUpdated_date() == null && a2.getUpdated_date() == null ) || (a1.getUpdated_date() != null && a2.getUpdated_date() != null )) && hashCode(a1.getUpdated_date()) == hashCode(a2.getUpdated_date()))){
            return true;
        }
        else{
            return false;
        }
    }



    public void set_article(Article article){
        /*
        Set an article
         */
        article_to_verify = article;
    }


    public boolean is_valid(){
        /**
         * Check if an article exists and wasn't modified from the source
         *
         * @return boolean
         * @see is_equal()
         */
        ArrayList<Article> articles = new ArrayList<>();
        articles = get_articles_from_source();
        boolean found = false;
        for(int i=0; i< articles.size(); i++){
            if(((article_to_verify.getTitle() == null && articles.get(i).getTitle() == null ) || (article_to_verify.getTitle() != null && articles.get(i).getTitle() != null )) && article_to_verify.getTitle().equals(articles.get(i).getTitle()) &&
                    ((article_to_verify.getAuthor() == null && articles.get(i).getAuthor() == null ) || (article_to_verify.getAuthor() != null && articles.get(i).getAuthor() != null )) && hashCode(article_to_verify.getAuthor()) == hashCode(articles.get(i).getAuthor())){
                article_from_source = articles.get(i);
                return is_equal();
            }
        }
        return false;
    }


    public boolean is_correctable(){
        /**
         * check if an article can be corrected
         * @see Article
         */

        /* Champs Unique (clé) ->  description , (title+ author), link -> si le champ de l'article broken correspond avec un champ d'un article d'une source on peut mettre à jour un article
        Champs non unique -> dates, categories, author, title -> à partir de ces champs on ne peut pas reconstruire un article (plusieurs articles peuvent avoir le meme titre ou le meme auteur, mais probablement pas le meme titre et le meme auteur)
        exemple, les description des articles-> un des deux champs est null -> false  ,  les deux champs sont null -> false  ,  les deux champs sont non null et correspondent -> true
        false = pas de possibilité de corriger l'article avec le champs courant
        true = possibilité de corriger l'article à partir de la source grace a une correspondance des champs*/
        ArrayList<Article> articles = new ArrayList<>();
        articles = get_articles_from_source();

        for(int i=0; i< articles.size(); i++){
            if(((article_to_verify.getLink() != null && articles.get(i).getLink() != null   )   &&   (   hashCode(article_to_verify.getLink()) == hashCode(articles.get(i).getLink()))) ||
                    ((article_to_verify.getTitle() != null && articles.get(i).getTitle() != null ) && (  hashCode(article_to_verify.getTitle()) == hashCode(articles.get(i).getTitle())) &&
                    (article_to_verify.getAuthor() != null && articles.get(i).getAuthor() != null   ) && (  hashCode(article_to_verify.getAuthor()) == hashCode(articles.get(i).getAuthor()))) ||
                        ((article_to_verify.getDescription() != null && articles.get(i).getDescription() != null ) && (  hashCode(article_to_verify.getDescription()) == hashCode(articles.get(i).getDescription())))){
                article_from_source = articles.get(i);
                return true;
            }
        }
        return false;
    }


    public void correct_article(){
        /*
         * correct an article if it is possible
         */
        if(is_correctable()){
            article_to_verify.setTitle(article_from_source.getTitle());
            article_to_verify.setAuthor(article_from_source.getAuthor());
            article_to_verify.setCategory(article_from_source.getCategory());
            article_to_verify.setLink(article_from_source.getLink());
            article_to_verify.setDescription(article_from_source.getDescription());
            article_to_verify.setUpdated_date(article_from_source.getUpdated_date());
            article_to_verify.setPublished_date(article_from_source.getPublished_date());
        }
    }



    public Article get_article(){
        /*
        Get the article that has to be verified
         */
        return article_to_verify;
    }


}
