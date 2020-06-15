package gmo.library.app.DTO;

import gmo.library.app.Main;
import gmo.library.app.Repositories.TicketRepository;

public class TicketDTO extends PointOfIssueDTO {
    @Override
    public String getURL() {
        return getURL(this);
    }

    public static String getURL(TicketDTO ticketDTO) {
        return Main.SERVER_URL + TicketRepository.URL + "/" + ticketDTO.getId();
    }
}
