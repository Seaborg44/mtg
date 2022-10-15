package MTG.MTG.domain;

import com.vaadin.flow.component.dnd.DragSource;

import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Transient;

@Getter
@Setter
@ToString
public class DragImage extends Image implements DragSource<Image> {

    private String identificator;
    private String name;
    private String url;
    private String manaCost;

    public DragImage() {
        setDraggable(true);
        setDragData(this);
    }

    public CardGraveYardInfo transformIntoCGYI(DragImage dragImage) {
        CardGraveYardInfo cGYI = new CardGraveYardInfo();
        cGYI.setManacost(dragImage.getManaCost());
        cGYI.setId(dragImage.getId().get());
        cGYI.setUrl(dragImage.getUrl());
        cGYI.setName(dragImage.getName());
        return cGYI;
    }

}
