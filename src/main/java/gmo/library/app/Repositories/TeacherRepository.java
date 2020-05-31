package gmo.library.app.Repositories;

import gmo.library.app.DTO.TeacherDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface TeacherRepository {
    @GET("teachers")
    Call<SpringJson<List<TeacherDTO>>> getAllTeachers();
    @GET("teachers/search/findByParams")
    Call<SpringJson<List<TeacherDTO>>> getTeachersByParams(@Query("lastName") String lastName, @Query("firstName") String firstName,
                                                           @Query("secondName") String secondName, @Query("department") long department,
                                                           @Query("poiid") long poiid, @Query("faculty") long faculty);
}
