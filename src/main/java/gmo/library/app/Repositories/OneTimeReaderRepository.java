package gmo.library.app.Repositories;

import gmo.library.app.DTO.OneTimeReaderDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface OneTimeReaderRepository {
    @GET("oneTimeReaders")
    Call<SpringJson<List<OneTimeReaderDTO>>> getAllOneTimeReaders();
    @GET("oneTimeReaders/search/findByParams")
    Call<SpringJson<List<OneTimeReaderDTO>>> getOneTimeReadersByParams(@Query("lastName") String lastName, @Query("firstName") String firstName,
                                                                       @Query("secondName") String secondName, @Query("poiid") long poiid);
}
