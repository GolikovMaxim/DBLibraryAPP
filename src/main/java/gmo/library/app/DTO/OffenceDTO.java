package gmo.library.app.DTO;

import gmo.library.app.Main;
import gmo.library.app.Repositories.OffenceRepository;
import gmo.library.app.Utilities.Sort;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter @Getter
public class OffenceDTO extends AbstractDTO<Long> {
    public static final Sort SORT_BY_ACCRUALDATE = new Sort("По дате нарушения", "accrualDate");
    public static final Sort SORT_BY_ENDDATE = new Sort("По дате окончания", "endDate");

    private String accrualDate;
    private String endDate;
    private BookTakeDTO bookTake;

    @Getter @Setter
    @NoArgsConstructor
    public static class OffenceHATEOAS extends AbstractHATEOAS<Long> {
        private String accrualDate;
        private String endDate;
        private String bookTake;

        public OffenceHATEOAS(OffenceDTO offenceDTO) {
            super(offenceDTO);
            accrualDate = offenceDTO.accrualDate;
            endDate = offenceDTO.endDate;
            bookTake = offenceDTO.bookTake.getURL();
        }
    }

    public String getURL() {
        return getURL(this);
    }

    public static String getURL(OffenceDTO offenceDTO) {
        return Main.SERVER_URL + OffenceRepository.URL + "/" + offenceDTO.getId();
    }
}
