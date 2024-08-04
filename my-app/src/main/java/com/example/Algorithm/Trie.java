package com.example.Algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.example.Dictionary.Dictionary;
import com.example.Dictionary.Word;

public class Trie {
    private static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        List<String> meanings = new ArrayList<>();
    }

    public TrieNode root = new TrieNode();

    public Trie() {
        this.root = new TrieNode();
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
                if (node.meanings.contains(meaning)) {
                    continue;
                }
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
     * Delete a word from the trie.
     * @param node                  the current node
     * @param remain_wordTarget     the remaining word target
     * @return                      true if the node should be deleted
     */
    public boolean delete(TrieNode node, String remain_wordTarget) {
        if (remain_wordTarget.isEmpty()) {
            node.meanings.clear();
            if (node.children.isEmpty()) {
                return true;
            }
            return false;
        }
        
        char currentCharacter = remain_wordTarget.charAt(0);
        TrieNode child = node.children.get(currentCharacter);
        
        if (child == null) {
            return true; 
        }
        
        // Recursively delete from child
        boolean shouldDeleteChild = delete(child, remain_wordTarget.substring(1));
        
        if (shouldDeleteChild) {
            node.children.remove(currentCharacter);
            return node.children.isEmpty() && node.meanings.isEmpty();
        }
        
        return false;
    }

    /**
     * Insert a word into the trie.
     * @param wordTarget        the word target
     * @return                  the list of meanings
     */
    public List<String> searchMeaning(String wordTarget) {
        TrieNode node = searchNode(wordTarget);
        return node == null ? new ArrayList<>() : new ArrayList<>(node.meanings);
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

    /**
     * Get all words that start with the given prefix.
     * @param prefix            the prefix
     * @return                  a list of suggestions
     */
    public List<Word> getSuggestions(String prefix) {
        TrieNode node = startsWith(prefix);
        List<Word> suggestions = new ArrayList<>();
        if (node != null) {
            collectWords(node, prefix, suggestions);
        }
        return suggestions;
    }

    /**
     * Collect all words in the subtree of the given node.
     * @param node              the starting node
     * @param prefix            the prefix leading to this node
     * @param suggestions       the list to add suggestions
     */
    private void collectWords(TrieNode node, String prefix, List<Word> suggestions) {
        if (!node.meanings.isEmpty()) {
            suggestions.add(new Word(prefix, node.meanings));
        }
        for (char c : node.children.keySet()) {
            collectWords(node.children.get(c), prefix + c, suggestions);
        }
    }    

    /**
     * Remove a meaning from the trie.
     * @param node                  the current node
     * @param remain_wordTarget     the remaining word target
     * @param meaning               the meaning to remove
     */
    public void removeMeaning(TrieNode node, String remain_wordTarget, String meaning) {
        if (remain_wordTarget.isEmpty()) {
            node.meanings.remove(meaning);
            if (node.meanings.isEmpty() && node.children.isEmpty()) {
                node.children.clear(); 
            }
            return;
        }
        
        char currentCharacter = remain_wordTarget.charAt(0);
        TrieNode child = node.children.get(currentCharacter);
        
        if (child == null) {
            return;
        }
        
        removeMeaning(child, remain_wordTarget.substring(1), meaning);
        
        if (child.meanings.isEmpty() && child.children.isEmpty()) {
            node.children.remove(currentCharacter);
        }
    }
    

    /**
     * Update a word target in the trie.
     * @param oldWordTarget     the old word target
     * @param newWordTarget     the new word target
     */
    public void updateWordTarget(String oldWordTarget, String newWordTarget) {
        List<String> meanings = searchMeaning(oldWordTarget);
        boolean wasDeleted = delete(this.root, oldWordTarget);
        if (wasDeleted) {
            insert(this.root, newWordTarget, meanings);
        }
    }
    

    /**
     * Update a word meaning in the trie.
     * @param wordTarget        the word target
     * @param oldMeaning        the old meaning
     * @param newMeaning        the new meaning
     */
    public void updateWordMeaning(String wordTarget, String oldMeaning, String newMeaning) {
        TrieNode node = searchNode(wordTarget);
        if (node != null) {
            if (node.meanings.contains(oldMeaning)) {
                node.meanings.remove(oldMeaning);
                node.meanings.add(newMeaning);
            }
        }
    }
    
    /**
     * Print all words in the trie.
     */
    public void printAllWords() {
        printAllWords(root, "");
    }
    
    /**
     * Print all words in the trie.
     * @param node              the current node
     * @param prefix            the prefix
     */
    private void printAllWords(TrieNode node, String prefix) {
        // Print the current node's word and meanings if it has any meanings
        if (!node.meanings.isEmpty()) {
            System.out.println("Word: " + prefix + " | Meanings: " + node.meanings);
        }
    
        if (node.children.isEmpty() || node.children == null) {
            return;
        }
        // Print the children nodes
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            char character = entry.getKey();
            TrieNode childNode = entry.getValue();
            System.out.println("Character: " + character + " | Prefix: " + prefix);
            printAllWords(childNode, prefix + character);
        }
    }
    
}