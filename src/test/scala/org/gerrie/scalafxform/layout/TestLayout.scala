package org.gerrie.scalafxform.layout

import org.junit.*
import org.junit.Assert.*
import org.junit.rules.*
import org.junit.runner.OrderWith
import org.junit.runner.manipulation.Alphanumeric

import org.gerrie.scalafxform.util.* 
import org.gerrie.scalafxform.layout.* 
import javafx.scene.control.*
import javafx.scene.text.*

import scala.collection.*

object TestLayout:
    /** 
     * initilize JavaFX
     */
    @BeforeClass
    def initAll() = 
        InitGlobals.initJavaFx()
        Load.views.clear()
        ()
    /** 
     * clear all constructed values in the tests
     */
    @AfterClass
    def tearDown() =
        Load.views.clear()
        ()

@OrderWith(classOf[Alphanumeric])
class TestLayout:
    /** 
     * Load structure and try out Layout structure.
     */
    @Test def test1() =
        Load.load("test1", Const.jsonOk)
        val vmName = "test1" // i

        val controlGroup = ControlGroup() // i
        controlGroup.control += Label("label")
        val controlGroup1 = ControlGroup() // i
        controlGroup1.control += TextField("field")
        val controls = Map("field" -> mutable.ArrayBuffer(controlGroup), "label" -> mutable.ArrayBuffer(controlGroup1)) // i
        val constraints = mutable.ArrayBuffer[Constraint]() // o
        val parentNode = Layout.format(vmName, controls, constraints)

        // there should only be 3 nodes as children of the parent pane
        assertEquals("There should only be 3 child nodes.", 3, parentNode.getChildren().size)
