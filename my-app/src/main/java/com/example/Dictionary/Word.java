package com.example.Dictionary;

import java.util.ArrayList;

public class Word {
    private String word_target;
    private ArrayList<String> word_explain;

    public Word() {
        word_target = "";
        word_explain = new ArrayList<>();
    }

    /**
     * Constructor Word with parameters.
     * @param word_target
     * @param word_explain
     */
    public Word(String word_target, ArrayList<String> word_explain) {
        this.word_target = word_target;
        this.word_explain = word_explain;
    }

    public String getWordTarget() {
        return word_target;
    }

    public void setWordTarget(String word_target) {
        this.word_target = word_target;
    }

    public ArrayList<String> getWordExplain() {
        return word_explain;
    }

    public void setWordExplain(ArrayList<String> word_explain) {
        this.word_explain = word_explain;
    }

    /**
     * Add a explain to word_explain.
     * @param word_explain  word_explain need to add
     */
    public void addWordExplain(String word_explain) {
        if (this.word_explain.isEmpty()) {
            this.word_explain.add(word_explain);
            return;
        }
        for (String s : this.word_explain) {
            if (s.equals(word_explain)) {
                return;
            }
        }
    }

    /**
     * Remove a explain from word_explain.
     * @param word_explain  word_explain need to remove
     */
    public void removeWordExplain(String word_explain) {
        if (this.word_explain.isEmpty()) {
            return;
        }
        for (String s : this.word_explain) {
            if (s.equals(word_explain)) {
                this.word_explain.remove(s);
                return;
            }
        }
    }

    @Override
    public String toString() {
        String result = "";
        result += word_target + "\n";
        for (String s : word_explain) {
            result += s + "\n";
        }
        return result;
    }
}