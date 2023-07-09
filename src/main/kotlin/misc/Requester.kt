package misc



import ChatBody
import ConfigFile
import fuel.Fuel
import fuel.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.*
import misc.AnsiColors.reset
import misc.AnsiColors.underline

class Requester(
    val configFile : ConfigFile
) {

     suspend fun chatRequest(chatBod : ChatBody, verbose:Boolean = false): String {
         if(verbose)
             println("\n${underline}sending request:\t\t\t\t${reset}\n" + chatBod.toJson().replace("\\n".toRegex(), "").indent(4))

         val responseStr = Fuel.post(
             "https://api.openai.com/v1/chat/completions",
             null,
             chatBod.toJson(),
             mapOf(
                 Pair("Content-Type", "application/json"),
                 Pair("Authorization", "Bearer ${configFile.apiKey}")
             )
         )

if(verbose)
    println("\n${underline}received response:\t\t\t\t\t${reset}\n" + responseStr.body.toString().replace("\\n".toRegex(),"").replace("\\s\\s".toRegex(),"").indent(4))


         val choices = Json.parseToJsonElement(responseStr.body).jsonObject["choices"]
             ?: throw Exception("error - unexpected response: ${responseStr.body}")
         val choiceResult = choices.jsonArray.first().jsonObject["message"]!!.jsonObject["content"]!!.jsonPrimitive

         return choiceResult.toString().trim('"')
     }



    suspend fun testGet() :String {
        val string = Fuel.get("https://publicobject.com/helloworld.txt")
        println(string.body)
        return string.toString()
    }

    suspend fun testPost()  :String{
        val string = Fuel.post(
            "https://postman-echo.com/post",
            null,
            "hello!",
            mapOf()
        )
        return string.toString()
    }



}