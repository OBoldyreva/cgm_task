package com.ob.cgm;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import java.util.Objects;

import static org.junit.Assert.*;

public class QuestionTest {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Test
    public void hasAnswers() {
        Question emptyQuestion = new Question("Q1");
        assertFalse(new Question("Q1").hasAnswers());
        assertFalse(new Question("Q1",null).hasAnswers());
        assertFalse(new Question("Q1",new String[0]).hasAnswers());
        assertTrue(new Question("Q2", new String[]{"AB1", "AB2", "AB3"}).hasAnswers());
    }

    @Test
    public void printAnswers() {
        new Question("Q1").printAnswers();
        assertEquals("the answer to life, universe and everything is 42\n",systemOutRule.getLogWithNormalizedLineSeparator());
        systemOutRule.clearLog();
        new Question("Q2", new String[]{"AB1", "AB2", "AB3"}).printAnswers();
        assertEquals("AB1\nAB2\nAB3\n",systemOutRule.getLogWithNormalizedLineSeparator());
        systemOutRule.clearLog();
        new Question("Q3", new String[]{"AC1"}).printAnswers();
        assertEquals("AC1\n",systemOutRule.getLogWithNormalizedLineSeparator());
        systemOutRule.clearLog();
        new Question("Q4", new String[0]).printAnswers();
        assertEquals("the answer to life, universe and everything is 42\n",systemOutRule.getLogWithNormalizedLineSeparator());
        systemOutRule.clearLog();
    }

    public static void assertQuestionsEquals(Question expected, Question actual){
        if(expected==null&&actual==null){
            return;
        } else if(expected==null||actual==null){
            fail();
        }else{
            if(!Objects.equals(expected.getQuestionText(),actual.getQuestionText())){
                fail();
            }
            if(expected.hasAnswers()&&!actual.hasAnswers() || !expected.hasAnswers()&&actual.hasAnswers()){
                fail();
            }
            if(expected.hasAnswers()&&actual.hasAnswers()) {
                assertEquals(expected.getAnswers().length, actual.getAnswers().length);
                for (int i = 0; i < expected.getAnswers().length; i++) {
                    assertEquals(expected.getAnswers()[i], actual.getAnswers()[i]);
                }
            }
        }
    }
}