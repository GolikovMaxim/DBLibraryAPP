package gmo.library.app.Repositories;

import gmo.library.app.DTO.FacultyDTO;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface FacultyRepository {
    String URL = "faculties";

    @GET(URL)
    Call<SpringJson<List<FacultyDTO>>> getAllFaculties();
}
