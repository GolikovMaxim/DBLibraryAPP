package gmo.library.app.DTO;

public class ReadingRoomDTO extends PointOfIssueDTO {
    @Override
    public String toString() {
        return "Reading Room " + getId();
    }
}
