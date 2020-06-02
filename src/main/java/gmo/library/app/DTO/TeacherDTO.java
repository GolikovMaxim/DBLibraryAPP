package gmo.library.app.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDTO extends ReaderDTO {
    private DepartmentDTO department;
    private DegreeDTO degree;
    private GradeDTO grade;
    private PointOfIssueDTO pointOfIssue;
}
