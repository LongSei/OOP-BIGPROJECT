package com.example;

import com.example.Dictionary.Dictionary;
import com.example.Dictionary.Word;
import com.example.GoogleAPI.APITranslator;
import com.example.GoogleAPI.Text2Speech;
import com.example.Algorithm.Trie;
import com.example.Dictionary.DictionaryCSV;
import com.example.Dictionary.DictionaryDatabase;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import java.io.File;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.beans.value.ChangeListener;
import javafx.beans.binding.Bindings;
import java.util.List;

import org.apiguardian.api.API;

public class MainController {
    private Dictionary dictionary = new Dictionary();
    private Trie trie = new Trie();
    private ObservableList<Word> wordList = FXCollections.observableArrayList();
    private ObservableList<Word> meaningList = FXCollections.observableArrayList();

    private final double TEXT_FIELD_WIDTH = 0.33;
    @FXML
    private javafx.scene.layout.VBox rootVBox;

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
    private TableColumn<Word, Void> actionColumn;
    @FXML
    private TableColumn<Word, String> wordColumn1;
    @FXML
    private TableColumn<Word, String> wordColumn2;
    @FXML
    private TableColumn<Word, String> meaningColumn2;

    /**
     * Initializes the controller class.
     */
    @FXML
    private void initialize() {
        rootVBox.sceneProperty().addListener(new ChangeListener<Scene>() {
            @Override
            public void changed(ObservableValue<? extends Scene> observable, Scene oldScene, Scene newScene) {
                if (newScene != null) {
                    wordField.prefWidthProperty().bind(Bindings.createDoubleBinding(
                        () -> newScene.getWidth() * TEXT_FIELD_WIDTH,
                        newScene.widthProperty()
                    ));
                    
                    meaningField.prefWidthProperty().bind(Bindings.createDoubleBinding(
                        () -> newScene.getWidth() * TEXT_FIELD_WIDTH,
                        newScene.widthProperty()
                    ));
                }
            }
        });

        // Initialize TableView columns
        setupActionColumn(actionColumn);
        actionColumn.getStyleClass().add("table-cell-center");
        wordColumn1.getStyleClass().add("table-cell-center");
        wordColumn1.setCellValueFactory(new PropertyValueFactory<>("wordTarget"));
        dictionaryTable1.setItems(wordList);

        wordColumn2.getStyleClass().add("table-cell-center");
        wordColumn2.setCellValueFactory(new PropertyValueFactory<>("wordTarget"));
        meaningColumn2.getStyleClass().add("table-cell-center");
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

    /**
     * Handle the Add Word button.
     */
    @FXML
    private void handleAddWord() {
        try {
            String word = wordField.getText().trim().toLowerCase();
            String definition = meaningField.getText().trim().toLowerCase();

            if (word.isEmpty()) {
                // logArea.appendText("Error: Word or definition cannot be empty.\n");
                return;
            }

            if (definition.isEmpty()) {
                definition = APITranslator.translate("en", "vi", word);
            }

            Word newWord = new Word(word, List.of(definition));
            dictionary.addWord(newWord);
            trie.insert(trie.root, word, List.of(definition));
            refreshTable();
            logArea.appendText("Added: \n- Word: " + word + "\n- Definition: " + definition + "\n");

            wordField.clear();
            meaningField.clear();

        } catch (Exception e) {
            // logArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }

    /**
     * Handle the Find Word button.
     */
    @FXML
    private void handleFindWord() {
        String word = wordField.getText().trim();
        if (word.isEmpty()) {
            // logArea.appendText("Error: Word field is empty.\n");
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

    /**
     * Handle the Delete Word button.
     */
    @FXML
    private void handleDeleteWord() {
        String word = wordField.getText().trim();
        String definition = meaningField.getText().trim();
        if (word.isEmpty()) {
            // logArea.appendText("Error: Word field is empty.\n");
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
                // logArea.appendText("Error: " + e.getMessage() + "\n");
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
                        // logArea.appendText("Error: Definition not found for Word: " + word + "\n");
                    }
                } else {
                    logArea.appendText("Word not found: " + word + "\n");
                }
            } catch (Exception e) {
                // logArea.appendText("Error: " + e.getMessage() + "\n");
            }
        }
    }

    /**
     * Handle the Update Word button.
     */
    @FXML
    private void handleUpdateWord() {
        if (selectedWord.isEmpty()) {
            // logArea.appendText("Error: Word field is empty.\n");
            return;
        }
        if (selectedMeaning.isEmpty()) {
            // logArea.appendText("Error: Meaning field is empty.\n");
            return;
        }

        if (dictionary.getWordByTarget(selectedWord.trim()) == null) {
            // logArea.appendText("Error: Word not exists.\n");
            return;
        }

        if (dictionary.getWordByTarget(selectedWord).getWordExplain().contains(selectedMeaning) == false) {
            // logArea.appendText("Error: Meaning not exists.\n");
            return;
        }

        String newWord = wordField.getText().trim();
        String newMeaning = meaningField.getText().trim();

        if (newWord.isEmpty()) {
            // logArea.appendText("Error: New Word cannot be empty.\n");
            return;
        }

        if (newMeaning.isEmpty()) {
            // logArea.appendText("Error: New Meaning cannot be empty.\n");
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
            // logArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }

    /**
     * Handle the Open Dictionary CSV File button.
     */
    @FXML
    private void handleOpenDictionaryCSVFile() {
        try {
            DictionaryCSV dictionaryCSV = new DictionaryCSV();

            dictionary = new Dictionary();
            trie = new Trie();

            dictionaryCSV.importData(dictionary);
            trie.importDictionary(dictionary);
            refreshTable();
            logArea.appendText("Dictionary loaded from CSV file.\n");
        } catch (Exception e) {
            // logArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }

    /**
     * Handle the Save Dictionary CSV File button.
     */
    @FXML 
    private void handleSaveDictionaryCSVFile() {
        try {
            DictionaryCSV dictionaryCSV = new DictionaryCSV();
            dictionaryCSV.exportData(dictionary);
            logArea.appendText("Dictionary saved to CSV file.\n");
        } catch (Exception e) {
            // logArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }

    /**
     * Handle the Open Dictionary Database button.
     */
    @FXML
    private void handleOpenDictionaryDatabase() {
        try {
            DictionaryDatabase dictionaryDatabase = new DictionaryDatabase();

            dictionary = new Dictionary();
            trie = new Trie();

            dictionaryDatabase.importData(dictionary);
            trie.importDictionary(dictionary);
            refreshTable();
            logArea.appendText("Dictionary loaded from Database.\n");
        } catch (Exception e) {
            // logArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }

    /**
     * Handle the Save Dictionary Database button.
     */
    @FXML
    private void handleSaveDictionaryDatabase() {
        try {
            DictionaryDatabase dictionaryDatabase = new DictionaryDatabase();
            dictionaryDatabase.exportData(dictionary);
            logArea.appendText("Dictionary saved to Database.\n");
        } catch (Exception e) {
            // logArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }

    /**
     * Handle the Exit button.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }

    /**
     * Update the suggestions list based on the prefix.
     * @param prefix    the prefix
     */
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

    /**
     * Save the sound of the word to a file.
     * @param word  the word
     */
    private void saveSound(String word) {
        String soundPath = "src/main/resources/audios/" + word + ".mp3";
        Text2Speech text2speech = new Text2Speech();
        text2speech.saveAudioToFile(word, "en", soundPath);
        try {
            logArea.appendText("Sound have been saved to " + soundPath + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Play the sound of the word.
     * @param word  the word
     */
    private void playSound(String word) {
        File soundPath = new File("src/main/resources/audios/" + word + ".mp3");
        
        if (soundPath.exists()) {
            AudioClip sound = new AudioClip(soundPath.toURI().toString());
            sound.play();
            logArea.appendText("Playing sound for: " + word + "\n");
        } else {
            System.err.println("Sound file not found: " + soundPath.getAbsolutePath());
        }
    }
    

    /**
     * Setup the action column for the table.
     * @param actionColumn  the action column
     */
    private void setupActionColumn(TableColumn<Word, Void> actionColumn) {
        actionColumn.setCellFactory(param -> new TableCell<Word, Void>() {
            private final Button button = new Button();
            private final ImageView imageView;
    
            {
                File imageFile = new File("src/main/resources/icons/audio.png");
                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString());
                    imageView = new ImageView(image);
                    
                    imageView.setFitHeight(16);
                    imageView.setFitWidth(16);
                    button.setGraphic(imageView);
    
                    button.setOnAction(event -> {
                        Word word = getTableRow().getItem();
                        if (word != null) {
                            saveSound(word.getWordTarget());
                            playSound(word.getWordTarget());
                        }
                    });
                } else {
                    System.err.println("Image file not found: " + imageFile.getAbsolutePath());
                    imageView = new ImageView();
                }
            }
    
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null) {
                    setGraphic(null);
                } else {
                    setGraphic(button);
                }
            }
        });
    }
      
}
