package gmo.library.app.DTO;

import gmo.library.app.Main;
import gmo.library.app.Repositories.FileCabinetRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
public class FileCabinetDTO extends AbstractDTO<Long> {
    private PointOfIssueDTO pointOfIssue;

    @Getter @Setter
    @NoArgsConstructor
    public static class FileCabinetHATEOAS extends AbstractHATEOAS<Long> {
        private String pointOfIssue;

        public FileCabinetHATEOAS(FileCabinetDTO fileCabinetDTO) {
            super(fileCabinetDTO);
            pointOfIssue = fileCabinetDTO.pointOfIssue.getURL();
        }
    }

    public String getURL() {
        return getURL(this);
    }

    public static String getURL(FileCabinetDTO fileCabinetDTO) {
        return Main.SERVER_URL + FileCabinetRepository.URL + "/" + fileCabinetDTO.getId();
    }
}
