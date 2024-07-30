package com.example.DictionaryTest;

import com.example.Dictionary.Dictionary;
import com.example.Dictionary.DictionaryDatabase;
import com.example.Database.DataHelper;
import com.example.Dictionary.Word;

import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.Statement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class DictionaryDatabaseTest {
    private DataHelper dataHelper = new DataHelper();

    @BeforeEach
    public void setupDatabase() {
        dataHelper.createTable();
        dataHelper.insertData("Words", new ArrayList<String>(List.of("word_target", "word_explain")), new ArrayList<String>(List.of("hello", "xin chào")));
        dataHelper.insertData("Words", new ArrayList<String>(List.of("word_target", "word_explain")), new ArrayList<String>(List.of("world", "thế giới")));
        dataHelper.insertData("Words", new ArrayList<String>(List.of("word_target", "word_explain")), new ArrayList<String>(List.of("goodbye", "tạm biệt")));
        dataHelper.insertData("Words", new ArrayList<String>(List.of("word_target", "word_explain")), new ArrayList<String>(List.of("cat", "mèo")));
    }

    @AfterEach
    public void teardownDatabase() {
        try (Connection conn = dataHelper.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS Words;");
            stmt.execute("DROP TABLE IF EXISTS Translations;");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testImportData() {
        Dictionary dictionary = new Dictionary();
        DictionaryDatabase dictionaryDatabase = new DictionaryDatabase();

        dictionaryDatabase.importData(dictionary);

        Word word1 = dictionary.getWordByTarget("hello");
        Word word2 = dictionary.getWordByTarget("world");
        Word word3 = dictionary.getWordByTarget("goodbye");
        Word word4 = dictionary.getWordByTarget("cat");

        assertEquals(4, dictionary.getSize());

        assertNotNull(word1);
        assertNotNull(word2);
        assertNotNull(word3);
        assertNotNull(word4);
        
        assertTrue(word1.getWordExplain().contains("xin chào"));
        assertTrue(word2.getWordExplain().contains("thế giới"));
        assertTrue(word3.getWordExplain().contains("tạm biệt"));
        assertTrue(word4.getWordExplain().contains("mèo"));
    }

    @Test
    public void testExportData() {
        Dictionary dictionary = new Dictionary();
        DictionaryDatabase dictionaryDatabase = new DictionaryDatabase();

        dictionaryDatabase.importData(dictionary);

        dictionary.addWord(new Word("red", List.of("đỏ")));
        dictionary.addWord(new Word("blue", List.of("xanh")));

        dictionaryDatabase.exportData(dictionary);

        List<List<String>> data = dataHelper.queryData("Words", null);

        assertEquals(6, data.size());

        for (List<String> row : data) {
            String word_target = row.get(1);
            String word_explain = row.get(2);

            if (word_target.equals("hello")) {
                assertEquals("xin chào", word_explain);
            } else if (word_target.equals("world")) {
                assertEquals("thế giới", word_explain);
            } else if (word_target.equals("goodbye")) {
                assertEquals("tạm biệt", word_explain);
            } else if (word_target.equals("cat")) {
                assertEquals("mèo", word_explain);
            } else if (word_target.equals("red")) {
                assertEquals("đỏ", word_explain);
            } else if (word_target.equals("blue")) {
                assertEquals("xanh", word_explain);
            } else {
                fail("Unexpected word_target: " + word_target);
            }
        }
    }
}
