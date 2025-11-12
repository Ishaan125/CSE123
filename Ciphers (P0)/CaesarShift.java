// Ishaan Srivastava 
// 10/8/2025 -> 10/17/25
// CSE 123 
// P0: Ciphers
// TA: Aidan Suen

// This class produces an encoding by shifting the alphabet by a specified number of positions.
// It extends the Substitution class.
public class CaesarShift extends Substitution {

    // Behavior: Constructs a Caesar Shift Cipher by using the provided shift.
    // Exceptions: Throws an IllegalArgumentException if shift is negative
    // Parameters: shift: the amount to shift the alphabet by. Should be positive or 0.
    public CaesarShift(int shift) throws IllegalArgumentException{
        if (shift < 0) {
            throw new IllegalArgumentException("Negative shift input!");
        }

        shift %= Cipher.TOTAL_CHARS;  // Helps with large shifts

        char[] encode = new char[Cipher.TOTAL_CHARS];

        for (int i = 0; i < Cipher.TOTAL_CHARS; i++) {
            char c = (char) (Cipher.MIN_CHAR + i);  // Current character in the normal alphabet
            char o = (char) (Cipher.MIN_CHAR + (c + shift - Cipher.MIN_CHAR) % Cipher.TOTAL_CHARS);
            encode[i] = o;
        }

        setEncoding(new String(encode));
    }
}