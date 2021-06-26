package org.gerrie.scalafxform.nodes

import scala.collection.*
import org.gerrie.scalafxform.nodes.*
import org.gerrie.scalafxform.util.*
import org.gerrie.scalafxform.viewmodel.*
import org.gerrie.scalafxform.layout.*
import javafx.scene.control.*
import javafx.scene.*
import javafx.scene.layout.*
import org.json.JSONObject

/******************************************************************************
 * Hierarchy of JSON definition of a view
 */
class TreeViewNodeView() extends Tree with Logs:

    val errMsg = "treeNodeDetailForm is not defined in TreeViewNodeView"

    /************************************************************************** 
     * get type  from tree node detail form
     */ 
    def getType() = 
        if treeNodeDetailForm != null && treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayTreeViewNodeVM].`type`.get()
        else
            error( errMsg )
            throw  Exception(errMsg) 
    /************************************************************************** 
     * get label from tree node detail form
     */ 
    def getLabel() = 
        if treeNodeDetailForm != null && treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayTreeViewNodeVM].label.get()
        else
            error( errMsg )
            throw  Exception(errMsg) 
    /************************************************************************** 
     * getlayout from tree node detail form
     */ 
    def getLayout() : String = 
        if treeNodeDetailForm != null && treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayTreeViewNodeVM].layout.get()
        else
            error( errMsg )
            throw  Exception(errMsg) 
    /************************************************************************** 
     * set type in tree node detail form
     */ 
    def setType(value : String) = 
        if treeNodeDetailForm != null && treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayTreeViewNodeVM].`type`.set(value)
        else
            error( errMsg )
            throw  Exception(errMsg) 
    /************************************************************************** 
     * set label in tree node detail form
     */ 
    def setLabel(value : String) = 
        if treeNodeDetailForm != null && treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayTreeViewNodeVM].label.set(value)
        else
            error( errMsg )
            throw  Exception(errMsg) 
    /************************************************************************** 
     * set layout in tree node detail form
     */ 
    def setLayout(value : String) = 
        if treeNodeDetailForm != null && treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayTreeViewNodeVM].layout.set(value)
        else
            error( errMsg )
            throw  Exception(errMsg) 
    /************************************************************************** 
     * save all children to JSON string
     */ 
    private def childrenToJSON() = children.map(c =>c.toJSONStr()).mkString(",\n")

    /************************************************************************** 
     * save self to JSON string
     */ 
    def toJSONStr() = s"""{"node" :{"type":"${getType()}","label":"${getLabel()}","layout":"${getLayout()}","children":[\n${childrenToJSON()}]}}"""  
    /************************************************************************** 
     * read self from JSON string
     */ 
    def fromJSON(json : JSONObject) =
        setType(json.getString("type"))
        setLabel(json.getString("label"))
        setLayout(json.getString("layout"))

    /************************************************************************** 
     * Get panel child instance defined by the TreeViewNodeView
     */ 
    def getNode(parentPane : Option[Pane],layout: String) : TreePane =
        MIGPane(layout,  parentPane)

    /************************************************************************** 
     * to string override
     */ 
    override def toString() = 
        s"${getType()}"
