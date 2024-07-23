package com.example.DictionaryTest;

import com.example.Dictionary.Dictionary;
import com.example.Dictionary.DictionaryCommandLine;
import com.example.Dictionary.Word;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class DictionaryCommandLineTest {

    private DictionaryCommandLine dictionaryCommandLine;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        dictionaryCommandLine = new DictionaryCommandLine();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testImportData() {
        String input = "2\nhello\nxin chào\nworld\nthế giới\n";
        Dictionary dictionary = new Dictionary();
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        dictionaryCommandLine.importData(dictionary);

        Word word1 = dictionary.getWordByTarget("hello");
        Word word2 = dictionary.getWordByTarget("world");

        assertEquals(2, dictionary.getSize());

        System.out.println(word1.getWordExplain());
        System.out.println(word2.getWordExplain());

        assertNotNull(word1);
        assertNotNull(word2);
        assertTrue(word1.getWordExplain().contains("xin chào"));
        assertTrue(word2.getWordExplain().contains("thế giới"));
    }

    @Test
    public void testExportData() {
        ArrayList<String> meanings1 = new ArrayList<>();
        Dictionary dictionary = new Dictionary();
        meanings1.add("xin chào");
        dictionary.addWord(new Word("hello", meanings1));

        ArrayList<String> meanings2 = new ArrayList<>();
        meanings2.add("thế giới");
        dictionary.addWord(new Word("world", meanings2));

        dictionaryCommandLine.exportData(dictionary);

        String output = outContent.toString();
        assertTrue(output.contains("hello"));
        assertTrue(output.contains("xin chào"));
        assertTrue(output.contains("world"));
        assertTrue(output.contains("thế giới"));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(null);
        System.setIn(System.in);
        outContent.reset();
        dictionaryCommandLine = null;
    }
}
