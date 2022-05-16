
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.paint.Color
import javafx.scene.text.Font
import tornadofx.*
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
                    controller.writeToDb(input.value)
                    output.value = input.value
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
}