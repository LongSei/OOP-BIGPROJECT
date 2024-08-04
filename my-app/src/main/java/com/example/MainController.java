package com.example;

import com.example.Dictionary.Dictionary;
import com.example.Dictionary.Word;
import com.example.Algorithm.Trie;

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
    private TableView<Word> dictionaryTable;
    @FXML
    private TableColumn<Word, String> wordColumn;
    @FXML 
    private TableColumn<Word, String> meaningColumn;

    private void refreshTable() {
        wordList.clear();
        wordList.addAll(dictionary.getAllWords());
    }    

    @FXML
    private void initialize() {
        wordColumn.setCellValueFactory(new PropertyValueFactory<>("wordTarget"));
        meaningColumn.setCellValueFactory(new PropertyValueFactory<>("wordExplain"));
        dictionaryTable.setItems(wordList);

        // Import existing dictionary into the Trie
        trie.importDictionary(dictionary);
    }

    @FXML
    private void handleAddWord() {
        try {
            String word = wordField.getText().trim();
            String definition = meaningField.getText().trim();
    
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
        if (trie.searchWord(word)) {
            List<String> meanings = trie.searchMeaning(word);
            logArea.appendText("Found Word: " + word + "\n");
            for (String meaning : meanings) {
                logArea.appendText("Definition: " + meaning + "\n");
            }
        } else {
            logArea.appendText("Word not found: " + word + "\n");
        }
    }

    @FXML
    private void handleDeleteWord() {
        String word = wordField.getText().trim();
        Word wordToDelete = dictionary.getWordByTarget(word);
        if (wordToDelete != null) {
            dictionary.removeWord(wordToDelete);
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
        Word wordToUpdate = dictionary.getWordByTarget(word);
        if (wordToUpdate != null) {
            wordToUpdate.addWordExplain(definition);
            refreshTable();
            logArea.appendText("Updated Word: " + word + "\n");
        } else {
            logArea.appendText("Word not found: " + word + "\n");
        }
    }
}
