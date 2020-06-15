package gmo.library.app.Repositories;

import gmo.library.app.DTO.OffenceDTO;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface OffenceRepository {
    String URL = "offences";

    @GET(URL + "/search/findByParams")
    Call<SpringJson<List<OffenceDTO>>> getOffencesByParams(@Query("lastName") String lastName, @Query("firstName") String firstName,
                                                            @Query("secondName") String secondName, @Query("poiid") long poiid,
                                                            @Query("bookName") String bookName, @Query("accrualDateAfter") String accrualDate,
                                                           @Query("endDateAfter") String endDate, @Query("size") int size,
                                                           @Query("page") int page, @Query("sort") String sort);

    @POST(URL) @Headers("Accept: */*")
    Call<OffenceDTO> createOffence(@Body OffenceDTO.OffenceHATEOAS offenceHATEOAS);

    @PATCH(URL + "/{id}") @Headers("Accept: */*")
    Call<OffenceDTO> updateOffence(@Path("id") String id, @Body OffenceDTO.OffenceHATEOAS offenceHATEOAS);

    @DELETE(URL + "/{id}") @Headers("Accept: */*")
    Call<OffenceDTO> deleteOffence(@Path("id") String id);
}
