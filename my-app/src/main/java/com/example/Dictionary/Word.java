package com.example.Dictionary;

import java.util.ArrayList;
import java.util.List;

public class Word {
    private String wordTarget;
    private List<String> wordExplain = new ArrayList<>();

    public Word() {
        this.wordTarget = "";
        this.wordExplain = new ArrayList<>();
    }

    /**
     * Constructor with parameters.
     * @param wordTarget the target word
     * @param wordExplain the explanations for the word
     */
    public Word(String wordTarget, List<String> wordExplain) {
        this.wordTarget = wordTarget;
        this.wordExplain = new ArrayList<>(wordExplain); 
    }

    public String getWordTarget() {
        return wordTarget;
    }

    public void setWordTarget(String wordTarget) {
        this.wordTarget = wordTarget;
    }

    public List<String> getWordExplain() {
        return this.wordExplain;
    }

    public void setWordExplain(List<String> wordExplain) {
        this.wordExplain = new ArrayList<>(wordExplain); 
    }

    /**
     * Adds an explanation to the list if it's not already present.
     * @param explanation the explanation to add
     */
    public void addWordExplain(String explanation) {
        if (explanation == null || explanation.trim().isEmpty()) {
            throw new IllegalArgumentException("Explanation cannot be null or empty");
        }
        if (!wordExplain.contains(explanation)) {
            wordExplain.add(explanation);
        }
    }

    /**
     * Removes an explanation from the list.
     * @param explanation the explanation to remove
     * @return true if the explanation was removed, false otherwise
     */
    public boolean removeWordExplain(String explanation) {
        if (explanation == null) {
            throw new IllegalArgumentException("Explanation cannot be null");
        }
        return wordExplain.remove(explanation);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(wordTarget + "\n");
        for (String s : wordExplain) {
            result.append(s).append("\n");
        }
        return result.toString();
    }
}
