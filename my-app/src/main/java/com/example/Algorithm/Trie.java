package com.example.Algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.example.Dictionary.Dictionary;

public class Trie {
    private static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        List<String> meanings = new ArrayList<>();
    }

    public final TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    /**
     * Insert a word into the trie.
     * @param node                  the current node
     * @param remain_wordTarget     the remaining word target
     * @param wordExplain           the list of meanings
     */
    public void insert(TrieNode node, String remain_wordTarget, List<String> wordExplain) {
        if (remain_wordTarget.isEmpty()) {
            for (String meaning : wordExplain) {
                node.meanings.add(meaning);
            }
            return;
        }
        
        char currentCharacter = remain_wordTarget.charAt(0);
        TrieNode child = node.children.get(currentCharacter);
        
        if (child == null) {
            child = new TrieNode();
            node.children.put(currentCharacter, child);
        }
        
        insert(child, remain_wordTarget.substring(1), wordExplain);
    }    

    /**
     * Insert a word into the trie.
     * @param wordTarget        the word target
     * @return                  the list of meanings
     */
    public List<String> searchMeaning(String wordTarget) {
        TrieNode node = searchNode(wordTarget);
        return node == null ? new ArrayList<>() : node.meanings;
    }

    /**
     * Search a word in the trie.
     * @param wordTarget        the word target
     * @return                  true if the word is in the trie
     */
    public boolean searchWord(String wordTarget) {
        TrieNode node = searchNode(wordTarget);
        return node != null && !node.meanings.isEmpty();
    }

    /**
     * Search a prefix in the trie.
     * @param prefix            the prefix
     * @return                  true if there is any word in the trie that starts with the given prefix
     */
    public TrieNode startsWith(String prefix) {
        return searchNode(prefix);
    }

    /**
     * Search a node in the trie.
     * @param word              the word
     * @return                  the node that contains the word
     */
    private TrieNode searchNode(String word) {
        TrieNode currentNode = root;
        for (char c : word.toCharArray()) {
            currentNode = currentNode.children.get(c);
            if (currentNode == null) {
                return null;
            }
        }
        return currentNode;
    }

    /**
     * Import a dictionary into the trie.
     * @param dictionary        the dictionary
     */
    public void importDictionary(Dictionary dictionary) {
        for (int i = 0; i < dictionary.getSize(); i++) {
            String wordTarget = dictionary.getWordByIndex(i).getWordTarget();
            List<String> wordExplain = dictionary.getWordByIndex(i).getWordExplain();
            insert(root, wordTarget, wordExplain);
        }
    }
}