package gmo.library.app.Repositories;

import gmo.library.app.DTO.GradeDTO;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface GradeRepository {
    String URL = "grades";

    @GET(URL)
    Call<SpringJson<List<GradeDTO>>> getAllGrades();
}
