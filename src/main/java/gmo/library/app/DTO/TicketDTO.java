package gmo.library.app.DTO;

public class TicketDTO extends PointOfIssueDTO {
    @Override
    public String toString() {
        return "Ticket " + getId();
    }
}
