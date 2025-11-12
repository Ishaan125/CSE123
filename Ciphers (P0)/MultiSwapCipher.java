// Ishaan Srivastava 
// 10/8/2025
// CSE 123 
// P0: Ciphers
// TA: Aidan Suen

import java.util.*;

// Represents a classical cipher in which a client can define any number of characters
// to swap in a circular fashion on encryption
public class MultiSwapCipher extends Cipher {
    private List<Character> swaps;
    
    // Behavior: Constructs an empty MultiSwapCipher
    public MultiSwapCipher() {
        this.swaps = null;
    }
    
    // Behavior: Constructs a MultiSwapCipher using the provided 'swaps' where each
    //           character of a plaintext is replaced with the one following it on 
    //           encryption. swaps is circular, meaning the character following
    //           the last index is the character at the first index.
    // Exceptions: Throws an IllegalArgumentException if swaps is null, 
    //             there are < 2 swaps in the
    //             provided list, or if any character within the list falls outside the
    //             encodable range
    // Parameters:
    //      - 'swaps': the swaps to use when encrypting. Should be non-null.
    public MultiSwapCipher(List<Character> swaps) {
        setSwaps(swaps);
    }

    // Behavior: Updates the swaps used for this MultiSwapCipher
    // Exceptions: Throws an IllegalArgumentException if swaps are null, 
    //             there are < 2 swaps in the
    //             provided list, or if any character within the list falls outside the
    //             encodable range
    // Parameters:
    //      - 'swaps': the updated swaps for this MultiSwapCipher. Should be non-null.
    public void setSwaps(List<Character> swaps) {
        if (swaps == null) {
            throw new IllegalArgumentException("Provided swaps is null");
        }
        if (swaps.size() <= 1) {
            throw new IllegalArgumentException("Need >1 swap to actually encrypt");
        }
        for (char c : swaps) {
            if (!isCharInRange(c)) {
                throw new IllegalArgumentException("Attempting to swap character " +
                                                   "outside of Cipher range");
            }
        }
        this.swaps = swaps;
    }

    // Behavior: Encrypts the provided 'input' by swapping characters based on
    //           previously provided swaps
    // Exceptions: Throws an IllegalStateException if the swaps were never provided
    //             Throws an IllegalArgumentException if input is null
    // Returns: The encrypted ciphertext
    // Parameters:
    //      - 'input': the plaintext input to encrypt. Should be non-null and all characters of
    //        'input' should be within the encodable range.
    public String encrypt(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Provided input is null");
        }
        if (this.swaps == null) {
            throw new IllegalStateException("Swaps never set after empty construction");
        }
        String ret = "";
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            int index = swaps.indexOf(c);
            if (index == -1) {
                ret += c;
            } else {
                int incremented = (index + 1) % swaps.size();
                ret += swaps.get(incremented);
            }
        }
        return ret;
    }

    // Behavior: Decrypt the provided 'input' by reversing character swaps
    //           based on previously provided swaps
    // Exceptions: Throws an IllegalStateException if the swaps were never provided
    //             Throws an IllegalArgumentException if input is null
    // Returns: The decrypted plaintext
    // Parameters:
    //      - 'input': the ciphertext input to decrypt. Should be non-null and all characters of
    //        'input' should be within the encodable range.
    public String decrypt(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Provided input is null");
        }
        if (this.swaps == null) {
            throw new IllegalStateException("Swaps never set after empty construction");
        }
        String ret = "";
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            int index = swaps.indexOf(c);
            if (index == -1) {
                ret += c;
            } else {
                int decremented = (index - 1 + swaps.size()) % swaps.size();
                ret += swaps.get(decremented);
            }
        }
        return ret;
    }
}