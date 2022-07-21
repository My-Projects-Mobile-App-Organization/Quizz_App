package com.example.quizapps.Models;

public class QuestionModel {
    private String qID;
    private String question;
    private String OptionA;
    private String OptionB;
    private String OptionC;
    private String OptionD;
    private int correctAns;
    private int selectedAns;
    private int status;
    private boolean isBookmark;

    public int getSelectedAns() {
        return selectedAns;
    }

    public void setSelectedAns(int selectedAns) {
        this.selectedAns = selectedAns;
    }


    public String getqID() {
        return qID;
    }

    public void setqID(String qID) {
        this.qID = qID;
    }

    public QuestionModel(String qID, String question, String optionA, String optionB, String optionC, String optionD, int correctAns, int selectedAns, int status, boolean isBookmark) {
        this.qID=qID;
        this.question = question;
        OptionA = optionA;
        OptionB = optionB;
        OptionC = optionC;
        OptionD = optionD;
        this.correctAns = correctAns;
        this.selectedAns = selectedAns;
        this.status = status;
        this.isBookmark=isBookmark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptionA() {
        return OptionA;
    }

    public void setOptionA(String optionA) {
        OptionA = optionA;
    }

    public String getOptionB() {
        return OptionB;
    }

    public void setOptionB(String optionB) {
        OptionB = optionB;
    }

    public String getOptionC() {
        return OptionC;
    }

    public void setOptionC(String optionC) {
        OptionC = optionC;
    }

    public String getOptionD() {
        return OptionD;
    }

    public void setOptionD(String optionD) {
        OptionD = optionD;
    }

    public int getCorrectAns() {
        return correctAns;
    }

    public void setCorrectAns(int correctAns) {
        this.correctAns = correctAns;
    }

    public boolean isBookmark() {
        return isBookmark;
    }

    public void setBookmark(boolean bookmark) {
        isBookmark = bookmark;
    }
}
