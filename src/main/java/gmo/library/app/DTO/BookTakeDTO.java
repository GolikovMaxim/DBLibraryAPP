package gmo.library.app.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class BookTakeDTO extends AbstractDTO<Long> {
    private ReaderDTO reader;
    private IssueDTO issue;
    private PointOfIssueDTO pointOfIssue;
    private Date takeDate;
    private Date returnDate;
}
