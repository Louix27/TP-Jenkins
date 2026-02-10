import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class FactorialTest {

    @Test
    public void testFactorial0() {
        assertEquals(1, Factorial.factorial(0));
    }

    @Test
    public void testFactorial1() {
        assertEquals(1, Factorial.factorial(1));
    }

    @Test
    public void testFactorial5() {
        assertEquals(120, Factorial.factorial(5));
    }

    @Test
    public void testFactorial10() {
        assertEquals(3628800, Factorial.factorial(10));
    }

    @Test
    public void testNombreNegatif() {
        Exception e = assertThrows(
                IllegalArgumentException.class,
                () -> Factorial.factorial(-1)
        );

        assertEquals("Le nombre doit être positif !", e.getMessage());
    }
}
