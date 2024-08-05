package com.example.Algorithm;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.Dictionary.Word;

import java.util.List;

public class TrieTest {
    private Trie trie;

    @BeforeEach
    public void setUp() {
        trie = new Trie();
    }

    @Test
    public void testInsertAndSearchWord() {
        trie.insert(trie.root, "hello", List.of("greeting"));
        assertTrue(trie.searchWord("hello"));
        assertFalse(trie.searchWord("hell"));
    }

    @Test
    public void testSearchMeaning() {
        trie.insert(trie.root, "world", List.of("earth", "globe"));
        List<String> meanings = trie.searchMeaning("world");
        assertTrue(meanings.contains("earth"));
        assertTrue(meanings.contains("globe"));
        assertFalse(meanings.contains("universe"));
    }

    @Test
    public void testDeleteWord() {
        trie.insert(trie.root, "test", List.of("experiment"));
        trie.delete(trie.root, "test");
        assertFalse(trie.searchWord("test"));
    }

    @Test
    public void testUpdateWordTarget() {
        Trie trie = new Trie();
        trie.insert(trie.root, "old", List.of("previous"));
        // Update the word from "old" to "new"
        trie.updateWordTarget("old", "new");
    
        // Check that the old word is not present
        assertFalse(trie.searchWord("old"));
    
        // Check that the new word is present
        assertTrue(trie.searchWord("new"));
        assertEquals(List.of("previous"), trie.searchMeaning("new"));
    }
    
    

    @Test
    public void testUpdateWordMeaning() {
        trie.insert(trie.root, "update", List.of("change"));
        trie.updateWordMeaning("update", "change", "modify");
        List<String> meanings = trie.searchMeaning("update");
        assertTrue(meanings.contains("modify"));
        assertFalse(meanings.contains("change"));
    }

    @Test
    public void testGetSuggestions() {
        trie.insert(trie.root, "apple", List.of("fruit"));
        trie.insert(trie.root, "appreciate", List.of("value"));
        List<Word> suggestions = trie.getSuggestions("app");
        assertEquals(2, suggestions.size());
        assertTrue(suggestions.stream().anyMatch(word -> word.getWordTarget().equals("apple")));
        assertTrue(suggestions.stream().anyMatch(word -> word.getWordTarget().equals("appreciate")));
    }

    @Test
    public void testRemoveMeaning() {
        trie.insert(trie.root, "word", List.of("definition1", "definition2"));
        trie.removeMeaning(trie.root, "word", "definition1");
        List<String> meanings = trie.searchMeaning("word");
        assertTrue(meanings.contains("definition2"));
        assertFalse(meanings.contains("definition1"));
    }
}
