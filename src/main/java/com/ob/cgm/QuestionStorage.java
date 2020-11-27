package com.ob.cgm;

import java.util.HashMap;

public class QuestionStorage {
    private HashMap<String,Question> questionMap= new HashMap<>();
    public void put(Question question){
        questionMap.put(question.getQuestionText(),question);
    }
    public Question get(String questionText){
        return questionMap.get(questionText);
    }
    public Question searchQuestion(Question question){
        if(questionMap.containsKey(question.getQuestionText())){
            return questionMap.get(question.getQuestionText());
        }
        return question;
    }
    public boolean hasQuestion(String questionText){
        return questionMap.containsKey(questionText);
    }
}
