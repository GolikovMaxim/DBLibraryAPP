package gmo.library.app.Repositories;

import gmo.library.app.DTO.FacultyDTO;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface FacultyRepository {
    @GET("faculties")
    Call<SpringJson<List<FacultyDTO>>> getAllFaculties();
}
