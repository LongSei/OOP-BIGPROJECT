package com.example.GoogleAPITest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.example.GoogleAPI.Text2Speech;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Text2SpeechTest {

    private static final String TEST_FILE_EN = "test_hello.mp3";
    private static final String TEST_FILE_VI = "test_xin_chao.mp3";

    @AfterEach
    public void cleanup() {
        File fileEn = new File(TEST_FILE_EN);
        if (fileEn.exists()) {
            fileEn.delete();
        }

        File fileVi = new File(TEST_FILE_VI);
        if (fileVi.exists()) {
            fileVi.delete();
        }
    }

    @Test
    public void testSaveAudioToFileEn() {
        String text = "Hello, how are you?";
        String language = "en";
        String filePath = TEST_FILE_EN;

        Text2Speech tool = new Text2Speech();
        tool.saveAudioToFile(text, language, filePath);

        File file = new File(filePath);
        assertTrue(file.exists(), "The file should exist after saving audio");
        assertTrue(file.length() > 0, "The file should contain data");
    }

    @Test
    public void testSaveAudioToFileVi() {
        String text = "Xin chào, bạn khỏe không?";
        String language = "vi";
        String filePath = TEST_FILE_VI;

        Text2Speech tool = new Text2Speech();
        tool.saveAudioToFile(text, language, filePath);

        File file = new File(filePath);
        assertTrue(file.exists(), "The file should exist after saving audio");
        assertTrue(file.length() > 0, "The file should contain data");
    }
}
