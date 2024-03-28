package com.app_nccaa.nccaa.Model;

public class IndividualScores {

    private String name;
    private String score;
    private String maximumScore;
    private String percentageCorrect;
    private String nationalCorrect;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getMaximumScore() {
        return maximumScore;
    }

    public void setMaximumScore(String maximumScore) {
        this.maximumScore = maximumScore;
    }

    public String getPercentageCorrect() {
        return percentageCorrect;
    }

    public void setPercentageCorrect(String percentageCorrect) {
        this.percentageCorrect = percentageCorrect;
    }

    public String getNationalCorrect() {
        return nationalCorrect;
    }

    public void setNationalCorrect(String nationalCorrect) {
        this.nationalCorrect = nationalCorrect;
    }

    public String getNationalPercentageCorrect() {
        return nationalPercentageCorrect;
    }

    public void setNationalPercentageCorrect(String nationalPercentageCorrect) {
        this.nationalPercentageCorrect = nationalPercentageCorrect;
    }

    private String nationalPercentageCorrect;

}
