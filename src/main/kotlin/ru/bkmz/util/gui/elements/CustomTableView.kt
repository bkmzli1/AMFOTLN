package ru.bkmz.util.gui.elements

import javafx.event.EventHandler
import javafx.scene.control.TableView
import javafx.scene.input.MouseEvent

class CustomTableView<S> : TableView<S>() {
    private val consumeEvent =
        EventHandler { obj: MouseEvent -> obj.consume() }

    override fun layoutChildren() {
        super.layoutChildren()
        val dragRects = lookup("TableHeaderRow").lookupAll("Rectangle")
        for (dragRect in dragRects) {
            dragRect.removeEventFilter(MouseEvent.ANY, consumeEvent)
            dragRect.addEventFilter(MouseEvent.ANY, consumeEvent)
        }
    }
}