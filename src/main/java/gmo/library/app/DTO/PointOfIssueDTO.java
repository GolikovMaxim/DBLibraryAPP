package gmo.library.app.DTO;

import gmo.library.app.Main;
import gmo.library.app.Repositories.PointOfIssueRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class PointOfIssueDTO extends AbstractDTO<Long> {
    @Override
    public String toString() {
        return "" + getId();
    }

    public String getURL() {
        return getURL(this);
    }

    public static String getURL(PointOfIssueDTO pointOfIssueDTO) {
        return Main.SERVER_URL + PointOfIssueRepository.URL + "/" + pointOfIssueDTO.getId();
    }
}
