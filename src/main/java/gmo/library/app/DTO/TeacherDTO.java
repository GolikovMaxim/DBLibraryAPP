package gmo.library.app.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TeacherDTO extends ReaderDTO {
    private DepartmentDTO department;
    private DegreeDTO degree;
    private GradeDTO grade;
    private PointOfIssueDTO pointOfIssue;
}
