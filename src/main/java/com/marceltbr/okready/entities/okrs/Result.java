package com.marceltbr.okready.entities.okrs;

import javax.persistence.*;

@Entity
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String title;

    private long winsRatio;

    private long wins;

//    @OneToOne(mappedBy = "result", fetch = FetchType.EAGER)
//    private ObjectiveResult objectiveResult;

    public Result(){}

    public Result(String title, long winsRatio, long wins) {
        this.title = title;
        this.winsRatio = winsRatio;
        this.wins = wins;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getWinsRatio() {
        return winsRatio;
    }

    public void setWinsRatio(long winsRatio) {
        this.winsRatio = winsRatio;
    }

    public long getWins() {
        return wins;
    }

    public void setWins(long wins) {
        this.wins = wins;
    }
}
