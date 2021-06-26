package org.gerrie.scalafxform.layout

import scala.collection.*
import org.gerrie.scalafxform.nodes.*
import org.gerrie.scalafxform.util.*
import org.gerrie.scalafxform.viewmodel.*
import javafx.scene.control.*
import javafx.scene.*

import org.json.JSONObject

/******************************************************************************
 * Group of nodes and the constrains that they are subjected to
 */
case class ControlGroup(
                        val control : mutable.ArrayBuffer[Node | ControlGroup] = mutable.ArrayBuffer[Node | ControlGroup]() 
                        )


/******************************************************************************
 * Hierarchy of a general tree defined in JSON
 */
class AbsNode:
    var formBeanName = ""
    var field        = ""
    private var _treeNodeDetailForm : Option[VM] = None
    var parent : Option[AbsNode] = None

    def treeNodeDetailForm = _treeNodeDetailForm
    def treeNodeDetailForm_=(aTreeNodeDetailForm : Option[VM]) = 
        if this.getClass().getName().equals("org.gerrie.scalafxform.nodes.DisplayTextView")
           &&  aTreeNodeDetailForm.get.getClass().getName().equals("org.gerrie.scalafxform.viewmodel.DisplayRadioGroupVM") then
            println("STOP...INCORRECT...")
        _treeNodeDetailForm=aTreeNodeDetailForm

/******************************************************************************
 * leaf node definition
 */
abstract class LeafNode extends AbsNode with JSONView

/******************************************************************************
 * Parent node definition, each parent node is a tree in itself
 */
abstract class Tree extends AbsNode with JSONView:
    import scala.language.postfixOps
    val children : mutable.ListBuffer[Tree | LeafNode] = mutable.ListBuffer[Tree | LeafNode]()

