package gmo.library.app.Repositories;

import gmo.library.app.DTO.PenaltyDTO;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface PenaltyRepository {
    String URL = "penalties";

    @GET(URL + "/search/findByParams")
    Call<SpringJson<List<PenaltyDTO>>> getPenaltiesByParams(@Query("lastName") String lastName, @Query("firstName") String firstName,
                                                            @Query("secondName") String secondName, @Query("poiid") long poiid,
                                                            @Query("bookName") String bookName, @Query("accrualDateAfter") String accrualDate,
                                                            @Query("payDateAfter") String payDate, @Query("costMore") int costMore,
                                                            @Query("costLess") int costLess, @Query("size") int size,
                                                            @Query("page") int page, @Query("sort") String sort);

    @POST(URL) @Headers("Accept: */*")
    Call<PenaltyDTO> createPenalties(@Body PenaltyDTO.PenaltyHATEOAS penaltyHATEOAS);

    @PATCH(URL + "/{id}") @Headers("Accept: */*")
    Call<PenaltyDTO> updatePenalties(@Path("id") String id, @Body PenaltyDTO.PenaltyHATEOAS penaltyHATEOAS);

    @DELETE(URL + "/{id}") @Headers("Accept: */*")
    Call<PenaltyDTO> deletePenalties(@Path("id") String id);
}
