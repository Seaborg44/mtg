package MTG.MTG.domain;

import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dnd.GridDragStartEvent;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Graveyard extends VerticalLayout implements DropTarget<VerticalLayout>, DragSource<Grid<CardGraveYardInfo>> {

    private Grid<CardGraveYardInfo> grid = new Grid<>(CardGraveYardInfo.class);
    private List<CardGraveYardInfo> list = new ArrayList<>();

    public Graveyard() {
        setActive(true);
        add(getGrid());
        setDraggable(false);
        addDropListener(drop -> {
            grid.setItems(list);
        });

    }

    public Grid<CardGraveYardInfo> getGrid() {
        grid.setClassName("graveyard-grid");
        grid.setColumns("manacost", "name");
        grid.setSizeFull();
        grid.setRowsDraggable(true);

        return grid;
    }


}
