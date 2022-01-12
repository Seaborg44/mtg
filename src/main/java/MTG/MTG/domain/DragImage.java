package MTG.MTG.domain;

import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.html.Image;

public class DragImage extends Image implements DragSource<Image> {

    public DragImage(){
        setDraggable(true);
        setDragData(this);
    }
}
