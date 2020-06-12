package gmo.library.app.Repositories;

import gmo.library.app.DTO.DepartmentDTO;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface DepartmentRepository {
    String URL = "departments";

    @GET(URL)
    Call<SpringJson<List<DepartmentDTO>>> getAllDepartments();
}
