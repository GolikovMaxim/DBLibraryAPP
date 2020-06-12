package gmo.library.app.DTO;

import gmo.library.app.Main;
import gmo.library.app.Repositories.FacultyRepository;
import gmo.library.app.Repositories.GradeRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
public class GradeDTO extends AbstractDTO<Long> {
    private String name;

    @Override
    public String toString() {
        return name;
    }

    public String getURL() {
        return getURL(this);
    }

    public static String getURL(GradeDTO gradeDTO) {
        return Main.SERVER_URL + GradeRepository.URL + "/" + gradeDTO.getId();
    }
}
