package org.gerrie.scalafxform.util

import javafx.scene.input.*

/******************************************************************************
 * Singleton containing constants
 */
object Single:
    val SERIALIZED_MIME_TYPE = DataFormat("application/x-java-serialized-object")
    val DROP_HINT_STYLE = "-fx-border-color: #eea82f; -fx-border-width: 0 0 2 0; -fx-padding: 3 3 1 3"

/******************************************************************************
 * current runtime configuration variables
 */
object RunConfig:
    var currentPathForSaving = ""
    var currentViewEdited    = ""    