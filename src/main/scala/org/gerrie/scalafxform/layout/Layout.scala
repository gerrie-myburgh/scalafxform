package org.gerrie.scalafxform.layout

import scala.collection.*
import javafx.scene.control.*
import javafx.scene.*
import javafx.scene.layout.*

import org.json.JSONObject
import org.json.JSONArray

import scala.language.postfixOps
import scala.io.Source
import scala.collection.*

import java.io.* 
import org.gerrie.scalafxform.nodes.*
import org.gerrie.scalafxform.util.*

/****************************************************************************** 
 * object that does layout of a view. 
 */
object Layout extends Logs:

    /******************************************************************************
     * format the view having vmName and controls with constraints on the parentPane
     * This is only done if the view defintion is known
     */
    def format( vmName : String, 
                controls : Map[String, mutable.ArrayBuffer[ControlGroup]], 
                constraints : mutable.ArrayBuffer[Constraint],
                parentPane : Option[Pane] = None) =

        assert(!vmName.isEmpty && controls.size > 0)

        info(s"Format ${vmName}.")

        if Load.views.contains(vmName) then
            if Load.views.contains(vmName) then
                val viewDef = Load.views(vmName)
                doLayoutOfBean(viewDef, controls, constraints, parentPane).getPane()
            else 
                val msg = s"${vmName} is not in the views keys."
                error( msg )
                throw Exception(msg) 
                null
        else
            val msg = s"${vmName}.view.json not one of the views."
            error( msg )
            throw Exception(msg)
            null
    /******************************************************************************
     * Use the view definition to do the form layout
     * The top node MUST ALWAYS be a LAYOUT
     */
    def doLayoutOfBean( viewNode : TreeViewNodeView, 
                        controls : Map[String, mutable.ArrayBuffer[ControlGroup]],
                        constraints : mutable.ArrayBuffer[Constraint],
                        parentPane : Option[Pane]) =
        /** 
         * panes can be nested 
         */
        val topLoad = mutable.Stack[TreePane]()

        info(s"Format top pane having name ${viewNode.formBeanName}")
        topLoad.push(viewNode.getNode(parentPane,viewNode.getLayout()))

        /** 
         * place the controls on the given pane
         */
        def formatControl(viewModel : LeafComponent, pane : TreePane) =
            if !viewModel.getLabel().isEmpty  then 
                info(s"Add label to pane ${viewModel.formBeanName}.")
                pane.add(Label(viewModel.getLabel()),"")

            if controls.contains(viewModel.getField()) then
                val c = controls(viewModel.getField())

                info(s"Add ${c.size} controls to pane.")

                c.foreach(controls =>
                    controls.control.foreach(control =>
                        if control.isInstanceOf[Node] then
                            pane.add(control.asInstanceOf[Node], viewModel.getLayout())
                    )
                )
            else
                val msg = s"${viewModel.getField()} is an unknown control name."
                error( msg )
                throw Exception(msg) 

        /** 
         * format all the children of the view model onto the given pane
         * 
         * // TODO nested components in the components map changes this drasticly...
         *         The loading of the jar files will have to be looked at as well.
         */
        def formatChildren(viewModel : TreeViewNodeView, pane : TreePane) : Unit=
            val newLoad = viewModel.getNode(parentPane, viewModel.getLayout())

            viewModel.children.foreach(child =>
                child match
                case c if c.isInstanceOf[TreeViewNodeView]  =>  info(s"Format child pane having name ${c.formBeanName}")
                                                        val layoutView = c.asInstanceOf[TreeViewNodeView]
                                                        val newLoad = viewModel.getNode(None,layoutView.getLayout())
                                                        pane.add(newLoad)
                                                        topLoad.push(newLoad)
                                                        formatChildren(layoutView, topLoad.top)

                                                        if layoutView.getType().equals("CONSTRAINT-RADIO") then
                                                            constraints += RadioButtonGroup(topLoad.top)

                                                        topLoad.pop
                case c if !c.isInstanceOf[TreeViewNodeView] =>  info(s"Format control ${c}.")
                                                        formatControl(c.asInstanceOf[LeafComponent], topLoad.top)
                case _ => println("--FAIL--")
            )
 
        viewNode.children.foreach(child =>
            child match
            case c if c.isInstanceOf[TreeViewNodeView]  => formatChildren(c.asInstanceOf[TreeViewNodeView], topLoad.top)
            case c if !c.isInstanceOf[TreeViewNodeView] => formatControl(c.asInstanceOf[LeafComponent], topLoad.top)
            case _ => println("--FAIL--")
        )

        topLoad.pop
  

