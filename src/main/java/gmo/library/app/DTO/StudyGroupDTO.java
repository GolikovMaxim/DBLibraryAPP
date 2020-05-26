package gmo.library.app.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StudyGroupDTO extends AbstractDTO<Long> {
    private FacultyDTO faculty;
}
