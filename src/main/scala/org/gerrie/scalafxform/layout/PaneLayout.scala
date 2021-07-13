package org.gerrie.scalafxform.layout

import javafx.scene.*
import javafx.scene.control.*
import javafx.scene.layout.*
import org.gerrie.scalafxform.util.*
import org.tbee.javafx.scene.layout.*

/******************************************************************************
 * Load panes. The only pane type currently is the MigLayout pane
 */
case class MIGPane(layout : String, parentPane : Option[Pane]) extends TreePane with Logs:

    val layouts = layout.split("""::""")
    if layouts.size != 3 then
        val msg = s"Layout string must be 3 long '::' seperated : '$layout'"
        error( msg )
        throw Exception(msg) 

    private val pane = MigPane(layouts(0),layouts(1),layouts(2))
    
    def add(c : Node, param : String) : Unit = 

        info(s"Add $c with mig parameter $param.")

        val layout =
            if param.contains("::") then
                val params = param.split("""::""")
                if params.size != 3 then
                    val msg = s"Layout string must be 3 long '::' seperated : '$param'"
                    error( msg )
                    throw Exception(msg) 
                else
                    params(0)
            else
                param

        pane.add(c,layout.asInstanceOf[String])

    def add(p : TreePane) : Unit =

        info(s"Add a pane to this pane.")

        pane.add(p.getPane())

    def getPane() : Pane =
        pane
        
    override def disable(value : Boolean) =
        if parentPane.isDefined then
            parentPane.get.setDisable(value)
