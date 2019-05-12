package be.ac.ulb.infof307.g04.model;


import de.l3s.boilerpipe.extractors.CommonExtractors;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;

/**
 * Class ArticleLabeliseur, used to associate a label to an article to use a recommandation system
 * @see DatabaseArticle
 */

public class ArticleLabelizer {

    public DatabaseArticle article;
    public ArrayList<String> bag_of_word = new ArrayList<String>();
    public ArrayList<String> labels = new ArrayList<>();
    public int bag_size;


    public double[] histogram_article;   // histogramme d'occurence de chaque mot du bag of word
    public double[][] histogram_topics;  // liste des histogramme de chaque topic

    /**
     * Constructor that construct the bag containing the words from the wordlists, and construct the histogram associated with each category
     */
    public ArticleLabelizer(DatabaseArticle _article){
        article = _article;
        //static{

            ArrayList<Integer> word_counts_each_category = new ArrayList<>();
            JSONParser parser = new JSONParser();
            try{
                Object objectparser = parser.parse(new FileReader("./groupe04/src/be/ac/ulb/infof307/g04/model/wordlists.json")); // parse the json, each entry has the label as the key and an array of words as value
                JSONObject object = (JSONObject) objectparser;

                // iterate through the keys of the JSONObject
                for (Object o : object.keySet()) {

                    String label = (String) o;
                    labels.add(label);
                    JSONArray array = (JSONArray) object.get(label); // get the array associated with the key

                    // iterate through the words of the wordlist
                    // add the word to the bag of words
                    bag_of_word.addAll(array);
                    word_counts_each_category.add(array.size());
                }

                bag_size = bag_of_word.size();
                histogram_article = new double[bag_size]; // allocation of the histogram of the article;
                histogram_topics = new double[labels.size()][bag_size]; // allocations of each histograms associated with each categories
                int word_count=0;

                // for each histogram associated with each category, increment the index if the word of the bow come from this category and normalize the histogram
                for(int i = 0; i<labels.size() ; i++){
                    for(int j= word_count; j<word_count+word_counts_each_category.get(i); j++){
                        histogram_topics[i][j] = 1.0/Math.sqrt(word_counts_each_category.get(i));
                    }
                    word_count+= word_counts_each_category.get(i); //
                }
            } catch(java.io.IOException | org.json.simple.parser.ParseException e){
                System.out.println(e);
            }


    }
    /**
     * Use the bag of word method to construct an histogram of occurence of word from the bag of words from the article content
     * Use the cosine similarity (check the theory behind it : https://towardsdatascience.com/overview-of-text-similarity-metrics-3397c4601f50)
     * to assign a score for each category (the most probable category based on the article content), then it labels the article with that category
     */
    public void labelize() throws de.l3s.boilerpipe.BoilerpipeProcessingException {
        int index;
        int words_count = 0;
        int most_probable_label_index=0;
        double scores[] = new double[labels.size()];

            article.getHtmlContent();
            String article_content = CommonExtractors.ARTICLE_EXTRACTOR.getText(article.getHtmlContent()); // boilerpipe extract the text content of the article
            for (String word: article_content.toLowerCase().split(" ")) {
                index = bag_of_word.indexOf(word);
                if(index!=-1){ // if the current word of the article is found in the bag of word, it increments the index of the histogram associated with that word
                    histogram_article[index]+=1;
                }
            }


            // sum up the square of each terms to normalize the vector sqrt(d1^2 + d2^2 + .. + dn^2)
            for(int i=0; i<bag_of_word.size();i++){
                words_count += Math.pow(histogram_article[i], 2);
            }
            // apply the square root of the sum
            for(int i=0; i<bag_of_word.size();i++){
                histogram_article[i] = histogram_article[i]/Math.sqrt(words_count);
            }


            // the score is calculated with the cosine similarity for each category ( score = A1*B1 + A2*B2 + .. + An*Bn )
            // the highest score is the most probable category
            for(int i=0; i<labels.size(); i++){
                for(int j=0; j<bag_of_word.size(); j++){
                    scores[i] += histogram_article[j]*histogram_topics[i][j];
                }
                if(scores[i]>scores[most_probable_label_index]){
                    most_probable_label_index = i;
                }
            }
            System.out.println("Most probable category : "+ labels.get(most_probable_label_index));
            article.setTags(labels.get(most_probable_label_index));


    }
}