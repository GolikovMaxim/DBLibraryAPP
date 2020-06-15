package gmo.library.app.DTO;

import gmo.library.app.Main;
import gmo.library.app.Repositories.OneTimeReaderRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class OneTimeReaderDTO extends ReaderDTO {
    private ReadingRoomDTO readingRoom;
    private String takeDate;

    @Getter @Setter
    @NoArgsConstructor
    public static class OneTimeReaderHATEOAS extends ReaderHATEOAS {
        private String readingRoom;
        private String takeDate;

        public OneTimeReaderHATEOAS(OneTimeReaderDTO oneTimeReaderDTO) {
            super(oneTimeReaderDTO);
            readingRoom = oneTimeReaderDTO.readingRoom.getURL();
            takeDate = oneTimeReaderDTO.takeDate;
        }
    }

    @Override
    public String getURL() {
        return getURL(this);
    }

    public static String getURL(OneTimeReaderDTO oneTimeReaderDTO) {
        return Main.SERVER_URL + OneTimeReaderRepository.URL + "/" + oneTimeReaderDTO.getId();
    }
}
