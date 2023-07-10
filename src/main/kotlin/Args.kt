import java.util.*

abstract class ArgumentOption(
    val name: String,
    val description: String
) {
    // Method to generate a help string for the -h option
    abstract fun buildOptionLine(): String
}


class Flag(
    val shortName: String,
    var active: Boolean,
    name: String,
    description: String
) : ArgumentOption(name, description) {

    companion object {
        fun possibleFlags() = listOf<Flag>(
            Flag("h", false, "help", "Show help menu"),
            Flag("c", false, "config", "Show config"),
            Flag("v", false, "verbose", "Verbose mode"),
            Flag("u", false, "ui", "Interactive UI / TUI"),
            Flag("l", false, "ls", "Include 'ls'"),
            Flag("f", false, "file", "desc"),
            Flag("t", false, "tree", "desc"),
            Flag("i", false, "include", "desc"),
            Flag("s", false, "system", "desc"),
        )
    }

    override fun buildOptionLine(): String {
        val shortNameLine = String.format("-%-2s", shortName)
        val longNameLine = String.format("--%-12s", name)
        val descriptionLine = String.format("%-20s", description)

        return "$shortNameLine $longNameLine $descriptionLine"
    }

    override fun toString(): String = "Flag: --$name (${active.toString().toUpperCase()})"
}





class Input(
    message :String,
) : ArgumentOption("input", "Your chat message"){
    override fun buildOptionLine(): String {
        val shortNameLine = String.format("%-3s", "")
        val longNameLine = String.format("%11s", name)
        val descriptionLine = String.format("%-22s", description)

        return "$longNameLine $shortNameLine $descriptionLine"

    }

    override fun toString(): String = "Message: '$message'"
    val message = message.trim();

}





class Args(args: Array<String>) {
    val resultsParsed: List<ArgumentOption>

    val hasHelp: Boolean get() = (this["help"] as? Flag)?.active ?: false
    val hasConfig: Boolean get() = (this["config"] as? Flag)?.active ?: false
    val hasVerbose: Boolean get() = (this["verbose"] as? Flag)?.active ?: false
    val hasInteractive: Boolean get() = (this["ui"] as? Flag)?.active ?: false
    val hasLs: Boolean get() = (this["ls"] as? Flag)?.active ?: false
    val hasFile: Boolean get() = (this["file"] as? Flag)?.active ?: false
    val hasTree: Boolean get() = (this["tree"] as? Flag)?.active ?: false
    val hasInclude: Boolean get() = (this["include"] as? Flag)?.active ?: false
    val hasSystem: Boolean get() = (this["system"] as? Flag)?.active ?: false

    val inputMessage :Input get() = (this["input"] as? Input)!!


    operator fun get(index: String): ArgumentOption? {
        val elSelect = resultsParsed.find { it.name[0] == index.trim('-')[0] }
        when(elSelect) {
            is Input -> return (elSelect as Input?)
            is Flag  -> return (elSelect as Flag ?)
            else     -> return null
        }




    }

    init {

        fun parseArgs(args: List<String>) : List<ArgumentOption> {
            val flagList = mutableListOf<ArgumentOption>()
            val inputStr = StringBuilder("")

            args.forEach { arg ->

                // - arg:
                if (arg.startsWith("--")) {
                    val argName = arg.removePrefix("--").lowercase(Locale.getDefault())
                    val possibleNames = Flag.possibleFlags().map { it.name }
                    //println(argName)
                    //println(possibleNames)
                    //println(possibleNames.contains(argName))

                    val foundFlag = Flag.possibleFlags().find { it.name == argName }
                    if (foundFlag != null) {//if found..
                        foundFlag.active = true;
                        flagList.add(foundFlag)

                    }

                    // -- arg:
                } else if (arg.startsWith("-")) {
                    val argShortName = arg.removePrefix("-").lowercase(Locale.getDefault())
                    val possibleShortNames = Flag.possibleFlags().map { it.shortName }
                    //println(argName)
                    //println(possibleNames)
                    //println(possibleNames.contains(argName))

                    val foundFlag = Flag.possibleFlags().find { it.shortName == argShortName }

                    //return found - else throw up:
                    if (foundFlag == null)
                        throw Exception("Invalid flag: ${arg}")
                    else {
                        flagList.add(foundFlag)
                        foundFlag.active = true;
                    }

                    // word arg:
                } else {
                    inputStr.append("$arg ")
                }
            }

            val lstOut = mutableListOf<ArgumentOption>().apply {
                if(flagList.isNotEmpty())
                    addAll(flagList)
                if(!args.joinToString("").contains("-[huc]".toRegex())   &&  inputStr.toString().isEmpty())
                    throw Exception("No <INPUT> parameter found in arguments")
                add(Input(inputStr.toString()))
            }.toList()
            return lstOut

        }


        // do arg parse:
        resultsParsed = parseArgs(args.toList())


    }




    fun generateHelpMenu(): String {
        val str = StringBuilder()

        str.appendLine("Required:")
        str.appendLine(this["input"]?.buildOptionLine())
        str.appendLine()

        str.appendLine("Options:")
        Flag.possibleFlags().forEachIndexed { i, flag ->
            if(i % 2 != 0)
                str.appendLine(flag.buildOptionLine())
            else
                str.append(flag.buildOptionLine())
        }


        return str.toString()
    }



    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("ArgParser:\n")
        sb.append("  active args:\n")

        for (option in resultsParsed) {
            sb.append("    - ").append(option.toString()).append("\n")
        }

        return sb.toString()
    }


}
