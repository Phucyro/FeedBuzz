package be.ac.ulb.infof307.g04.model;


import be.ac.ulb.infof307.g04.view.MessageBoxes;
import de.l3s.boilerpipe.extractors.CommonExtractors;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;

/**
 * Class ArticleLabeliseur, used to associate a label to an article to use a recommandation system
 */

public class ArticleLabelizer {
    public static final String WORDLISTS_FILENAME = "src/be/ac/ulb/infof307/g04/model/wordlists.json";
    private static ArrayList<String> bagOfWord = new ArrayList<String>();
    private static ArrayList<String> tags = new ArrayList<>();
    private static int bagSize;
    private static double[] histogramArticle; //occurence histogram for each words of the bagOfWord
    private static double[][] histogramTopics; //list of the histograms for each topic
    private static ArrayList<Integer> wordCountsEachCategory;
    private static boolean jsonParsed = false;

    private static boolean getJsonParsed() {
        return jsonParsed;
    }

    private static void setJsonParsed(boolean newValue) {
        jsonParsed = newValue;
    }

    /**
     * Constructor that construct the bag containing the words from the wordlists, and construct the histogram associated with each category
     */
    public static String labelizeArticle(String _HTMLContent) {
        if (!getJsonParsed()) {
            JSONParser parser = new JSONParser();
            parseJson(parser);
        }
        resetHistogram();
        String res;
        try {
            res = labelize(_HTMLContent);
        } catch (de.l3s.boilerpipe.BoilerpipeProcessingException | RuntimeException ignore) {
            // If error caused by Boilerpipe, tag stays at Default
            res = "Default";
        }
        return res;
    }

    /**
     * Method called once that open the json "wordlists.json" file and parse it into the word arrays
     *
     * @param _parser is the JSON parser that is used to read the json file
     * @throws java.io.IOException
     * @throws org.json.simple.parser.ParseException
     */
    private static void parseJson(JSONParser _parser) {
        wordCountsEachCategory = new ArrayList<>();
        try {
            Object objectParser = _parser.parse(new FileReader(WORDLISTS_FILENAME));
            JSONObject object = (JSONObject) objectParser;
            // iterate through the keys of the JSONObject
            for (Object o : object.keySet()) {

                String tag = (String) o;
                tags.add(tag);
                JSONArray array = (JSONArray) object.get(tag); // get the array associated with the key

                // iterate through the words of the wordlist
                // add the word to the bag of words
                bagOfWord.addAll(array);
                wordCountsEachCategory.add(array.size());
            }
        } catch (java.io.IOException | org.json.simple.parser.ParseException e) {
            MessageBoxes.showErrorBox("Parse exception");
        }
        setJsonParsed(true);
    }

    /**
     * Method that reset the values that have been put in the histogramArticle array
     */
    private static void resetHistogram() {
        bagSize = bagOfWord.size();
        histogramArticle = new double[bagSize]; // allocation of the histogram of the article;
        histogramTopics = new double[tags.size()][bagSize]; // allocations of each histograms associated with each categories
        int wordCount = 0;

        // for each histogram associated with each category, increment the index if the word of the bow come from this category and normalize the histogram
        for (int i = 0; i < tags.size(); i++) {
            for (int j = wordCount; j < wordCount + wordCountsEachCategory.get(i); j++) {
                histogramTopics[i][j] = 1.0 / Math.sqrt(wordCountsEachCategory.get(i));
            }
            wordCount += wordCountsEachCategory.get(i); //

        }
    }

    /**
     * Use the bag of word method to construct an histogram of occurence of word from the bag of words from the article content
     * Use the cosine similarity (check the theory behind it : https://towardsdatascience.com/overview-of-text-similarity-metrics-3397c4601f50)
     * to assign a score for each category (the most probable category based on the article content), then it tags the article with that category
     *
     * @param _HTMLContent html linl
     */
    private static String labelize(String _HTMLContent) throws de.l3s.boilerpipe.BoilerpipeProcessingException {
        if (_HTMLContent.isEmpty()) {
            throw new RuntimeException("String to labelize is empty");
        }
        int index;
        int wordsCount = 0;
        int mostProbableLabelIndex = 0;
        double scores[] = new double[tags.size()];
        String articleContent = CommonExtractors.ARTICLE_EXTRACTOR.getText(_HTMLContent); // boilerpipe extract the text content of the article
        for (String word : articleContent.toLowerCase().split(" ")) {
            //Read the text word by word
            index = bagOfWord.indexOf(word);
            if (index != -1) { // if the current word of the article is found in the bag of word, it increments the index of the histogram associated with that word
                histogramArticle[index] += 1;
            }
        }
        doCalculations(wordsCount);
        mostProbableLabelIndex = findMostProbableLabel(mostProbableLabelIndex, scores);
        return tags.get(mostProbableLabelIndex);
    }

    /**
     * Method that does the calculations related to finding the right label for the text
     *
     * @param _wordsCount Is a variable that counts the relevant words
     */
    private static void doCalculations(int _wordsCount) {
        // sum up the square of each terms to normalize the vector sqrt(d1^2 + d2^2 + .. + dn^2)
        // the score is calculated with the cosine similarity for each category ( score = A1*B1 + A2*B2 + .. + An*Bn )
        for (int i = 0; i < bagOfWord.size(); i++) {
            _wordsCount += Math.pow(histogramArticle[i], 2);
        }
        // apply the square root of the sum
        for (int i = 0; i < bagOfWord.size(); i++) {
            histogramArticle[i] = histogramArticle[i] / Math.sqrt(_wordsCount);
        }
    }

    /**
     * @param _mostProbableLabelIndex is the index of the most probable label for a given text
     * @param _scores                 is the score of a given label
     * @return the index of the label that is most probable for a given text
     */
    private static int findMostProbableLabel(int _mostProbableLabelIndex, double[] _scores) {
        // the highest score is the most probable category
        for (int i = 0; i < tags.size(); i++) {
            for (int j = 0; j < bagOfWord.size(); j++) {
                _scores[i] += histogramArticle[j] * histogramTopics[i][j];
            }
            if (_scores[i] > _scores[_mostProbableLabelIndex]) {
                _mostProbableLabelIndex = i;
            }
        }
        return _mostProbableLabelIndex;
    }
}