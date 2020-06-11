package gmo.library.app.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FullReader {
    private Long id;
    private String firstName;
    private String secondName;
    private String lastName;
    private String birthday;
    private StudyGroupDTO studyGroup;
    private String department;
    private String degree;
    private String grade;
    private String faculty;
    private PointOfIssueDTO pointOfIssue;
    private String takeDate;
}
