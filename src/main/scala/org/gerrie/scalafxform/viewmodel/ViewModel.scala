package org.gerrie.scalafxform.viewmodel

import org.gerrie.scalafxform.util.*
import org.gerrie.scalafxform.nodes.*
import org.gerrie.scalafxform.layout.*
import scala.collection.*
import javafx.scene.control.*
import javafx.scene.control.cell.*
import scala.beans.*
import javafx.scene.control.*
import javafx.scene.input.*
import javafx.scene.text.*
import javafx.util.*
import javafx.beans.property.*


import org.gerrie.scalafxform.controller.*
import javafx.collections.ObservableList
import java.util.Observable
import javafx.beans.value.ObservableObjectValue

/******************************************************************************
 * Tree node formVM, used to display/edit values associated with Tree
 */ 
class DisplayTreeViewNodeVM extends FormVM:
    val `type` = DisplayComboBox("MIGLAYOUT")

    override def getFieldName() = field.get()

    def makeCopy()= 
        val aCopy= DisplayTreeViewNodeVM()
        aCopy.`type`.set( `type`.get() )
        aCopy.label.set( label.get() )
        aCopy.layout.set( layout.get() )
        aCopy

    override def copy(from : FormVM) =
        super.copy(from)


/******************************************************************************
 * Generic Tree node formVM, used to display/edit values associated with Tree
 */ 
class DisplayTreeVM extends FormVM:
    override def getFieldName() = field.get()

    def makeCopy()= 
        val aCopy = DisplayTreeVM()
        aCopy.label.set( label.get() )
        aCopy.field.set( field.get() )
        aCopy.layout.set( layout.get() )
        aCopy

/******************************************************************************
 * Radion button group formVM, used to display/edit values associated 
 * with group of radio buttons
 */ 
class DisplayRadioGroupVM extends FormVM:
    override def getFieldName() = field.get()

    def makeCopy()= 
        val aCopy = DisplayRadioGroupVM()
        aCopy.label.set( label.get() )
        aCopy.field.set( field.get() )
        aCopy.layout.set( layout.get() )
        aCopy

/******************************************************************************
 * Radio button formVM, used to display/edit values associated with Radio button VM
 */ 
class DisplayRadioVM extends FormVM:
    override def getFieldName() = field.get()

    def makeCopy()= 
        val aCopy = DisplayRadioVM()
        aCopy.label.set( label.get() )
        aCopy.field.set( field.get() )
        aCopy.layout.set( layout.get() )
        aCopy

/******************************************************************************
 * Checkbox formVM, used to display/edit values associated with boolean value
 */ 
class DisplayBooleanVM extends FormVM:
    override def getFieldName() = field.get()

    def makeCopy()= 
        val aCopy = DisplayBooleanVM()
        aCopy.label.set( label.get() )
        aCopy.field.set( field.get() )
        aCopy.layout.set( layout.get() )
        aCopy

/******************************************************************************
 * Text formVM, used to display/edit values associated with Text
 */ 
class DisplayTextVM extends FormVM:
    override def getFieldName() = field.get()
    val validateREGEX = DisplayText("")
    val errString     = DisplayText("")

    def makeCopy()= 
        val aCopy = DisplayTextVM()
        aCopy.validateREGEX.set( validateREGEX.get() )
        aCopy.errString.set( errString.get() )
        aCopy.label.set( label.get() )
        aCopy.field.set( field.get() )
        aCopy.layout.set( layout.get() )
        aCopy

    override def copy(from : FormVM) =
        super.copy(from)
        val f = from.asInstanceOf[DisplayTextVM]
        validateREGEX.set(f.validateREGEX.get())
        errString.set(f.errString.get())

    
/******************************************************************************
 * List formVM, used to display/edit values associated with List
 */ 
class DisplayListVM extends FormVM:
    override def getFieldName() = field.get()

    def makeCopy()= 
        val aCopy = DisplayListVM()
        aCopy.label.set( label.get() )
        aCopy.field.set( field.get() )
        aCopy.layout.set( layout.get() )
        aCopy

/******************************************************************************
 * ComboBox formVM, used to display/edit values associated with Combobox
 */ 
class DisplayComboBoxVM extends FormVM:
    override def getFieldName() = field.get()

    def makeCopy()= 
        val aCopy = DisplayComboBoxVM()
        aCopy.label.set( label.get() )
        aCopy.field.set( field.get() )
        aCopy.layout.set( layout.get() )
        aCopy

/******************************************************************************
 * Unsed internally // TODO (check this form)
 */ 
class DisplayToVM extends VM:
    override def getFieldName() = ""

    val view = DisplayText("")
    val vm = DisplayText("")
    def makeCopy()= 
        val aCopy = DisplayToVM()
        aCopy.view.set( view.get() )
        aCopy.vm.set( vm.get() )
        aCopy

/******************************************************************************
 * Tree components formVM, used to display/edit components associate with tree
 */ 
class DisplayComponentTreeVM extends FormVM:
    override def getFieldName() = field.get()

    def makeCopy()= 
        val aCopy = DisplayComponentTreeVM()
        aCopy.label.set( label.get() )
        aCopy.field.set( field.get() )
        aCopy.layout.set( layout.get() )
        aCopy

/******************************************************************************
 * label formVM, used to display/edit values associated with labels
 */ 
class DisplayLabelVM extends VM:
    override def getFieldName() = field.get()

    val field = DisplayLabel()
    val layout = DisplayText()
    def makeCopy()= 
        val aCopy = DisplayLabelVM()
        aCopy.field.set( field.get() )
        aCopy.layout.set( layout.get() )
        aCopy
/******************************************************************************
 * DatePicker formVM, used to display/edit values associated with a DatePicker
 */ 
class DisplayDatePickerVM extends FormVM:
    override def getFieldName() = field.get()

    def makeCopy()= 
        val aCopy = DisplayLabelVM()
        aCopy.field.set( field.get() )
        aCopy.layout.set( layout.get() )
        aCopy
/******************************************************************************
 * Button formVM, used to display/edit values associated with a Button
 */ 
class DisplayButtonVM extends FormVM:
    override def getFieldName() = field.get()

    def makeCopy()= 
        val aCopy = DisplayButtonVM()
        aCopy.label.set( label.get() )
        aCopy.field.set( field.get() )
        aCopy.layout.set( layout.get() )
        aCopy

/******************************************************************************
 * HTML formVM, used to display/edit values associated with a HTML Editor
 */ 
class DisplayHTMLEditorVM extends FormVM:
    override def getFieldName() = field.get()
    val prefWidth = DisplayText("506")
    val prefHeight = DisplayText("200")

    def makeCopy()= 
        val aCopy = DisplayHTMLEditorVM()
        aCopy.prefWidth.set( prefWidth.get() )
        aCopy.prefHeight.set( prefHeight.get() )
        aCopy.label.set( label.get() )
        aCopy.field.set( field.get() )
        aCopy.layout.set( layout.get() )
        aCopy

    override def copy(from : FormVM) = 
        super.copy(from)
        val f = from.asInstanceOf[DisplayHTMLEditorVM]
        prefWidth.set ( f.prefWidth.get() )
        prefHeight.set ( f.prefHeight.get() )