import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MyControllerTest {
    private val controller = MyController()

    @Test
    fun extractUrl() {

        val urlInput = "ID: \"20220514003\", \"Traveling\", \"Car\", \"Compare prices / Preisvergleic\", -#\"https://www.autouncle.ch/de-ch/gebrauchtwagen/Skoda/Fabia/y-2013\"#-"
        val urlOutput = controller.extractUrl(urlInput)
        val urlExpectedOutput = "https://www.autouncle.ch/de-ch/gebrauchtwagen/Skoda/Fabia/y-2013"
        println("urlOutput: $urlOutput")
        Assertions.assertEquals(urlExpectedOutput,urlOutput)
    }
}