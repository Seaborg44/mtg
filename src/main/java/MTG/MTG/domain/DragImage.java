package MTG.MTG.domain;

import com.vaadin.flow.component.dnd.DragSource;

import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import lombok.Data;

public class DragImage extends Image implements DragSource<Image> {


    public DragImage() {
        setDraggable(true);
        setDragData(this);
    }
}
