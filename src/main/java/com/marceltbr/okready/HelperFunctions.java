package com.marceltbr.okready;

import com.marceltbr.okready.entities.AppUser;
import com.marceltbr.okready.entities.motivator.*;
import com.marceltbr.okready.repositories.motivator.*;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

public class HelperFunctions {

    private static final Logger LOGGER = Logger.getLogger(HelperFunctions.class.getName());

    public static Object makeMotivatorsListDTO(Motivator userMotivator, CategoryQuoteRepository categoryQuoteRepo) {


        List<Map<String, Object>> motivatorsList = null;

        Set<Category> categorySet = userMotivator.getCategorySet();



        boolean cateogriesFound = categorySet.size() > 0;


        if(cateogriesFound){


            motivatorsList =  categorySet.stream().map(category -> {

                /**
                 * @first:
                 *
                 * fill motivators list with each category's id and category name.
                 *
                 */

                Map<String, Object> categoryMap = makeCategoryObjectWithNameAndId(category);


                /**
                 *
                 * @second:
                 *
                 *  find all quotes belonging to the current category
                 *
                 */

                Set<CategoryQuote> categoryQuoteSet = categoryQuoteRepo.findByCategoryId(category.getId());

                /**
                 * @third:
                 *
                 * make a quoteObject for each curent category's quotes
                 * and add it to the current category's map
                 *
                 */

                Set<Map<String, Object>> quoteMapSet = makeQuoteMapSet(categoryQuoteSet);

                categoryMap.put("items", quoteMapSet);

                return categoryMap;

            }).collect(Collectors.toList());


        } else {

            /**
             *
             * if no category was found, return an empty array;
             *
             */

            motivatorsList = new ArrayList<>();
        }



        return motivatorsList ;
    }


    public static Set<Map<String, Object>> makeQuoteMapSet(Set<CategoryQuote> categoryQuoteSet) {
        return categoryQuoteSet.stream().map(categoryQuote -> {

            Quote quote = categoryQuote.getQuote();

            return makeQuoteMap(quote);

        }).collect(Collectors.toSet());
    }

    public static Map<String,Object> makeQuoteMap(Quote quote) {

        Map<String, Object> quoteMap = new HashMap<>();

        quoteMap.put("id", quote.getId());
        quoteMap.put("content", quote.getContent());

        return quoteMap;
    }

    public static Map<String, Object> makeCategoryObjectWithNameAndId(Category category) {
        Map<String, Object> categoryMap = new HashMap<>();

        categoryMap.put("id", category.getId());
        categoryMap.put("category", category.getName());
        return categoryMap;
    }

    public static void saveCategoryQuotesFromMotivatorListObject(Object nextObject, CategoryRepository categoryRepo, QuoteRepository quoteRepo, CategoryQuoteRepository categoryQuoteRepo) {

        String categoryObjectStr = nextObject.toString();

        JSONObject motivatorObj = new JSONObject(  categoryObjectStr );
        String categoryName = motivatorObj.getString("category");

        JSONArray quotesObject = motivatorObj.getJSONArray("items");

        System.out.println("\n" + categoryName + ": " + quotesObject + "\n");


        long categoryId = motivatorObj.getLong("id");
        LOGGER.warning("categoryId: " + categoryId);

        Category currentCategory;
        if(categoryId != 0 ) {
             currentCategory = categoryRepo.findById(categoryId);
        } else {
            currentCategory = categoryRepo.findByName(categoryName);
        }


        Iterator quotesIterator = quotesObject.iterator();

        while(quotesIterator.hasNext()){
            /**
             *
             * @quoteString: JSON's current quote object in String format
             * @content: JSON's current quote.content
             * @quoteID: JSON's current quote.id
             *
             * if @quoteID is equal to zero that means that it is new and must be created
             *
             * else, check if it was created under the current category id
             *
             */

            String quoteString = quotesIterator.next().toString();

            JSONObject quoteObject = new JSONObject(quoteString);

            long quoteID = quoteObject.getLong("id");

            String content = quoteObject.getString("content");

            if(quoteID == 0){

                List<Quote> maybeQuoteList = quoteRepo.findByContent(content);

                boolean quoteExists = maybeQuoteList.size() > 0;

                if(!quoteExists) {
                    Quote newQuote = makeQuote(content, quoteRepo);
                    CategoryQuote categoryQuote = makeCategoryQuote(currentCategory, newQuote, categoryQuoteRepo);
                    LOGGER.warning("quote and categoryQuote made!");
                    LOGGER.info(newQuote.getContent());
                    LOGGER.info(categoryQuote.toString());

                } else {

                    decideIfNewQuoteIsNeededAndMakeIt(quoteRepo, categoryQuoteRepo, currentCategory, content);
                }

            } else {
                decideIfNewQuoteIsNeededAndMakeIt(quoteRepo, categoryQuoteRepo, currentCategory, content);
            }

        }


    }

    private static void decideIfNewQuoteIsNeededAndMakeIt(QuoteRepository quoteRepo, CategoryQuoteRepository categoryQuoteRepo, Category currentCategory, String content) {
        List<Quote> maybeQuoteList;
        maybeQuoteList = quoteRepo.findByContent(content);

        long foundSameCategoryQuotes = maybeQuoteList.stream().map(quote -> {

            CategoryQuote refCategoryQuote = categoryQuoteRepo.findByQuote(quote);


            LOGGER.severe("currentCategoryId: " + currentCategory.getId() + " | refCategoryQuoteCategoryId: " +refCategoryQuote.getCategory().getId());

             return Objects.equals(currentCategory.getId(), refCategoryQuote.getCategory().getId());

        }).filter(quoteFound -> quoteFound == true ).count();

        boolean sameCategoryQuote = foundSameCategoryQuotes >= 1;

        if(!sameCategoryQuote) {
            Quote newQuote = makeQuote(content, quoteRepo);
            CategoryQuote categoryQuote = makeCategoryQuote(currentCategory, newQuote, categoryQuoteRepo);
            LOGGER.warning("newQuote and categoryQuote made!");
            LOGGER.info(newQuote.getContent());
            LOGGER.info(categoryQuote.toString());

        }
    }


    public static Motivator saveCategoryFromMotivatorListObject(Object nextObject, Motivator motivator, CategoryRepository categoryRepo) {


        String categoryObjectStr = nextObject.toString();

        System.out.println(categoryObjectStr);

        JSONObject motivatorObj = new JSONObject(  categoryObjectStr );

        String categoryName = motivatorObj.getString("category");

        boolean categoryExists = motivator.getCategorySet().stream().filter(category -> Objects.equals(category.getName(), categoryName ) ).count() > 0;

        if(!categoryExists){
            makeCategory(categoryName, motivator, categoryRepo);
            System.out.println(categoryName + " created!\n");
        } else {
            System.out.println(categoryName + " already exists!\n");
        }




        return motivator;
    }

    public static AppUserMotivator makeAppUserMotivator(Motivator motivator, AppUser appUser, AppUserMotivatorRepository appUserMotivatorRepository){


        AppUserMotivator appUserMotivator = new AppUserMotivator(motivator, appUser);

        appUserMotivatorRepository.save(appUserMotivator);

        return appUserMotivator;
    }


    public static Motivator makeMotivator(MotivatorRepository motivatorRepository){

        Motivator motivator = new Motivator();
        motivatorRepository.save(motivator);

        return motivator;
    }

    public static Category makeCategory (@NotNull String categoryName, @NotNull Motivator motivator, CategoryRepository categoryRepository) {

        Category category = new Category(motivator, categoryName);
        categoryRepository.save(category);

        return category;

    }

    public static CategoryQuote makeCategoryQuote(Category category, Quote quote, CategoryQuoteRepository categoryQuoteRepository){

        CategoryQuote categoryQuote = new CategoryQuote(quote, category);
        categoryQuoteRepository.save(categoryQuote);

        return categoryQuote;
    }

    public static Quote makeQuote (@NotNull String content, QuoteRepository quoteRepository){

        Quote quote = new Quote(content);
        quoteRepository.save(quote);

        return quote;
    }

    protected static Map<String, Object> makeMap(String s, Object object) {

        Map<String, Object> map = new HashMap<String, Object>() {

            {
                put(s, object);

            }

        };
        return map;
    }


}
