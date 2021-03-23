package com.mastervidya.mastervidya.model;

public class ReportModel
{
    String chapter_id;
    String chapter;
    String correct;
    String incorrect;
    String not_answered;
    String scored_percentage;
    String status1;
    String time;

    public String getChapter_id() {
        return chapter_id;
    }

    public void setChapter_id(String chapter_id) {
        this.chapter_id = chapter_id;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public String getIncorrect() {
        return incorrect;
    }

    public void setIncorrect(String incorrect) {
        this.incorrect = incorrect;
    }

    public String getNot_answered() {
        return not_answered;
    }

    public void setNot_answered(String not_answered) {
        this.not_answered = not_answered;
    }

    public String getScored_percentage() {
        return scored_percentage;
    }

    public void setScored_percentage(String scored_percentage) {
        this.scored_percentage = scored_percentage;
    }

    public String getStatus1() {
        return status1;
    }

    public void setStatus1(String status1) {
        this.status1 = status1;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
