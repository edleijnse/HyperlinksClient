
//import javafx.scene.control.SelectionMode
//import javafx.scene.control.skin.TableColumnHeader
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.control.SelectionMode
import javafx.scene.paint.Color
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
    // var hyperLinksList = listOf("").toMutableList().asObservable()
    val hyperLinksList = FXCollections.observableArrayList<String>(
    )

    var hyperLinksListSimple = listOf("")


    override val root = form {
        input.value = ""
        setPrefSize(1000.0, 600.0)
        fieldset {
            field("Input") {
                textfield(input)
            }

            button("search") {
                action {
                    hyperLinksList.clear()
                    hyperLinksListSimple = controller.readHyperlinks(input.value)
                    for (hyperlinkItem in hyperLinksListSimple) {
                        hyperLinksList.add(hyperlinkItem)
                    }
                    input.value = ""
                }
                style {
                    textFill = Color.RED
                }
            }
            listview(hyperLinksList) {
                selectionModel.selectionMode = SelectionMode.SINGLE
                //TableColumnHeader columnHeader =
                // onUserSelect(1) { "print you selected" }
                onDoubleClick {
                    println("double click")
                }
            }
            hyperlink("open hyperlink") {
                action {
                    println("hyperlink clicked")}
            }
        }
    }
}


class MyController : Controller() {
    fun writeToDb(inputValue: String) {
        println("Writing $inputValue to database!")
    }

    fun readHyperlinks(searchString: String): List<String> {
        println("hyperlinks restcall with $searchString ")
        val encodedSearchString = URLEncoder.encode(searchString, "utf-8")
        val url = URL(
            "https://leijnse.info/hyperlinks/rest/Restcontroller.php/?command=allmysql&count=900&from=0&search=$encodedSearchString"
        )
        val jsonData = url.readText()
        println("output: $jsonData")
        var hyperlinksText: String
        var hyperlinksList = listOf("").toMutableList()
        hyperlinksList.clear()

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
                "ID: $id" + ", $group" + ", $category" + ", $webdescription" + ", $website" + "\n"
            hyperlinksList.add(hyperlinksText)

        }
        return hyperlinksList
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