package gmo.library.app.DTO;

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
}
