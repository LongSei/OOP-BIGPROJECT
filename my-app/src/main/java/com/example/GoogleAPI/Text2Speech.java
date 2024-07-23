package com.example.GoogleAPI;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Text2Speech {
    /**
     * Convert English input {@code text} to voice and play it with Google Translator TTS API
     *
     * @param text The text to be converted to voice in English
     * @param languageTarget The target language for the TTS conversion (e.g., "en" for English, "vi" for Vietnamese)
     * @throws RuntimeException If an error occurs during the conversion or playing the audio
     */
    public static void playSoundGoogleTranslate(String text, String languageTarget) throws RuntimeException {
        try {
            String api =
                    "https://translate.google.com/translate_tts?ie=UTF-8&tl="
                            + languageTarget
                            + "&client=tw-ob&q="
                            + URLEncoder.encode(text, StandardCharsets.UTF_8);
            URL url = new URL(api);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            try (InputStream audio = con.getInputStream()) {
                // Use the Java Sound API to play the audio
                javax.sound.sampled.AudioInputStream ais = javax.sound.sampled.AudioSystem.getAudioInputStream(audio);
                javax.sound.sampled.Clip clip = javax.sound.sampled.AudioSystem.getClip();
                clip.open(ais);
                clip.start();

                // Wait for the clip to finish playing
                while (!clip.isRunning()) {
                    Thread.sleep(10);
                }
                while (clip.isRunning()) {
                    Thread.sleep(10);
                }

                clip.close();
            }

            con.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error in playing audio");
        }
    }

    /**
     * Save audio from Google Translate TTS API to a file
     *
     * @param text The text to be converted to voice
     * @param language The target language for the TTS conversion (e.g., "en" for English, "vi" for Vietnamese)
     * @param filePath The path to the file where the audio will be saved
     */
    public static void saveAudioToFile(String text, String language, String filePath) {
        try {
            String api =
                    "https://translate.google.com/translate_tts?ie=UTF-8&tl="
                            + language
                            + "&client=tw-ob&q="
                            + URLEncoder.encode(text, StandardCharsets.UTF_8);
            URL url = new URL(api);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            try (InputStream audio = con.getInputStream();
                 FileOutputStream fos = new FileOutputStream(filePath)) {

                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = audio.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }

            con.disconnect();
            System.out.println("Audio saved to " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error in saving audio");
        }
    }
}
