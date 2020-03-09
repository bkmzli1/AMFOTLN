package ru.bkmz.util.gui.elements;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

import java.util.Set;

public class CustomTableView<S> extends TableView<S> {
    private final EventHandler<MouseEvent> consumeEvent = MouseEvent::consume;

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        final Set<Node> dragRects = lookup("TableHeaderRow").lookupAll("Rectangle");
        for (Node dragRect : dragRects) {
            dragRect.removeEventFilter(MouseEvent.ANY, consumeEvent);
            dragRect.addEventFilter(MouseEvent.ANY, consumeEvent);
        }
    }
}