package gmo.library.app.DTO;

import gmo.library.app.Main;
import gmo.library.app.Repositories.DegreeRepository;
import gmo.library.app.Repositories.FacultyRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
public class DegreeDTO extends AbstractDTO<Long> {
    private String name;

    @Override
    public String toString() {
        return name;
    }

    public String getURL() {
        return getURL(this);
    }

    public static String getURL(DegreeDTO degreeDTO) {
        return Main.SERVER_URL + DegreeRepository.URL + "/" + degreeDTO.getId();
    }
}
