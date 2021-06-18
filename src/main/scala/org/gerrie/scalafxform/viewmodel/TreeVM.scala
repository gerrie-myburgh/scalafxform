package org.gerrie.scalafxform.viewmodel

import org.gerrie.scalafxform.util.*
import org.gerrie.scalafxform.nodes.*
import org.gerrie.scalafxform.layout.*
import org.gerrie.scalafxform.viewmodel.*
import scala.collection.*
import javafx.scene.control.*
import javafx.scene.control.cell.*
import scala.beans.*
import javafx.scene.control.*
import javafx.scene.input.*
import javafx.scene.text.*
import javafx.util.*
import javafx.beans.property.*

import controller.*
import javafx.collections.ObservableList
import javafx.beans.value.ObservableObjectValue
import org.tbee.javafx.scene.layout.MigPane

import org.tbee.javafx.scene.layout.*

/**************************************************************************
 * Wrapper for the select tree
 */
private class DisplayComponentTree(var respondToVM : Option[DisplayClassMembersListVM]) extends DisplayTree(None):

    /**************************************************************************
     * get the tree item having the field Name, note that the field names is
     * a set of names. The field in this case is retreived from the list of
     * tree ietms
     */
    override def getTreeItem(fieldName : String, theChild : Tree | LeafNode) = 
        dirty = true
        if respondToVM.isDefined then
            respondToVM.get.getTreeItem(fieldName)
        else
            TreeItem(theChild)

    /**************************************************************************
     * remove the current selected item from the tree and inform the assosiated 
     * list that this item will be returned to the list as unused
     * canot remove the rott of the tree
     */
    override def delete() = 
        val itemToRemove = getSelection()  
        if itemToRemove.isDefined && respondToVM.isDefined && 
            itemToRemove.get.getParent() != null && 
            respondToVM.get.isDroppedItem(itemToRemove.get) then
            itemToRemove.get.getParent().getChildren().remove(itemToRemove)
            respondToVM.get.removedFromTarget(itemToRemove.get)
            getTree().refresh()
            // align the tree with the treeview layout
            alignTree(getTree().getRoot())

        else if itemToRemove != null && itemToRemove.get.getParent() != null then
            super.delete()


/**************************************************************************
 * The view model of the tree (Component select tree)
 */
class DisplayTreeViewComponentsVM(private var respondToVM : Option[DisplayClassMembersListVM] = None, 
                                 var controller : Option[ControllerMain] = None) extends VM with Logs:

    var tree      = DisplayComponentTree(respondToVM)
    val treeView  = tree.getTree()
    
    override def getFieldName() = ""

    def makeCopy() = DisplayTreeViewComponentsVM()

    /**************************************************************************
     * clear tree view of all items
     */
    def clear() =
        tree.clear()
    /**************************************************************************
     * The is a settor as the tree respond to VM must also be set when this
     * instance respondToVm is set  
     */
    def setRespondToVM( value : Option[DisplayClassMembersListVM]) =
        respondToVM = value
        tree.respondToVM = value

    /**************************************************************************
     * Create a cell factory for tree. The cells must display the
     * toString of the items in the cell.
     * The drag and drop methods is also set with lambdas
     */
    treeView.setCellFactory(param => new TreeCell[Tree | LeafNode]() {
        override def updateItem(item : Tree | LeafNode, empty : Boolean) = {
            super.updateItem(item, empty)
            if (empty) {
                setText("")
                setGraphic(null)
            } else {
                setText(item.toString())
            }
        }
       
        this.setOnDragDetected(
             event  => {
                dragDetected(event, this, treeView)
            }
        )
        this.setOnDragDropped(
             event  => {
                drop(event, this, treeView)
            }
        )
        this.setOnDragOver(
             event  => {
                dragOver(event, this, treeView)
            }
        )
    })

    var draggedItem     : Option[TreeItem[Tree | LeafNode]] = null
    var draggedFormBean : Option[VM]                     = None
    var dropZone        : TreeCell[Tree | LeafNode]         = null

    /**************************************************************************
     * If a drag is detected then make sure that the item that can be dropped is
     * a valid instance of a draggedFormBean.
     */
    private def dragDetected( event : MouseEvent, treeCell : TreeCell[Tree | LeafNode], treeView :  TreeView[Tree | LeafNode]) = 
        draggedItem = Some(treeCell.getTreeItem())
        // form bean cannot be serialised
        draggedFormBean = draggedItem.get.getValue().treeNodeDetailForm
        draggedItem.get.getValue().treeNodeDetailForm = None

        // note that the root can't be dragged
        if (draggedItem.isDefined && draggedItem.get.getParent() != null) then
            val db = treeCell.startDragAndDrop(TransferMode.MOVE)

            val content = new ClipboardContent()
            content.put(Single.SERIALIZED_MIME_TYPE, draggedItem.get.getValue())
            db.setContent(content)
            db.setDragView(treeCell.snapshot(null, null))
            event.consume()
    

    /**************************************************************************
     * drop dragged node in new location
     */
    private def drop(event : DragEvent ,  treeCell : TreeCell[Tree | LeafNode],  treeView : TreeView[Tree | LeafNode]) = 

        val db = event.getDragboard()
        var success = false
        if (db.hasContent(Single.SERIALIZED_MIME_TYPE)) then
            val thisItem = treeCell.getTreeItem()
            val droppedItemParent = draggedItem.get.getParent()

            // remove from previous location
            if droppedItemParent != null then
                droppedItemParent.getChildren().remove(draggedItem.get)

            // dropping on parent node makes it the first child
            tree.dirty = true
            if (thisItem.getValue().isInstanceOf[Tree]) then
                thisItem.getChildren().add(0, draggedItem.get)
                treeView.getSelectionModel().select(draggedItem.get)
                tree.alignTree(tree.getRoot(thisItem))
            else 
                // add to new location
               
                if thisItem.getParent() == null then
                    val indexInParent = thisItem.getChildren().indexOf(thisItem)
                    thisItem.getChildren().add(indexInParent + 1, draggedItem.get)
                    tree.alignTree(tree.getRoot(thisItem))
                else 
                    val indexInParent = thisItem.getParent().getChildren().indexOf(thisItem)
                    thisItem.getParent().getChildren().add(indexInParent + 1, draggedItem.get)
                    tree.alignTree(tree.getRoot(thisItem))

            if respondToVM.isDefined then
                respondToVM.get.droppedAtTarget(draggedItem.get)

            draggedItem.get.getValue().treeNodeDetailForm = draggedFormBean
            treeView.getSelectionModel().select(draggedItem.get)
            event.setDropCompleted(success)
            event.getDragboard().clear()
            clearDropLocation();
    
    /**************************************************************************
     * Set the drop style if the dragged ietm is defined and the thisItem is not
     * null and the dragged item is not been dropped on itself.
     */
    private def dragOver(event : DragEvent ,  treeCell : TreeCell[Tree | LeafNode],  treeView : TreeView[Tree | LeafNode]) =
        if (event.getDragboard().hasContent(Single.SERIALIZED_MIME_TYPE)) then
            val thisItem = treeCell.getTreeItem()

            // can't drop on itself
            if !(draggedItem.isEmpty || thisItem == null || thisItem == draggedItem.get ) then
            // ignore if this is the root
                event.acceptTransferModes(TransferMode.MOVE)
                if (!AnyRef.equals(dropZone, treeCell)) then
                    clearDropLocation()
                    this.dropZone = treeCell
                    dropZone.setStyle(Single.DROP_HINT_STYLE)

    /**************************************************************************
     * Set dropzone stye to nothing
     */
    private def clearDropLocation() =
        if (dropZone != null) dropZone.setStyle("")

