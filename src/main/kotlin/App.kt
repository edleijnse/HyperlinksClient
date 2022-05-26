
//import javafx.scene.control.SelectionMode
//import javafx.scene.control.skin.TableColumnHeader
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.SelectionMode
import javafx.scene.paint.Color
import tornadofx.*
import java.awt.Desktop
import java.net.URI
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
    var hyperLinksListSimple = FXCollections.observableArrayList<HyperlinkItem>(
    )
    var hyperLinksList = FXCollections.observableArrayList<HyperlinkItem>(
    )
    val ID = SimpleStringProperty()
    val group = SimpleStringProperty()
    val category = SimpleStringProperty()
    val webdescription = SimpleStringProperty()
    val website = SimpleStringProperty()

    var selectedUrl = "nothing selected"

    override val root = form {
        // https://edvin.gitbooks.io/tornadofx-guide/content/part1/7_Layouts_and_Menus.html
        input.value = ""
        ID.value = ""
        group.value = ""
        category.value = ""
        webdescription.value = ""
        website.value = ""
        tabpane{
            tab("search"){
                input.value = ""
                setPrefSize(1000.0, 600.0)
                fieldset ("search for hyperlinks"){
                    field("Input") {
                        textfield(input)
                    }
                    button("search") {
                        action {
                            hyperLinksListSimple.clear()
                            hyperLinksListSimple = controller.readHyperlinks(input.value) as ObservableList<HyperlinkItem>?

                            hyperLinksList.clear()
                            for (hyperlinkItem in hyperLinksListSimple) {
                                hyperLinksList.add(hyperlinkItem)
                            }
                            input.value = ""
                        }
                        style {
                            textFill = Color.RED
                        }
                    }

                    tableview(hyperLinksList) {
                        selectionModel.selectionMode = SelectionMode.SINGLE
                        // columnResizePolicy = SmartResize.POLICY
                        //TableColumnHeader columnHeader =
                        // onUserSelect(1) { "print you selected" }
                        // https://catwolf.org/qs?id=5b2a883c-15d0-4d56-bd0f-cc89543fa044&x=y


                        column("ID",HyperlinkItem::ID)
                        column("group",HyperlinkItem::group)
                        readonlyColumn("category",HyperlinkItem::category)
                        // column("webdescription",HyperlinkItem::webdescription).remainingWidth()
                        column("webdescription",HyperlinkItem::webdescription)
                        // readonlyColumn("url",HyperlinkItem::website).remainingWidth()
                        readonlyColumn("url",HyperlinkItem::website)

                        onDoubleClick {
                            // selectedUrl = MyController().extractUrl(selectedItem.toString())
                            var selectedHyperlink = selectedItem


                            println("selectedItem: $selectedHyperlink.website")
                            if (selectedHyperlink != null) {
                                selectedUrl = controller.cleanUrl(selectedHyperlink.website)
                                println("double click: $selectedUrl")
                                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                                    Desktop.getDesktop().browse(URI(selectedUrl));
                                }
                            }

                        }
                    }

                    hyperlink("double click on list to go to hyperlink") {
                        action {
                            println("hyperlink clicked: $selectedUrl")
                            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                                Desktop.getDesktop().browse(URI(selectedUrl));
                            }
                        }
                    }
                }
                tab("insert"){
                    setPrefSize(1000.0, 600.0)
                    fieldset ("insert a hyperlink"){
                        hbox{
                            label("group                 ")
                            field("group"){
                                textfield(group)
                            }
                        }
                        hbox{
                            label("category             ")
                            field("category"){
                                textfield(category)
                            }
                        }
                        hbox{
                            label("webdescription  ")
                            field("webdescription"){
                                textfield(webdescription)
                            }
                        }
                        hbox{
                            label("website              ")
                            field("website"){
                                textfield(website)
                            }
                        }
                        button("insert") {
                            action {
                                controller.insertHyperLink( group.value, category.value,webdescription.value,website.value)
                            }
                            style {
                                textFill = Color.RED
                            }
                        }
                    }

                }
                tab("delete"){
                    setPrefSize(1000.0, 600.0)
                    fieldset ("delete a hyperlink"){
                        hbox{
                            label("ID                       ")
                            field("ID"){
                                textfield(ID)
                            }
                            button("delete") {
                                action {
                                    controller.deleteHyperLink(ID.value)
                                }
                                style {
                                    textFill = Color.RED
                                }
                            }
                        }
                    }

                }
            }
        }

    }
}


class MyController : Controller() {
    fun writeToDb(inputValue: String) {
        println("Writing $inputValue to database!")
    }
    fun extractUrl(inputUrl: String): String {
        val startIndex = inputUrl.lastIndexOf("-#") + 3
        val stopIndex = inputUrl.lastIndexOf("#-") - 1
        var outputUrl = ""
        if ((startIndex>=0) &&  (stopIndex>0)) {
            outputUrl = inputUrl.substring(startIndex,stopIndex)
        }

        return outputUrl
    }
    fun cleanUrl(inputUrl: String): String {
        val outputCleaned = inputUrl.substring(1, inputUrl.length-1)

        return outputCleaned
    }
    fun insertHyperLink( group: String, category: String, webdescription: String, website: String): String {
        var resultString: String
        println("group:$group category:$category webdescription:$webdescription website:$website")
        val encodedGroup = URLEncoder.encode(group, "utf-8")
        val encodedCategory = URLEncoder.encode(category, "utf-8")
        val encodedWebdescription = URLEncoder.encode(webdescription, "utf-8")
        val encodedWebsite = URLEncoder.encode(website, "utf-8")
        val url = URL(
            "https://leijnse.info/hyperlinks/rest/Restcontroller.php/?command=insertmysql&group=$encodedGroup&category=$encodedCategory&webdescription=$encodedWebdescription&website=$encodedWebsite"

        )
        val jsonData = url.readText()
        println("output: $jsonData")

        resultString= "OK"
        return resultString
    }
    fun deleteHyperLink(ID: String): String {
        var resultString: String
        println("ID: $ID ")
        val encodedID = URLEncoder.encode(ID, "utf-8")
        val url = URL(
            "https://leijnse.info/hyperlinks/rest/Restcontroller.php/?command=deletemysql&ID=$encodedID"

        )
        val jsonData = url.readText()
        println("output: $jsonData")

        resultString= "OK"
        return resultString
    }
    fun readHyperlinks(searchString: String): List<HyperlinkItem> {
        println("hyperlinks restcall with $searchString ")
        val encodedSearchString = URLEncoder.encode(searchString, "utf-8")
        val url = URL(
            "https://leijnse.info/hyperlinks/rest/Restcontroller.php/?command=allmysql&count=900&from=0&search=$encodedSearchString"
        )
        val jsonData = url.readText()
        println("output: $jsonData")
        var hyperlinksText: String
        val hyperLinksList = FXCollections.observableArrayList<HyperlinkItem>(
        )
        hyperLinksList.clear()

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
                "ID: $id" + ", $group" + ", $category" + ", $webdescription" + ", -#$website#-" + "\n"
            var myHyperlink = HyperlinkItem(id.toString(), group.toString(), category.toString(), webdescription.toString(), website.toString())
            hyperLinksList.add(myHyperlink)

        }
        return hyperLinksList
    }
}

class Hyperlinks {
    var hyperlinks: List<HyperlinkItem>? = null
}

class HyperlinkItem(ID: String, group: String, category: String, webdescription: String, website: String){
    val IDProperty = SimpleStringProperty(ID)
    var ID by IDProperty
    val groupProperty = SimpleStringProperty(group)
    var group by groupProperty
    val categoryProperty = SimpleStringProperty(category)
    var category by categoryProperty
    val webdescriptionProperty = SimpleStringProperty(webdescription)
    var webdescription by webdescriptionProperty
    val websiteProperty = SimpleStringProperty(website)
    var website by websiteProperty

}

