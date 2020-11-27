package com.ob.cgm;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Objects;

import static org.junit.Assert.*;

public class QuestionStorageTest {

    QuestionStorage storage;

    @Before
    public void setUp() throws Exception {
        storage=new QuestionStorage();
        storage.put(new Question("Q1", new String[]{"AA1", "AA2"}));
        storage.put(new Question("Q2", new String[]{"AB1", "AB2", "AB3"}));
        storage.put(new Question("What is Peters favorite food", new String[]{"Pizza", "Spaghetti","Ice cream"}));
    }

    @After
    public void tearDown() throws Exception {
        storage=null;
    }
    @Test
    public void put() {
        assertFalse(storage.hasQuestion("Q3"));
        storage.put(new Question("Q3", new String[]{"AC1"}));
        assertTrue(storage.hasQuestion("Q3"));
    }

    @Test
    public void get() {
        assertNull(storage.get("Q3"));
        Question inputQuestion=new Question("Q3", new String[]{"AC1"});
        storage.put(inputQuestion);
        Question resultQuestion=storage.get("Q3");
        assertNotNull(resultQuestion);
        QuestionTest.assertQuestionsEquals(inputQuestion,resultQuestion);
    }

    @Test
    public void searchQuestionNotFound() {
        Question emptyQuestion=new Question("Q3");
        assertNotNull(storage.searchQuestion(emptyQuestion));
        assertEquals(emptyQuestion.getQuestionText(),storage.searchQuestion(emptyQuestion).getQuestionText());
        assertFalse(storage.searchQuestion(emptyQuestion).hasAnswers());
        QuestionTest.assertQuestionsEquals(emptyQuestion,storage.searchQuestion(emptyQuestion));
    }
    public void searchQuestionFound() {
        Question emptyQuestion=new Question("Q3");
        Question inputQuestion=new Question("Q3", new String[]{"AC1"});
        storage.put(inputQuestion);
        assertNotNull(storage.searchQuestion(emptyQuestion));
        assertEquals(emptyQuestion.getQuestionText(),storage.searchQuestion(emptyQuestion).getQuestionText());
        assertTrue(storage.searchQuestion(emptyQuestion).hasAnswers());
        QuestionTest.assertQuestionsEquals(inputQuestion,storage.searchQuestion(emptyQuestion));
    }
    public void searchQuestionReplace() {
        Question emptyQuestion=new Question("Q3");
        Question inputQuestion=new Question("Q3", new String[]{"AC1"});
        Question inputQuestion2=new Question("Q3", new String[]{"AC1","AC2","AC3"});
        storage.put(inputQuestion);
        QuestionTest.assertQuestionsEquals(inputQuestion,storage.searchQuestion(emptyQuestion));
        storage.put(inputQuestion2);
        QuestionTest.assertQuestionsEquals(inputQuestion2,storage.searchQuestion(emptyQuestion));
    }

    @Test
    public void hasQuestion() {
        assertFalse(storage.hasQuestion("Q3"));
        storage.put(new Question("Q3", new String[]{"AC1"}));
        assertTrue(storage.hasQuestion("Q3"));
    }

}