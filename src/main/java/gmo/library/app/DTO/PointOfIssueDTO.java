package gmo.library.app.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class PointOfIssueDTO extends AbstractDTO<Long> {
    @Override
    public String toString() {
        return "" + getId();
    }
}
