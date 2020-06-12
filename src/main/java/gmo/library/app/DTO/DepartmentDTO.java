package gmo.library.app.DTO;

import gmo.library.app.Main;
import gmo.library.app.Repositories.DepartmentRepository;
import gmo.library.app.Repositories.FacultyRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
public class DepartmentDTO extends AbstractDTO<Long> {
    private FacultyDTO faculty;
    private String name;

    @Override
    public String toString() {
        return name;
    }

    public String getURL() {
        return getURL(this);
    }

    public static String getURL(DepartmentDTO departmentDTO) {
        return Main.SERVER_URL + DepartmentRepository.URL + "/" + departmentDTO.getId();
    }
}
