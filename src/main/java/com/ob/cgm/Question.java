package com.ob.cgm;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class Question {
    private String questionText;
    private String[] answers;

    public Question(String questionText) {
        this.questionText = questionText;
    }

    public Question(String questionText, String[] answers) {
        this.questionText = questionText;
        this.answers = answers;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getAnswers() {
        return answers;
    }

    public boolean hasAnswers(){
        return this.answers!=null&&this.answers.length>0;
    }
    public void printAnswers(){
        if(hasAnswers()){
            for (int i = 0; i < this.answers.length; i++) {
                System.out.println(answers[i]);
            }
        }else{
            System.out.println("the answer to life, universe and everything is 42");
        }
    }

//    public boolean equals(Object anObject){
//        if(anObject==null){
//            return false;
//        } else if(!(anObject instanceof Question)){
//            return false;
//        }else{
//            Question aQuestion= (Question) anObject;
//            if(!Objects.equals(getQuestionText(),aQuestion.getQuestionText())){
//                return false;
//            }
//            if(hasAnswers()&&!aQuestion.hasAnswers() || !hasAnswers()&&aQuestion.hasAnswers()){
//                return false;
//            }
//            if(hasAnswers()&&aQuestion.hasAnswers()) {
//                if(getAnswers().length!=aQuestion.getAnswers().length) {
//                    return false;
//                }
//                for (int i = 0; i < getAnswers().length; i++) {
//                    if(!Objects.equals(getAnswers()[i],aQuestion.getAnswers()[i])) {
//                        return false;
//                    }
//                 }
//            }
//        }
//        return true;
//    }
}
