package com.example;

import com.example.Dictionary.Dictionary;
import com.example.Dictionary.Word;
import com.example.Algorithm.Trie;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class MainController {
    private Dictionary dictionary = new Dictionary();
    private Trie trie = new Trie();
    private ObservableList<Word> wordList = FXCollections.observableArrayList();
    private ObservableList<Word> meaningList = FXCollections.observableArrayList();

    // Main Content
    @FXML
    private TextField wordField;
    @FXML
    private TextField meaningField;

    // Main UI
    @FXML
    private Button addWordButton;
    @FXML
    private Button findWordButton;
    @FXML
    private Button deleteWordButton;
    @FXML
    private Button updateWordButton;

    // Log Area
    @FXML
    private TextArea logArea;

    // Dictionary Table
    private String selectedWord = "";
    private String selectedMeaning = "";
    @FXML
    private TableView<Word> dictionaryTable1;
    @FXML
    private TableView<Word> dictionaryTable2;
    @FXML
    private TableColumn<Word, String> wordColumn1;
    @FXML
    private TableColumn<Word, String> wordColumn2;
    @FXML
    private TableColumn<Word, String> meaningColumn2;

    @FXML
    private void initialize() {
        // Initialize TableView columns
        wordColumn1.setCellValueFactory(new PropertyValueFactory<>("wordTarget"));
        dictionaryTable1.setItems(wordList);

        wordColumn2.setCellValueFactory(new PropertyValueFactory<>("wordTarget"));
        meaningColumn2.setCellValueFactory(cellData -> {
            List<String> meanings = cellData.getValue().getWordExplain();
            return new SimpleStringProperty(String.join(", ", meanings));
        });
        dictionaryTable2.setItems(meaningList);

        trie.importDictionary(dictionary);

        wordField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateSuggestions(newValue);
        });

        dictionaryTable1.setRowFactory(tv -> {
            TableRow<Word> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    Word rowData = row.getItem();
                    wordField.setText(rowData.getWordTarget());
                    meaningField.clear();
                    selectedWord = rowData.getWordTarget();
                    selectedMeaning = ""; 
                }
            });
            return row;
        });

        dictionaryTable2.setRowFactory(tv -> {
            TableRow<Word> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    Word rowData = row.getItem();
                    wordField.setText(rowData.getWordTarget());
                    meaningField.setText(rowData.getWordExplain().get(0));
                    selectedWord = rowData.getWordTarget();
                    selectedMeaning = rowData.getWordExplain().get(0);
                }
            });
            return row;
        });
    }

    @FXML
    private void handleAddWord() {
        try {
            String word = wordField.getText().trim();
            String definition = meaningField.getText().trim();

            if (word.isEmpty() || definition.isEmpty()) {
                logArea.appendText("Error: Word or definition cannot be empty.\n");
                return;
            }

            Word newWord = new Word(word, List.of(definition));
            dictionary.addWord(newWord);
            trie.insert(trie.root, word, List.of(definition));
            refreshTable();
            logArea.appendText("Added: \n- Word: " + word + "\n- Definition: " + definition + "\n");

            wordField.clear();
            meaningField.clear();

            for (Word w : dictionary.getAllWords()) {
                logArea.appendText(w.getWordTarget() + " " + w.getWordExplain() + "\n");
            }

        } catch (Exception e) {
            logArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }

    @FXML
    private void handleFindWord() {
        String word = wordField.getText().trim();
        if (word.isEmpty()) {
            logArea.appendText("Error: Word field is empty.\n");
            return;
        }

        if (trie.searchWord(word)) {
            Word wordFound = dictionary.getWordByTarget(word);
            meaningList.clear();
            for (String meaning : wordFound.getWordExplain()) {
                meaningList.add(new Word(word, List.of(meaning)));
            }
            dictionaryTable2.setItems(meaningList);
            logArea.appendText("Found Word: " + word + "\n");
        } else {
            logArea.appendText("Word not found: " + word + "\n");
            meaningList.clear(); // Clear the list if word is not found
            dictionaryTable2.setItems(meaningList);
        }
    }

    @FXML
    private void handleDeleteWord() {
        String word = wordField.getText().trim();
        String definition = meaningField.getText().trim();
        if (word.isEmpty()) {
            logArea.appendText("Error: Word field is empty.\n");
            return;
        }

        if (definition.isEmpty()) {
            // Remove all Words with the same wordTarget
            try {
                List<Word> wordsToRemove = dictionary.getAllWords().stream()
                        .filter(w -> w.getWordTarget().equals(word))
                        .toList();

                for (Word wordToRemove : wordsToRemove) {
                    dictionary.removeWord(wordToRemove);
                    trie.delete(trie.root, wordToRemove.getWordTarget());
                }

                meaningList.removeIf(w -> w.getWordTarget().equals(word));
                wordList.removeIf(w -> w.getWordTarget().equals(word));

                refreshTable();
                logArea.appendText("Deleted all entries for Word: " + word + "\n");
            } catch (Exception e) {
                logArea.appendText("Error: " + e.getMessage() + "\n");
            }

        } else {
            // Remove only the specific meaning of the word
            try {
                Word wordToUpdate = dictionary.getWordByTarget(word);
                if (wordToUpdate != null) {
                    logArea.appendText("Deleted Meaning: " + definition + " from Word: " + word + "\n");
                    boolean removed = wordToUpdate.removeWordExplain(definition);
                    if (removed) {
                        if (wordToUpdate.getWordExplain().isEmpty()) {
                            logArea.appendText("Deleted Word: " + word + "\n");
                            // If no meanings left, remove the word completely
                            dictionary.removeWord(wordToUpdate);
                            trie.delete(trie.root, wordToUpdate.getWordTarget());
                        } else {
                            // Update the word in the dictionary
                            dictionary.removeMeaning(wordToUpdate, definition);
                            trie.removeMeaning(trie.root, word, definition);
                        }

                        // Remove specific meaning from the meaningList and wordList
                        meaningList.removeIf(w -> w.getWordTarget().equals(word) && w.getWordExplain().contains(definition));
                        wordList.removeIf(w -> w.getWordTarget().equals(word) && w.getWordExplain().contains(definition));

                        refreshTable();
                        logArea.appendText("Deleted Meaning: " + definition + " from Word: " + word + "\n");
                    } else {
                        logArea.appendText("Error: Definition not found for Word: " + word + "\n");
                    }
                } else {
                    logArea.appendText("Word not found: " + word + "\n");
                }
            } catch (Exception e) {
                logArea.appendText("Error: " + e.getMessage() + "\n");
            }
        }
    }

    @FXML
    private void handleUpdateWord() {
        if (selectedWord.isEmpty()) {
            logArea.appendText("Error: Word field is empty.\n");
            return;
        }
        if (selectedMeaning.isEmpty()) {
            logArea.appendText("Error: Meaning field is empty.\n");
            return;
        }

        if (dictionary.getWordByTarget(selectedWord.trim()) == null) {
            logArea.appendText("Error: Word not exists.\n");
            return;
        }

        if (dictionary.getWordByTarget(selectedWord).getWordExplain().contains(selectedMeaning) == false) {
            logArea.appendText("Error: Meaning not exists.\n");
            return;
        }

        String newWord = wordField.getText().trim();
        String newMeaning = meaningField.getText().trim();

        if (newWord.isEmpty()) {
            logArea.appendText("Error: New Word cannot be empty.\n");
            return;
        }

        if (newMeaning.isEmpty()) {
            logArea.appendText("Error: New Meaning cannot be empty.\n");
            return;
        }

        try {
            Word wordToUpdate = dictionary.getWordByTarget(selectedWord);

            if (wordToUpdate != null) {
                // Update the meaning of the word
                dictionary.updateMeaning(newWord, selectedMeaning, newMeaning);
                trie.updateWordMeaning(newWord, selectedMeaning, newMeaning);
                logArea.appendText("Updated Meaning: " + selectedMeaning + " to " + newMeaning + "\n");

                // Update the word
                dictionary.updateWord(selectedWord, newWord);
                trie.updateWordTarget(selectedWord, newWord);
                logArea.appendText("Updated Word: " + selectedWord + " to " + newWord + "\n");

                refreshTable();
                wordField.clear();
                meaningField.clear();
            } else {
                logArea.appendText("Word not found: " + selectedWord + "\n");
            }
        } catch (Exception e) {
            logArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }

    private void updateSuggestions(String prefix) {
        List<Word> suggestions = trie.getSuggestions(prefix.trim());
        wordList.setAll(suggestions);
    }

    private void refreshTable() {
        wordList.clear();
        wordList.addAll(dictionary.getAllWords());
        dictionaryTable1.setItems(wordList);

        meaningList.clear();
        if (!selectedWord.isEmpty()) {
            Word word = dictionary.getWordByTarget(selectedWord);
            for (String meaning : word.getWordExplain()) {
                meaningList.add(new Word(selectedWord, List.of(meaning)));
            }
        }
        dictionaryTable2.setItems(meaningList);
    }
}
