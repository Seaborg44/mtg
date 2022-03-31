package MTG.MTG.layout;

import MTG.MTG.service.Broadcaster;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

@StyleSheet("../CSS/stylesheet.css")
public class Board extends VerticalLayout implements DropTarget<VerticalLayout> {

    @Autowired
    public Board() {
        this.setMaxWidth("62%");
        this.addClassName("Board");
        this.setHeight(94 + "px");
        setActive(true);

        addDropListener(event -> {
            MainView.boardId = this.getId().get();
            Broadcaster.broadcast("");
            this.add(event.getDragSourceComponent().get());
            this.remove(event.getDragSourceComponent().get());
        });


    }

}
