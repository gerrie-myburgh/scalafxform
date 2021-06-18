
import org.gerrie.scalafxform.fxform.*
import javafx.beans.property.*
import javafx.application.*

import javafx.fxml._
import javafx.scene._
import javafx.stage._

import org.gerrie.scalafxform.layout.*
import org.gerrie.scalafxform.util.*

import org.slf4j.*;

@main def hello: Unit = 

  val LOGGER = LoggerFactory.getLogger(getClass())
  LOGGER.info("Program start")

  Platform.startup(() => {})

  Load.loadViews(Construct.viewFileNames)

  Platform.runLater(new Runnable(){
      def run() =
        val loader = FXMLLoader()
        loader.setLocation(getClass().getResource("/form_main.fxml"))
        val root = loader.load().asInstanceOf[javafx.scene.layout.VBox]
        val scene = Scene(root)
        val stage = Stage()
        stage.setScene(scene)
        stage.show()
  })
  LOGGER.info("Program shutdown")

val msg = "Nothing"
