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

import org.json.JSONObject
import scala.annotation.meta.beanSetter

/****************************************************************************** 
 * all the component view case classes
 * These classes gets / sets the information from / on the View Models that is 
 * The treeNodeDetailForm is associated with this componentview
 * These classes load and save so json and display on the tree using toString
 *****************************************************************************/

/****************************************************************************** 
 * Display label view
 */
case class DisplayLabelView() extends LeafComponent:
    /************************************************************************** 
     * get field value from tree node detail form
     */
    def getField()   = 
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayLabelVM].field.get()
        else
            "undefined treeNodeDetailForm"

    def getLabel() = ""

    /************************************************************************** 
     * Get Layout
     */ 
    def getLayout()   = 
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayLabelVM].layout.get()
        else
            "undefined treeNodeDetailForm"

    /************************************************************************** 
     * load values from JSON string
     */ 
    def toJSONStr()= s"""{"DisplayLabel":{"field":"${getField()}","layout":"${getLayout()}"}}"""
    /************************************************************************** 
     * save value to JSON string
     */ 
    def fromJSON(json : JSONObject) = 
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayLabelVM].field.set( json.getString("field") )
            treeNodeDetailForm.get.asInstanceOf[DisplayLabelVM].layout.set( json.getString("layout") )
    /************************************************************************** 
     * to string override
     */ 
    override def toString() = s"${getField()} : $formBeanName"

/****************************************************************************** 
 */
case class DisplayTreeView() extends LeafComponent():

    /************************************************************************** 
     * get label value from tree node detail form
     */ 
    def getLabel() = 
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayTreeVM].label.get()
        else
            "undefined treeNodeDetailForm"

    /************************************************************************** 
     * get field value from tree node detail form
     */ 
    def getField()   = 
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayTreeVM].field.get()
        else
            "undefined treeNodeDetailForm"

    /************************************************************************** 
     *  Get Layout
     */ 
    def getLayout()   = 
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayTreeVM].layout.get()
        else
            "undefined treeNodeDetailForm"

    /************************************************************************** 
     * load values from JSON string
     */ 
    def toJSONStr()= s"""{"DisplayTree":{"label":"${getLabel()}", "field":"${getField()}","layout":"${getLayout()}"}}"""
    /************************************************************************** 
     * save value to JSON string
     */ 
    def fromJSON(json : JSONObject) = 
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayTreeVM].label.set( json.getString("label") )
            treeNodeDetailForm.get.asInstanceOf[DisplayTreeVM].field.set( json.getString("field") )
            treeNodeDetailForm.get.asInstanceOf[DisplayTreeVM].layout.set( json.getString("layout") )
    /************************************************************************** 
     *
     */ 
    override def toString() = s"${getField()} : $formBeanName"

/****************************************************************************** 
 */
case class DisplayRadioGroupView() extends LeafComponent():
    /************************************************************************** 
     * get label value from tree node detail form
     */ 
    def getLabel() = 
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayRadioGroupVM].label.get()
        else
            "undefined treeNodeDetailForm"

    /************************************************************************** 
     * get field value from tree node detail form
     */ 
    def getField()   = 
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayRadioGroupVM].field.get()
        else
            "undefined treeNodeDetailForm"

    /************************************************************************** 
     * Get Layout
     */ 
    def getLayout()   = 
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayRadioGroupVM].layout.get()
        else
            "undefined treeNodeDetailForm"

    /************************************************************************** 
     * load values from JSON string
     */ 
    def toJSONStr()= s"""{"DisplayRadioGroup":{"label":"${getLabel()}", "field":"${getField()}","layout":"${getLayout()}"}}"""
    /************************************************************************** 
     * save value to JSON string
     */ 
    def fromJSON(json : JSONObject) = 
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayRadioGroupVM].label.set( json.getString("label") )
            treeNodeDetailForm.get.asInstanceOf[DisplayRadioGroupVM].field.set( json.getString("field") )
            treeNodeDetailForm.get.asInstanceOf[DisplayRadioGroupVM].layout.set( json.getString("layout") )
    /************************************************************************** 
     *
     */ 
    override def toString() = s"${getField()} : $formBeanName"

/****************************************************************************** 
 */
case class DisplayRadioView() extends LeafComponent():
    /************************************************************************** 
     * get label value from tree node detail form
     */ 
    def getLabel() = 
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayRadioVM].label.get()
        else
            "undefined treeNodeDetailForm"

    /************************************************************************** 
     * get field value from tree node detail form
     */ 
    def getField()   = 
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayRadioVM].field.get()
        else
            "undefined treeNodeDetailForm"

    /************************************************************************** 
     * Get Layout
     */ 
    def getLayout()   = 
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayRadioVM].layout.get()
        else
            "undefined treeNodeDetailForm"

    /************************************************************************** 
     * load values from JSON string
     */ 
    def toJSONStr()= s"""{"SelectRadio":{"label":"${getLabel()}", "field":"${getField()}","layout":"${getLayout()}"}}"""
    /************************************************************************** 
     * save value to JSON string
     */ 
    def fromJSON(json : JSONObject) = 
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayRadioVM].label.set( json.getString("label") )
            treeNodeDetailForm.get.asInstanceOf[DisplayRadioVM].field.set( json.getString("field") )
            treeNodeDetailForm.get.asInstanceOf[DisplayRadioVM].layout.set( json.getString("layout") )
    /************************************************************************** 
     *
     */ 
    override def toString() = s"${getField()} : $formBeanName"

/****************************************************************************** 
 */
case class DisplayBooleanView() extends LeafComponent():
    /************************************************************************** 
     * get label value from tree node detail form
     */ 
    def getLabel() = 
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayBooleanVM].label.get()
        else
            "undefined treeNodeDetailForm"

    /************************************************************************** 
     * get field value from tree node detail form
     */ 
    def getField()   = 
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayBooleanVM].field.get()
        else
            "undefined treeNodeDetailForm"

    /************************************************************************** 
     * Get Layout
     */ 
    def getLayout()   = 
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayBooleanVM].layout.get()
        else
            "undefined treeNodeDetailForm"

    /************************************************************************** 
     * load values from JSON string
     */ 
    def toJSONStr()= s"""{"DisplayBoolean":{"label":"${getLabel()}", "field":"${getField()}","layout":"${getLayout()}"}}"""
    /************************************************************************** 
     * save value to JSON string
     */ 
    def fromJSON(json : JSONObject) = 
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayBooleanVM].label.set( json.getString("label") )
            treeNodeDetailForm.get.asInstanceOf[DisplayBooleanVM].field.set( json.getString("field") )
            treeNodeDetailForm.get.asInstanceOf[DisplayBooleanVM].layout.set( json.getString("layout") )
    /************************************************************************** 
     *
     */ 
    override def toString() = s"${getField()} : $formBeanName"

/****************************************************************************** 
 */
case class DisplayTextView() extends LeafComponent():
    /************************************************************************** 
     * get label value from tree node detail form
     */ 
    def getLabel() = 
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayTextVM].label.get()
        else
            "undefined treeNodeDetailForm"

    /************************************************************************** 
     * get field value from tree node detail form
     */ 
    def getField()   = 
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayTextVM].field.get()
        else
            "undefined treeNodeDetailForm"

    /************************************************************************** 
     * Get Layout
     */ 
    def getLayout()   = 
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayTextVM].layout.get()
        else
            "undefined treeNodeDetailForm"

    /************************************************************************** 
     * load values from JSON string
     */ 
    def toJSONStr()= s"""{"DisplayText":{"label":"${getLabel()}", "field":"${getField()}","layout":"${getLayout()}"}}"""
    /************************************************************************** 
     * save value to JSON string
     */ 
    def fromJSON(json : JSONObject) = 
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayTextVM].label.set( json.getString("label") )
            treeNodeDetailForm.get.asInstanceOf[DisplayTextVM].field.set( json.getString("field") )
            treeNodeDetailForm.get.asInstanceOf[DisplayTextVM].layout.set( json.getString("layout") )
    /************************************************************************** 
     *
     */ 
    override def toString() = s"${getField()} : $formBeanName"

/****************************************************************************** 
 */
case class DisplayListView() extends LeafComponent():
    /************************************************************************** 
     * get label value from tree node detail form
     */ 
    def getLabel() = 
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayListVM].label.get()
        else
            "undefined treeNodeDetailForm"

    /************************************************************************** 
     * get field value from tree node detail form
     */ 
    def getField()   = 
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayListVM].field.get()
        else
            "undefined treeNodeDetailForm"

    /************************************************************************** 
     * Get Layout
     */ 
    def getLayout()   = 
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayListVM].layout.get()
        else
            "undefined treeNodeDetailForm"

    /************************************************************************** 
     * load values from JSON string
     */ 
    def toJSONStr()= s"""{"DisplayList":{"label":"${getLabel()}", "field":"${getField()}","layout":"${getLayout()}"}}"""
    /************************************************************************** 
     * save value to JSON string
     */ 
    def fromJSON(json : JSONObject) = 
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayListVM].label.set( json.getString("label") )
            treeNodeDetailForm.get.asInstanceOf[DisplayListVM].field.set( json.getString("field") )
            treeNodeDetailForm.get.asInstanceOf[DisplayListVM].layout.set( json.getString("layout") )
    override def toString() = s"${getField()} : $formBeanName"

/****************************************************************************** 
 */
case class DisplayComboBoxView() extends LeafComponent():

    /************************************************************************** 
     * get label value from tree node detail form
     */ 
    def getLabel() = 
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayComboBoxVM].label.get()
        else
            "undefined treeNodeDetailForm"
    /************************************************************************** 
     * get field value from tree node detail form
     */ 
    def getField()   = 
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayComboBoxVM].field.get()
        else
            "undefined treeNodeDetailForm"

    /************************************************************************** 
     * Get Layout
     */ 
    def getLayout()   = 
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayComboBoxVM].layout.get()
        else
            "undefined treeNodeDetailForm"

    /************************************************************************** 
     * load values from JSON string
     */ 
    def toJSONStr()= s"""{"DisplayComboBox":{"label":"${getLabel()}", "field":"${getField()}","layout":"${getLayout()}"}}"""
    /************************************************************************** 
     * save value to JSON string
     */ 
    def fromJSON(json : JSONObject) = 
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayComboBoxVM].label.set( json.getString("label") )
            treeNodeDetailForm.get.asInstanceOf[DisplayComboBoxVM].field.set( json.getString("field") )
            treeNodeDetailForm.get.asInstanceOf[DisplayComboBoxVM].layout.set( json.getString("layout") )
    /************************************************************************** 
     *
     */ 
    override def toString() = s"${getField()} : $formBeanName"

/****************************************************************************** 
 */
case class DisplayTOVMMapView() extends LeafComponent():
    def getField() = ""
    def getLabel() = "SelectTOVMMap"

    /************************************************************************** 
     * get view value from  tree node detail form
     */ 
    def getView() = 
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayToVM].view.get()
        else
            "undefined treeNodeDetailForm"

    /************************************************************************** 
     * get vm value from tree node detail form
     */ 
    def getVM()   = 
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayToVM].vm.get()
        else
            "undefined treeNodeDetailForm"

    /************************************************************************** 
     * Get Layout
     */ 
    def getLayout() = ""

    /************************************************************************** 
     * load values from JSON string
     */ 
    def toJSONStr()= s"""{"SelectTOVMMap":{"view":"${getView()}", "vm":"${getVM()}"}}"""
    /************************************************************************** 
     * save value to JSON string
     */ 
    def fromJSON(json : JSONObject) =  
        if treeNodeDetailForm.isDefined then
            treeNodeDetailForm.get.asInstanceOf[DisplayToVM].view.set( json.getString("view") )
            treeNodeDetailForm.get.asInstanceOf[DisplayToVM].vm.set( json.getString("vm") )
    /************************************************************************** 
     *
     */ 
    override def toString() = s"${getView()} : ${getVM()}"

/****************************************************************************** 
 */
case class DisplayComponentTreeView() extends LeafComponent():
    def getField() = ""
    def getLabel() = "SelectDisplayComponentTreeView"

    /************************************************************************** 
     * get view value from  tree node detail form
     */ 
    def getView() = 
        if treeNodeDetailForm.isDefined then
            ""//treeNodeDetailForm.get.asInstanceOf[DisplayComponentTreeVM].view.get()
        else
            "undefined treeNodeDetailForm"

    def getLayout() = ""

    /************************************************************************** 
     * get vm value from tree node detail form
     */ 
    def getVM()   = 
        if treeNodeDetailForm.isDefined then
            ""//treeNodeDetailForm.get.asInstanceOf[DisplayComponentTreeVM].vm.get()
        else
            "undefined treeNodeDetailForm"

    /************************************************************************** 
     * load values from JSON string
     */ 
    def toJSONStr()= s"""{"ComponentDisplayTree":{}}"""
    /************************************************************************** 
     * save value to JSON string
     */ 
    def fromJSON(json : JSONObject) =  
        ()
    /************************************************************************** 
     * to string override
     */ 
    override def toString() = s"Component MV Tree"

