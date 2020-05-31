package gmo.library.app.Repositories;

import gmo.library.app.DTO.StudentDTO;
import gmo.library.app.DTO.StudyGroupDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface StudentRepository {
    @GET("students")
    Call<SpringJson<List<StudentDTO>>> getAllStudents();
    @GET("students/search/findByParams")
    Call<SpringJson<List<StudentDTO>>> getStudentsByParams(@Query("fullName") String fullName, @Query("group") long group);
}
