
import javafx.application.*
import javafx.beans.property.*
import javafx.fxml.*
import javafx.scene.*
import javafx.stage.*
import org.gerrie.scalafxform.controller.*
import org.gerrie.scalafxform.fxform.*
import org.gerrie.scalafxform.layout.*
import org.gerrie.scalafxform.util.*
import org.slf4j.*

@main def main() =
    Application.launch(classOf[App])

class App extends Application:

  /************************************************************************** 
   * start of application
   */
  override def start(stage : Stage) =
    //val LOGGER = LoggerFactory.getLogger(getClass())
    Form.showMain(stage)
  
  /************************************************************************** 
   * all cleanup actions
   */
  override def stop() = 
    RunConfig.storeConfig()

val msg = "Nothing"
