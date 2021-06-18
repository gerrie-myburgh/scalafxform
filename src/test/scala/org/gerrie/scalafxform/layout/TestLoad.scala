package org.gerrie.scalafxform.layout

import org.junit.Test
import org.junit.Assert.*
import org.junit.*
import org.junit.rules.*
import org.junit.runner.OrderWith
import org.junit.runner.manipulation.Alphanumeric

import org.json.*

import org.gerrie.scalafxform.util.* 
import org.gerrie.scalafxform.nodes.TreeViewNodeView
import org.junit.internal.runners.statements.ExpectException

import java.nio.file.*
object TestLoad:
    val jsonOk = """
{
    "node": {
        "id": "LAYOUT",
        "type": "MIGLAYOUT",
        "label": "Test",
        "layout":" :: :: ",
        "children": [
            {
                "node": {
                    "id": "GROUP",
                    "type": "COMPONENT",
                    "label": "",
                    "layout":" :: :: ",
                    "children": [
                        {
                            "DisplayLabel": {
                                "label": "Field",
                                "field": "field",
                                "layout":"wrap"
                            }
                        },
                        {
                            "DisplayText": {
                                "label": "Label",
                                "field": "label",
                                "layout":" :: :: "
                            }
                        }
                    ]
                }
            }
        ]
    }
}"""
    val jsonError = """
{
    "node": {
        "id": "LAYOUT",
        "type": "MIGLAYOUT",
        "label": "Test",
        "layout":" :: :: ",
        "children": [
            {
                "node": {
                    "id": "GROUP",
                    "type": "COMPONENT",
                    "label": "",
                    "layout":" :: :: ",
                    "children": [
                        {
                            "DisplayLabel": {
                                "label": "Field",
                                "field": "field",
                                "layout":"wrap"
                            }
                        },
                        {
                            "DisplayText": {
                                "label": "Label",
                                "field": "label",
                                "layout":" :: :: "
                            }
                        }
                    ]
                }
            }
    }
}"""

    // temp file to load from
    val path = Paths.get("test6.view.json")
    /** 
     * initilize JavaFX
     */
    @BeforeClass
    def initAll() = 
        InitGlobals.initJavaFx()
        Files.writeString(path, jsonOk)
        ()
    /** 
     * clear all constructed values in the tests
     */
    @AfterClass
    def tearDown() =
        Load.views.clear()
        Files.delete(path)
        ()


@OrderWith(classOf[Alphanumeric])
class TestLoad:
    /** 
     * Load a valid tree
     */
    @Test def test1() =
        assertEquals( "jsonOk must load fine", Load.load("test1", TestLoad.jsonOk), () )
    /** 
     * mke sure the tree is loaded
     */
    @Test def test2() = 
        assertEquals("Must return a TreeView node Node", Load.views("test1").getClass(), classOf[TreeViewNodeView])
        ()
    /** 
     * Try reload an existing tree
     */
    @Test def test3() =
        try
            Load.load("test1", TestLoad.jsonOk)
        catch
            case e : AssertionError => 
                val msg = e.getMessage()
                assertEquals( "Cannot load tree more than once", msg, "assertion failed" )
    /** 
     * Try load invalid tree
     */
    @Test def test4() =
        try
            Load.load("test4", TestLoad.jsonError)
        catch
            case e : JSONException => 
                val msg = e.getMessage()
                assertEquals( "Cannot load tree more than once", msg, "Expected a ',' or ']' at 1014 [character 5 line 33]" )
    /** 
     * Make sure that test4 did not load anything
     */
    @Test def test5() =
        assertEquals("Test4 did not leave anyting in the views map", Load.views.contains("test4"), false)
    /** 
     * Try load file 
     */
    @Test def test6() =
        Load.loadView("test6", "test6.view.json")
        assertEquals("Must return a TreeView node Node", Load.views("test6").getClass(), classOf[TreeViewNodeView])
    /** 
     * Try load with incorrect file name
     */
    @Test def test7() =
        try
            Load.loadView("test7", "test7.view.json")
        catch 
            case e : NoSuchFileException =>   
                val msg = e.getMessage()
                assertEquals("Cannot find the file test7.view.json", "test7.view.json", msg)
        ()
    /** 
     * try load a file that does not exist as part of a batch files.
     */
    @Test def test8() =
        try
            Load.loadViews(Array("test7"))
        catch 
            case e : Exception =>   
                val msg = e.getMessage()
                assertEquals("Cannot find the file test7.view.json", "File test7 not found in resource folder", msg)

