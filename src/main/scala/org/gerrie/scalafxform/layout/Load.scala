package org.gerrie.scalafxform.layout

import scala.collection.*
import javafx.scene.control.*
import javafx.scene.*
import javafx.scene.layout.*

import org.json.*

import scala.language.postfixOps
import scala.io.Source
import scala.collection.*

import java.nio.file.*
import java.net.* 
import org.gerrie.scalafxform.nodes.*
import org.gerrie.scalafxform.util.*

/******************************************************************************
* Load object that is used to load json view definitions from 
* the resource folder
*/
object Load extends Logs:

    /******************************************************************************
     * map of bean name to the view definition of the bean.
     */
    val views  = mutable.HashMap[String, TreeViewNodeView]()

    /******************************************************************************
     * Load all JSON view definitions from the resource folder
     */
    def loadViews(files : Array[String]) =
        assert(files.size > 0)

        info(s"Load from list of file names.")

        files.foreach(file =>

            info(s"Load file ${file}.")

            val resStream = getClass().getClassLoader().getResourceAsStream(s"viewjson/$file")
            if resStream != null then
                val resString = String( resStream.readAllBytes() )
                load(file.dropRight(10), resString)
                resStream.close()
            else
                val msg = s"File $file not found in resource folder" 
                error(msg)
                throw  Exception(msg)
        )

    /******************************************************************************
     * Load the json className definition for the file
     */
    def loadView(className : String, file : String) =
        assert(!className.isEmpty && !file.isEmpty)

        info(s"Load ${className} from the file named ${file}.")

        views.remove(className)

        val path = Path.of(file)
        try
            val jsonString = Files.readString(path)
            if jsonString != null then
                load(className, jsonString)
            else 
                val msg = s"${className}.view.json is empty."
                error(msg)
                throw Exception(msg)
        catch
            case e : Exception => info(s"File $file does not exist")

    /******************************************************************************
     * Load JSON view definition where the viewName is loaded from the 
     * viewList json string definition
     */
    def load(viewName : String, jsonString : String) = 
        assert(!viewName.isEmpty && !jsonString.isEmpty)

        info(s"Load $viewName from the json string")
        /**
         * Recursivly load child nodes
         */ 
        def loadChild(obj : JSONObject, parent : TreeViewNodeView) : Unit =
            val it = obj.keys
            while it.hasNext do
                val componentName =  it.next 
                componentName match
                    case comp if !comp.equals("node") =>
                                            info(s"Leaf node $componentName created.")

                                            val root = obj.get(comp).asInstanceOf[JSONObject] 

                                            if !Construct.newTreeNode.contains(comp) then
                                                val msg = s"$comp has not been defined in the newTreeNode map."
                                                error( msg )
                                                throw Exception(msg)

                                            val child = Construct.newTreeNode(comp)()
                                            child.formBeanName = Construct.mvToVMMap("org.gerrie.scalafxform.nodes")(s"${comp}")
                                            child.treeNodeDetailForm = Some(Construct.newVMBean(child.formBeanName))
                                            child.asInstanceOf[JSONView].fromJSON(root)
                                            child.parent = Some(parent)
                                            parent.children += child.asInstanceOf[LeafNode]
                    case "node"           =>
                                            info(s"Tree node $componentName created.")
                                            
                                            val root = obj.get("node").asInstanceOf[JSONObject]
                                            val child = Construct.newTreeNode("node")()
                                            child.formBeanName = Construct.mvToVMMap("org.gerrie.scalafxform.nodes")("DisplayTreeViewNode")
                                            child.treeNodeDetailForm = Some(Construct.newVMBean(child.formBeanName))
                                            child.asInstanceOf[JSONView].fromJSON(root)
                                            val children = root.getJSONArray("children")
                                            val count = children.length()
                                            for (i <- 0 until count)
                                                loadChild(children.get(i).asInstanceOf[JSONObject], child.asInstanceOf[TreeViewNodeView])
                                            parent.children += child.asInstanceOf[TreeViewNodeView]
                                            child.parent = Some(parent)
            
        val obj = JSONObject(jsonString)
        val root = obj.get("node").asInstanceOf[JSONObject]
        val parent = TreeViewNodeView()
        parent.formBeanName = Construct.mvToVMMap("org.gerrie.scalafxform.nodes")("DisplayTreeViewNode")
        parent.treeNodeDetailForm = Some(Construct.newVMBean(parent.formBeanName))
        views += viewName -> parent
        parent.fromJSON(root)

        val children = root.getJSONArray("children")
        val count = children.length()
        for (i <- 0 until count)
            loadChild(children.get(i).asInstanceOf[JSONObject], parent)
