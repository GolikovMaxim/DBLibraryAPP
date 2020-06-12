package gmo.library.app.DTO;

import gmo.library.app.Main;
import gmo.library.app.Repositories.StudyGroupRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudyGroupDTO extends AbstractDTO<Long> {
    private Integer number;
    private FacultyDTO faculty;

    @Override
    public String toString() {
        return number.toString();
    }

    public static class StudyGroupHATEOAS extends AbstractHATEOAS<Long> {
        private Integer number;
        private String faculty;

        public StudyGroupHATEOAS(StudyGroupDTO studyGroupDTO) {
            super(studyGroupDTO);
            number = studyGroupDTO.number;
            faculty = studyGroupDTO.faculty.getURL();
        }
    }

    public String getURL() {
        return getURL(this);
    }

    public static String getURL(StudyGroupDTO studyGroupDTO) {
        return Main.SERVER_URL + StudyGroupRepository.URL + "/" + studyGroupDTO.getId();
    }
}
