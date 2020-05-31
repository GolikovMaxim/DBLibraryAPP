package gmo.library.app.Repositories;

import gmo.library.app.DTO.StudyGroupDTO;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface StudyGroupRepository {
    @GET("studyGroups")
    Call<SpringJson<List<StudyGroupDTO>>> getAllStudyGroups();
}
