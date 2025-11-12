// Ishaan Srivastava 
// 10/8/2025 -> 10/17/25
// CSE 123 
// P0: Ciphers
// TA: Aidan Suen

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assume.assumeTrue;
import java.util.*;

// This class runs the unit tests for the ciphers implemented in this project.
// The tests cover scenarios for Substitution, CaesarKey, CaesarShift, and MultiCipher.
public class Testing {

    @Test
    @DisplayName("EXAMPLE TEST CASE - 'A'-'G' Spec Example")
    public void subAGTest() {
        // Remember that you can change MIN_CHAR AND MAX_CHAR 
        // in Cipher.java to make testing easier! For this 
        // example test, we are using MIN_CHAR = A and MAX_CHAR = G

        // If this is false (i.e. the constants are not 'A'-'G'), the test will halt and 
        // be ignored (aborted). This doesn't mean that the code is wrong! It just means that
        // the test won't produce any meaningful information if the assumption is not met.
        assumeTrue(Cipher.MIN_CHAR == (int)('A') && Cipher.MAX_CHAR == (int)('G'));

        // Substitution cipher with "GCBEAFD"
        Cipher testSubstitution = new Substitution("GCBEAFD");
        assertEquals("FGE", testSubstitution.encrypt("FAD"));
        assertEquals("BAD", testSubstitution.decrypt("CGE"));
        
        // Per the spec, we should throw an IllegalArgumentException if 
        // the length of the shifter doesn't match the number of characters
        // within our Cipher's encodable range
        assertThrows(IllegalArgumentException.class, () -> {
            new Substitution("GCB");
        });
    }

    @Test
    @DisplayName("EXAMPLE TEST CASE - 'A'-'Z' Shifter")
    public void subAZTest() {
        // If this is false (i.e. the constants are not 'A'-'Z'), the test will halt and 
        // be ignored (aborted). This doesn't mean that the code is wrong! It just means that
        // the test won't produce any meaningful information if the assumption is not met.
        assumeTrue(Cipher.MIN_CHAR == (int)('A') && Cipher.MAX_CHAR == (int)('Z'));

        // Reverse alphabetic
        Cipher testSubstitution = new Substitution(
            "ZYXWVUTSRQPONMLKJIHGFEDCBA"
        );
        assertEquals("UZW", testSubstitution.encrypt("FAD"));
        assertEquals("BAD", testSubstitution.decrypt("YZW"));
    }

    @Test
    @DisplayName("EXAMPLE TEST CASE - ' '-'}' Shifter")
    public void subComplexTest() {
        // If this is false (i.e. the constants are not ' '-'}'), the test will halt and 
        // be ignored (aborted). This doesn't mean that the code is wrong! It just means that
        // the test won't produce any meaningful information if the assumption is not met.
        assumeTrue(Cipher.MIN_CHAR == (int)(' ') && Cipher.MAX_CHAR == (int)('}'));
        
        // Swapping lowercase a<->b
        Cipher testSubstitution = new Substitution(
            " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`" +
            "bacdefghijklmnopqrstuvwxyz{|}"
        );
        assertEquals("FAD", testSubstitution.encrypt("FAD"));
        assertEquals("fbd", testSubstitution.encrypt("fad"));
        assertEquals("BAD", testSubstitution.decrypt("BAD"));
        assertEquals("bad", testSubstitution.decrypt("abd"));
    }

    @Test
    @DisplayName("TODO: CaesarKey - 'A'-'Z'")
    public void keyAZOne() {
        // If this is false (i.e. the constants are not 'A'-'Z'), the test will halt and 
        // be ignored (aborted). This doesn't mean that the code is wrong! It just means that
        // the test won't produce any meaningful information if the assumption is not met.
        assumeTrue(Cipher.MIN_CHAR == (int)('A') && Cipher.MAX_CHAR == (int)('Z'));

        // Key "TIN", which produces the shifted alphabet: TINABCDEFGHJKLMOPQSUVWXYZ
        Cipher testCaesarKey = new CaesarKey("TIN");
        assertEquals("EBJJM", testCaesarKey.encrypt("HELLO"));
        assertEquals("HELLO", testCaesarKey.decrypt("EBJJM"));
        //tinabcdefghjklmop
        //abcdefghijklmnopq
    }

    @Test
    @DisplayName("TODO: CaesarShift - 'A'-'Z' Shifter")
    public void shiftAZOne() {
        // If this is false (i.e. the constants are not 'A'-'Z'), the test will halt and 
        // be ignored (aborted). This doesn't mean that the code is wrong! It just means that
        // the test won't produce any meaningful information if the assumption is not met.
        assumeTrue(Cipher.MIN_CHAR == (int)('A') && Cipher.MAX_CHAR == (int)('Z'));

        // Shift of 1
        Cipher testCaesarShift = new CaesarShift(1);
        assertEquals("IFMMP", testCaesarShift.encrypt("HELLO"));
        assertEquals("HELLO", testCaesarShift.decrypt("IFMMP"));
    }

    @Test
    @DisplayName("TODO: MultiCipher - 'A'-'Z' Shifter")
    public void multiAZOne() {
        // If this is false (i.e. the constants are not 'A'-'Z'), the test will halt and 
        // be ignored (aborted). This doesn't mean that the code is wrong! It just means that
        // the test won't produce any meaningful information if the assumption is not met.
        assumeTrue(Cipher.MIN_CHAR == (int)('A') && Cipher.MAX_CHAR == (int)('Z'));

        // MultiCipher with CaesarKey "TIN" followed by CaesarShift 6
        Cipher testMultiCipher = new MultiCipher(List.of(new CaesarKey("TIN"),new CaesarShift(6)));
        assertEquals("KHPPS", testMultiCipher.encrypt("HELLO"));
        assertEquals("HELLO", testMultiCipher.decrypt("KHPPS"));
    }
}