package org.gerrie.scalafxform.fxform

import java.lang.reflect.*
import scala.collection.*
import javafx.scene.control.*
import javafx.beans.value.*
import javafx.scene.layout.*
import javafx.scene.*

import org.gerrie.scalafxform.layout.*
import org.gerrie.scalafxform.nodes.*
import org.gerrie.scalafxform.util.*
/****************************************************************************** 
 * companion object tot he FXForm class
 */
object FXForm:
    def apply[T](model : T) = new FXForm(model).getForm()


/****************************************************************************** 
 * used to represent the form. 
 */
case class FXForm[T](model : T) extends Logs:

    // map of control names -> list of nodes that the control has
    private val fieldControls = mutable.HashMap[String, mutable.ArrayBuffer[ControlGroup]]()

    // name of model that this form is been constructed for
    private var modelName = ""

    /************************************************************************** 
     * get the constructed form by
     * 1 get elements of form
     * 2 construct the form
     */
    def getForm() =
        getFormElements() 
        constuctForm()

    /************************************************************************** 
     * construct the form by getting the layout to 
     * format model field controls along with the 
     * constraint on the nodes
     */
    private def constuctForm() : Node = 
        Layout.format(modelName, fieldControls.toMap)

    /************************************************************************** 
     * Get the field components of the model and if
     * the field is a MV component then get the 
     * controls of the component
     */
    private def getFormElements() = 
        val clazz = model.getClass
        modelName = clazz.getName()

        info(s"Create form for ${modelName}.")

        val methodArray = clazz.getMethods
        val fieldArray  = clazz.getDeclaredFields

        for (field <- fieldArray)
            val name = field.getName

            val `type` = field.getGenericType.getTypeName
            val accessor = methodArray.find(p => p.getName.equals(name))
            if accessor.isDefined then
                val themodel = accessor.get.invoke(model)
                if themodel.isInstanceOf[MV] then

                    info(s"Field called ${name} have been created.")

                    val prop = themodel.asInstanceOf[MV]
                    val fieldControl = ControlGroup()

                    fieldControl.control += prop
                    val controls = mutable.ArrayBuffer[ControlGroup] ()
                    controls += fieldControl
                    fieldControls += name -> controls
