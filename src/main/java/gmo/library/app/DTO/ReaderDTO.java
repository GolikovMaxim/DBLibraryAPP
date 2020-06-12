package gmo.library.app.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class ReaderDTO extends AbstractDTO<Long> {
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
