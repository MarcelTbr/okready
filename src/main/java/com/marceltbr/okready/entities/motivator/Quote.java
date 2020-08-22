package com.marceltbr.okready.entities.motivator;

import javax.persistence.*;

@Entity
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String content;

    @OneToOne(mappedBy = "quote", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE )
    private CategoryQuote categoryQuote;

    public Quote() {
    }

    public Quote(String content) {
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
