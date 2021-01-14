package cocktail.info.api

import cocktail.info.response.CocResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiCocktail {
    @GET("filter.php?")
    fun getCocktail(
        @QueryMap parameters : HashMap<String,String>
    ):Call<CocResponse>
}