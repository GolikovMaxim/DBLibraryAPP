package gmo.library.app.DTO;

import gmo.library.app.Main;
import gmo.library.app.Repositories.ReadingRoomRepository;

public class ReadingRoomDTO extends PointOfIssueDTO {
    @Override
    public String getURL() {
        return getURL(this);
    }

    public static String getURL(ReadingRoomDTO readingRoomDTO) {
        return Main.SERVER_URL + ReadingRoomRepository.URL + "/" + readingRoomDTO.getId();
    }
}
