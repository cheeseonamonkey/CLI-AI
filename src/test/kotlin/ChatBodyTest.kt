import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.*

class ChatBodyTest {
    @Test
    fun testEmptyChat() {
        chatBody {

        }
    }

    @Test
    fun testUserMessage(){
        val cb = chatBody { user("hi") }
        assertEquals(cb.messages.first().content, "hi")
            assertEquals(cb.messages.first().role, "user")
    }
    @Test
    fun testAssistantMessage() {
        val cb = chatBody { assistant("hi") }
                assertEquals(cb.messages.first().content, "hi")
assertEquals(cb.messages.first().role, "assistant")

    }

    @Test
    fun testSystemMessage() {
        val cb = chatBody { system("hi") }
                assertEquals(cb.messages.first().content, "hi")
                      assertEquals(cb.messages.first().role, "system")

    }

    @Test
    fun testAsJson(){
        chatBody {
            assistant("hi!")
            system   ("hey")
            user     ("ayy")
        }
    }


}