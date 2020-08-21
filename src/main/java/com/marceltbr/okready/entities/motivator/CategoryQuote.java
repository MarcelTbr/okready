package com.marceltbr.okready.entities.motivator;

import javax.persistence.*;
import java.util.Set;

@Entity
public class CategoryQuote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "quote_id")
    private Quote quote;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "category_id")
    private Category category;

    public CategoryQuote() {

    }

    public CategoryQuote(Quote quote, Category category) {
        this.quote = quote;
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public Quote getQuote() {
        return quote;
    }

    public Category getCategory() {
        return category;
    }
}
