package gmo.library.app.Repositories;

import gmo.library.app.DTO.TicketDTO;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface TicketRepository {
    String URL = "tickets";

    @GET(URL)
    Call<SpringJson<List<TicketDTO>>> getAllTickets();
}
