package gmo.library.app.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DepartmentDTO extends AbstractDTO<Long> {
    private FacultyDTO faculty;
    private String name;

    @Override
    public String toString() {
        return name;
    }
}
