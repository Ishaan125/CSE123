// Ishaan Srivastava 
// 10/8/2025
// CSE 123 
// P0: Ciphers
// TA: Aidan Suen

import java.util.*;

// Represents a classical cipher in which a client can define two characters
// to swap on encryption
public class SwapCipher extends MultiSwapCipher {
    
    // Behavior: Constructs a new SwapCipher in which all 'a's from the plaintext
    //           are replaced with 'b's and vice-versa
    // Exceptions: Throws an IllegalArgumentException if either of the provided
    //             characters fall outside the encodable range
    // Parameters:
    //      - a: the first character to swap
    //      - b: the second character to swap
    public SwapCipher(char a, char b) {
        super();
        
        List<Character> swaps = new ArrayList<>();
        swaps.add(a);
        swaps.add(b);

        super.setSwaps(swaps);
    }

    @Override
    // Exceptions: Throws an UnsupportedOperationException
    public void setSwaps(List<Character> chars) {
        // While we're inheriting useful behavior from MultiSwapCipher (encrypt / decrypt)
        // we're also inheriting non-useful behavior (setSwaps) so we override and make
        // an exception be thrown if a client ever tries to call this method
        throw new UnsupportedOperationException("Unable to setSwaps for SwapCipher");
    }
}