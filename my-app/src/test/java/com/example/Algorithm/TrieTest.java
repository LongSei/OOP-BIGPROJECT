package com.example.Algorithm;

import com.example.Dictionary.Dictionary;
import com.example.Dictionary.Word;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TrieTest {

    private Trie trie;

    @BeforeEach
    public void setUp() {
        trie = new Trie();
    }

    @Test
    public void testInsertAndSearchWord() {
        List<String> meanings = new ArrayList<>();
        meanings.add("a greeting");
        trie.insert(trie.root, "hello", meanings);

        assertTrue(trie.searchWord("hello"));
        assertFalse(trie.searchWord("hell"));
        assertFalse(trie.searchWord("helloo"));
    }

    @Test
    public void testSearchMeaning() {
        List<String> meanings = new ArrayList<>();
        meanings.add("a greeting");
        trie.insert(trie.root, "hello", meanings);

        List<String> result = trie.searchMeaning("hello");
        assertEquals(meanings, result);
    }

    @Test
    public void testStartsWith() {
        List<String> meanings = new ArrayList<>();
        meanings.add("a greeting");
        trie.insert(trie.root, "hello", meanings);
        trie.insert(trie.root, "hell", meanings);

        assertNotNull(trie.startsWith("hel"));
        assertNull(trie.startsWith("heaven"));
    }

    @Test
    public void testImportDictionary() {
        Dictionary dictionary = new Dictionary(); 
        List<String> meanings = new ArrayList<>();
        meanings.add("a greeting");
        dictionary.addWord(new Word("hello", meanings));
        dictionary.addWord(new Word("hell", meanings));

        trie.importDictionary(dictionary);

        assertTrue(trie.searchWord("hello"));
        assertTrue(trie.searchWord("hell"));
        assertFalse(trie.searchWord("helloo"));
    }
}
