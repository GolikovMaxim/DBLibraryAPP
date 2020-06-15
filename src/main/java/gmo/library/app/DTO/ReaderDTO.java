package gmo.library.app.DTO;

import gmo.library.app.Main;
import gmo.library.app.Repositories.ReaderRepository;
import gmo.library.app.Utilities.Sort;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReaderDTO extends AbstractDTO<Long> {
    public static final Sort SORT_BY_FIRSTNAME = new Sort("По имени", "firstName");
    public static final Sort SORT_BY_SECONDNAME = new Sort("По отчеству", "secondName");
    public static final Sort SORT_BY_LASTNAME = new Sort("По фамилии", "lastName");

    private String firstName;
    private String secondName;
    private String lastName;
    private String birthday;

    public String getFullName() {
        return lastName + " " + firstName + " " + secondName;
    }

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

    public String getURL() {
        return getURL(this);
    }

    public static String getURL(ReaderDTO readerDTO) {
        return Main.SERVER_URL + ReaderRepository.URL + "/" + readerDTO.getId();
    }
}
