package com.example.GoogleAPI;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class APITranslator {
    /**
     * Translates text from one language to another using a web API.
     *
     * @param langFrom The source language code (e.g., "en" for English).
     * @param langTo The target language code (e.g., "vi" for Vietnamese).
     * @param text The text to be translated.
     * @return The translated text as a String.
     * @throws IOException If an I/O error occurs.
     */
    public static String translate(String langFrom, String langTo, String text) throws IOException {
        try {
            String urlStr = "https://script.google.com/macros/s/AKfycbzhfVzJpR_QDIBAtpiHD8zcvCpt01TNhUcnkQS_yQTzVDPz97933inwllY96R79JW13/exec" +
                    "?q=" + URLEncoder.encode(text, StandardCharsets.UTF_8) +
                    "&target=" + langTo +
                    "&source=" + langFrom;
            URL url = new URL(urlStr);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int length; (length = con.getInputStream().read(buffer)) != -1; ) {
                result.write(buffer, 0, length);
            }

            return result.toString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Error in translating text");
        }
    }
}
