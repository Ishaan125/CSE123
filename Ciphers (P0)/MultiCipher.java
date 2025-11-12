// Ishaan Srivastava 
// 10/8/2025 -> 10/17/25
// CSE 123 
// P0: Ciphers
// TA: Aidan Suen

import java.util.*;

// This class implements a MultiCipher that applies ciphers in sequence for encryption/decryption.
// It extends the Cipher class.
public class MultiCipher extends Cipher {
    private List<Cipher> ciphers;

    // Behavior: Constructs a MultiCipher with a list of ciphers.
    // Exceptions: Throws an IllegalArgumentException if the list is null.
    // Parameters: ciphers: the list of ciphers to use in sequence. Should not be null
    public MultiCipher(List<Cipher> ciphers) throws IllegalArgumentException {
        if (ciphers == null) {
            throw new IllegalArgumentException("Null input!");
        }
        this.ciphers = ciphers;
    }

    // Behavior: Encrypts the input string by applying each cipher in sequence.
    // Exceptions: Throws an IllegalArgumentException if the input is null or contains characters
    //             outside the encodable range(characters between 
    //             MIN_CHAR and MAX_CHAR (inclusive), default A->Z).
    // Parameters: input: the string to encrypt. Should not be null, should be within the
    //             encodable range.
    // Returns: the encrypted string after applying all ciphers
    @Override
    public String encrypt(String input) throws IllegalArgumentException {
        if (input == null) {
            throw new IllegalArgumentException("Null input!");
        }
        for (Cipher enc : ciphers) {
            input = enc.encrypt(input);
        }
        return input;
    }

    // Behavior: Decrypts the input string by applying each cipher in reverse sequence.
    // Exceptions: Throws an IllegalArgumentException if the input is null or contains characters
    //             outside the encodable range(characters between 
    //             MIN_CHAR and MAX_CHAR (inclusive), default A->Z).
    // Parameters: input: the string to decrypt. Should not be null, should be within the
    //             encodable range.
    // Returns: the decrypted string after applying all ciphers in reverse order
    @Override
    public String decrypt(String input) throws IllegalArgumentException {
        if (input == null) {
            throw new IllegalArgumentException("Null input!");
        }
        for (int i = ciphers.size() - 1; i >= 0; i--) {
            input = ciphers.get(i).decrypt(input);
        }
        return input;
    }
}