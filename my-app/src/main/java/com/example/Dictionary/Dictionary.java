package com.example.Dictionary;

import java.util.ArrayList;

public class Dictionary {
    private ArrayList<Word> words = new ArrayList<Word>();

    public Dictionary() {
        words = new ArrayList<Word>();
    }

    /**
     * Add a word to dictionary.
     * If the word is already in the dictionary, add the explain to the word.
     * @param word      word need to add
     */
    public void addWord(Word word) {
        for (int i = 0; i < words.size(); i++) {
            Word w = words.get(i); 
            if (w.getWordTarget().equals(word.getWordTarget())) {
                for (String s : word.getWordExplain()) {
                    w.addWordExplain(s);
                }
                return;
            }
        }
        words.add(word);
    }

    /**
     * Remove a word from dictionary.
     * @param word      word need to remove
     */
    public void removeWord(Word word) {
        for (int i = 0; i < words.size(); i++) {
            Word w = words.get(i); 
            if (w.getWordTarget().equals(word.getWordTarget())) {
                words.remove(w);
                return;
            }
        }
    }

    /**
     * Get a word by index.
     * @param index     index of word
     * @return          word at index
     */
    public Word getWordByIndex(int index) {
        return words.get(index);
    }

    /**
     * Get a word by target.
     * @param target    target of word
     * @return          word with target
     */
    public Word getWordByTarget(String target) {
        for (int i = 0; i < words.size(); i++) {
            Word w = words.get(i);
            if (w.getWordTarget().equals(target)) {
                return w;
            }
        }
        return null;
    }

    public int getSize() {
        return words.size();
    }

    public ArrayList<Word> getAllWords() {
        return words;
    }    
}
