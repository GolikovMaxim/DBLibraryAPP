package gmo.library.app.Repositories;

import gmo.library.app.DTO.StudyGroupDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import java.util.List;

public interface StudyGroupRepository {
    String URL = "studyGroups";

    @GET(URL)
    Call<SpringJson<List<StudyGroupDTO>>> getAllStudyGroups();

    @POST(URL) @Headers("Accept: */*")
    Call<StudyGroupDTO> addStudyGroup(@Body StudyGroupDTO.StudyGroupHATEOAS studyGroupHATEOAS);
}
