package MTG.MTG.domain;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.html.Span;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardGraveYardInfo extends Span implements DragSource<CardGraveYardInfo> {
    private String identification;
    private String url;
    private String manacost;
    private String name;

    public CardGraveYardInfo() {
        this.setDragData(this);
        this.setDraggable(true);
    }

    public void assignValuesFromDragImage(DragImage dragImage){
        this.setName(dragImage.getName());
        this.setUrl(dragImage.getUrl());
        this.setIdentification(dragImage.getId().get());
        this.setManacost(dragImage.getManaCost());
    }

    public DragImage transformIntoDragImage(){
        DragImage dragImage = new DragImage();
        dragImage.setName(this.getName());
        dragImage.setUrl(this.getUrl());
        dragImage.setId(this.getIdentification());
        dragImage.setManaCost(this.getManacost());
        return dragImage;
    }

    public CardGraveYardInfo transformIntoCGYI(DragImage dragImage){
        CardGraveYardInfo cGYI = new CardGraveYardInfo();
        cGYI.setManacost(dragImage.getManaCost());
        cGYI.setId(dragImage.getId().get());
        cGYI.setUrl(dragImage.getUrl());
        cGYI.setName(dragImage.getName());
        return cGYI;
    }
}
