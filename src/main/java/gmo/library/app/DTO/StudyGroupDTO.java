package gmo.library.app.DTO;

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
}
