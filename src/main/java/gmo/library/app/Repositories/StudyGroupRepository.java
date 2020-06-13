package gmo.library.app.Repositories;

import gmo.library.app.DTO.StudyGroupDTO;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface StudyGroupRepository {
    String URL = "studyGroups";

    @GET(URL + "?size=" + Integer.MAX_VALUE)
    Call<SpringJson<List<StudyGroupDTO>>> getAllStudyGroups();

    @POST(URL) @Headers("Accept: */*")
    Call<StudyGroupDTO> addStudyGroup(@Body StudyGroupDTO.StudyGroupHATEOAS studyGroupHATEOAS);

    @GET(URL + "/search/findByNumber")
    Call<StudyGroupDTO> getGroupByNumber(@Query("number") int number);
}
