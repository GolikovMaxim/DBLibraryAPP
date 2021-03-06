package gmo.library.app.Repositories;

import gmo.library.app.DTO.OneTimeReaderDTO;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface OneTimeReaderRepository {
    String URL = "oneTimeReaders";

    @GET(URL)
    Call<SpringJson<List<OneTimeReaderDTO>>> getAllOneTimeReaders();
    @GET(URL + "/search/findByParams")
    Call<SpringJson<List<OneTimeReaderDTO>>> getOneTimeReadersByParams(@Query("lastName") String lastName, @Query("firstName") String firstName,
                                                                       @Query("secondName") String secondName, @Query("poiid") long poiid,
                                                                       @Query("size") int size, @Query("page") int page, @Query("sort") String sort);
    @POST(URL) @Headers("Accept: */*")
    Call<OneTimeReaderDTO> createOneTimeReader(@Body OneTimeReaderDTO.OneTimeReaderHATEOAS oneTimeReaderHATEOAS);

    @PATCH(URL + "/{id}") @Headers("Accept: */*")
    Call<OneTimeReaderDTO> updateOneTimeReader(@Path("id") String id, @Body OneTimeReaderDTO.OneTimeReaderHATEOAS oneTimeReaderHATEOAS);

    @DELETE(URL + "/{id}") @Headers("Accept: */*")
    Call<OneTimeReaderDTO> deleteOneTimeReader(@Path("id") String id);
}
