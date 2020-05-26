package gmo.library.app.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StudentDTO extends ReaderDTO {
    private StudyGroupDTO group;
    private PointOfIssueDTO pointOfIssue;
}
