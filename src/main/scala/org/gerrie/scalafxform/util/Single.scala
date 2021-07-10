package org.gerrie.scalafxform.util

import javafx.scene.input.*
import javax.validation.Validation
import javax.validation.constraints.* 
import javafx.scene.control.* 
import javafx.event.* 

import scala.collection.*
import scala.util.Using

import java.io.*
import java.util.Properties
import java.nio.file.*

/******************************************************************************
 * Singleton containing constants
 */
object Single:
    val SERIALIZED_MIME_TYPE = DataFormat("application/x-java-serialized-object")
    val DROP_HINT_STYLE = "-fx-border-color: #eea82f; -fx-border-width: 0 0 2 0; -fx-padding: 3 3 1 3"

//    val validator = Validation.buildDefaultValidatorFactory().getValidator()

/******************************************************************************
 * current runtime configuration variables
 */
object RunConfig:
    var currentPathForSaving    = ""
    var currentViewEdited       = "" 
    private val recentOpenFiles = mutable.ArrayBuffer[String]()

    val filePath =  getClass()
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation() //.toURI
                    .getPath()

    val configFilePath = """D:\Work\metals-dotty\scalafxform\src\main\resources\config.properties"""

    /************************************************************************** 
     * add the file name that have been loaded to the list of names.
     */
    def addFileNameLoaded(fileName : String, menu : Menu, action : (file : File) => Unit) = 
        if !recentOpenFiles.contains(fileName) then
            val menuItem = MenuItem(fileName)
            menu.getItems().add(menuItem)

            menuItem.setOnAction( e => 
                val file = File(e.getSource().asInstanceOf[MenuItem].getText())
                action(file) 
            )

            recentOpenFiles += fileName

    /************************************************************************** 
     * save current system state to config file
     */
    def storeConfig() = 
        // save recent files loaded if changed
        Using(FileOutputStream(configFilePath)) { output =>
            val prop = Properties()
            val fileNames = recentOpenFiles.mkString(",")
            prop.setProperty("files.recent", fileNames)    
            prop.store(output, null)
        }
        
    /************************************************************************** 
     * load current system state from config file
     */
    def loadConfig(menu : Menu, action : (file : File) => Unit) = 
        Using(FileInputStream(configFilePath)) { input =>
            val prop = Properties()
            prop.load(input)
            val fileNames = prop.getProperty("files.recent")
            fileNames
            .split(",")
            .filter(s => s.length > 0)    
            .foreach(fileName => addFileNameLoaded(fileName, menu, action))
        } 
