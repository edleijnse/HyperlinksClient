import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import javafx.beans.property.SimpleStringProperty
import javafx.scene.paint.Color
import javafx.scene.text.Font
import tornadofx.*
import java.net.URL
import java.net.URLEncoder


// https://edvin.gitbooks.io/tornadofx-guide/content/part1/3_Components.html


fun main() {
    // TornadoFX-Programm starten
    launch<MyApp>()
}

// zentrale App-Klasse
class MyApp : App(MyView::class)

// Fensterinhalt
class MyView : View() {
    val controller: MyController by inject()
    val input = SimpleStringProperty()
    val output = SimpleStringProperty()

    override val root = form {
        input.value = ""
        setPrefSize(1000.0, 600.0)
        fieldset {
            field("Input") {
                textfield(input)
            }

            button("search") {
                action {
                    val myOutput = controller.readHyperlinks(input.value)
                    output.value = myOutput
                    input.value = ""
                }
                style {
                    textFill = Color.RED
                }
            }
            text(output) {
                fill = Color.BLACK
                font = Font(12.0)
            }
            field("Output") {
                textfield(output)
            }


        }
    }
}


class MyController : Controller() {
    fun writeToDb(inputValue: String) {
        println("Writing $inputValue to database!")
    }

    fun readHyperlinks(searchString: String): String {
        println("hyperlinks restcall with $searchString ")
        val encodedSearchString = URLEncoder.encode(searchString, "utf-8")
        val url = URL(
            "https://leijnse.info/hyperlinks/rest/Restcontroller.php/?command=allmysql&count=900&from=0&search=$encodedSearchString"
        )
        val jsonData = url.readText()
        println("output: $jsonData")
        var hyperlinksText = "";

        // val hyperlinkListFromJson: kotlin.Any = ObjectMapper().readTree(jsonData)
        val mapper = ObjectMapper()
        val hyperlinksArray: JsonNode = mapper.readTree(jsonData)

        for (hyperlink in hyperlinksArray) {

            // Get id
            val id = hyperlink.path("ID")
            println("id : $id")
            // Get group
            val group = hyperlink.path("group")
            println("group : $group")
            // Get category
            val category = hyperlink.path("category")
            println("category : $category")
            // Get webdescription
            val webdescription = hyperlink.path("webdescription")
            println("id : $webdescription")
            // Get website
            val website = hyperlink.path("website")
            println("website : $website")

            hyperlinksText =
                hyperlinksText + "ID: $id" + ", $group" + ", $category" + ", $webdescription" + ", $website" + "\n"


        }
        return hyperlinksText
    }
}

class Hyperlinks {
    var hyperlinks: List<Hyperlink>? = null
}

class Hyperlink(
    val ID: String,
    val group: String,
    val category: String,
    val webdescription: String,
    val website: String
)