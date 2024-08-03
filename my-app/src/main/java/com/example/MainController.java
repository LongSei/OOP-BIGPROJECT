package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MainController {

    @FXML
    private TextField wordField;

    @FXML
    private TextArea outputArea;

    @FXML
    private Button addWordButton;
    @FXML
    private Button findWordButton;
    @FXML
    private Button deleteWordButton;
    @FXML
    private Button updateWordButton;

    @FXML
    private void handleAddWord() {
        String word = wordField.getText();
        outputArea.appendText("Added Word: " + word + "\n");
        wordField.clear();
    }

    @FXML
    private void handleFindWord() {
        String word = wordField.getText();
        outputArea.appendText("Found Word: " + word + "\n");
    }

    @FXML
    private void handleDeleteWord() {
        String word = wordField.getText();
        outputArea.appendText("Deleted Word: " + word + "\n");
    }

    @FXML
    private void handleUpdateWord() {
        String word = wordField.getText();
        outputArea.appendText("Updated Word: " + word + "\n");
    }
}
