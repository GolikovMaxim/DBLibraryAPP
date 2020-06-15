package gmo.library.app.DTO;

import gmo.library.app.Main;
import gmo.library.app.Repositories.StudentRepository;
import gmo.library.app.Utilities.Sort;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class StudentDTO extends ReaderDTO {
    public static final Sort SORT_BY_STUDYGROUP = new Sort("По группе", "group");

    private StudyGroupDTO group;
    private PointOfIssueDTO pointOfIssue;

    @Getter @Setter
    @NoArgsConstructor
    public static class StudentHATEOAS extends ReaderHATEOAS {
        private String group;
        private String pointOfIssue;

        public StudentHATEOAS(StudentDTO studentDTO) {
            super(studentDTO);
            group = studentDTO.group.getURL();
            pointOfIssue = studentDTO.pointOfIssue.getURL();
        }
    }

    @Override
    public String getURL() {
        return getURL(this);
    }

    public static String getURL(StudentDTO studentDTO) {
        return Main.SERVER_URL + StudentRepository.URL + "/" + studentDTO.getId();
    }
}
