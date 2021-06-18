package org.gerrie.scalafxform.fxform

import org.junit.Test
import org.junit.Assert.*

private[this] class EmptyModel

class TestFXForm:
    @Test def t1() =
        // setup the view.json layou
        val e = EmptyModel()
        val form = FXForm(e)
        ()