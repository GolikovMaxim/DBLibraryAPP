package gmo.library.app.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class PenaltyDTO extends AbstractDTO<Long> {
    private Date accrualDate;
    private int cost;
    private Date payDate;
    private OffenceDTO offence;
}
