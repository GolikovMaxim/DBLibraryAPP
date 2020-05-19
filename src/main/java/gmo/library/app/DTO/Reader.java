package gmo.library.app.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class Reader {
    private String firstName;
    private String secondName;
    private String lastName;
    private LocalDate birthday;
    private int StudyGroup;
    private String department;
    private String degree;
    private String grade;
    private String faculty;
}
