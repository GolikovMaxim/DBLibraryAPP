package gmo.library.app.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter @Getter
public class OffenceDTO extends AbstractDTO<Long> {
    private Date accrualDate;
    private Date endDate;
    private ReaderDTO reader;
    private BookTakeDTO bookTake;
}
