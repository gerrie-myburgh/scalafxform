
import org.gerrie.scalafxform.fxform.*
import javafx.beans.property.*
import javafx.application.*

import javafx.fxml._
import javafx.scene._
import javafx.stage._

import org.gerrie.scalafxform.layout.*
import org.gerrie.scalafxform.util.*
import controller.* 

import org.slf4j.*

@main def main() =
    Application.launch(classOf[App])

class App extends Application:

  /************************************************************************** 
   * start of application
   */
  override def start(stage : Stage) =
    val LOGGER = LoggerFactory.getLogger(getClass())
    Load.loadViews(Construct.viewFileNames)

    val loader = FXMLLoader()
    loader.setLocation(getClass().getResource("/form_main.fxml"))
    val root = loader.load().asInstanceOf[javafx.scene.layout.VBox]

    val controller = loader.getController[ControllerMain]()
    RunConfig.loadConfig(controller.mnuRecent, controller.openJarFile)

    val scene = Scene(root)
    stage.setScene(scene)
    stage.show()
  
  /************************************************************************** 
   * all cleanup actions
   */
  override def stop() = 
    RunConfig.storeConfig()

val msg = "Nothing"
