package gmo.library.app.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class StudentDTO extends ReaderDTO {
    private StudyGroupDTO group;
    private PointOfIssueDTO pointOfIssue;
}
