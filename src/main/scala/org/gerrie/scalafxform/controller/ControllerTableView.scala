package org.gerrie.scalafxform.controller

import java.net.URL
import java.util.ResourceBundle
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.layout.*
import javafx.scene.*
import javafx.stage.*

final case class ControllerTableView():

    var append : Option[() => Unit] = None
    var insert : Option[() => Unit] = None
    var edit : Option[() => Unit] = None
    var delete : Option[() => Unit] = None

    @FXML
    private var resources : ResourceBundle = null

    @FXML
    var anchorpane : AnchorPane = null

    @FXML
    def onAppendClick( event : ActionEvent )  =
        if append.isDefined then
            append.get()
    @FXML
    def onInsertClick( event : ActionEvent )  =
        if insert.isDefined then
            insert.get()
    @FXML
    def onEditClick( event : ActionEvent )  =
        if edit.isDefined then
            edit.get()
    @FXML
    def onDeleteClick( event : ActionEvent )  =
        if delete.isDefined then
            delete.get()

    @FXML
    def initialize() =
        ()            