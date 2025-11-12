// Ishaan Srivastava 
// 10/8/2025 -> 10/17/25
// CSE 123 
// P0: Ciphers
// TA: Aidan Suen

import java.util.*;

// This class implements a substitution cipher which replaces each character in the input
// with a character from an encoding string.
// There's methods for encryption, decryption, and constructors for initializing the encoding.
public class Substitution extends Cipher {
    private String encoding;

    // Behavior: Constructs an empty Substitution Cipher
    public Substitution() {
        this.encoding = null;
    }

    // Behavior: Constructs a Substitution Cipher using the provided encoding
    // Exceptions: Throws an IllegalArgumentException if encoding is null, the length isn't equal
    //             to TOTAL_CHARS(the total number of characters in the encodable range),
    //             contains chars outside the encodable range(characters
    //             between MIN_CHAR and MAX_CHAR (inclusive), default A->Z),
    //             or contains duplicate characters
    // Parameters: encoding: the encoding to use when encrypting. Should not be null, should be
    //             within the encodable range, should be of length TOTAL_CHARS
    public Substitution(String encoding) throws IllegalArgumentException {
        setEncoding(encoding);
    }

    // Behavior: Sets the encoding for a Substitution Cipher using the provided encoding
    // Exceptions: Throws an IllegalArgumentException if encoding is null, the length isn't equal
    //             to TOTAL_CHARS(the total number of characters in the encodable range),
    //             contains chars outside the encodable range(characters
    //             between MIN_CHAR and MAX_CHAR (inclusive), default A->Z),
    //             or contains duplicate characters
    // Parameters: encoding: the encoding to use when encrypting. Should not be null, should be
    //             within the encodable range, should be of length TOTAL_CHARS
    public void setEncoding(String encoding) throws IllegalArgumentException {
        if (encoding == null) {
            throw new IllegalArgumentException("Null input!");
        }
        if (encoding.length() != Cipher.TOTAL_CHARS) {
            throw new IllegalArgumentException("Wrong length for input!");
        }
        Set<Character> chars = new HashSet<>();
        for (int i = 0; i < encoding.length(); i++) {
            char c = encoding.charAt(i);
            if (!isCharInRange(c)) {
                throw new IllegalArgumentException("Character outside of range in input!");
            }
            if (chars.contains(c)) {
                throw new IllegalArgumentException("Duplicates in input!");
            }
            chars.add(c);
        }
        this.encoding = encoding;
    }

    // Behavior: Encrypts the provided input by substituting characters based on
    //           previously provided encoding
    // Exceptions: Throws an IllegalStateException if the encoding was never provided.
    //             Throws an IllegalArgumentException if input is null
    // Returns: The encrypted ciphertext(String)
    // Parameters: input: the plaintext input to encrypt. Should not be null and all characters of
    //             input should be within the encodable range(characters between MIN_CHAR 
    //             and MAX_CHAR (inclusive), default A->Z).
    @Override
    public String encrypt(String input) throws IllegalArgumentException, IllegalStateException {
        if (input == null) {
            throw new IllegalArgumentException("Provided input is null");
        }
        if (this.encoding == null) {
            throw new IllegalStateException("Encoding never set after empty construction");
        }
        String output = "";
        for (int i = 0; i < input.length(); i++) {
            output += encoding.charAt(input.charAt(i) - Cipher.MIN_CHAR);
        }
        return output;
    }
    
    // Behavior: Decrypts the provided input by reversing the substitution based on
    //           previously provided encoding
    // Exceptions: Throws an IllegalStateException if the encoding was never provided.
    //             Throws an IllegalArgumentException if input is null
    // Returns: The decrypted plaintext(String)
    // Parameters: input: the ciphertext input to decrypt. Should not be null and all characters
    //             of input should be within the encodable range(characters between MIN_CHAR 
    //             and MAX_CHAR (inclusive), default A->Z).
    @Override
    public String decrypt(String input) throws IllegalArgumentException, IllegalStateException {
        if (input == null) {
            throw new IllegalArgumentException("Provided input is null");
        }
        if (this.encoding == null) {
            throw new IllegalStateException("Encoding never set after empty construction");
        }
        String output = "";
        for (int i = 0; i < input.length(); i++) {
            output += (char) (encoding.indexOf(input.charAt(i)) + Cipher.MIN_CHAR);
        }
        return output;
    }
}