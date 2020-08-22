package com.marceltbr.okready;

import com.marceltbr.okready.entities.AppUser;
import com.marceltbr.okready.entities.motivator.*;
import com.marceltbr.okready.repositories.motivator.*;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HelperFunctions {

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

    public static Category makeCategory (@NotNull String categoryName, CategoryRepository categoryRepository) {

        Category category = new Category(categoryName);
        categoryRepository.save(category);

        return category;

    }

    public static void makeCategoryQuote(Category category, Quote quote, CategoryQuoteRepository categoryQuoteRepository){

        CategoryQuote categoryQuote = new CategoryQuote(quote, category);
        categoryQuoteRepository.save(categoryQuote);

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
