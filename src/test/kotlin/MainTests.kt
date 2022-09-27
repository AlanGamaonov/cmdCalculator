
import junit.framework.TestCase.assertEquals
import org.junit.Test
import kotlin.test.assertFails

class MainTests {

    // Решил не писать тесты на каждый класс, так как если там что-то не так, более общие тесты тоже упадут

    @Test
    fun `space insensitivity test`() {
        assertEquals(calculate("-1+2+3"), calculate("-1 + 2 + 3"))
    }

    @Test
    fun `correct cases test`() {
        var answer = calculate("1+2+3")
        assertEquals(6.0, answer)

        answer = calculate("-5+6*(-3*8)")
        assertEquals(-149.0, answer)

        answer = calculate("((8)+(-3))")
        assertEquals(5.0, answer)
    }

    @Test
    fun `incorrect cases test`() {
        assertFails { calculate("1+2+3-") }

        assertFails { calculate("()") }

        assertFails { calculate("(3+2") }

        assertFails { calculate("3+2)") }

        assertFails { calculate("") }
    }

    private fun calculate(input : String) : Double =
        StringExpressionParser.parseExpression(input).let { RpnCalculator.calcRpnQueue(it) }

}