package com.ob.cgm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.IllegalFormatException;

public class SimpleAnsweringMachine {
    public static String QUIT_STRING="Q";
    public static String ASK_STRING="A";
    public static String STORE_STRING="S";
    private QuestionStorage storage=new QuestionStorage();


    public Question parseQuestion(String inputString,boolean askingQuestion) throws IllegalArgumentException {
        if (inputString==null){
            throw new IllegalArgumentException("Null input string");
        }
        int qPosition= inputString.indexOf('?');
        if(qPosition<0){
            throw new IllegalArgumentException("No ? sign");
        }
        if(qPosition>255){
            throw new IllegalArgumentException("Question longer then 255");
        }
        String questionText=inputString.substring(0,qPosition);
        String answersString=inputString.substring(qPosition+1).trim();
        if(askingQuestion){//Question asked
            if(answersString==null||"".equals(answersString)) {
                return new Question(questionText);
            }else{
                throw new IllegalArgumentException("Question has chars after '?'");
            }
        }else{ // Question stored
            if(answersString==null||answersString.length()<2||!answersString.startsWith("\"")||!answersString.endsWith("\"")){
                throw new IllegalArgumentException("Answer has wrong format");
            }
            String[] answerArray=answersString.substring(1,answersString.length()-1).split("\" +\"");
            for (int i = 0; i < answerArray.length; i++) {
                if(answerArray[i].indexOf('"')>=0){
                    throw new IllegalArgumentException("Answer has wrong format");
                }
                if(answerArray[i].length()>255){
                    throw new IllegalArgumentException("Answer longer then 255");
                }
            }
            return new Question(questionText,answerArray);
        }
    }
    public void answerQuestions(){
        BufferedReader systemIn=new BufferedReader(new InputStreamReader(System.in));
        try {
            while(true){
                System.out.print(">");
                String optionString = systemIn.readLine();
                if(QUIT_STRING.equalsIgnoreCase(optionString)){
                    break;
                }else if(!ASK_STRING.equalsIgnoreCase(optionString)&&!STORE_STRING.equalsIgnoreCase(optionString)){
                    System.out.println("Wrong Option");
                    continue;
                }
                System.out.print(">");
                String inputString = systemIn.readLine();
                try {
                    Question inputQuestion=parseQuestion(inputString,ASK_STRING.equalsIgnoreCase(optionString));
                    if(inputQuestion.hasAnswers()){
                        storage.put(inputQuestion);
                    }else{
                        storage.searchQuestion(inputQuestion).printAnswers();
                    }
                }catch (IllegalArgumentException ife){
                    System.out.println("Parse Error:"+ ife.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("IO Error:"+ e.getMessage());
        }
    }
    public static void main(String[] args) {
        System.out.println("'S' store a question <question>? \"<answer1>\" \"<answer2>\" \"<answerX>\"");
        System.out.println("'A' ask a question <question>?");
        System.out.println("'Q' quit");
        SimpleAnsweringMachine answeringMachine= new SimpleAnsweringMachine();
        answeringMachine.answerQuestions();
//        System.exit(0);
    }
}
