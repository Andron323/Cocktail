package cocktail.info.api

import cocktail.info.response.FrResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiFilter {
    @GET("list.php?")
    fun getFilter(
        @QueryMap parameters : HashMap<String,String>
    ):Call<FrResponse>
}