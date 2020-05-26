package gmo.library.app.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FileCabinetDTO extends AbstractDTO<Long> {
    private PointOfIssueDTO pointOfIssue;
}
