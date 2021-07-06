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
        ()
