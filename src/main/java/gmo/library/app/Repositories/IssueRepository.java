package gmo.library.app.Repositories;

import gmo.library.app.DTO.IssueDTO;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface IssueRepository {
    String URL = "issues";

    @GET(URL)
    Call<SpringJson<List<IssueDTO>>> getAllIssues();

    @GET(URL + "/search/getByBookName")
    Call<SpringJson<List<IssueDTO>>> getIssueByBookName(@Query("bookName") String bookName, @Query("fileCabinet") long fileCabinet,
                                                        @Query("size") int size, @Query("page") int page, @Query("sort") String sort);

    @POST(URL) @Headers("Accept: */*")
    Call<IssueDTO> createIssue(@Body IssueDTO.IssueHATEOAS issueHATEOAS);

    @PATCH(URL + "/{id}") @Headers("Accept: */*")
    Call<IssueDTO> updateIssue(@Path("id") long id, @Body IssueDTO.IssueHATEOAS issueHATEOAS);
}
