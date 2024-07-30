package com.example.Dictionary;

import java.util.ArrayList;
import java.util.List;

public class Dictionary {
    private List<Word> words = new ArrayList<Word>();

    public Dictionary() {
        this.words = new ArrayList<Word>();
    }

    /**
     * Add a word to dictionary.
     * If the word is already in the dictionary, add the explain to the word.
     * @param word      word need to add
     */
    public void addWord(Word word) {
        for (int i = 0; i < this.words.size(); i++) {
            Word w = this.words.get(i); 
            if (w.getWordTarget().equals(word.getWordTarget())) {
                for (String s : word.getWordExplain()) {
                    w.addWordExplain(s);
                }
                return;
            }
        }
        this.words.add(word);
    }

    /**
     * Remove a word from dictionary.
     * @param word      word need to remove
     */
    public void removeWord(Word word) {
        for (int i = 0; i < this.words.size(); i++) {
            Word w = this.words.get(i); 
            if (w.getWordTarget().equals(word.getWordTarget())) {
                this.words.remove(w);
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
        return this.words.get(index);
    }

    /**
     * Get a word by target.
     * @param target    target of word
     * @return          word with target
     */
    public Word getWordByTarget(String target) {
        for (int i = 0; i < this.words.size(); i++) {
            Word w = this.words.get(i);
            if (w.getWordTarget().equals(target)) {
                return w;
            }
        }
        return null;
    }

    public int getSize() {
        return this.words.size();
    }

    public List<Word> getAllWords() {
        return this.words;
    }    
}
