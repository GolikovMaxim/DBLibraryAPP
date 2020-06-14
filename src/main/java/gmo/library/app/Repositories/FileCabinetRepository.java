package gmo.library.app.Repositories;

import gmo.library.app.DTO.FileCabinetDTO;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface FileCabinetRepository {
    String URL = "fileCabinets";

    @GET(URL)
    Call<SpringJson<List<FileCabinetDTO>>> getAllFileCabinets();
}
