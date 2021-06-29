package controller

import java.net.URL
import java.util.ResourceBundle
import java.io.*
import javafx.event.ActionEvent
import javafx.fxml.*
import javafx.scene.*
import javafx.scene.text.*
import javafx.scene.control.*
import javafx.stage.*
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.layout.*
import javafx.collections.*
import javafx.application._
import javafx.beans.property.*
import javafx.stage.Stage
import javafx.scene.control.*
import javafx.scene.input.*
import javafx.util.*

import scala.collection._
import java.lang.annotation.{ElementType, Target}

import org.gerrie.scalafxform.controller.*
import org.gerrie.scalafxform.nodes.*
import org.gerrie.scalafxform.layout.*
import org.gerrie.scalafxform.util.*
import org.gerrie.scalafxform.fxform.*
import org.gerrie.scalafxform.viewmodel.*
import org.gerrie.scalafxform.viewmodel.*

import scala.beans.*
import java.nio.file.*
import java.util.jar.JarFile
import java.net.URLClassLoader

final case class ControllerMain():
    val classMemberList = DisplayClassMembersListVM() 
    var viewTree        = DisplayTreeViewComponentsVM() 
    var urlClassloader : URLClassLoader = null

    @FXML
    private var resources : ResourceBundle = null
    @FXML
    private var x1 : Font                  = null
    @FXML
    private var x2 : Color                 = null
    @FXML
    private var x3 : Font                  = null 
    @FXML
    private var x4 : Color                 = null 
    @FXML
    private var inbed : AnchorPane         = null
    @FXML
    var mnuRecent : Menu                   = null
    @FXML
    private var detail : AnchorPane         = null
    @FXML
    private var classMembersPane : AnchorPane = null

    @FXML
    def onAddTestClick( event : ActionEvent )  =
        ()
    
    @FXML
    def onButtonClick( event : ActionEvent )  =
        ()

    @FXML
    def onNewClick( event : ActionEvent )  =
        ()

    def openJarFile( file : File ) : Unit =
        if file != null then
            val pathToFile = Paths.get(file.getAbsolutePath()).getParent()
            // record as a recent opened file
            RunConfig.addFileNameLoaded(file.toPath().toString(), mnuRecent, openJarFile _)

            // get the classes form jar file
            val jarFile = JarFile(file)
            val jarEntries = jarFile.entries()

            urlClassloader = URLClassLoader.newInstance(
                Array[URL]( file.toURI.toURL ), 
                getClass().getClassLoader())

            Construct.getClass.clear()

            while(jarEntries.hasMoreElements())
                val jarEntry = jarEntries.nextElement()
                if jarEntry.getName().endsWith(".class") then

                    val className = jarEntry
                        .getName()
                        .replace(".class", "")
                        .replace("/",".")

                    val clazz = Class.forName(className, true, urlClassloader)
                    if classOf[VM].isAssignableFrom(clazz) then
                        Construct.getClass += {className -> clazz}

                        // try load the associated *view.json files
                        val viewFileName = s"${className}.view.json"
                        val viewFilePathName = s"${pathToFile}/$viewFileName"
                        Load.loadView(clazz.getName(), viewFilePathName)

            classMemberList.clear()            
            classMemberList.classList ++= Construct.getClass.keys
            classMemberList.classes.items(classMemberList.classList.sortWith( _ < _ ).toList*) 

            viewTree.clear()

    @FXML
    def onJarOpenClick( event : ActionEvent )  =
        // get jar file
        val fileChooser = FileChooser()
        fileChooser.setTitle("Open Jar File")
        fileChooser.getExtensionFilters().addAll(
                FileChooser.ExtensionFilter("Open View Model Jar File", "*.jar"))

        val file = fileChooser.showOpenDialog(inbed.getScene().getWindow())
        
        openJarFile(file)    

    @FXML
    def onSaveClick( event : ActionEvent )  = 
        val fileName = 
            if RunConfig.currentViewEdited.isEmpty then 
                val fileChooser = FileChooser()
                fileChooser.setTitle("Save View File")
                fileChooser.setInitialFileName(viewTree.tree.treeName)
                fileChooser.getExtensionFilters().addAll(
                        FileChooser.ExtensionFilter("View Files", "*.view.json"))
                fileChooser.showSaveDialog(inbed.getScene().getWindow())
            else 
                File(RunConfig.currentViewEdited)
        if fileName != null then
            Files.writeString(fileName.toPath(), viewTree.tree.asJSON())
            RunConfig.currentViewEdited = fileName.toPath().toString()

    @FXML
    def onSaveAsClick( event : ActionEvent )  = 
        ()        

    @FXML
    def onCloseClick( event : ActionEvent )  = 
        RunConfig.currentViewEdited = ""
        ()        

    @FXML
    def onQuitClick( event : ActionEvent )  = 
        Platform.exit()

    @FXML
    def onNewFormClick( event : ActionEvent )  =
        val selectedBeanName = classMemberList.classes.get()
        if selectedBeanName != null then
            val clazz = Class.forName(selectedBeanName, true, urlClassloader)
            val bean = clazz.getConstructor().newInstance()
            // the view.json file must be available before this is called.
            val form = FXForm(bean)

            val loader = FXMLLoader()
            loader.setLocation(getClass().getResource("/form_detail.fxml"))
            val root = loader.load().asInstanceOf[javafx.scene.layout.AnchorPane]
            val scene = Scene(root)
            val stage = Stage()
            stage.setScene(scene)

            val controller = loader.getController[ControllerDetail]()
            controller.insert(form)

            stage.initModality(Modality.APPLICATION_MODAL)
            stage.showAndWait()

    @FXML
    def initialize() =
        val form = FXForm(classMemberList)

        AnchorPane.setTopAnchor(form,0)
        AnchorPane.setBottomAnchor(form,0)
        AnchorPane.setLeftAnchor(form,0)
        AnchorPane.setRightAnchor(form,0)

        var node = form
        val table = classMemberList.members.getControl()(0).asInstanceOf[TableView[_]]

        classMembersPane.getChildren().add( node )

        val treeform = FXForm(viewTree)
        val tree = viewTree.treeView

        AnchorPane.setTopAnchor(treeform,0)
        AnchorPane.setBottomAnchor(treeform,0)
        AnchorPane.setLeftAnchor(treeform,0)
        AnchorPane.setRightAnchor(treeform,0)

        inbed.getChildren().add( treeform )
        viewTree.tree.setDetailPane(detail)         

        classMemberList.dropTarget = Some(viewTree)
        classMemberList.controller = Some(this)
        viewTree.setRespondToVM(Some(classMemberList))

