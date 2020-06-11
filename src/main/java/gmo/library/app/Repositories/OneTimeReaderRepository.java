package gmo.library.app.Repositories;

import gmo.library.app.DTO.OneTimeReaderDTO;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface OneTimeReaderRepository {
    @GET("oneTimeReaders")
    Call<SpringJson<List<OneTimeReaderDTO>>> getAllOneTimeReaders();
    @GET("oneTimeReaders/search/findByParams")
    Call<SpringJson<List<OneTimeReaderDTO>>> getOneTimeReadersByParams(@Query("lastName") String lastName, @Query("firstName") String firstName,
                                                                       @Query("secondName") String secondName, @Query("poiid") long poiid);
    @POST("oneTimeReaders")
    Call<OneTimeReaderDTO> createOneTimeReader(@Body OneTimeReaderDTO oneTimeReaderDTO);

    @PATCH("oneTimeReaders/{id}")
    Call<OneTimeReaderDTO> updateOneTimeReader(@Path("id") String id, @Body OneTimeReaderDTO oneTimeReaderDTO);

    @DELETE("oneTimeReaders/{id}")
    Call<OneTimeReaderDTO> deleteOneTimeReader(@Path("id") String id);
}
