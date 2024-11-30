import com.example.thenewsapi.data.remote.model.ApiResponse
import com.example.thenewsapi.data.remote.model.NewDetailDto
import retrofit2.Retrofit
import retrofit2.http.Path
import retrofit2.http.GET
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query

private const val API_KEY = "EYgwqhbW9QNgqqZwbsFtc7pPrGUb8rfZAqsfYyeo"
private const val BASE_URL = "https://api.thenewsapi.com/v1/news/"

interface NewApi {
    @GET("top")
    suspend fun getNews(
        @Query("api_token") apiKey : String = API_KEY
    ): ApiResponse

    // Get each news by id
    @GET("uuid/{id}")
    suspend fun getNewDetail(
        @Path("id") newId: String,
        @Query("api_token") apiKey: String = API_KEY
    ): NewDetailDto
}

object RetrofitInstance {
    val api: NewApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewApi::class.java)
    }
}