package gmo.library.app.Repositories;

import gmo.library.app.DTO.BookTakeDTO;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface BookTakeRepository {
    String URL = "bookTakes";

    @GET(URL + "/search/findByParams")
    Call<SpringJson<List<BookTakeDTO>>> getBookTakesByParams(@Query("lastName") String lastName, @Query("firstName") String firstName,
                                                            @Query("secondName") String secondName, @Query("poiid") long poiid,
                                                            @Query("bookName") String bookName, @Query("takeDateAfter") String date,
                                                            @Query("size") int size, @Query("page") int page, @Query("sort") String sort);

    @POST(URL) @Headers("Accept: */*")
    Call<BookTakeDTO> createBookTake(@Body BookTakeDTO.BookTakeHATEOAS bookTakeHATEOAS);

    @PATCH(URL + "/{id}") @Headers("Accept: */*")
    Call<BookTakeDTO> updateBookTake(@Path("id") String id, @Body BookTakeDTO.BookTakeHATEOAS bookTakeHATEOAS);

    @DELETE(URL + "/{id}") @Headers("Accept: */*")
    Call<BookTakeDTO> deleteBookTake(@Path("id") String id);
}
