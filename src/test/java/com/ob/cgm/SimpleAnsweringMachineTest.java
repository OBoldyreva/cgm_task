package com.ob.cgm;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;

import java.util.Arrays;
import java.util.Objects;

import static com.ob.cgm.SimpleAnsweringMachine.*;
import static org.junit.Assert.*;

public class SimpleAnsweringMachineTest {
    @Rule
    public final TextFromStandardInputStream systemInMock = TextFromStandardInputStream.emptyStandardInputStream();
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Test
    public void parseQuestion() {
        SimpleAnsweringMachine answeringMachine= new SimpleAnsweringMachine();
        QuestionTest.assertQuestionsEquals(new Question("Q1"),
                answeringMachine.parseQuestion("Q1?",true));
        QuestionTest.assertQuestionsEquals(new Question("What is Peters favorite food"),
                answeringMachine.parseQuestion("What is Peters favorite food?",true));
        QuestionTest.assertQuestionsEquals(new Question("When is Peters birthday"),
                answeringMachine.parseQuestion("When is Peters birthday?",true));
        QuestionTest.assertQuestionsEquals(new Question("Q1", new String[]{"AA1","AA2"}),
                answeringMachine.parseQuestion("Q1? \"AA1\" \"AA2\"",false));
        QuestionTest.assertQuestionsEquals(new Question("Q2", new String[]{"AB1"}),
                answeringMachine.parseQuestion("Q2? \"AB1\"",false));
        QuestionTest.assertQuestionsEquals(new Question("What is Peters favorite food", new String[]{"Pizza","Spaghetti","Ice cream"}),
                answeringMachine.parseQuestion("What is Peters favorite food? \"Pizza\" \"Spaghetti\" \"Ice cream\"",false));
        QuestionTest.assertQuestionsEquals(new Question("Q1", new String[]{"AA1","AA2 "}),
                answeringMachine.parseQuestion("Q1? \"AA1\" \"AA2 \"",false));
        QuestionTest.assertQuestionsEquals(new Question("Q1", new String[]{"AA1","AA2"}),
                answeringMachine.parseQuestion("Q1?   \"AA1\"   \"AA2\"   ",false));
        QuestionTest.assertQuestionsEquals(new Question("Q1", new String[]{"AA1","AA2"}),
                answeringMachine.parseQuestion("Q1?\"AA1\"   \"AA2\"   ",false));
        char[] longChars = new char[255];
        Arrays.fill(longChars, 'X');
        QuestionTest.assertQuestionsEquals(new Question(new String(longChars)),
                answeringMachine.parseQuestion(new String(longChars)+"?",true));
    }

    @Test
    public void parseQuestion_Asked_FormatError() {
        assertParseQuestionException(null,true,"Null input string");
        assertParseQuestionException("",true,"No ? sign");
        assertParseQuestionException("Q1",true,"No ? sign");
        assertParseQuestionException("Q1? xxx",true,"Question has chars after '?'");
        assertParseQuestionException("Q1? \"AA1\" \"AA2\"",true,"Question has chars after '?'");
        char[] longChars = new char[256];
        Arrays.fill(longChars, 'X');
        assertParseQuestionException(new String(longChars)+"?",true,"Question longer then 255");
    }
    @Test
    public void parseQuestion_Stored_FormatError() {
        assertParseQuestionException("Q1? xxx\"AA1\" \"AA2\"",false,"Answer has wrong format");
        assertParseQuestionException("Q1? xxx \"AA1\" \"AA2\"",false,"Answer has wrong format");
        assertParseQuestionException("Q1? \"AA1\"\"AA2 \"",false,"Answer has wrong format");
        assertParseQuestionException("Q1? \"AA1\" \"A\"A2 \"",false,"Answer has wrong format");
        char[] longChars = new char[256];
        Arrays.fill(longChars, 'X');
        assertParseQuestionException(new String(longChars)+"? \"AA1\" \"AA2\"",false,"Question longer then 255");
        assertParseQuestionException("Q1? \"" + new String(longChars)+"\"",false,"Answer longer then 255");
    }
    private void assertParseQuestionException(String inputString,boolean askingQuestion,String exceptionMessage){
        SimpleAnsweringMachine answeringMachine= new SimpleAnsweringMachine();
        try {
            answeringMachine.parseQuestion(inputString,askingQuestion);
            fail();
        }catch (IllegalArgumentException iae){
            if(!iae.getMessage().equals(exceptionMessage)){
                fail();
            }
        }
    }
    @Test
    public void answerQuestions_AskUnknown() {
        SimpleAnsweringMachine answeringMachine= new SimpleAnsweringMachine();
        systemInMock.provideLines(ASK_STRING,"Q1?",QUIT_STRING);
        answeringMachine.answerQuestions();
        assertEquals(">>the answer to life, universe and everything is 42\n>",systemOutRule.getLogWithNormalizedLineSeparator());
        systemOutRule.clearLog();
        systemInMock.provideLines(ASK_STRING,"What is Peters favorite food?",QUIT_STRING);
        answeringMachine.answerQuestions();
        assertEquals(">>the answer to life, universe and everything is 42\n>",systemOutRule.getLogWithNormalizedLineSeparator());
        systemOutRule.clearLog();
    }

    @Test
    public void answerQuestions_StoreAsk() {
        SimpleAnsweringMachine answeringMachine= new SimpleAnsweringMachine();
        systemInMock.provideLines(STORE_STRING,"Q1? \"AA1\" \"AA2\"",
                STORE_STRING,"What is Peters favorite food? \"Pizza\" \"Spaghetti\" \"Ice cream\"",
                ASK_STRING,"Q1?",QUIT_STRING);
        answeringMachine.answerQuestions();
        assertEquals(">>>>>>AA1\nAA2\n>",systemOutRule.getLogWithNormalizedLineSeparator());
        systemOutRule.clearLog();
        systemInMock.provideLines(STORE_STRING,"Q1? \"AA1\" \"AA2\"",
                STORE_STRING,"What is Peters favorite food? \"Pizza\" \"Spaghetti\" \"Ice cream\"",
                ASK_STRING,"What is Peters favorite food?",QUIT_STRING);
        answeringMachine.answerQuestions();
        assertEquals(">>>>>>Pizza\nSpaghetti\nIce cream\n>",systemOutRule.getLogWithNormalizedLineSeparator());
        systemOutRule.clearLog();
    }

    @Test
    public void answerQuestions_WrongFormat() {
        systemInMock.provideLines(STORE_STRING,"Q1? \"AA1\" \"AA2",QUIT_STRING);
        SimpleAnsweringMachine answeringMachine= new SimpleAnsweringMachine();
        answeringMachine.answerQuestions();
        assertEquals(">>Parse Error:Answer has wrong format\n>",systemOutRule.getLogWithNormalizedLineSeparator());
        systemOutRule.clearLog();
    }

    @Test
    public void answerQuestions_WrongOption() {
        systemInMock.provideLines("X",QUIT_STRING);
        SimpleAnsweringMachine answeringMachine= new SimpleAnsweringMachine();
        answeringMachine.answerQuestions();
        assertEquals(">Wrong Option\n>",systemOutRule.getLogWithNormalizedLineSeparator());
        systemOutRule.clearLog();
    }

    @Test
    public void main() {
        systemInMock.provideLines(QUIT_STRING);
        SimpleAnsweringMachine.main(new String[0]);
        assertEquals("S:<question>? \"<answer1>\" \"<answer2>\" \"<answerX>\"\nA:<question>?\nQ:quit\n>",systemOutRule.getLogWithNormalizedLineSeparator());
    }
}