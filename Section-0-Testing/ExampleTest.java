import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ExampleTest {
    @Test
    @DisplayName("Example 1")
    public void firstCaseTest() {
        assertEquals(1, Example.one());
    }

    @Test
    @DisplayName("Example 2")
    public void secondCaseTest() {
        assertEquals(2, Example.two());
    }
}