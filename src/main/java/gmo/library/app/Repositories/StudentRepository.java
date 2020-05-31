package gmo.library.app.Repositories;

import gmo.library.app.DTO.StudentDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface StudentRepository {
    @GET("students")
    Call<SpringJson<List<StudentDTO>>> getAllStudents();
    @GET("students/search/findByParams")
    Call<SpringJson<List<StudentDTO>>> getStudentsByParams(@Query("lastName") String lastName, @Query("firstName") String firstName,
                                                           @Query("secondName") String secondName, @Query("group") long group,
                                                           @Query("poiid") long poiid, @Query("faculty") long faculty);
}
