package gmo.library.app.Repositories;

import gmo.library.app.DTO.StudentDTO;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface StudentRepository {
    @GET("students")
    Call<SpringJson<List<StudentDTO>>> getAllStudents();
}
