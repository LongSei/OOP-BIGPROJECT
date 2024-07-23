package com.example.GoogleAPITest;

import org.junit.jupiter.api.Test;

import com.example.GoogleAPI.APITranslator;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class APITranslatorTest {

    @Test
    public void testTranslateEnToVi() throws IOException {
        String text = "System";
        String translatedText = APITranslator.translate("en", "vi", text);
        assertEquals("Hệ thống", translatedText);
    }

    @Test
    public void testTranslateViToEn() throws IOException {
        String text = "Hệ thống";
        String translatedText = APITranslator.translate("vi", "en", text);
        assertEquals("System", translatedText);
    }
}
