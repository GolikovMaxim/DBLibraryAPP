package gmo.library.app.DTO;

import gmo.library.app.Main;
import gmo.library.app.Repositories.BookTakeRepository;
import gmo.library.app.Utilities.Sort;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
public class BookTakeDTO extends AbstractDTO<Long> {
    public static final Sort SORT_BY_TAKEDATE = new Sort("По дате выдачи", "takeDate");
    public static final Sort SORT_BY_RETURNDATE = new Sort("По дате возврата", "returnDate");

    private ReaderDTO reader;
    private IssueDTO issue;
    private PointOfIssueDTO pointOfIssue;
    private String takeDate;
    private String returnDate;

    @Getter @Setter
    @NoArgsConstructor
    public static class BookTakeHATEOAS extends AbstractHATEOAS<Long> {
        private String reader;
        private String issue;
        private String pointOfIssue;
        private String takeDate;
        private String returnDate;

        public BookTakeHATEOAS(BookTakeDTO bookTakeDTO) {
            super(bookTakeDTO);
            reader = bookTakeDTO.reader.getURL();
            issue = bookTakeDTO.issue.getURL();
            pointOfIssue = bookTakeDTO.pointOfIssue.getURL();
            takeDate = bookTakeDTO.takeDate;
            returnDate = bookTakeDTO.returnDate;
        }
    }

    public String getURL() {
        return getURL(this);
    }

    public static String getURL(BookTakeDTO bookTakeDTO) {
        return Main.SERVER_URL + BookTakeRepository.URL + "/" + bookTakeDTO.getId();
    }
}
