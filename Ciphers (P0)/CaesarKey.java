// Ishaan Srivastava 
// 10/8/2025 -> 10/17/25
// CSE 123 
// P0: Ciphers
// TA: Aidan Suen

import java.util.*;

// This class implements a Caesar cipher which adds a key to the beginning of the encoding
// and shifts the alphabet by that key.
// It extends the Substitution class.
public class CaesarKey extends Substitution {

    // Behavior: Constructs a CaesarKey Cipher by using the provided key 
    //           and shifting the alphabet by that key.
    // Exceptions: Throws an IllegalArgumentException if key is null, contains chars outside
    //             the encodable range(characters between MIN_CHAR 
    //             and MAX_CHAR (inclusive), default A->Z), or contains duplicate characters
    // Parameters: key: the key to use when constructing the encoding. Should not be null and
    //             all characters of key should be within the encodable range with no duplicates
    public CaesarKey(String key) throws IllegalArgumentException {       
        if (key == null) {
            throw new IllegalArgumentException("Null input!");
        }

        Set<Character> chars = new HashSet<>();

        // Checks for exceptions before constructing encoding
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (!isCharInRange(c)) {
                throw new IllegalArgumentException("Character outside of range in input!");
            }
            if (chars.contains(c)) {
                throw new IllegalArgumentException("Duplicates in input!");
            }
            chars.add(c);
        }

        String encode = "";
        for (int i = 0; i < key.length(); i++) {
            encode += key.charAt(i);
        }

        for (char c = (char) Cipher.MIN_CHAR; c <= (char) Cipher.MAX_CHAR; c++) {
            if (!encode.contains(c+"")) {
                encode += c;
            }
        }

        setEncoding(encode);
    }
}