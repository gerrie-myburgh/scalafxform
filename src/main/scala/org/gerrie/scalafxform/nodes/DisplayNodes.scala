package org.gerrie.scalafxform.nodes

import javafx.beans.property.*
import javafx.util.*
import javafx.scene.control.*
import javafx.collections.*
import javafx.stage.*
import javafx.fxml.*
import javafx.scene.*
import javafx.scene.layout.*
import javafx.scene.text.Text;
import javafx.scene.input.*

import org.gerrie.scalafxform.util.*
import org.gerrie.scalafxform.layout.*
import org.gerrie.scalafxform.fxform.FXForm
import org.gerrie.scalafxform.controller.*
import org.gerrie.scalafxform.viewmodel.*

import javafx.beans.Observable
import javafx.scene.control.cell.PropertyValueFactory

import scala.collection.*

import controller.*

import org.json.JSONObject
import scala.annotation.meta.beanSetter

/******************************************************************************
 * The following classes are all wrappers for display components
 */

/******************************************************************************
 * MV tree of object 
 */
class DisplayTree() extends MV: 

    private var control         : AnchorPane        = null
    private val tree                                = TreeView[Tree | LeafNode]()
    private var theDetailPane   : Option[Pane]      = None
    var treeName                                    = ""
    var dirty                                       = false

    /************************************************************************** 
     * add change listmer to tree. In this case the listner will display the
     * associated form iff the detail pane is defined
     */ 
    tree.getSelectionModel().selectedItemProperty().addListener( (o,t0,t1) => 
        if t1 != null && theDetailPane.isDefined then
            theDetailPane.get.getChildren().clear()
            val treeNodeDetailForm = t1.getValue().asInstanceOf[AbsNode].treeNodeDetailForm
            if treeNodeDetailForm.isDefined && theDetailPane.isDefined then
                val form = FXForm(treeNodeDetailForm.get)
                form.setDisable(true)
                theDetailPane.get.getChildren().add(form)

    )

    /************************************************************************** 
     * constructor
     * if the tree is defined then build the populate the treeview
     */ 
    def this(treeRoot : Option[Tree]) =
        this()
        if treeRoot.isDefined then
            populateTreeView(tree, treeRoot.get)

        // create component from fxml and add this as the control
        val loader = FXMLLoader()
        loader.setLocation(getClass().getResource("/form_tableview.fxml"))
        control = loader.load().asInstanceOf[javafx.scene.layout.AnchorPane]

        // anchor control to grow with parent
        AnchorPane.setTopAnchor(control,0)
        AnchorPane.setBottomAnchor(control,0)
        AnchorPane.setLeftAnchor(control,0)
        AnchorPane.setRightAnchor(control,0)

        AnchorPane.setTopAnchor(tree,0)
        AnchorPane.setBottomAnchor(tree,40)
        AnchorPane.setLeftAnchor(tree,0)
        AnchorPane.setRightAnchor(tree,0)

        val controller = loader.getController[ControllerTableView]()

        controller.anchorpane.getChildren().add(0, tree)

        controller.append = Some(append)
        controller.insert = Some(insert)
        controller.edit   = Some(edit)
        controller.delete = Some(delete)

    /************************************************************************** 
     * populate the forms attached to the Tree and LeafNodes
     */ 
    def populateForms() =

        def populateChild(child : Tree | LeafNode, parent : TreeItem[Tree | LeafNode]) : Unit = 
            child match
            case c if c.isInstanceOf[LeafNode] => 
                val theChild = c.asInstanceOf[LeafComponent]
                val treeNodeDetailForm = theChild.treeNodeDetailForm.get.asInstanceOf[FormVM]

            case c if c.isInstanceOf[Tree] => 
                val theChild = c.asInstanceOf[Tree]
                val localParent = TreeItem(theChild)
                theChild.children.foreach(c => populateChild(c, localParent.asInstanceOf[TreeItem[Tree | LeafNode]]))

            case _ => println("BUGGERUP")

        tree.getRoot().getChildren().toArray().foreach(child => 
            val c = child.asInstanceOf[TreeItem[Tree | LeafNode]]
            populateChild(c.getValue(), c))

    /************************************************************************** 
     * Populate the treeView with the tree definition
     */ 
    def populateTreeView(treeView :TreeView[ Tree | LeafNode ], treeDef : Tree) =

        def addChild(child : Tree | LeafNode, parent : TreeItem[Tree | LeafNode]) : Unit = 
            child match
            case c if c.isInstanceOf[LeafNode] => 
                val theChild = c.asInstanceOf[LeafNode]
                val fieldName = theChild.treeNodeDetailForm.get.getFieldName()

                val newTreeItem = getTreeItem(fieldName, theChild)
                newTreeItem.getValue().treeNodeDetailForm = theChild.treeNodeDetailForm

                parent.getChildren().add(newTreeItem)
            case c if c.isInstanceOf[Tree] => 
                val theChild = c.asInstanceOf[Tree]
                val localParent = TreeItem(theChild)
                parent.getChildren().add(localParent.asInstanceOf[TreeItem[Tree | LeafNode]] )
                theChild.children.foreach(c => addChild(c, localParent.asInstanceOf[TreeItem[Tree | LeafNode]]))
            case _ => println("BUGGERUP")

        treeView.setRoot(TreeItem(treeDef))
        treeDef.children.foreach(child => addChild(child, treeView.getRoot()))

    /************************************************************************** 
     * Get the tree item. This is used get the tree item as identified by the
     * fieldName of by the child. In this case return a TreeItem of the child
     * value. In the case of overriding tree definition then get the
     * Tree node of Leaf node as defined by the fieldName
     */ 
    def getTreeItem(fieldName : String, theChild : Tree | LeafNode) = 
        TreeItem(theChild)

    /************************************************************************** 
     * Append to the children of the current selected nodes parent a node.
     * This node can either be a Tree node or Leaf node
     */ 
    def append() =
        val selection = getSelection()
        if selection.isDefined then
            val select = selection.get
            val parent = select.getParent()
            if parent != null then
                val child = Construct.newTreeNode("node")()
                child.formBeanName = Construct.mvToVMMap("org.gerrie.scalafxform.nodes")("DisplayTreeViewNode")
                child.treeNodeDetailForm = Some(Construct.newVMBean(child.formBeanName))

                val treeItem = TreeItem(child)
                parent.getChildren().add(treeItem)

    /************************************************************************** 
     * Isert a new ndode just below the current seelcted node. This node 
     * can either be a Tree node or a Leaf node
     */ 
    def insert() =
        val selection = getSelection()
        if selection.isDefined then
            val select = selection.get
            val parent = select.getParent()
            if parent != null then
                val index = parent.getChildren().indexOf(select)

                val child = Construct.newTreeNode("node")()
                child.formBeanName = Construct.mvToVMMap("org.gerrie.scalafxform.nodes")("DisplayTreeViewNode")
                child.treeNodeDetailForm = Some(Construct.newVMBean(child.formBeanName))

                val treeItem = TreeItem(child)
                parent.getChildren().add(index, treeItem)

    /************************************************************************** 
     * Edit detail of current selected node iff there is a tree node detail form
     * attached. If the detail pane is defined then display the node detail
     * on the detail pane else open dialog window and display it there
     */ 
    def edit() =
        // get form and controller
        val loader = FXMLLoader()
        loader.setLocation(getClass().getResource("/form_detail.fxml"))
        val root = loader.load().asInstanceOf[javafx.scene.layout.AnchorPane]

        val controller = loader.getController[ControllerDetail]()

        // make sure something is selected on the tree
        val isSelected = getSelection()
        val selected =
            if isSelected.isDefined && isSelected.get.getValue().treeNodeDetailForm.isEmpty then
                val beanName = Construct.mvNameToVMMap(getSelection().get.getValue().formBeanName)
                val bean = Construct.newVMBean(beanName)
                isSelected.get.getValue().treeNodeDetailForm = Some(bean)
                Some(bean)
            else
                if isSelected.isDefined then
                    Some(isSelected.get.getValue().treeNodeDetailForm.get)
                else
                    None

        if selected.isDefined then
            val copyBean = selected.get.makeCopy()
            val form = FXForm(copyBean)
            controller.insert(form)

            controller.apply = Some(apply)
            controller.cancel = Some(cancel)

            // display the detail of the node to user
            if theDetailPane.isDefined then
                theDetailPane.get.getChildren().clear
                theDetailPane.get.getChildren().add(root)
            else
                val scene = Scene(root)
                val stage = Stage()
                stage.setScene(scene)
                stage.initModality(Modality.APPLICATION_MODAL)
                stage.showAndWait()

            // callback bandlers. apply the changes to the detail form    
            def apply() = 
                if isSelected.isDefined then
                    isSelected.get.getValue().treeNodeDetailForm = Some(copyBean) 
                    root.setDisable( true )
                    alignTree(tree.getRoot())
                    tree.refresh()
                    dirty = true

            // callback handelers. cancel the current changes to the detail form
            def cancel() =
                val oldForm = FXForm(selected.get)
                controller.insert(oldForm)
                root.setDisable( true )
                tree.refresh()

    /************************************************************************** 
     * remove selected node from tree
     * This is done isn the inherited tree. this must be moved to this base tree.
     */ 
    def delete() = 
        val itemToRemove = getSelection()
        if itemToRemove.isDefined then 
            val remove = itemToRemove.get
            remove.getParent().getChildren().remove(remove)
            alignTree(tree.getRoot)
            tree.refresh()


    /************************************************************************** 
     * Get a list of the controls that is in this view
     */ 
    def getControl() =
        List(control)

    /************************************************************************** 
     * get the tree defined int the display
     */ 
    def getTree() = 
        tree
        
    /**************************************************************************
     * write tree out as a json string
     */ 
    def asJSON() =
        tree.getRoot()
            .getValue()
            .asInstanceOf[Tree]
            .toJSONStr()

    /************************************************************************** 
     * set current detail pane to use in case of there been a tree 
     * node detail form
     */ 
    def setDetailPane(detailPane : Pane) =
        theDetailPane = Some(detailPane)

    /************************************************************************** 
     * return the selected node as an Option
     */ 
    def getSelection() =
        val selectedItem = tree
            .getSelectionModel()
            .getSelectedItem()
        if selectedItem != null then
            Some(selectedItem)
        else 
            None

    /************************************************************************** 
     * Travel to root of list and return start
     */ 
    def getRoot(node : TreeItem[Tree | LeafNode]) :  TreeItem[Tree | LeafNode] =
        if node.getParent() != null then
            getRoot(node.getParent())
        else 
            node

    /************************************************************************** 
     *  align contained treeView def with the TreeItem and children
     */ 
    def alignTree(node : TreeItem[Tree | LeafNode]) : Unit =
        dirty = true
        if node.getValue().isInstanceOf[Tree] then
            val rootTreeNode = node.getValue().asInstanceOf[Tree]
            rootTreeNode.children.clear()
            node
                .getChildren()
                .toArray()
                .foreach(child => 
                    rootTreeNode.children += child.asInstanceOf[TreeItem[Tree | LeafNode]].getValue()
                    alignTree(child.asInstanceOf[TreeItem[Tree | LeafNode]])
                )
                 
    /************************************************************************** 
     *  remove all nodes from tree
     */ 
    def clear() =
        tree.setRoot( null )

/******************************************************************************
 * MV list of object 
 */
// TODO list status must be READ or READWRITE - check and fix this
class DisplayList[T <: VM]() extends MV: 

    private var control : AnchorPane = null
    private val table = TableView[T]()
    protected val list = FXCollections.observableArrayList[T]()
    private var getInstanceOfT : () => T = null
    private var mode = ListStatus.READ

    /************************************************************************** 
     * constructor.
     * The table in this case is places on an AnchorPane. This classbacks is
     * then set to local methods. They are
     * 1. append
     * 2. insert
     * 3. edit
     * 4. delete
     */
    def this(instanceOfT : () => T, defaults : T*) =
        this()
        getInstanceOfT = instanceOfT
        values(defaults*)

        // create component from fxml and add this as the control
        val loader = FXMLLoader()
        loader.setLocation(getClass().getResource("/form_tableview.fxml"))
        control = loader.load().asInstanceOf[javafx.scene.layout.AnchorPane]
        val controller = loader.getController[ControllerTableView]()
        controller.anchorpane.getChildren().add(0, table)

        controller.append = Some(append)
        controller.insert = Some(insert)
        controller.edit   = Some(edit)
        controller.delete = Some(delete)

    /************************************************************************** 
     * Set values in the list to defaults. Get the class defintion of the
     * passed defaults and set the column to the fioelds in T
     */
    def values(defaults : T*) =
        table.getColumns().clear()
        if defaults.length > 0 then
            val example = defaults(0)
            val clazz = example.getClass
            val fields = clazz.getDeclaredFields

            for(field <- fields)
                val name = field.getName
                val col = if classOf[MV].isAssignableFrom(field.getType) then
                    val col = TableColumn[T,String](name)                
                    col.setCellValueFactory(PropertyValueFactory[T,String](name))
                    table.getColumns().add(col)

        list.addAll(defaults*)
        table.setItems(list)
        
    /************************************************************************** 
     * clear list
     */
    def clear() = 
        list.clear()

    /************************************************************************** 
     * append to list and empty instance of T
     */
    def append() =
        detail(getInstanceOfT(), TableViewAction.APPEND)

    /************************************************************************** 
     * insert into lit instance of T
     */
    def insert() =
        detail(getInstanceOfT(), TableViewAction.INSERT)

    /************************************************************************** 
     * edit current selection of T
     */
    def edit() =
        detail(table.getSelectionModel().getSelectedItem(), TableViewAction.EDIT)

    /************************************************************************** 
     * delete selected instance in list of T
     */
    def delete() = 
        remove(table.getSelectionModel().getSelectedItem())

    /************************************************************************** 
     * return table
     */
    def getTable() = 
        table

    /************************************************************************** 
     * get list of controls
     */
    def getControl() =
        if mode == ListStatus.READ then
            List(table)
        else
            List(control)

    /************************************************************************** 
     * Dislay the detail of the value T. This value is normally from the 
     * selected item getValue. The detail is showed in a modal form. 
     * If the status from the form is Apply and action is edit then replace selection 
     * If the status from the form is Apply and action is edit then replace selection 
     * If the status from the form is Apply and insert is edit then insert new item
     * If the status from the form is Apply and insert is edit then append new item
     * If the status from the form is cancel then ignore changes
     */
    def detail(value : T, action : TableViewAction) =
        val loader = FXMLLoader()
        loader.setLocation(getClass().getResource("/form_detail.fxml"))
        val root = loader.load().asInstanceOf[javafx.scene.layout.AnchorPane]
        val scene = Scene(root)
        val stage = Stage()
        stage.setScene(scene)

        val controller = loader.getController[ControllerDetail]()
        val selected = getSelection().asInstanceOf[FormVM]
        val copy     = value
        val form = FXForm(copy)
        controller.insert(form)

        stage.initModality(Modality.APPLICATION_MODAL)
        stage.showAndWait()

        action match
        case TableViewAction.EDIT => 
            if controller.status == ControllerStatus.APPLY then
                val i = list.indexOf(selected)
                list.remove(selected)
                list.add(i, value.asInstanceOf[T])
                table.getSelectionModel().select(i)
                table.refresh()
        case TableViewAction.INSERT =>
            if controller.status == ControllerStatus.APPLY then
                val i = list.indexOf(selected)
                list.add(i, value.asInstanceOf[T])
                table.getSelectionModel().select(i)
                table.refresh()
        case TableViewAction.APPEND =>
            if controller.status == ControllerStatus.APPLY then
                list.add(value)
                table.getSelectionModel().select(list.size - 1)                
                table.refresh()

    /************************************************************************** 
     * Get the selected item
     */
    def getSelection() : T =
        table.getSelectionModel().getSelectedItem()
    
    /************************************************************************** 
     * Add a item value to the list
     */
    def add(value : T) =
        list.add(value)

    /************************************************************************** 
     * Remove the item from the list
     */
    def remove(value : T) =
        list.remove(value)

    /************************************************************************** 
     * Redraw table
     */
    def refresh() = 
        table.refresh()

/******************************************************************************
 * text input control
 */
class DisplayText() extends MV: 
    private val control = TextField()
    private val prop = SimpleStringProperty()

    /************************************************************************** 
     * constructor
     */
    def this(default : String) =
        this()
        control.textProperty().bindBidirectional(prop)
        prop.setValue(default)

    /************************************************************************** 
     * get all controlls
     */
    def getControl() : List[Control] =
        List(control)

    /************************************************************************** 
     * Set value of control
     */
    def set(name : String) =
        prop.setValue(name)

    /************************************************************************** 
     * Get value of control
     */
    def get() =
        prop.getValue()

    /************************************************************************** 
     * to string is the value of the constrol.
     */
    override def toString() = get()

/******************************************************************************
 * combobox input control
 */
class DisplayComboBox[T]() extends MV: 
    private val control = ComboBox[T]()
    private val prop = SimpleObjectProperty[T]()

    /************************************************************************** 
     * constructor
     */
    def this(default : T*) =
        this()
        control.valueProperty().bindBidirectional(prop)
        control.getItems.addAll(default*)

    /************************************************************************** 
     * clear combobox of all items
     */
    def clear() =
        control.getItems().removeAll(control.getItems())

    /************************************************************************** 
     * get all controlls
     */
    def getControl() : List[Control] =
        List(control)

    /************************************************************************** 
     * Set value of control
     */
    def set(value : T) =
        control.setValue(value)

    /************************************************************************** 
     * Get value of control
     */
    def get() =
        control.getValue()

    /************************************************************************** 
     * Set default items of control
     */
    def items(default : T*) =
        control.getItems.clear()
        control.getItems.addAll(default*)
/******************************************************************************
 * combobox input control
 */
class DisplayBoolean() extends MV: 
    private val control = CheckBox()
    private val prop = SimpleBooleanProperty()

    /************************************************************************** 
     * constructor
     */
    def this(default : Boolean) =
        this()
        control.selectedProperty().bindBidirectional(prop)
        prop.setValue(default)

    /************************************************************************** 
     * get all controlls
     */
    def getControl() : List[Control] =
        List(control)

    /************************************************************************** 
     * Set value of control
     */
    def set(value : Boolean) =
        prop.setValue(value)

    /************************************************************************** 
     * Get value of control
     */
    def get() =
        prop.getValue()
/******************************************************************************
 * radio button input control
 */
class DisplayRadioButton() extends MV: 
    private val control = RadioButton()
    private val prop    = SimpleBooleanProperty()

    /************************************************************************** 
     * constructor
     */
    def this(default : Boolean) =
        this()

    /************************************************************************** 
     * get all controlls
     */
    def getControl() : List[Control] =
        List(control)
/******************************************************************************
 * radio button input control
 */
class DisplayRadioGroup[T]() extends MV: 
    private val controls    = mutable.HashMap[T, RadioButton]()
    private val controlList = mutable.ArrayBuffer[RadioButton]()
    private val toggleGroup = ToggleGroup()
    private val prop        = SimpleBooleanProperty()

    /************************************************************************** 
     * constructor
     */
    def this(default : T*) =
        this()
        default.foreach(p =>
            val rb = RadioButton(p.toString())
            rb.setToggleGroup(toggleGroup)
            controlList += rb
            controls += p -> rb
        )
        if controlList.length > 0 then
            controlList(0).fire()

    /************************************************************************** 
     * get all controlls
     */
    def getControl()  =
        controlList.toList

    /************************************************************************** 
     * Get toggle group
     */
    def getToggle() = 
        toggleGroup    

    /************************************************************************** 
     * Get value of control
     */
    def get() : T =
        val c = toggleGroup.getSelectedToggle().asInstanceOf[RadioButton]
        val x = 
            if c != null then
                controls.find(p => p._2 == c)
            else
                None    
        x.get._1

    /************************************************************************** 
     * Set value of control
     */
    def set(value : T) =
        controls(value).fire()

/******************************************************************************
 * display label
 */
class DisplayLabel() extends  MV:
    private val control = Label()

    /************************************************************************** 
     * constructor
     */
    def this(default : String) =
        this()
        set(default)

    /************************************************************************** 
     * get all controlls
     */
    def getControl() : List[Control] =
        List(control)

    /************************************************************************** 
     * Set value of control
     */
    def set(value : String) =
        control.setText(value)

    /************************************************************************** 
     * Get value of control
     */
    def get() =
        control.getText()
