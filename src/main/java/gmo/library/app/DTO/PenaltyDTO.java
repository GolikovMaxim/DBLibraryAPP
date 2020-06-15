package gmo.library.app.DTO;

import gmo.library.app.Main;
import gmo.library.app.Repositories.PenaltyRepository;
import gmo.library.app.Utilities.Sort;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
public class PenaltyDTO extends AbstractDTO<Long> {
    public static final Sort SORT_BY_ACCRUALDATE = new Sort("По дате назначения", "accrualDate");
    public static final Sort SORT_BY_PAYDATE = new Sort("По дате оплаты", "payDate");
    public static final Sort SORT_BY_COST = new Sort("По размеру штрафа", "cost");

    private String accrualDate;
    private int cost;
    private String payDate;
    private OffenceDTO offence;

    @Getter @Setter
    @NoArgsConstructor
    public static class PenaltyHATEOAS extends AbstractHATEOAS<Long> {
        private String accrualDate;
        private int cost;
        private String payDate;
        private String offence;

        public PenaltyHATEOAS(PenaltyDTO penaltyDTO) {
            super(penaltyDTO);
            accrualDate = penaltyDTO.accrualDate;
            cost = penaltyDTO.cost;
            payDate = penaltyDTO.payDate;
            offence = penaltyDTO.offence.getURL();
        }
    }

    public String getURL() {
        return getURL(this);
    }

    public static String getURL(PenaltyDTO penaltyDTO) {
        return Main.SERVER_URL + PenaltyRepository.URL + "/" + penaltyDTO.getId();
    }
}
