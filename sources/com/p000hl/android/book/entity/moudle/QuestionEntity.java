package com.p000hl.android.book.entity.moudle;

import java.util.ArrayList;

/* renamed from: com.hl.android.book.entity.moudle.QuestionEntity */
public class QuestionEntity {
    public int chooseIndex = -1;
    public String imgSource = "";
    private ArrayList<OptionEntity> optionList = new ArrayList<>();
    public String questionType = "";
    private ArrayList<Integer> rightAnswerList = new ArrayList<>();
    public int score = 0;
    public String soundSource = "";
    public String titleResource = "";

    public ArrayList<OptionEntity> getOptionList() {
        return this.optionList;
    }

    public ArrayList<Integer> getRightAnswerList() {
        return this.rightAnswerList;
    }
}
