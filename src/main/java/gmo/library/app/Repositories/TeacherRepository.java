package gmo.library.app.Repositories;

import gmo.library.app.DTO.TeacherDTO;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface TeacherRepository {
    @GET("teachers")
    Call<SpringJson<List<TeacherDTO>>> getAllTeachers();
    @GET("teachers/search/findByParams")
    Call<SpringJson<List<TeacherDTO>>> getTeachersByParams(@Query("lastName") String lastName, @Query("firstName") String firstName,
                                                           @Query("secondName") String secondName, @Query("department") long department,
                                                           @Query("poiid") long poiid, @Query("faculty") long faculty);
    @POST("teachers")
    Call<TeacherDTO> createTeacher(@Body TeacherDTO teacherDTO);

    @PATCH("teachers/{id}")
    Call<TeacherDTO> updateTeacher(@Path("id") String id, @Body TeacherDTO teacherDTO);

    @DELETE("teachers/{id}")
    Call<TeacherDTO> deleteTeacher(@Path("id") String id);
}
