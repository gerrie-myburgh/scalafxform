package org.gerrie.scalafxform.util

import org.gerrie.scalafxform.layout.*
import org.gerrie.scalafxform.nodes.*
import org.gerrie.scalafxform.viewmodel.*
import org.gerrie.scalafxform.viewmodel.*
import org.gerrie.scalafxform.viewmodel.*

import scala.collection.*

/**********************************************************************************
 * object used to costruct instances if named classes
 */
object Construct:
    /******************************************************************************
     * construct new view model bean
     */
    def newVMBean(name : String) = 
        val clazz = Class.forName(name)
        clazz.getConstructor().newInstance().asInstanceOf[VM]

    /******************************************************************************
     * construct new model view bean
     */
    def newMVBean(name : String) = 
        val clazz = Class.forName(name)
        clazz.getConstructor().newInstance().asInstanceOf[MV]

    /******************************************************************************
     * defined classes in the system
     */ 
    val getClass = mutable.HashMap[String, Class[_]]()

    /******************************************************************************
     * get a new tree of leaf node
     */
    val newTreeNode = Map[String, () => Tree | LeafNode](
        "node" ->                {() => TreeViewNodeView()},
        "DisplayTree" ->         {() => DisplayTreeView()},
        "DisplayRadioGroup" ->   {() => DisplayRadioGroupView()},
        "SelectRadio" ->         {() => DisplayRadioView()},
        "DisplayBoolean" ->      {() => DisplayBooleanView()},
        "DisplayText" ->         {() => DisplayTextView()},
        "DisplayButton" ->       {() => DisplayButtonView()},
        "DisplayDatePicker" ->   {() => DisplayDatePickerView()},
        "DisplayList" ->         {() => DisplayListView()},
        "DisplayComboBox" ->     {() => DisplayComboBoxView()},
        "DisplayTo" ->           {() => DisplayTOVMMapView()},
        "ComponentDisplayTree" ->{() => DisplayComponentTreeView()},
        "DisplayLabel" ->        {() => DisplayLabelView()},
        "DisplayHTMLEditor" ->   {() => DisplayHTMLEditorView()}
    )

    /******************************************************************************
     * mapping of model view names to view model names.
     */
    def mvNameToVMMap(name : String) = 
        val className = name.split("""\.""").last
        mvToVMMap("org.gerrie.scalafxform.nodes")(className)

    val mvToVMMap = mutable.HashMap[String, String => String]()
      
    mvToVMMap += "org.gerrie.scalafxform.nodes" -> {(value : String) =>  s"org.gerrie.scalafxform.viewmodel.${value}VM"}

    /******************************************************************************
     * List of view json file names
     */
    val viewFileNames = Array(
        "org.gerrie.scalafxform.viewmodel.DisplayBooleanVM.view.json",
        "org.gerrie.scalafxform.viewmodel.DisplayClassMembersListVM.view.json",
        "org.gerrie.scalafxform.viewmodel.DisplayComboBoxVM.view.json",
        "org.gerrie.scalafxform.viewmodel.DisplayRadioGroupVM.view.json",
        "org.gerrie.scalafxform.viewmodel.DisplayTextVM.view.json",
        "org.gerrie.scalafxform.viewmodel.DisplayButtonVM.view.json",
        "org.gerrie.scalafxform.viewmodel.DisplayDatePickerVM.view.json",
        "org.gerrie.scalafxform.viewmodel.DisplayToVM.view.json",
        "org.gerrie.scalafxform.viewmodel.DisplayToVM.view.json",
        "org.gerrie.scalafxform.viewmodel.DisplayClassMembersListVM.view.json",
        "org.gerrie.scalafxform.viewmodel.MVToVMMap.view.json",
        "org.gerrie.scalafxform.viewmodel.DisplayTreeViewComponentsVM.view.json",
        "org.gerrie.scalafxform.viewmodel.DisplayTreeViewNodeVM.view.json",
        "org.gerrie.scalafxform.viewmodel.DisplayListVM.view.json",
        "org.gerrie.scalafxform.viewmodel.DisplayHTMLEditorVM.view.json",
    )