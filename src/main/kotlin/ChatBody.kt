import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import misc.AnsiColors.bold
import misc.AnsiColors.red
import misc.AnsiColors.green
import misc.AnsiColors.cyan
import misc.AnsiColors.reset
import misc.AnsiColors.underline

@Serializable
data class Message(
    val role    :String,
    var content :String,
    val name    :String? = null,
    val function_call :String? = null,

) {



    override fun toString(): String = "${underline}${green}${(role.toUpperCase()+":${reset}").padEnd(11)}\n${content.indent(4)}"
}

fun chatBody(
    temperature: Double = 0.5,
    topP: Double = 1.0,
    presencePenalty: Double = 0.4,
    frequencyPenalty: Double = 0.1,
    model :String = "gpt-3.5-turbo",
    maxTokens: Int = 1200,
    block: ChatBody.() -> Unit
): ChatBody {
    val requestBody = ChatBody(temperature, topP, presencePenalty, frequencyPenalty, model, maxTokens)
    requestBody.block()
    return requestBody
}

@Serializable
data class ChatBody(

    var temperature: Double,
    @SerialName("top_p")
    var topP:Double,
    @SerialName("presence_penalty")
    var presencePenalty: Double,
    @SerialName("frequency_penalty")
    var frequencyPenalty: Double,
    @Serializable
    @SerialName("model")
    var model: String,
    @Serializable
    @SerialName("max_tokens")
    var maxTokens: Int
) {
    val messages: MutableList<Message> = mutableListOf()

    fun ChatBody.user(text: String) =
        messages.add(Message("user", text))

    fun ChatBody.assistant(text: String) =
        messages.add(Message("assistant", text))

    fun ChatBody.system(text: String) =
        messages.add(Message("system", text))


    fun toJson() :String {
        return Json.encodeToString(this)
    }

    override fun toString(): String {




        val sb = StringBuilder()
        sb.append("${underline}ChatBody{${reset}\n")
        sb.append("- ${bold}temperature:${reset}   ${underline}$temperature${reset}\n")
        sb.append("- ${bold}topP:${reset}          ${underline}$topP${reset}\n")
        sb.append("- ${bold}presence ${reset}pen:  ${underline}$presencePenalty${reset}\n")
        sb.append("- ${bold}frequenc ${reset}pen:  ${underline}$frequencyPenalty${reset}\n")
        sb.append("- ${bold}messages:${reset}\n")
        for (message in messages)
            sb.append("⁍${message.toString()}".indent(4))
        sb.append("}")
        return sb.toString()
    }



}






