package gmo.library.app.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Teacher extends Reader {
    private String department;
    private String degree;
    private String grade;
    private int pointOfIssue;
}
