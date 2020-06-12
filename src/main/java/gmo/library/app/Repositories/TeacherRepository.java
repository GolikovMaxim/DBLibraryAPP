package gmo.library.app.Repositories;

import gmo.library.app.DTO.TeacherDTO;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface TeacherRepository {
    String URL = "teachers";

    @GET(URL)
    Call<SpringJson<List<TeacherDTO>>> getAllTeachers();
    @GET(URL + "/search/findByParams")
    Call<SpringJson<List<TeacherDTO>>> getTeachersByParams(@Query("lastName") String lastName, @Query("firstName") String firstName,
                                                           @Query("secondName") String secondName, @Query("department") long department,
                                                           @Query("poiid") long poiid, @Query("faculty") long faculty);
    @POST(URL)
    Call<TeacherDTO> createTeacher(@Body TeacherDTO.TeacherHATEOAS teacherHATEOAS);

    @PATCH(URL + "/{id}")
    Call<TeacherDTO> updateTeacher(@Path("id") String id, @Body TeacherDTO.TeacherHATEOAS teacherHATEOAS);

    @DELETE(URL + "/{id}")
    Call<TeacherDTO> deleteTeacher(@Path("id") String id);
}
