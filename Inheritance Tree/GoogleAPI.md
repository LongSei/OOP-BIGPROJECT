# Inheritance Tree

## APITranslator Class
- **`APITranslator`**
  - Provides a method to translate text using a web API.
  - **Methods:**
    - `translate(String langFrom, String langTo, String text)`

## Text2Speech Class
- **`Text2Speech`**
  - Provides methods to convert text to speech using a web API and handle audio playback.
  - **Methods:**
    - `playSoundGoogleTranslate(String text, String languageTarget)`
    - `saveAudioToFile(String text, String language, String filePath)`
