package gmo.library.app.Repositories;

import gmo.library.app.DTO.TeacherDTO;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface TeacherRepository {
    @GET("teachers")
    Call<SpringJson<List<TeacherDTO>>> getAllTeachers();
}
