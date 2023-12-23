package args

import Args
import Flag
import Input
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.TestInstance
import java.lang.Exception

class ArgsTest {

    private lateinit var parsed: Args

    @BeforeEach
    fun setUp1() {
        parsed = Args(arrayOf("-v", "-l", "-f", "hello", "world!"))
    }

    @Test
    fun testParseArgsWithNoInputMessage() {
        assertThrows<Exception> {
            Args(arrayOf("-v", "-l", "-f"))
        }
    }

   /* @Test
    fun testParseArgsWithDifferentOrders() {
        val args1 = arrayOf("-v", "hello", "world!", "-f")
        val args2 = arrayOf("-v", "hello", "-f", "world!")
        val args3 = arrayOf("joy", "-v", "to", "the", "-f", "world!")
        val parsed1 = Args(args1)
        val parsed2 = Args(args2)
        val parsed3 = Args(args3)
        assertEquals("hello world!", (parsed1["message"] as Input).message)
        assertEquals("hello world!", (parsed2["message"] as Input).message)
        assertEquals("joy to the world!", (parsed3["message"] as Input).message)
        assertEquals(true, (parsed1["v"] as Flag).active)
        assertEquals(true, (parsed2["v"] as Flag).active)
        assertEquals(true, (parsed3["v"] as Flag).active)
        assertEquals(true, (parsed1["f"] as Flag).active)
        assertEquals(true, (parsed2["f"] as Flag).active)
        assertEquals(true, (parsed3["f"] as Flag).active)
    }*/

    @Test
    fun testParseArgsWithFlags() {
        assertEquals(true, (parsed["v"] as Flag).active)
        assertEquals(true, (parsed["l"] as Flag).active)
        assertEquals(true, (parsed["f"] as Flag).active)
        assertEquals("hello world!", (parsed["input"] as Input).message)
        assertEquals(null, (parsed["t"]))
    }

    @Test
    fun testParseArgsWithoutInput() {
        assertThrows<Exception> {
            Args(arrayOf<String>())
        }
    }

    @Test
    fun testParseArgsWithOnlyInput() {
        val args = arrayOf("input")
        val parsed = Args(args)
        assertNull(parsed["v"])
        assertNull(parsed["h"])
        assertNull(parsed["t"])
        assertNull(parsed["f"])
        assertNull(parsed["l"])
        assertNull(parsed["h"])
        assertEquals("input", (parsed["input"] as Input).message)
    }
cat 
    @Test
    fun testParseArgsWithUnknownFlag() {
        val args = arrayOf("-x", "hello", "world!")
        assertThrows<Exception> {
            Args(args)
        }
    }

    @Test
    fun testParseArgsWithMultipleFlags() {
        assertEquals(true, (parsed["v"] as Flag).active)
        assertEquals(true, (parsed["l"] as Flag).active)
        assertEquals(true, (parsed["f"] as Flag).active)
        assertEquals(null, (parsed["t"]))
    }

    @Test
    fun testParseArgsWithDuplicateFlags() {
        val args = arrayOf("-v", "-v", "-f", "-f", "hello", "world!")
        val parsed = Args(args)
        assertEquals(true, (parsed["v"] as Flag).active)
        assertEquals(true, (parsed["f"] as Flag).active)
        assertEquals(null, (parsed["t"]))
    }

    @Test
    fun testParseArgsWithMixedFlagsAndMessage() {
        val args = arrayOf("-v", "-h", "Hello!")
        val parsed = Args(args)
        assertEquals(true, (parsed["v"] as Flag).active)
        assertEquals(true, (parsed["h"] as Flag).active)
        assertEquals("Hello!", (parsed["input"] as Input).message)
    }

    @Test
    fun testGetFlagByShortName() {
        assertEquals(true, (parsed["v"] as Flag).active)
    }

    @Test
    fun testNullAssertions() {
        assertNotNull(parsed["v"])
        assertNotNull(parsed["l"])
        assertNotNull(parsed["f"])
        assertNotNull(parsed["input"])
        assertNull(parsed["h"])
        assertNull(parsed["t"])
        assertNull(parsed["s"])
    }

    @Test
    fun testGetFlagByFullName() {
        val args = arrayOf("--verbose", "hello", "world!")
        val parsed = Args(args)
        assertEquals(true, (parsed["verbose"] as Flag).active)
    }

    @Test
    fun testGetNonExistingFlag() {
        val args = arrayOf("-x", "hello", "world!")
        assertThrows<Exception> {
            Args(args)
        }
    }

    @Test
    fun testGetMessageOption() {
        val args = arrayOf("Hello World!")
        val parsed = Args(args)
        assertEquals("Hello World!", (parsed["input"] as Input).message)
    }



    @Test
    fun testGetFlagByShortNameWithInactiveFlag() {
        val args = Args(arrayOf("-v", "-l", "-f", "hello", "world!"))
        val flag = args["s"]
        assertNull(flag)
    }

    @Test
    fun testGetFlagByFullNameWithInactiveFlag() {
        val args = Args(arrayOf("-v", "-l", "-f", "hello", "world!"))
        val flag = args["system"]
        assertNull(flag)
    }

    @Test
    fun testGetFlagByShortNameWithActiveFlag() {
        val args = Args(arrayOf("-v", "-l", "-f", "hello", "world!"))
        val flag = args["v"]
        assertNotNull(flag)
        assertTrue(flag is Flag)
        assertTrue((flag as Flag).active)
    }

    @Test
    fun testNoInputExceptionCases() {
        val args = Args(arrayOf("-h" ))
        assertTrue((args["help"] as Flag).active)
        val args2 = Args(arrayOf("-u" ))
        assertTrue((args2["ui"] as Flag).active)
    }

}
