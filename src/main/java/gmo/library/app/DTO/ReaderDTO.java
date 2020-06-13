package gmo.library.app.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class ReaderDTO extends AbstractDTO<Long> {
    public static final Sort SORT_BY_FIRSTNAME = new Sort("По имени", "firstName");
    public static final Sort SORT_BY_SECONDNAME = new Sort("По отчеству", "secondName");
    public static final Sort SORT_BY_LASTNAME = new Sort("По фамилии", "lastName");

    private String firstName;
    private String secondName;
    private String lastName;
    private String birthday;

    @Getter @Setter
    @NoArgsConstructor
    public static abstract class ReaderHATEOAS extends AbstractHATEOAS<Long> {
        private String firstName;
        private String secondName;
        private String lastName;
        private String birthday;

        public ReaderHATEOAS(ReaderDTO readerDTO) {
            super(readerDTO);
            firstName = readerDTO.firstName;
            secondName = readerDTO.secondName;
            lastName = readerDTO.lastName;
            birthday = readerDTO.birthday;
        }
    }
}
