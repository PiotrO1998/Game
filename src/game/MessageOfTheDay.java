package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

/**
 * Performs network requests and String manipulation in order to present a completed MessageOfTheDay.
 *
 * @author Pedro Caetano
 * @version 1.1
 */
public class MessageOfTheDay {
    private static final String BASE_URL = "http://cswebcat.swan.ac.uk/";
    private static final String PUZZLE_URL = BASE_URL + "puzzle";
    private static final String MESSAGE_URL = BASE_URL + "message?solution=";
    private final Log log;
    private String message;
    private Boolean isValid;

    /**
     * Fetches and creates a MOTD Object.
     */
    public MessageOfTheDay() {
        log = new Log(this.getClass().getSimpleName());
        log.setImportance(1);
        try {
            String puzzle = makeRequest(PUZZLE_URL);
            //String decryptedPuzzle = solvePuzzle(Objects.requireNonNull(puzzle));
            //this.message = makeRequest(MESSAGE_URL + decryptedPuzzle);
            this.isValid = true;
        } catch (IOException e) {
            log.add("IOException during creation of MOTD", 1);
            this.isValid = false;
        }
    }

    /**
     * Gets the MOTD if it is valid.
     *
     * @return Message of The Day if valid
     */
    public String getMessage() {
        if (this.isValid) {
            return this.message;
        } else {
            return "Invalid message";
        }
    }

    /**
     * Overrides toString() to make outputs easier.
     *
     * @return MOTD's message
     */
    public String toString() {
        return this.message;
    }

    /**
     * Performs a left/right logical shift on alternating characters within a String.
     *
     * @param puzzle Scrambled Puzzle provided by Server
     * @return Unscrambled Puzzle that can be used to get the solved message
     */
    private String solvePuzzle(String puzzle) {
        int length = puzzle.length();
        char[] puzzleArray = puzzle.toCharArray();
        for (int i = 0; i < length; i++) {
            //mod 2 = even number, therefor splitting into every other char
            if (i % 2 == 0) {
                char c = puzzle.charAt(i);
                if (c == 'Z') {
                    puzzleArray[i] = 'A';
                } else {
                    puzzleArray[i] = (char) (c + 1);
                }
            } else {
                char c = puzzle.charAt(i);
                if (c == 'A') {
                    puzzleArray[i] = 'Z';
                } else {
                    puzzleArray[i] = (char) (c - 1);
                }
            }
        }
        String decryptedString = new String(puzzleArray);
        log.add("Decrypted Puzzle: " + decryptedString, 1);
        return decryptedString;
    }

    /**
     * Generic HTTP GET request method.
     *
     * @param address Web address of the request. Must include any queries
     * @return Result of the GET request
     * @throws IOException When a HTTP error occurs and the operation is therefor  unsuccessful
     */
    private String makeRequest(String address) throws IOException {
        log.add("GET Request URL: " + address);
        String requestString;
        URL url = new URL(address);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int httpResponseCode = con.getResponseCode();
        log.add("GET HTTP Response Code: " + httpResponseCode);

        if (httpResponseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            requestString = response.toString();
            // print result
            log.add("GET Result: " + requestString, 1);
            return requestString;
        } else {
            log.add("GET request unsuccessful", 1);
            return null;
        }
    }
}
