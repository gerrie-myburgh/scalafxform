package org.gerrie.scalafxform.layout

import javafx.scene.*
import javafx.scene.control.*
import org.gerrie.scalafxform.nodes.*
import org.gerrie.scalafxform.util.*
import org.gerrie.scalafxform.viewmodel.*
import org.json.JSONObject

import scala.collection.*

/******************************************************************************
 * Group of nodes and the constrains that they are subjected to
 */
case class ControlGroup(
                        val control : mutable.ArrayBuffer[MV] = mutable.ArrayBuffer[MV]() 
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
         _treeNodeDetailForm=aTreeNodeDetailForm

/******************************************************************************
 * leaf node definition
 */
abstract class LeafNode extends AbsNode with JSONView

/******************************************************************************
 * Parent node definition, each parent node is a tree in itself
 */
abstract class Tree extends AbsNode with JSONView:
    val children : mutable.ListBuffer[Tree | LeafNode] = mutable.ListBuffer[Tree | LeafNode]()

