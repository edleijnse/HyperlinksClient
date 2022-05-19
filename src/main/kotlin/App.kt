
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
class MyApp: App(MyView::class)

// Fensterinhalt
class MyView : View() {
    val controller: MyController by inject()
    val input = SimpleStringProperty()
    val output = SimpleStringProperty()

    override val root = form {
        input.value = ""
        fieldset {
            field("Input") {
                textfield(input)
            }

            button("Commit") {
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
                fill = Color.PURPLE
                font = Font(20.0)
            }
        }
    }
}


class MyController: Controller() {
    fun writeToDb(inputValue: String) {
        println("Writing $inputValue to database!")
    }
    fun readHyperlinks(searchString: String): String {
        println("hyperlinks restcall with $searchString ")
        val encodedSearchString = URLEncoder.encode(searchString, "utf-8")
        val url = URL(
            "https://leijnse.info/hyperlinks/rest/Restcontroller.php/?command=allmysql&count=900&from=0&search=$encodedSearchString")
        val jsonData = url.readText()
        println("output: $jsonData")

        // val hyperlinkListFromJson: kotlin.Any = ObjectMapper().readTree(jsonData)
        val mapper = ObjectMapper()
        val rootArray: JsonNode = mapper.readTree(jsonData)

        for (root in rootArray) {

            // Get id
            val id = root.path("ID")
            println("id : $id")
            // Get group
            val group = root.path("group")
            println("group : $group")
            // Get category
            val category = root.path("category")
            println("category : $category")
            // Get webdescription
            val webdescription = root.path("webdescription")
            println("id : $webdescription")
            // Get website
            val website = root.path("website")
            println("website : $website")


        }
        return jsonData.toString()
    }
}
class Hyperlinks {
    var hyperlinks: List<Hyperlink>? = null
}
class Hyperlink (
    val ID: String,
    val group: String,
    val category: String,
    val webdescription: String,
    val website: String
)