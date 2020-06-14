package gmo.library.app.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter @Getter
public class IssueDTO extends AbstractDTO<Long> {
    public static final Sort SORT_BY_BOOKNAME = new Sort("По названию", "bookName");
    public static final Sort SORT_BY_BOOKCOUNT = new Sort("По количеству книг", "bookCount");
    public static final Sort SORT_BY_RECEIPTDATE = new Sort("По дате получения", "receiptDate");
    public static final Sort SORT_BY_BOOKSINSTOCK = new Sort("По количеству книг в наличии", "booksInStock");

    private String bookName;
    private int bookCount;
    private FileCabinetDTO fileCabinet;
    private String receiptDate;
    private int booksInStock;

    @Getter @Setter
    @NoArgsConstructor
    public static class IssueHATEOAS extends AbstractHATEOAS<Long> {
        private String bookName;
        private int bookCount;
        private String fileCabinet;
        private String receiptDate;
        private int booksInStock;

        public IssueHATEOAS(IssueDTO issueDTO) {
            super(issueDTO);
            bookName = issueDTO.bookName;
            bookCount = issueDTO.bookCount;
            fileCabinet = issueDTO.fileCabinet.getURL();
            receiptDate = issueDTO.receiptDate;
            booksInStock = issueDTO.booksInStock;
        }
    }
}
