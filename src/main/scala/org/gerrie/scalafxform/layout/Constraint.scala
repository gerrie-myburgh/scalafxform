package org.gerrie.scalafxform.layout

import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.layout.*
import org.gerrie.scalafxform.util.*

/****************************************************************************** 
 * group of radio butons that is constrained insofar that only one can be active
 * at a given time 
 */
class RadioButtonGroup extends Constraint with Logs:
    val toggleGroup = ToggleGroup()
    /**
     * All children of the node must be radio buttons and be 
     * placed into a toggle group
     */
    def this(pane : TreePane) =    
        this()

        info(s"Place radio buttons in toggle group.")
        
        pane.getPane().getChildren().forEach(child =>
            if child.isInstanceOf[RadioButton] then
                val rb = child.asInstanceOf[RadioButton]
                rb.setToggleGroup(toggleGroup)
        )

/****************************************************************************** 
 * group of check boxes that is constrained insofar that any combination
 * can be active at one time. 
 */
case class CheckBoxGroup() extends Constraint
