package org.gerrie.scalafxform.util

import javafx.beans.*
import javafx.scene.*
import javafx.scene.control.*
import javafx.scene.layout.*
import org.apache.log4j.*
import org.gerrie.scalafxform.layout.*
import org.gerrie.scalafxform.nodes.*
import org.json.JSONObject 

/******************************************************************************
 * base of all constraits
 */
trait Constraint

/******************************************************************************
 * base of all view models. This is for a vm that does not have a label, 
 * field or layout
 */
trait VM extends Serializable:
    /** Make a copy of the VM
     */
    def makeCopy() : VM
    /** Get field name of the VM
     */
    def getFieldName() : String

/******************************************************************************
 * base of all form vieww models
 */
trait FormVM extends VM:
    val label = DisplayText("")
    // TODO investigate if the field should not move to VM.
    val field = DisplayLabel("")
    val layout = DisplayText("")
    /** copy from the form then values to this
     */
    def copy(from : FormVM) =
        field.set( from.field.get() )
        label.set( from.label.get() )
        layout.set( from.layout.get() )


/******************************************************************************
 * base of panes onto which Nodes can be loaded on
 */
trait TreePane:
    /** 
     * add node with parameters to the pane
     */
    def add(c : Node, param : String) : Unit
    /** 
     * add a nested pane - TODO check that as I am not passing the param string for the pane
     */
    def add(p : TreePane) : Unit
    /** 
     * get instance of the pace been used
     */
    def getPane() : Pane
    /**
     * enable / disable the pane for user events
     */
    def disable(value : Boolean) = ()
        
/******************************************************************************
 * A view that loads and save from and to a json string
 */
trait JSONView:
    def toJSONStr() : String
    def fromJSON(json : JSONObject) : Unit    

/******************************************************************************
 * components
 */
trait LeafComponent extends LeafNode:
    def getLabel() : String
    def getField() : String
    def getLayout() : String

/******************************************************************************
 * base trait of all components that can be displayed on a pane. The Model View
 */
trait MV:
    def getControl() : List[Node]
    def addTo(pane : TreePane, viewModel : LeafComponent) : Unit
/******************************************************************************
 * logger trait
 * https://stackoverflow.com/questions/2018528/logging-in-scala
 */
trait Logs:
    private[this] val logger = Logger.getLogger(getClass().getName());

    import org.apache.log4j.Level.* 

    def debug(message: => String) = if (logger.isEnabledFor(DEBUG)) logger.debug(message)
    def debug(message: => String, ex:Throwable) = if (logger.isEnabledFor(DEBUG)) logger.debug(message,ex)
    def debugValue[T](valueName: String, value: => T):T =
        val result:T = value
        debug(valueName + " == " + result.toString)
        result

    def info(message: => String) = if (logger.isEnabledFor(INFO)) logger.info(message)
    def info(message: => String, ex:Throwable) = if (logger.isEnabledFor(INFO)) logger.info(message,ex)

    def warn(message: => String) = if (logger.isEnabledFor(WARN)) logger.warn(message)
    def warn(message: => String, ex:Throwable) = if (logger.isEnabledFor(WARN)) logger.warn(message,ex)

    def error(ex:Throwable) = if (logger.isEnabledFor(ERROR)) logger.error(ex.toString,ex)
    def error(message: => String) = if (logger.isEnabledFor(ERROR)) logger.error(message)
    def error(message: => String, ex:Throwable) = if (logger.isEnabledFor(ERROR)) logger.error(message,ex)

    def fatal(ex:Throwable) = if (logger.isEnabledFor(FATAL)) logger.fatal(ex.toString,ex)
    def fatal(message: => String) = if (logger.isEnabledFor(FATAL)) logger.fatal(message)
    def fatal(message: => String, ex:Throwable) = if (logger.isEnabledFor(FATAL)) logger.fatal(message,ex)

