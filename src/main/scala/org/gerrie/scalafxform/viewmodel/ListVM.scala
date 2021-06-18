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
import java.util.Observable
import javafx.beans.value.ObservableObjectValue
import javafx.scene.control.Alert.AlertType


/******************************************************************************
 * member that is inserted into the tableview
 */ 
private class Member( @BeanProperty var name  : DisplayText = new DisplayText(), 
              @BeanProperty var `type` : DisplayText = new DisplayText()) extends VM:

    override def getFieldName() = name.get()

    var usedInTree = false
    def makeCopy() = Member()


/******************************************************************************
 * wrapper for the the list view (table view)
 */ 
private class DisplayMemberList(instances : () => Member, default : Member*) extends DisplayList[Member](instances, default*):

    /******************************************************************************
    * Set the columns for the table view . Note that the cell 
    * contects are Text Nodes that can be formatted to strikethrough
    */ 
    override def values(defaults : Member*) =
        getTable().getColumns().clear()
        if defaults.length > 0 then
            val colName = TableColumn[Member, Text]("Name")
            colName.setCellValueFactory(callBack =>
                new SimpleObjectProperty(
                        {
                            val text = Text() 
                            text.setText(callBack.getValue().name.get())
                            if callBack.getValue().usedInTree then
                                text.setStrikethrough(true)
                            text
                        }
                )
            )
            getTable().getColumns().add(colName)
            val colType= TableColumn[Member, Text]("Type")
            colType.setCellValueFactory(callBack =>
                new SimpleObjectProperty(
                        {
                            val text = Text() 
                            text.setText(callBack.getValue().`type`.get())
                            if callBack.getValue().usedInTree then
                                text.setStrikethrough(true)
                            text
                        }
                )
            )
            getTable().getColumns().add(colType)
        list.addAll(defaults*)
        getTable().setItems(list)

/******************************************************************************
 * The compount comonent that is a view model This compount has a combo box and 
 * select list (member select list). The combobox displays member items
 * This class may be related to the main controller and a drop target.
 * The drop target is the destination Node in when Drag and Drop is used
 */ 
class DisplayClassMembersListVM( var dropTarget : Option[DisplayTreeViewComponentsVM] = None, 
                                var controller : Option[ControllerMain] = None) extends VM with Logs:

    val classes         = DisplayComboBox[String]() 
    val members         = DisplayMemberList( () => Member() )   
    val classList       = mutable.ArrayBuffer[String]()
    val droppableItems  = mutable.HashMap[Integer, TreeItem[Tree | LeafNode]]()
    val droppedItems    = mutable.HashSet[TreeItem[Tree | LeafNode]]()

    override def getFieldName() = ""

    def makeCopy() = DisplayClassMembersListVM()

    def clear() = 
        members.clear()
        classes.clear()
        classList.clear()

    /******************************************************************************
    * find fieldName in the droppable item list 
    */ 
    def getTreeItem(fieldName : String) = 
        val item = droppableItems.find(item => 
                val value = item 
                    ._2
                    .getValue()
                    .field
                    .equals(fieldName)
                value)
                
        if item.isDefined then 
            val droppedItem = item.get._2
            droppedAtTarget(droppedItem)
            droppedItem
        else
            val msg = s"$fieldName not found in droppableItems."
            error( msg )
            throw Exception(msg) 
            null
    /******************************************************************************
    * is the itrritem a item that can be dropped from this component
    */ 
    def isDroppedItem(drop : TreeItem[Tree | LeafNode]) =
        droppableItems.find(item => item._2== drop).isDefined

    /******************************************************************************
    * if the drop item is a item that can be dropped from this list then
    * set it's state
    */ 
    def setDroppedMemberState(drop : TreeItem[Tree | LeafNode], state : Boolean) =
        if isDroppedItem(drop) then
            val index = droppableItems.find(item => item._2== drop).get._1
            members.getTable().getItems().get(index).usedInTree = state
            members.getTable().refresh()

    /******************************************************************************
    * If thie drop is at some target locatio then set the drop status to true
    */ 
    def droppedAtTarget(drop : TreeItem[Tree | LeafNode]) =
        if !droppedItems.contains(drop) then
            setDroppedMemberState(drop, true)
            droppedItems += drop

    /******************************************************************************
    * the drop item has been removed from the drop targe so set its status to false
    */ 
    def removedFromTarget(drop : TreeItem[Tree | LeafNode]) =
        if droppedItems.contains(drop) then
            setDroppedMemberState(drop, false)
            droppedItems.remove(drop)

    /******************************************************************************
    * listener for state change of combo box class items
    */ 
    info(s"Add listner to ListVM.classes.")

    classes.getControl()(0)
        .asInstanceOf[ComboBox[String]]
        .valueProperty()
        .addListener(( o, t1, t2 ) => 
            // if the tree is diry the inform the user that this is the case
            if dropTarget.isDefined && dropTarget.get.tree.dirty then 
                val alert = Alert(AlertType.CONFIRMATION)
                alert.setContentText("""View layout has changed. Do you want to save?""")
                val result = alert.showAndWait()
                dropTarget.get.tree.dirty = false
                
                if result.get == ButtonType.OK && controller.isDefined then
                    controller.get.onSaveClick( null )
                end if

            val className = t2
            if className != null then
                // if is a new drop target so item have been dragged and dropped 
                droppableItems.clear()
                val clazz = Construct.getClass(className)
                val fieldArray  = clazz.getDeclaredFields
                members.getControl()(0).asInstanceOf[TableView[Member]].getItems().clear()
                var count = 0
                val fieldMembers = fieldArray
                    .filter(field => classOf[MV].isAssignableFrom(field.getType) ||
                                     classOf[VM].isAssignableFrom(field.getType))
                    .map(field=>
                        // TODO to load a VM node from the class requires some thought.... get back to this.

                        // create a tree item for every selectable item
                        // do not create a form bean for the node as this 
                        // will come from the target tree
                        val treeItem = TreeItem[Tree | LeafNode]()
                        droppableItems(count) = treeItem
                        val fieldName = field.getType().getName().replace("/",".")
                        val beanVM = fieldName.split("""\.""").last
                        val beanName = field.getName
                        val view = Construct.newTreeNode(beanVM)()

                        // attach form bean to the leaf or tree node
                        val fromBeanClassName = fieldName.split("""\.""").dropRight(1).mkString(".")
                        val beanClassName = Construct.mvToVMMap(fromBeanClassName)(beanVM)

                        view.formBeanName = fieldName
                        view.field = field.getName()

                        treeItem.setValue(view)
                        count += 1

                        // create a member instance that is to go into members list
                        Member(DisplayText(field.getName), DisplayText(field.getType.getName)))
                    .toList

                members.values(fieldMembers*)
                members.refresh()

                // see if the view exist then populate the tree view with correct json
                // else load default new json
                if !Load.views.contains(className) then
                    Load.load(className, 
                        """{
                           |    "node": {
                           |    "id": "LAYOUT",
                           |    "type": "MIGLAYOUT",
                           |    "label": "GROUP",
                           |    "layout":"wrap 2",
                           |    "children": []
                           |    }
                           }""".stripMargin)

                // if the drop target is defined then have it populate itself with the selected tree
                // of fields
                if dropTarget.isDefined then 
                    dropTarget.get.tree.populateTreeView(
                        dropTarget.get.tree.getTree(), 
                        Load.views(className)
                        )
                    dropTarget.get.tree.dirty = false    
                    dropTarget.get.tree.treeName = className
                    dropTarget.get.tree.populateForms()


        )
        
    /******************************************************************************
    * inititate drag and drop 
    */ 
    info(s"Create row factory Drag and Drop for ListVM.members.")

    members.getControl()(0).asInstanceOf[TableView[Member]].setRowFactory(tv =>
        val row = new TableRow[Member]()
        row.setOnDragDetected(event =>
            val index = row.getIndex()
            // make sure the target exist and there are items available to be dropped
            if dropTarget.isDefined && !droppedItems.contains(droppableItems(index)) then
                dropTarget.get.draggedItem = Some(droppableItems(index))
                dropTarget.get.draggedFormBean = droppableItems(index).getValue().treeNodeDetailForm
                droppableItems(index).getValue().treeNodeDetailForm = None
                
                val db = row.startDragAndDrop(TransferMode.MOVE)
                val content = ClipboardContent()
                content.put(Single.SERIALIZED_MIME_TYPE, dropTarget.get.draggedItem.get.getValue())
                db.setContent(content)
            else
                dropTarget.get.draggedItem = None
                dropTarget.get.draggedFormBean = null
            event.consume()
        )
        row
    )        
