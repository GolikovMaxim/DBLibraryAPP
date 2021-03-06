package gmo.library.app.Utilities;

import gmo.library.app.DTO.PointOfIssueDTO;
import gmo.library.app.DTO.ReaderDTO;
import gmo.library.app.DTO.StudyGroupDTO;
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

    public ReaderDTO toReader() {
        ReaderDTO readerDTO = new ReaderDTO();
        readerDTO.setId(id);
        readerDTO.setLastName(lastName);
        readerDTO.setFirstName(firstName);
        readerDTO.setSecondName(secondName);
        readerDTO.setBirthday(birthday);
        return readerDTO;
    }
}
