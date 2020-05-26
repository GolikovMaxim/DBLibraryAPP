package gmo.library.app.Repositories;

import gmo.library.app.DTO.OneTimeReaderDTO;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface OneTimeReaderRepository {
    @GET("oneTimeReaders")
    Call<SpringJson<List<OneTimeReaderDTO>>> getAllOneTimeReaders();
}
