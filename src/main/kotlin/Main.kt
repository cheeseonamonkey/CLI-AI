@file:OptIn(ExperimentalCoroutinesApi::class)

import eu.jrie.jetbrains.kotlinshell.shell.shell
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.runBlocking
import misc.AnsiColors.bold
import misc.AnsiColors.code
import misc.AnsiColors.reset
import misc.AnsiColors.underline
import misc.Requester
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*
import kotlin.system.exitProcess

typealias CR = GlobalScope

var verbose = false;


fun String.logIfVerbose() { if(verbose) println(this) }
fun String.log() { println(this) }


suspend fun main(args: Array<String>) {







    // parse args
    val parsedArgs = Args(args)

    // verbose
    verbose = parsedArgs.hasVerbose
    "\n${underline}${bold}Arguments:${reset}\n${parsedArgs.toString().indent(2)}".logIfVerbose()

    if(parsedArgs.hasHelp) {
        println(parsedArgs.generateHelpMenu())
        exitProcess(0)
    }else if(parsedArgs.hasConfig){
        println("Config is located at:\n  ~/.config/chatapp/chat.config")
        exitProcess(0)
    }



    // ConfigFile
    val config = ConfigFile()
    "\n${underline}${bold}Read config file:${reset}\n${config.toString().indent(2)}".logIfVerbose()





    // ChatBody:
    val chatBody = chatBody(config.temperature, config.topP, config.presensePenalty, config.frequencyPenalty) {
        system(config.systemPrompt)
    }



    // ls (before user message!):
    if(parsedArgs.hasLs) {

        val lsResults = runShell("ls")?.trim() ?: "error"

        chatBody.messages.add(
            Message(
                "system",
                "Here are the contents of the current directory:\n```" + ("\n> ls;\n" + "${lsResults}\n") + "```".trimIndent().trim()
            )
        )
    }

    if(parsedArgs.hasInteractive){
        Tui(chatBody, config).start()
        exitProcess(0)
    }

    // user input message:
    chatBody.messages.add(Message("user", parsedArgs.inputMessage.message.toString()))
    "\n${underline}${bold}ChatBody:${reset}\n${chatBody.toString().indent(2)}".logIfVerbose()








    // send chat
    val responseMessage = Requester(config).chatRequest(chatBody, verbose)
        .trim('"')
        .replace("\\n", "\n")
        .replace("```(.*?)```".toRegex(RegexOption.DOT_MATCHES_ALL), "${code}$1${reset}")
        .replace("`(.*?)`".toRegex(), "${code}$1${reset}")



    println(responseMessage)














}












fun runShell(cmd:String): String? {
    var lsResult: String? = null

    val process = ProcessBuilder(cmd).start()
    val reader = BufferedReader(InputStreamReader(process.inputStream))
    val output = StringBuilder()

    var line: String?
    while (reader.readLine().also { line = it } != null) {
        output.append(line).append("\n")
    }

    lsResult = output.toString()
    println(lsResult)

    return lsResult
}
