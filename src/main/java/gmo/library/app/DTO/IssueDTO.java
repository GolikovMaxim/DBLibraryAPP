package gmo.library.app.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter @Getter
public class IssueDTO extends AbstractDTO<Long> {
    private String bookName;
    private int bookCount;
    private FileCabinetDTO fileCabinet;
    private Date receiptDate;
}
