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
        if (word.isEmpty()) {
            logArea.appendText("Error: Word field is empty.\n");
            return;
        }

        Word wordToDelete = dictionary.getWordByTarget(word);
        if (wordToDelete != null) {
            dictionary.removeWord(wordToDelete);
            trie.delete(trie.root, word);
            refreshTable();
            logArea.appendText("Deleted Word: " + word + "\n");
        } else {
            logArea.appendText("Word not found: " + word + "\n");
        }
    }

    @FXML
    private void handleUpdateWord() {
        String word = wordField.getText().trim();
        String definition = meaningField.getText().trim();
        if (word.isEmpty() || definition.isEmpty()) {
            logArea.appendText("Error: Word or definition cannot be empty.\n");
            return;
        }

        Word wordToUpdate = dictionary.getWordByTarget(word);
        if (wordToUpdate != null) {
            wordToUpdate.addWordExplain(definition);
            refreshTable();
            logArea.appendText("Updated Word: " + word + "\n");
        } else {
            logArea.appendText("Word not found: " + word + "\n");
        }
    }

    private void updateSuggestions(String prefix) {
        List<Word> suggestions = trie.getSuggestions(prefix.trim());
        wordList.clear();
        wordList.addAll(suggestions);
        dictionaryTable1.setItems(wordList);
    }

    private void refreshTable() {
        wordList.clear();
        wordList.addAll(dictionary.getAllWords());
        dictionaryTable1.setItems(wordList);
    }
}
