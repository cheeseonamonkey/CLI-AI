import kotlinx.coroutines.runBlocking
import misc.Requester
import java.util.*
import misc.AnsiColors.bold
import misc.AnsiColors.code
import misc.AnsiColors.reset
import misc.AnsiColors.underline
import misc.AnsiColors.green
import misc.AnsiColors.blue
import misc.AnsiColors.cyan
import misc.AnsiColors.magenta
import misc.AnsiColors.italics





class Tui(
    val chatBody: ChatBody,
    val configFile: ConfigFile
) {
    private val scanner: Scanner = Scanner(System.`in`)


    fun start() {



        println("Welcome to the chat program!")

        for(msg in chatBody.messages)
            msg.draw()

        var input: String
        do {
            print("${blue } ${underline}${"user:".toUpperCase()}${reset} ${blue}${reset}\n${italics}${reset}")
            print("      ")
            input = scanner.nextLine()

            // Process the user input
            processInput(input)
        } while (true)
    }

    private fun processInput(input: String) {

        //don't draw the user message!
        chatBody.messages.add(Message("user", input))

        runBlocking {
            val chatresponse = Requester(configFile).chatRequest(chatBody)
            chatBody.messages.add(Message("assistant", chatresponse))
            chatBody.messages.last().draw()
        }

    }


    private fun Message.draw() :Unit {
        when(this.role){
            "system"    -> print("${cyan } ${underline}${role.toUpperCase()}:${reset}\n${italics}${content.indent(6)}${reset}")
            "user"      -> print("${blue } ${underline}${role.toUpperCase()}:${reset} ${blue}${reset}\n${italics}${content.indent(6)}${reset}")
            "assistant" -> print("${green} ${underline}${role.toUpperCase()}:${reset}\n${italics}${content.indent(6)}${reset}")

            else -> throw Exception("role not found (when drawing TUI message)")
        }

    }
}

