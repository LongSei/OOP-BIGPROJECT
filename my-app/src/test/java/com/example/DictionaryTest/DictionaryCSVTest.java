package com.example.DictionaryTest;

import com.example.Dictionary.Dictionary;
import com.example.Dictionary.DictionaryCSV;
import com.example.Dictionary.Word;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DictionaryCSVTest {
    private DictionaryCSV dictionaryCSV;
    private File testFile = new File("src/main/resources/data.csv");

    @BeforeEach
    void setUp() {
        dictionaryCSV = new DictionaryCSV(); // Assuming you have a setter for file path in DictionaryCSV
    }

    @AfterEach
    void tearDown() {
        // Clean up test file
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    void testImportData() throws IOException {
        // Setup test data
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("word1,meaning1\n");
            writer.write("word2,meaning2\n");
        }

        Dictionary dictionary = new Dictionary();
        dictionaryCSV.importData(dictionary);

        assertEquals(2, dictionary.getAllWords().size(), "Dictionary should have 2 words after import");
        Word word1 = dictionary.getAllWords().get(0);
        assertEquals("word1", word1.getWordTarget(), "First word should be 'word1'");
        assertEquals(List.of("meaning1"), word1.getWordExplain(), "First word explanation should be 'meaning1'");

        Word word2 = dictionary.getAllWords().get(1);
        assertEquals("word2", word2.getWordTarget(), "Second word should be 'word2'");
        assertEquals(List.of("meaning2"), word2.getWordExplain(), "Second word explanation should be 'meaning2'");
    }

    @Test
    void testExportData() {
        Dictionary dictionary = new Dictionary();
        dictionary.addWord(new Word("word1", List.of("meaning1", "meaning3")));
        dictionary.addWord(new Word("word2", List.of("meaning2")));

        dictionaryCSV.exportData(dictionary);

        // Read the file and check content
        try (BufferedReader reader = new BufferedReader(new FileReader(testFile))) {
            String line = reader.readLine();
            assertNotNull(line, "File should not be empty");
            assertEquals("word1,meaning1", line, "First line should match 'word1,meaning1'");

            line = reader.readLine();
            assertNotNull(line, "File should have a second line");
            assertEquals("word1,meaning3", line, "Second line should match 'word1,meaning3'");

            line = reader.readLine();
            assertNotNull(line, "File should have a third line");
            assertEquals("word2,meaning2", line, "Second line should match 'word2,meaning2'");
        } catch (IOException e) {
            fail("IOException should not occur while reading the file");
        }
    }
}
