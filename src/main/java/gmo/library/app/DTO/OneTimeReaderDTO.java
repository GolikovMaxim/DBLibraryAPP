package gmo.library.app.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter @Getter
public class OneTimeReaderDTO extends ReaderDTO {
    private ReadingRoomDTO readingRoom;
    private Date takeDate;
}
