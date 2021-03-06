package gmo.library.app.Repositories;

import gmo.library.app.DTO.ReadingRoomDTO;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface ReadingRoomRepository {
    String URL = "readingRooms";

    @GET(URL)
    Call<SpringJson<List<ReadingRoomDTO>>> getAllReadingRooms();
}
