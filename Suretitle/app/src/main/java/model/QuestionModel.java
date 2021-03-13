package model;

public class QuestionModel
{
    String question,question_id,question_credit,subquestion_count;

    public String getSubquestion_count() {
        return subquestion_count;
    }

    public void setSubquestion_count(String subquestion_count) {
        this.subquestion_count = subquestion_count;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getQuestion_credit() {
        return question_credit;
    }

    public void setQuestion_credit(String question_credit) {
        this.question_credit = question_credit;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
