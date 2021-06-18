package org.gerrie.scalafxform.util

import javafx.application.*

object InitGlobals:
    var javafxHasBeenInit = false

    /** 
     * init javafx only once per run
     */
    def initJavaFx() =
        if (!javafxHasBeenInit)
            Platform.startup(() => {})
            javafxHasBeenInit = true

object Const:
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