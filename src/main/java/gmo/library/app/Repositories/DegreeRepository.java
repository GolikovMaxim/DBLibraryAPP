package gmo.library.app.Repositories;

import gmo.library.app.DTO.DegreeDTO;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface DegreeRepository {
    @GET("degrees")
    Call<SpringJson<List<DegreeDTO>>> getAllDegrees();
}
