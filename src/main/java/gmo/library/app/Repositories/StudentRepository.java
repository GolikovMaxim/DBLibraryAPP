package gmo.library.app.Repositories;

import gmo.library.app.DTO.StudentDTO;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface StudentRepository {
    String URL = "students";

    @GET(URL)
    Call<SpringJson<List<StudentDTO>>> getAllStudents();
    @GET(URL + "/search/findByParams")
    Call<SpringJson<List<StudentDTO>>> getStudentsByParams(@Query("lastName") String lastName, @Query("firstName") String firstName,
                                                           @Query("secondName") String secondName, @Query("group") long group,
                                                           @Query("poiid") long poiid, @Query("faculty") long faculty,
                                                           @Query("size") int size, @Query("page") int page);
    @POST(URL)
    Call<StudentDTO> createStudent(@Body StudentDTO.StudentHATEOAS studentHATEOAS);

    @PATCH(URL + "/{id}")
    Call<StudentDTO> updateStudent(@Path("id") String id, @Body StudentDTO.StudentHATEOAS studentHATEOAS);

    @DELETE(URL + "/{id}")
    Call<StudentDTO> deleteStudent(@Path("id") String id);
}
