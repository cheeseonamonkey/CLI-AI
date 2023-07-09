@file:Suppress("EXTENSION_SHADOWED_BY_MEMBER")

import misc.AnsiColors.reset
import misc.AnsiColors.underline
import java.io.File
import kotlin.script.experimental.jsr223.*
import java.io.FileInputStream
import java.util.Properties

operator fun Properties.get(key: String): String? =  getProperty(key)

class ConfigFile(
    val filePath: String = "/home/alexander/.config/chatapp/chat.config"
): Properties() {

    val apiKey        :String
    val systemPrompt  :String
    val temperature       :Double
    val topP              :Double
    val frequencyPenalty  :Double
    val presensePenalty   :Double

    init {
        val fileInputStream = FileInputStream(filePath)
        this.load(fileInputStream)
        fileInputStream.close()

        apiKey = this["api_key"]!!.toString()

        temperature = this["temperature"]!!.toString().toDoubleOrNull()!!
        topP = this["top_p"]!!.toString().toDoubleOrNull()!!
        frequencyPenalty = this["frequency_penalty"]!!.toString().toDoubleOrNull()!!
        presensePenalty = this["presence_penalty"]!!.toString().toDoubleOrNull()!!
        systemPrompt = this["system_prompt"]!!.toString()

    }


    override fun toString(): String {
        return StringBuilder("").apply {
            //appendln("filePath:     \t $filePath")
            appendln("- apiKey:${reset}            ${underline}$apiKey${reset}")
            appendln("- systemPrompt:${reset}\n${systemPrompt.indent(5).trimEnd()}")
            appendln("- temperature:${reset}       ${underline}$temperature${reset}");
            appendln("- topP:${reset}              ${underline}$topP${reset}");
            appendln("- frequencyPenalty:${reset}  ${underline}$frequencyPenalty${reset}");
            appendln("- presensePenalty:${reset}   ${underline}$presensePenalty${reset}");
        }.toString()
    }




}



