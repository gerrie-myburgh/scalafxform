package org.gerrie.scalafxform.controller

import java.net.URL
import java.util.ResourceBundle
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.layout.*
import javafx.scene.*
import javafx.stage.*

enum ControllerStatus:
    case APPLY, CANCEL, NONE

final case class ControllerDetail():

    var status = ControllerStatus.NONE

    var cancel: Option[() => Unit] = None
    var apply : Option[() => Unit] = None

    @FXML
    private var resources : ResourceBundle = null

    @FXML
    private var anchorpane : AnchorPane = null

    @FXML
    def onCancelClick( event : ActionEvent )  =
        if cancel.isDefined then 
            cancel.get()
        else
            status = ControllerStatus.CANCEL
            close()

    @FXML
    def onApplyClick( event : ActionEvent )  =
        if apply.isDefined then
            apply.get()
        else
            status = ControllerStatus.APPLY
            close()
    
    def insert(node : Node) =
        anchorpane.getChildren().add(0, node)
        AnchorPane.setTopAnchor(node,0)
        AnchorPane.setBottomAnchor(node,40)
        AnchorPane.setLeftAnchor(node,0)
        AnchorPane.setRightAnchor(node,0)

    def close() = 
        val stage = anchorpane.getScene().getWindow().asInstanceOf[Stage]
        stage.close()

    @FXML
    def initialize() =
        ()    