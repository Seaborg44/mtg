package MTG.MTG.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardGraveYardInfo {
    private String id;
    private String url;
    private String manacost;
    private String name;

}
